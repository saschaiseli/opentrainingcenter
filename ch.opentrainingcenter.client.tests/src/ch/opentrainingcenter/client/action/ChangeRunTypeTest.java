package ch.opentrainingcenter.client.action;

import java.util.HashMap;
import java.util.Map;

import javax.xml.datatype.DatatypeConfigurationException;

import org.eclipse.jface.preference.IPreferenceStore;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import ch.opentrainingcenter.client.cache.Cache;
import ch.opentrainingcenter.client.cache.impl.TrainingCenterDataCache;
import ch.opentrainingcenter.client.model.RunType;
import ch.opentrainingcenter.client.views.ApplicationContext;
import ch.opentrainingcenter.db.IDatabaseAccess;
import ch.opentrainingcenter.importer.IConvert2Tcx;
import ch.opentrainingcenter.transfer.CommonTransferFactory;
import ch.opentrainingcenter.transfer.IImported;
import ch.opentrainingcenter.transfer.ITrainingType;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ChangeRunTypeTest {
    private ChangeRunType changeRunType;
    private RunType type;
    private IDatabaseAccess databaseAccess;
    private IPreferenceStore store;
    private final Map<String, IConvert2Tcx> converters = new HashMap<String, IConvert2Tcx>();

    @Before
    public void before() {
        databaseAccess = Mockito.mock(IDatabaseAccess.class);
        type = RunType.LONG_JOG;
        store = Mockito.mock(IPreferenceStore.class);
        ApplicationContext.getApplicationContext().setSelection(new Object[] {});
    }

    @Test
    public void testConstructor() {
        changeRunType = new ChangeRunType(type, databaseAccess, TrainingCenterDataCache.getInstance(databaseAccess));
        assertNotNull(changeRunType);
    }

    @Test
    public void testType() {
        changeRunType = new ChangeRunType(type, databaseAccess, TrainingCenterDataCache.getInstance(databaseAccess));
        assertEquals(type, changeRunType.getType());
    }

    @Test
    public void testRunNoSelection() throws DatatypeConfigurationException {
        final Cache cache = Mockito.mock(Cache.class);

        changeRunType = new ChangeRunType(type, databaseAccess, cache);
        changeRunType.run();

        Mockito.verify(databaseAccess, Mockito.times(0)).updateRecord((IImported) Mockito.any(), Mockito.anyInt());
    }

    @Test
    public void testRunNullSelection() throws DatatypeConfigurationException {
        final MockCache cache = new MockCache();
        ApplicationContext.getApplicationContext().setSelection(new Object[] {});

        changeRunType = new ChangeRunType(type, databaseAccess, cache);
        changeRunType.run();
    }

    @Test
    public void testRunWithSelection() throws DatatypeConfigurationException {
        final MockCache cache = new MockCache();
        ApplicationContext.getApplicationContext().setSelection(new Object[] {});

        final IImported selected = CommonTransferFactory.createIImported();
        selected.setId(42);
        final ITrainingType trainingType = CommonTransferFactory.createTrainingType(11, "junit traiing type", "description");
        selected.setTrainingType(trainingType);
        ApplicationContext.getApplicationContext().setSelection(new Object[] { selected });
        // execute
        changeRunType = new ChangeRunType(RunType.EXT_INTERVALL, databaseAccess, cache);
        changeRunType.run();

        Mockito.verify(databaseAccess, Mockito.times(1)).updateRecord((IImported) Mockito.any(), Mockito.anyInt());

    }
}
