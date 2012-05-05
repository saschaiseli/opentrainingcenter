package ch.opentrainingcenter.client.action;

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
import ch.opentrainingcenter.transfer.IAthlete;
import static org.junit.Assert.assertNotNull;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ImportManualGpsFileTest {
    private ImportManualGpsFiles files;
    private IWorkbenchWindow window;
    private IDatabaseAccess databaseAccess;
    private Cache cache;
    private IPreferenceStore preferenceStore;
    private Map<String, IConvert2Tcx> converters;
    private ISelectionService service;
    private IAthlete athlete;

    @Before
    public void before() {
        window = mock(IWorkbenchWindow.class);
        service = mock(ISelectionService.class);
        databaseAccess = mock(IDatabaseAccess.class);
        cache = mock(Cache.class);
        preferenceStore = mock(IPreferenceStore.class);
        converters = new HashMap<String, IConvert2Tcx>();
        athlete = mock(IAthlete.class);
        when(window.getSelectionService()).thenReturn(service);

    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructor() {
        files = new ImportManualGpsFiles(window, "junit", databaseAccess, cache, preferenceStore, converters);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorMitKorruptemAthlete() {
        when(preferenceStore.getString(PreferenceConstants.ATHLETE_ID)).thenReturn("");
        files = new ImportManualGpsFiles(window, "junit", databaseAccess, cache, preferenceStore, converters);

        assertNotNull(files);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorMitAthleteIdAberOhneInDb() {
        when(preferenceStore.getString(PreferenceConstants.ATHLETE_ID)).thenReturn("1");
        files = new ImportManualGpsFiles(window, "junit", databaseAccess, cache, preferenceStore, converters);
    }

    @Test
    public void testConstructorMitAthleteIdUndInDb() {
        when(preferenceStore.getString(PreferenceConstants.ATHLETE_ID)).thenReturn("1");
        when(databaseAccess.getAthlete(1)).thenReturn(athlete);
        files = new ImportManualGpsFiles(window, "junit", databaseAccess, cache, preferenceStore, converters);
    }

    @Test
    public void disposeTest() {
        when(preferenceStore.getString(PreferenceConstants.ATHLETE_ID)).thenReturn("1");
        when(databaseAccess.getAthlete(1)).thenReturn(athlete);
        files = new ImportManualGpsFiles(window, "junit", databaseAccess, cache, preferenceStore, converters);
        when(window.getSelectionService()).thenReturn(service);
        files.dispose();
        verify(service, times(1)).removeSelectionListener(files);
    }
}
