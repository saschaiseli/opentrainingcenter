package ch.opentrainingcenter.client.cache.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.xml.datatype.DatatypeConfigurationException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import ch.opentrainingcenter.client.cache.ActivityTTestHelper;
import ch.opentrainingcenter.client.cache.MockGpsFileLoader;
import ch.opentrainingcenter.client.cache.MockRecordListener;
import ch.opentrainingcenter.client.views.ApplicationContext;
import ch.opentrainingcenter.db.IDatabaseAccess;
import ch.opentrainingcenter.tcx.ActivityT;
import ch.opentrainingcenter.transfer.CommonTransferFactory;
import ch.opentrainingcenter.transfer.IAthlete;
import ch.opentrainingcenter.transfer.IImported;
import ch.opentrainingcenter.transfer.ITraining;
import ch.opentrainingcenter.transfer.ITrainingType;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class TrainingCenterDataCacheTest {

    private static IDatabaseAccess mockDataAccess;
    private static MockGpsFileLoader mockGps;
    private static TrainingCenterDataCache cache;

    final MockRecordListener listener = new MockRecordListener();

    @Before
    public void before() {
        mockDataAccess = Mockito.mock(IDatabaseAccess.class);
        mockGps = new MockGpsFileLoader();
        cache = TrainingCenterDataCache.getInstanceForTests(mockGps, mockDataAccess);
    }

    @After
    public void tearDown() {
        cache.resetCache();
    }

    @Test
    public void testNotNull() {
        assertNotNull(cache);
    }

    @Test
    public void testGetIfEmpty() {
        final ActivityT activity = cache.get(new Date());
        assertNull("Wenn nichts hinzugefügt wurde, kommt auch nix zurück.", activity);
    }

    @Test
    public void simpleAdd() throws DatatypeConfigurationException {
        final ActivityT activity = ActivityTTestHelper.createActivity(2012);

        // execute
        cache.add(activity);

        // assert
        final GregorianCalendar gregorianCalendar = activity.getId().toGregorianCalendar();
        final Date date = gregorianCalendar.getTime();
        final ActivityT activityFromCache = cache.get(date);
        assertNotNull("Activity muss im Cache gefunden werden: ", activityFromCache);
    }

    @Test
    public void testRemove() throws DatatypeConfigurationException {
        // prepare

        cache.addListener(listener);

        final ActivityT activityA = ActivityTTestHelper.createActivity(2012);
        final ActivityT activityB = ActivityTTestHelper.createActivity(2013);

        final Date dateA = activityA.getId().toGregorianCalendar().getTime();
        final Date dateB = activityB.getId().toGregorianCalendar().getTime();

        cache.add(activityA);
        cache.add(activityB);

        final List<Date> deletedIds = new ArrayList<Date>();
        deletedIds.add(dateA);
        deletedIds.add(dateB);
        // execute
        cache.remove(deletedIds);

        // assert
        assertEquals("Beide Record-Deleted müssen an Listener propagiert werden", 2, listener.getDeletedEntry().size());
    }

    @Test
    public void testRemoveOne() throws DatatypeConfigurationException {
        // prepare
        cache.addListener(listener);
        final ActivityT activityA = ActivityTTestHelper.createActivity(2012);
        final ActivityT activityB = ActivityTTestHelper.createActivity(2013);

        final Date dateA = activityA.getId().toGregorianCalendar().getTime();
        final Date dateB = activityB.getId().toGregorianCalendar().getTime();
        final IImported impA = createImported(dateA);
        final IImported impB = createImported(dateB);

        Mockito.when(mockDataAccess.getImportedRecord(dateA)).thenReturn(impA);
        Mockito.when(mockDataAccess.getImportedRecord(dateB)).thenReturn(impB);

        cache.add(activityA);
        cache.add(activityB);

        final List<Date> deletedIds = new ArrayList<Date>();
        deletedIds.add(new Date());
        deletedIds.add(dateB);
        // execute
        cache.remove(deletedIds);

        // assert
        assertEquals("Ein Record-Deleted müssen an Listener propagiert werden", 1, listener.getDeletedEntry().size());
    }

    @Test
    public void testUpdate() {
        // prepare
        cache.addListener(listener);
        // execute
        cache.update();
        // assert
        assertEquals("Nichts wurde upgedated", 0, listener.getChangedEntry().size());
        assertEquals("Nichts wurde upgedated", 0, listener.getDeletedEntry().size());
    }

    @Test
    public void removeListenerTestMitNull() {
        cache.removeListener(null);
    }

    @Test
    public void removeListenerTest() {
        // prepare
        cache.removeListener(listener);
    }

    @Test
    public void testAthleteInitial() {
        // assert
        assertNull("Athlete ist initial null", ApplicationContext.getApplicationContext().getAthlete());
    }

    @Test
    public void testAthlete() {
        // prepare
        final IAthlete athlete = CommonTransferFactory.createAthlete("Junit", 37, Integer.valueOf(200));
        ApplicationContext.getApplicationContext().setAthlete(athlete);

        // execute
        final IAthlete selectedProfile = ApplicationContext.getApplicationContext().getAthlete();

        // assert
        assertEquals("Athlete korrekt gesetzt", athlete, selectedProfile);
    }

    @Test
    public void containsMitNull() {
        assertFalse("Nichts im cache", cache.contains(null));
    }

    @Test
    public void containsTest() throws DatatypeConfigurationException {

        // prepare
        final ActivityT activityA = ActivityTTestHelper.createActivity(2012);
        final ActivityT activityB = ActivityTTestHelper.createActivity(2013);

        final Date dateA = activityA.getId().toGregorianCalendar().getTime();
        final Date dateB = activityB.getId().toGregorianCalendar().getTime();

        cache.add(activityA);
        cache.add(activityB);

        assertTrue("Erster Record ist im Cache (" + dateA + ")", cache.contains(dateA));
        assertTrue("Zweiter Record ist im Cache (" + dateB + ")", cache.contains(dateB));
        assertFalse("Wenn nichts gefunden", cache.contains(new Date()));
    }

    @Test
    public void testNotesNull() throws DatatypeConfigurationException {
        // prepare
        final ActivityT activityA = ActivityTTestHelper.createActivity(2012);

        final Date dateA = activityA.getId().toGregorianCalendar().getTime();

        // execute
        cache.updateNote(dateA, "test");
        cache.add(activityA);

        // assert
        final ActivityT activityTFromCache = cache.get(dateA);
        assertEquals("Note ist nicht gesetzt, da Record erst später in cache kam", null, activityTFromCache.getNotes());
    }

    @Test
    public void testNotes() throws DatatypeConfigurationException {
        // prepare
        final ActivityT activityA = ActivityTTestHelper.createActivity(2012);

        final Date dateA = activityA.getId().toGregorianCalendar().getTime();
        cache.add(activityA);

        // execute
        cache.updateNote(dateA, "test");

        // assert
        final ActivityT activityTFromCache = cache.get(dateA);
        assertEquals("Note muss korrekt gesetzt sein", "test", activityTFromCache.getNotes());
    }

    @Test
    public void testNotesNotification() throws DatatypeConfigurationException {
        // prepare
        cache.addListener(listener);
        final ActivityT activityA = ActivityTTestHelper.createActivity(2012);

        final Date dateA = activityA.getId().toGregorianCalendar().getTime();
        cache.add(activityA);

        // execute
        cache.updateNote(dateA, "test");

        // assert
        final ActivityT activityTFromCache = cache.get(dateA);
        assertEquals("Note muss korrekt gesetzt sein", "test", activityTFromCache.getNotes());
        assertEquals("Ein Record-Changed muss an Listener propagiert werden", 1, listener.getChangedEntry().size());
    }

    @Test
    public void testUpdateOhneListener() {
        cache.update();

        assertEquals("Eh kein listener angehängt", 0, listener.getChangedEntry().size());
        assertEquals("Eh kein listener angehängt", 0, listener.getDeletedEntry().size());
    }

    @Test
    public void testToStringInit() {
        assertEquals("Cache: Anzahl Elemente: 0", cache.toString());
    }

    @Test
    public void testToString() throws DatatypeConfigurationException {
        // prepare
        final ActivityT activityA = ActivityTTestHelper.createActivity(2012);
        final ActivityT activityB = ActivityTTestHelper.createActivity(2013);

        cache.add(activityA);
        cache.add(activityB);

        assertEquals("Cache: Anzahl Elemente: 2", cache.toString());
    }

    private IImported createImported(final Date date) {
        final IImported result = CommonTransferFactory.createIImported();
        result.setActivityId(date);
        final ITrainingType type = CommonTransferFactory.createTrainingType(1, "junit", "description");
        result.setTrainingType(type);
        final ITraining overview = CommonTransferFactory.createTraining(date, 1, 1, 1, 1, 1, null, null);
        result.setTraining(overview);
        return result;
    }

}
