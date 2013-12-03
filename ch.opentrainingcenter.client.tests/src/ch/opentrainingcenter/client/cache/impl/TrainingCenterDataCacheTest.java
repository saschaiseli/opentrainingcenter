package ch.opentrainingcenter.client.cache.impl;

import java.util.ArrayList;
import java.util.List;

import javax.xml.datatype.DatatypeConfigurationException;

import org.joda.time.DateTime;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import ch.opentrainingcenter.client.cache.ActivityTTestHelper;
import ch.opentrainingcenter.client.cache.MockRecordListener;
import ch.opentrainingcenter.client.views.ApplicationContext;
import ch.opentrainingcenter.core.cache.Cache;
import ch.opentrainingcenter.core.cache.TrainingCache;
import ch.opentrainingcenter.core.db.IDatabaseAccess;
import ch.opentrainingcenter.transfer.IAthlete;
import ch.opentrainingcenter.transfer.ITraining;
import ch.opentrainingcenter.transfer.ITrainingType;
import ch.opentrainingcenter.transfer.factory.CommonTransferFactory;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

@SuppressWarnings("nls")
public class TrainingCenterDataCacheTest {

    private static IDatabaseAccess mockDataAccess;
    private static Cache cache;

    final MockRecordListener<ITraining> listener = new MockRecordListener<ITraining>();

    @Before
    public void before() {
        mockDataAccess = Mockito.mock(IDatabaseAccess.class);
        cache = TrainingCache.getInstance();
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
        final ITraining activity = cache.get(42L);
        assertNull("Wenn nichts hinzugefügt wurde, kommt auch nix zurück.", activity);
    }

    @Test
    public void simpleAdd() throws DatatypeConfigurationException {
        final ITraining activity = ActivityTTestHelper.createActivity(2012);

        // execute
        cache.add(activity);

        // assert
        final ITraining activityFromCache = cache.get(activity.getDatum());
        assertNotNull("Activity muss im Cache gefunden werden: ", activityFromCache);
    }

    @Test
    public void testRemove() throws DatatypeConfigurationException {
        // prepare

        cache.addListener(listener);

        final ITraining activityA = ActivityTTestHelper.createActivity(2012);
        final ITraining activityB = ActivityTTestHelper.createActivity(2013);

        final Long dateA = activityA.getDatum();
        final Long dateB = activityB.getDatum();

        cache.add(activityA);
        cache.add(activityB);

        final List<Long> deletedIds = new ArrayList<Long>();
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
        final ITraining activityA = ActivityTTestHelper.createActivity(2012);
        final ITraining activityB = ActivityTTestHelper.createActivity(2013);

        final Long dateA = activityA.getDatum();
        final Long dateB = activityB.getDatum();
        final ITraining impA = createImported(dateA);
        final ITraining impB = createImported(dateB);

        Mockito.when(mockDataAccess.getTrainingById(dateA)).thenReturn(impA);
        Mockito.when(mockDataAccess.getTrainingById(dateB)).thenReturn(impB);

        cache.add(activityA);
        cache.add(activityB);

        final List<Long> deletedIds = new ArrayList<Long>();
        deletedIds.add(42L);
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
        cache.notifyListeners();
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
    public void testAthlete() {
        assertNull("Athlete ist initial null", ApplicationContext.getApplicationContext().getAthlete());
        // prepare
        final IAthlete athlete = CommonTransferFactory.createAthlete("Junit", DateTime.now().toDate(), Integer.valueOf(200));
        ApplicationContext.getApplicationContext().setAthlete(athlete);

        // execute
        final IAthlete selectedProfile = ApplicationContext.getApplicationContext().getAthlete();

        // assert
        assertEquals("Athlete korrekt gesetzt", athlete, selectedProfile);
    }

    @Test
    public void containsMitNull() {
        assertFalse("Nichts im cache", cache.contains(-42L));
    }

    @Test
    public void containsTest() throws DatatypeConfigurationException {

        // prepare
        final ITraining activityA = ActivityTTestHelper.createActivity(2012);
        final ITraining activityB = ActivityTTestHelper.createActivity(2013);

        final Long dateA = activityA.getDatum();
        final Long dateB = activityB.getDatum();

        cache.add(activityA);
        cache.add(activityB);

        assertTrue("Erster Record ist im Cache (" + dateA + ")", cache.contains(dateA));
        assertTrue("Zweiter Record ist im Cache (" + dateB + ")", cache.contains(dateB));
        assertFalse("Wenn nichts gefunden", cache.contains(-42L));
    }

    @Test
    public void testNotesNull() throws DatatypeConfigurationException {
        // prepare
        final ITraining activityA = ActivityTTestHelper.createActivity(2012);

        final Long dateA = activityA.getDatum();

        // execute
        cache.update(dateA, "", null, null);
        cache.add(activityA);

        // assert
        final ITraining activityTFromCache = cache.get(dateA);
        assertEquals("Note ist nicht gesetzt, da Record erst später in cache kam", "", activityTFromCache.getNote());
    }

    @Test
    public void testNotes() throws DatatypeConfigurationException {
        // prepare
        final ITraining activityA = ActivityTTestHelper.createActivity(2012);

        final Long dateA = activityA.getDatum();
        cache.add(activityA);

        // execute
        cache.update(dateA, "test", null, null);

        // assert
        final ITraining activityTFromCache = cache.get(dateA);

        assertEquals("Note muss korrekt gesetzt sein", "test", activityTFromCache.getNote());
    }

    @Test
    public void testNotesNotification() throws DatatypeConfigurationException {
        // prepare
        cache.addListener(listener);
        final ITraining activityA = ActivityTTestHelper.createActivity(2012);

        final Long dateA = activityA.getDatum();
        cache.add(activityA);

        // execute
        cache.update(dateA, "test", null, null);

        // assert
        final ITraining activityTFromCache = cache.get(dateA);

        assertEquals("Note muss korrekt gesetzt sein", "test", activityTFromCache.getNote());
        assertEquals("Ein Record-Changed muss an Listener propagiert werden", 1, listener.getChangedEntry().size());
    }

    @Test
    public void testUpdateOhneListener() {
        cache.notifyListeners();

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
        final ITraining activityA = ActivityTTestHelper.createActivity(2012);
        final ITraining activityB = ActivityTTestHelper.createActivity(2013);

        cache.add(activityA);
        cache.add(activityB);

        assertEquals("Cache: Anzahl Elemente: 2", cache.toString());
    }

    private ITraining createImported(final Long date) {
        final ITraining overview = CommonTransferFactory.createTraining(date, 1, 1, 1, 1, 1);
        final ITrainingType type = CommonTransferFactory.createTrainingType(1, "junit", "description");
        overview.setTrainingType(type);
        return overview;
    }

}
