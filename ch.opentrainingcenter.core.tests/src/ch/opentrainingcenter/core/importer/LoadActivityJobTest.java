package ch.opentrainingcenter.core.importer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import ch.opentrainingcenter.core.cache.Cache;
import ch.opentrainingcenter.tcx.ActivityT;
import ch.opentrainingcenter.tcx.ExtensionsT;
import ch.opentrainingcenter.transfer.ActivityExtension;
import ch.opentrainingcenter.transfer.CommonTransferFactory;
import ch.opentrainingcenter.transfer.IImported;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

@SuppressWarnings("nls")
public class LoadActivityJobTest {

    private IImported record;
    private Cache cache;
    private IImportedConverter loader;

    @Before
    public void before() throws IOException {
        record = Mockito.mock(IImported.class);
        cache = Mockito.mock(Cache.class);
        loader = Mockito.mock(IImportedConverter.class);
    }

    @Test
    public void test() {

        final LoadActivityJob job = new LoadActivityJob("Junit", record, cache, loader);
        assertNotNull(job);
    }

    @Test
    public void testSelected() {
        final LoadActivityJob job = new LoadActivityJob("Junit", record, cache, loader);
        assertNull("Noch nichts geladen", job.getLoaded());
    }

    @Test
    public void testRun() {
        final LoadActivityJob job = new LoadActivityJob("Junit", record, cache, loader);
        final IProgressMonitor monitor = Mockito.mock(IProgressMonitor.class);
        final IStatus status = job.run(monitor);
        assertEquals("Job durchgelaufen", Status.OK_STATUS, status);
    }

    @Test
    public void testConvertRecord() throws Exception {
        // prepare
        final ActivityT activity = Mockito.mock(ActivityT.class);
        final String text = "eben geladen";
        final ExtensionsT mock = Mockito.mock(ExtensionsT.class);
        final List<Object> list = new ArrayList<Object>();
        list.add(new ActivityExtension(text, CommonTransferFactory.createDefaultWeather()));
        Mockito.when(mock.getAny()).thenReturn(list);
        Mockito.when(activity.getExtensions()).thenReturn(mock);

        Mockito.when(loader.convertImportedToActivity((IImported) Mockito.any())).thenReturn(activity);
        final LoadActivityJob job = new LoadActivityJob("Junit", record, cache, loader);
        final IProgressMonitor monitor = Mockito.mock(IProgressMonitor.class);
        // execute
        job.run(monitor);
        // assert
        final ActivityT loaded = job.getLoaded();
        final ExtensionsT extensions = loaded.getExtensions();
        ActivityExtension ae = new ActivityExtension();
        if (extensions != null) {
            final Object object = extensions.getAny().get(0);
            if (object != null) {
                ae = (ActivityExtension) object;
            }
        }
        assertEquals("Activity geladen", text, ae.getNote());
    }

    @Test
    public void testKannRecordNichtLesen() throws Exception {
        // prepare
        final ActivityT activity = Mockito.mock(ActivityT.class);
        final String text = "eben geladen";
        final ExtensionsT mock = Mockito.mock(ExtensionsT.class);
        final List<Object> list = new ArrayList<Object>();
        list.add(new ActivityExtension(text, CommonTransferFactory.createDefaultWeather()));
        Mockito.when(mock.getAny()).thenReturn(list);
        Mockito.when(activity.getExtensions()).thenReturn(mock);

        Mockito.when(loader.convertImportedToActivity((IImported) Mockito.any())).thenThrow(new LoadImportedException("junit"));
        final LoadActivityJob job = new LoadActivityJob("Junit", record, cache, loader);
        final IProgressMonitor monitor = Mockito.mock(IProgressMonitor.class);
        // execute
        final IStatus status = job.run(monitor);
        // assert
        assertEquals("Job nicht durchgelaufen", Status.CANCEL_STATUS, status);
    }

    @Test
    public void testLoadFromCache() throws Exception {
        // prepare
        final Date date = new Date();
        Mockito.when(record.getActivityId()).thenReturn(date);
        Mockito.when(cache.contains(date)).thenReturn(true);

        final ActivityT activity = Mockito.mock(ActivityT.class);
        final String text = "eben geladen";
        final ExtensionsT mock = Mockito.mock(ExtensionsT.class);
        final List<Object> list = new ArrayList<Object>();
        list.add(new ActivityExtension(text, CommonTransferFactory.createDefaultWeather()));
        Mockito.when(mock.getAny()).thenReturn(list);
        Mockito.when(activity.getExtensions()).thenReturn(mock);

        Mockito.when(cache.get(date)).thenReturn(activity);
        final LoadActivityJob job = new LoadActivityJob("Junit", record, cache, loader);
        final IProgressMonitor monitor = Mockito.mock(IProgressMonitor.class);
        // execute
        job.run(monitor);
        // assert
        final ActivityT loaded = job.getLoaded();
        final ExtensionsT extensions = loaded.getExtensions();
        ActivityExtension ae = new ActivityExtension();
        if (extensions != null) {
            final Object object = extensions.getAny().get(0);
            if (object != null) {
                ae = (ActivityExtension) object;
            }
        }
        assertEquals("Activity geladen", text, ae.getNote());
    }
}
