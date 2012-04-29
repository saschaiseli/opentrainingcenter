package ch.opentrainingcenter.client.cache;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;

import ch.opentrainingcenter.client.model.ISimpleTraining;
import ch.opentrainingcenter.client.model.RunType;
import ch.opentrainingcenter.tcx.ActivityT;
import ch.opentrainingcenter.transfer.CommonTransferFactory;
import ch.opentrainingcenter.transfer.IAthlete;
import ch.opentrainingcenter.transfer.IImported;
import ch.opentrainingcenter.transfer.ITraining;
import ch.opentrainingcenter.transfer.ITrainingType;

public class TrainingCenterDataCacheTest {

    private static MockDataAccess mockData;
    private static MockGpsFileLoader mockGps;
    private static TrainingCenterDataCache cache;

    final MockRecordListener listener = new MockRecordListener();

    @BeforeClass
    public static void beforeClass() {
        mockData = new MockDataAccess();
        mockGps = new MockGpsFileLoader();
        cache = TrainingCenterDataCache.getInstance(mockGps, mockData);
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
    public void testGetInstance() {
        assertNotNull("Get Instance darf nicht null zurückgeben", TrainingCenterDataCache.getInstance());
    }

    @Test
    public void simpleAdd() throws DatatypeConfigurationException {
        final ActivityT activity = createActivity(2012);

        // execute
        cache.add(activity);

        // assert
        final GregorianCalendar gregorianCalendar = activity.getId().toGregorianCalendar();
        final Date date = gregorianCalendar.getTime();
        final ActivityT activityFromCache = cache.get(date);
        assertNotNull("Activity muss im Cache gefunden werden: ", activityFromCache);
    }

    @Test
    public void getAllActivitiesTestNotNull() throws DatatypeConfigurationException {
        assertNotNull(cache.getAllActivities());
        assertEquals("Es sind noch keine Elemente im Cache", 0, cache.getAllActivities().size());
    }

    @Test
    public void getAllActivities() throws DatatypeConfigurationException {
        final ActivityT activity = createActivity(2012);

        final IImported iimported = CommonTransferFactory.createIImported();
        mockData.setIimported(iimported);

        cache.add(activity);

        // execute
        final Collection<IImported> all = cache.getAllActivities();

        // Asserts
        assertEquals("Es eine Elemente im Cache", 1, all.size());
        final IImported element = all.iterator().next();

        assertNotNull("Element im Cache darf nicht null sein", element);
    }

    @Test
    public void testSetSelected() {
        // prepare
        final IImported selected = CommonTransferFactory.createIImported();

        // execute
        cache.setSelectedRun(selected);

        // assert

        assertEquals("Richtiger Record ist als selektiert markiert", selected, cache.getSelected());
    }

    @Test
    public void testGetSelectedWennNochkeinerSelektiertist() throws DatatypeConfigurationException {
        final ActivityT activity = createActivity(2012);

        final IImported iimported = CommonTransferFactory.createIImported();
        mockData.setIimported(iimported);

        cache.add(activity);

        // execute
        final IImported selected = cache.getSelected();

        // assert
        assertEquals("Wenn noch keiner selektiert ist, muss neuster selektiert sein", iimported, selected);
    }

    @Test
    public void testGetSelectedWennNochkeinerSelektiertistBeiMehreren() throws DatatypeConfigurationException {
        // prepare
        final ActivityT activityA = createActivity(2012);
        final ActivityT activityB = createActivity(2013);

        final Date dateA = activityA.getId().toGregorianCalendar().getTime();
        final Date dateB = activityB.getId().toGregorianCalendar().getTime();

        cache.add(activityA);
        cache.add(activityB);

        final List<IImported> records = new ArrayList<IImported>();

        final IImported impA = createImported(dateA);
        records.add(impA);
        final IImported impB = createImported(dateB);
        records.add(impB);

        cache.addAllImported(records);

        // execute
        final IImported selected = cache.getSelected();

        // assert
        assertEquals("Wenn noch keiner selektiert ist, muss neuster selektiert sein", dateB, selected.getActivityId());
    }

    @Test
    public void resetCacheWennNeuerAthlete() throws DatatypeConfigurationException {
        final ActivityT activity = createActivity(2012);

        cache.add(activity);

        // execute
        final IAthlete athlete = CommonTransferFactory.createAthlete("Junit", 37, Integer.valueOf(200));
        cache.setSelectedProfile(athlete);

        assertEquals("Keine IImporteds mehr:", 0, cache.getAllActivities().size());
        assertEquals("Keine Activities mehr:", 0, cache.getAllSimpleTrainings().size());
    }

    @Test
    public void getSelectedOverviewNull() {
        assertNull("Wenn nix im cache, kann es auch keine Overview geben", cache.getSelectedOverview());
    }

    @Test
    public void getSelectedOverview() throws DatatypeConfigurationException {
        assertNull("Wenn nix im cache, kann es auch keine Overview geben", cache.getSelectedOverview());

        // prepare
        final ActivityT activityA = createActivity(2012);
        final ActivityT activityB = createActivity(2013);

        final Date dateA = activityA.getId().toGregorianCalendar().getTime();
        final Date dateB = activityB.getId().toGregorianCalendar().getTime();

        cache.add(activityA);
        cache.add(activityB);

        final List<IImported> records = new ArrayList<IImported>();

        final IImported impA = createImported(dateA);
        records.add(impA);
        final IImported impB = createImported(dateB);
        records.add(impB);

        cache.addAllImported(records);

        // execute
        final ISimpleTraining overview = cache.getSelectedOverview();

        assertNotNull("Da Elemente im cache sind, darf die Overview nicht null sein", overview);
    }

    @Test
    public void getSelectedOverviewReload() throws DatatypeConfigurationException {
        assertNull("Wenn nix im cache, kann es auch keine Overview geben", cache.getSelectedOverview());

        // prepare
        final ActivityT activityA = createActivity(2012);
        final ActivityT activityB = createActivity(2013);

        final Date dateA = activityA.getId().toGregorianCalendar().getTime();
        final Date dateB = activityB.getId().toGregorianCalendar().getTime();

        cache.add(activityA);
        cache.add(activityB);

        final List<IImported> records = new ArrayList<IImported>();

        final IImported impA = createImported(dateA);
        records.add(impA);
        final IImported impB = createImported(dateB);
        records.add(impB);

        cache.setSelectedRun(createImported(new Date()));

        cache.addAllImported(records);

        mockGps.setActivity(activityA);
        // execute
        final ISimpleTraining overview = cache.getSelectedOverview();

        assertNotNull("Da Elemente im cache sind, darf die Overview nicht null sein", overview);
    }

    @Test
    public void testRemove() throws DatatypeConfigurationException {
        // prepare

        cache.addListener(listener);

        final ActivityT activityA = createActivity(2012);
        final ActivityT activityB = createActivity(2013);

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
        assertEquals("Alle Imported aus dem Cache entfernt. Cache muss leer sein", 0, cache.getAllActivities().size());
        assertEquals("Alle Simpletrainings aus dem Cache entfernt. Cache muss leer sein", 0, cache.getAllSimpleTrainings().size());
        assertNull("Keines mehr selektiert", cache.getSelected());

        assertEquals("Beide Record-Deleted müssen an Listener propagiert werden", 2, listener.getDeletedEntry().size());
    }

    @Test
    public void testRemoveOne() throws DatatypeConfigurationException {
        // prepare
        final ActivityT activityA = createActivity(2012);
        final ActivityT activityB = createActivity(2013);

        final Date dateA = activityA.getId().toGregorianCalendar().getTime();
        final Date dateB = activityB.getId().toGregorianCalendar().getTime();

        cache.add(activityA);
        cache.add(activityB);

        final List<Date> deletedIds = new ArrayList<Date>();
        deletedIds.add(new Date());
        deletedIds.add(dateB);
        // execute
        cache.remove(deletedIds);

        // assert
        assertEquals("Nur ein Imported aus dem Cache entfernt. Im Cache ist noch ein Element", 1, cache.getAllActivities().size());
        assertEquals("Nur ein Simpletrainings aus dem Cache entfernt. Im Cache ist noch ein Element", 1, cache.getAllSimpleTrainings().size());
        assertNotNull("Eines muss noch selektiert sein", cache.getSelected());
    }

    @Test
    public void testUpdate() {
        // prepare
        cache.addListener(listener);
        // execute
        cache.update();
        // assert
        assertNull("Nichts wurde upgedated", listener.getChangedEntry());
        assertNull("Nichts wurde upgedated", listener.getDeletedEntry());
    }

    @Test
    public void testUpdateTypeEmptyList() {
        // prepare
        cache.addListener(listener);
        final List<IImported> changedRecords = new ArrayList<IImported>();
        final RunType type = RunType.LONG_JOG;
        // execute
        cache.changeType(changedRecords, type);
        // assert
        assertNull("Nichts wurde upgedated", listener.getChangedEntry());
        assertNull("Nichts wurde upgedated", listener.getDeletedEntry());
    }

    @Test
    public void testUpdateType() throws DatatypeConfigurationException {
        // prepare
        cache.addListener(listener);

        final ActivityT activityA = createActivity(2012);

        final Date dateA = activityA.getId().toGregorianCalendar().getTime();
        final List<IImported> changedRecords = new ArrayList<IImported>();
        final IImported imported = createImported(dateA);
        changedRecords.add(imported);

        mockData.setIimported(imported);

        cache.add(activityA);

        // changedRecords.add(createImported(dateB));

        final RunType type = RunType.LONG_JOG;
        // execute
        assertEquals("Vor dem change Type ist der Lauftyp noch unbekannt", RunType.NONE, cache.getAllSimpleTrainings().get(0).getType());
        cache.changeType(changedRecords, type);
        // assert
        assertEquals("Korrekter Lauftyp gesetzt", type, cache.getAllSimpleTrainings().get(0).getType());
    }

    @Test
    public void testUpdateTypeWennNichtgefunden() throws DatatypeConfigurationException {
        // prepare
        cache.addListener(listener);

        final ActivityT activityA = createActivity(2012);

        // final Date dateA = activityA.getId().toGregorianCalendar().getTime();
        final List<IImported> changedRecords = new ArrayList<IImported>();
        final IImported imported = createImported(new Date());
        changedRecords.add(imported);

        mockData.setIimported(imported);

        cache.add(activityA);

        // changedRecords.add(createImported(dateB));

        final RunType type = RunType.LONG_JOG;
        // execute
        assertEquals("Vor dem change Type ist der Lauftyp noch unbekannt", RunType.NONE, cache.getAllSimpleTrainings().get(0).getType());
        cache.changeType(changedRecords, type);
        // assert
        assertEquals("Lauftyp immer noch unbekannt", RunType.NONE, cache.getAllSimpleTrainings().get(0).getType());
    }

    @Test(expected = IllegalArgumentException.class)
    public void removeListenerTestMitNull() {
        // prepare
        cache.removeListener(null);
    }

    @Test
    public void removeListenerTest() {
        // prepare
        cache.removeListener(listener);
    }

    @Test
    public void testAthleteInitial() {
        // prepare

        // execute
        final IAthlete selectedProfile = cache.getSelectedProfile();

        // assert
        assertNull("Athlete ist initial null", selectedProfile);
    }

    @Test
    public void testAthlete() {
        // prepare
        final IAthlete athlete = CommonTransferFactory.createAthlete("Junit", 37, Integer.valueOf(200));
        cache.setSelectedProfile(athlete);

        // execute
        final IAthlete selectedProfile = cache.getSelectedProfile();

        // assert
        assertEquals("Athlete korrekt gesetzt", athlete, selectedProfile);
    }

    @Test
    public void cacheLoadedInit() {
        assertFalse("cache ist anfangs nicht geladen", cache.isCacheLoaded());
    }

    @Test
    public void isCacheLoadedAfterSetting() {
        cache.setCacheLoaded(true);
        assertTrue("cache wurde geladen", cache.isCacheLoaded());
    }

    @Test
    public void containsMitNull() {
        assertFalse("Nichts im cache", cache.contains(null));
    }

    @Test
    public void containsTest() throws DatatypeConfigurationException {

        // prepare
        final ActivityT activityA = createActivity(2012);
        final ActivityT activityB = createActivity(2013);

        final Date dateA = activityA.getId().toGregorianCalendar().getTime();
        final Date dateB = activityB.getId().toGregorianCalendar().getTime();

        cache.add(activityA);
        cache.add(activityB);

        assertTrue("Erster Record ist im Cache (" + dateA + ")", cache.contains(dateA));
        assertTrue("Zweiter Record ist im Cache (" + dateB + ")", cache.contains(dateB));
        assertFalse("Wenn nichts gefunden", cache.contains(new Date()));
    }

    @Test
    public void testToStringInit() {
        assertEquals("Cache: Anzahl Elemente: 0", cache.toString());
    }

    @Test
    public void testToString() throws DatatypeConfigurationException {
        // prepare
        final ActivityT activityA = createActivity(2012);
        final ActivityT activityB = createActivity(2013);

        cache.add(activityA);
        cache.add(activityB);

        assertEquals("Cache: Anzahl Elemente: 2", cache.toString());
    }

    private IImported createImported(final Date date) {
        final IImported result = CommonTransferFactory.createIImported();
        result.setActivityId(date);
        final ITrainingType type = CommonTransferFactory.createTrainingType(1, "junit", "description");
        result.setTrainingType(type);
        final ITraining overview = CommonTransferFactory.createTraining(new Date(), 1, 1, 1, 1, 1);
        result.setTraining(overview);
        return result;
    }

    private ActivityT createActivity(final int year) throws DatatypeConfigurationException {
        final ActivityT activity = new ActivityT();
        final GregorianCalendar gcal = new GregorianCalendar();
        final XMLGregorianCalendar id = DatatypeFactory.newInstance().newXMLGregorianCalendar(gcal);
        id.setTime(11, 55, 05, 1);
        id.setYear(year);
        id.setMonth(8);
        id.setDay(29);
        activity.setId(id);
        return activity;
    }
}
