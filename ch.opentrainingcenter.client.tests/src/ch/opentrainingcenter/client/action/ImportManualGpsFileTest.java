package ch.opentrainingcenter.client.action;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.ui.ISelectionService;
import org.eclipse.ui.IWorkbenchWindow;
import org.junit.Before;
import org.junit.Test;

import ch.opentrainingcenter.client.PreferenceConstants;
import ch.opentrainingcenter.client.cache.Cache;
import ch.opentrainingcenter.db.IDatabaseAccess;
import ch.opentrainingcenter.importer.IConvert2Tcx;

public class ImportManualGpsFileTest {
    private ImportManualGpsFiles files;
    private IWorkbenchWindow window;
    private IDatabaseAccess databaseAccess;
    private Cache cache;
    private IPreferenceStore preferenceStore;
    private Map<String, IConvert2Tcx> converters;
    private ISelectionService service;

    @Before
    public void before() {
        window = mock(IWorkbenchWindow.class);
        service = mock(ISelectionService.class);
        databaseAccess = mock(IDatabaseAccess.class);
        cache = mock(Cache.class);
        preferenceStore = mock(IPreferenceStore.class);
        converters = new HashMap<String, IConvert2Tcx>();

        when(window.getSelectionService()).thenReturn(service);

    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructor() {

        files = new ImportManualGpsFiles(window, "junit", databaseAccess, cache, preferenceStore, converters);

        verify(service, times(1)).addSelectionListener(files);

        assertNotNull(files);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorMitKorruptemAthlete() {
        when(preferenceStore.getString(PreferenceConstants.ATHLETE_ID)).thenReturn("");
        files = new ImportManualGpsFiles(window, "junit", databaseAccess, cache, preferenceStore, converters);

        assertNotNull(files);
    }

    @Test
    public void testConstructorMitAthlete() {
        when(preferenceStore.getString(PreferenceConstants.ATHLETE_ID)).thenReturn("1");
        files = new ImportManualGpsFiles(window, "junit", databaseAccess, cache, preferenceStore, converters);

        assertNotNull(files);
    }

    @Test
    public void disposeTest() {
        when(preferenceStore.getString(PreferenceConstants.ATHLETE_ID)).thenReturn("1");
        files = new ImportManualGpsFiles(window, "junit", databaseAccess, cache, preferenceStore, converters);
        when(window.getSelectionService()).thenReturn(service);
        files.dispose();
        verify(service, times(1)).removeSelectionListener(files);
    }
}
