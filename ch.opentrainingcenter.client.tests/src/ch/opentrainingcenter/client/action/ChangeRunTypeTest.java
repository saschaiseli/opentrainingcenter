package ch.opentrainingcenter.client.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.datatype.DatatypeConfigurationException;

import org.eclipse.jface.preference.IPreferenceStore;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import ch.opentrainingcenter.client.cache.MockDataAccess;
import ch.opentrainingcenter.client.cache.impl.TrainingCenterDataCache;
import ch.opentrainingcenter.client.model.RunType;
import ch.opentrainingcenter.importer.IConvert2Tcx;
import ch.opentrainingcenter.transfer.CommonTransferFactory;
import ch.opentrainingcenter.transfer.IImported;
import ch.opentrainingcenter.transfer.ITrainingType;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ChangeRunTypeTest {
    private ChangeRunType changeRunType;
    private RunType type;
    private MockDataAccess databaseAccess;
    private IPreferenceStore store;
    private final Map<String, IConvert2Tcx> converters = new HashMap<String, IConvert2Tcx>();

    @Before
    public void before() {
        databaseAccess = new MockDataAccess();
        type = RunType.LONG_JOG;
        store = Mockito.mock(IPreferenceStore.class);

    }

    @Test
    public void testConstructor() {
        changeRunType = new ChangeRunType(type, databaseAccess, TrainingCenterDataCache.getInstance(databaseAccess, store, converters));
        assertNotNull(changeRunType);
    }

    @Test
    public void testType() {
        changeRunType = new ChangeRunType(type, databaseAccess, TrainingCenterDataCache.getInstance(databaseAccess, store, converters));
        assertEquals(type, changeRunType.getType());
    }

    @Test
    public void testRunNoSelection() throws DatatypeConfigurationException {
        final MockCache cache = new MockCache();
        cache.setSelection(new Object[] {});

        changeRunType = new ChangeRunType(type, databaseAccess, cache);
        changeRunType.run();

        final RunType typeResult = cache.getType();
        final List<IImported> changedRecords = cache.getChangedRecords();

        assertEquals("Kein Type wurde geändert", type, typeResult);
        assertEquals("Nichts wurde geändert", 0, changedRecords.size());
    }

    @Test
    public void testRunNullSelection() throws DatatypeConfigurationException {
        final MockCache cache = new MockCache();
        cache.setSelection(new Object[] {});

        changeRunType = new ChangeRunType(type, databaseAccess, cache);
        changeRunType.run();
    }

    @Test
    public void testRunWithSelection() throws DatatypeConfigurationException {
        final MockCache cache = new MockCache();
        cache.setSelection(new Object[] {});

        final IImported selected = CommonTransferFactory.createIImported();
        selected.setId(42);
        final ITrainingType trainingType = CommonTransferFactory.createTrainingType(11, "junit traiing type", "description");
        selected.setTrainingType(trainingType);
        cache.setSelection(new Object[] { selected });
        // execute
        changeRunType = new ChangeRunType(RunType.EXT_INTERVALL, databaseAccess, cache);
        changeRunType.run();

        final RunType typeResult = cache.getType();
        final List<IImported> changedRecords = cache.getChangedRecords();

        assertEquals("Type muss im cache gesetzt worden sein", RunType.EXT_INTERVALL, typeResult);
        assertEquals("Ein Imported muss geändert worden sein", 1, changedRecords.size());
    }
}
