package ch.opentrainingcenter.core.importer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.Mockito;

import ch.opentrainingcenter.core.cache.Cache;
import ch.opentrainingcenter.transfer.ActivityExtension;
import ch.opentrainingcenter.transfer.CommonTransferFactory;
import ch.opentrainingcenter.transfer.ITraining;

@SuppressWarnings("nls")
public class LoadActivityJobTest {

    private ITraining record;
    private Cache cache;
    private IImportedConverter loader;

    @Before
    public void before() throws IOException {
        record = Mockito.mock(ITraining.class);
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
        final ITraining activity = Mockito.mock(ITraining.class);
        final String text = "eben geladen";
        Mockito.when(activity.getNote()).thenReturn(text);

        Mockito.when(loader.convertImportedToActivity((ITraining) Matchers.any())).thenReturn(activity);
        final LoadActivityJob job = new LoadActivityJob("Junit", record, cache, loader);
        final IProgressMonitor monitor = Mockito.mock(IProgressMonitor.class);
        // execute
        job.run(monitor);
        // assert
        final ITraining loaded = job.getLoaded();
        assertEquals("Activity geladen", text, loaded.getNote());
    }

    @Test
    public void testKannRecordNichtLesen() throws Exception {
        // prepare
        final ITraining activity = Mockito.mock(ITraining.class);
        final String text = "eben geladen";
        Mockito.when(activity.getNote()).thenReturn(text);

        final List<Object> list = new ArrayList<Object>();
        list.add(new ActivityExtension(text, CommonTransferFactory.createDefaultWeather(), null));

        Mockito.when(loader.convertImportedToActivity((ITraining) Matchers.any())).thenThrow(new LoadImportedException("junit"));
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
        Mockito.when(record.getDatum()).thenReturn(date.getTime());
        Mockito.when(cache.contains(date.getTime())).thenReturn(true);

        final ITraining activity = Mockito.mock(ITraining.class);
        final String text = "eben geladen";
        Mockito.when(activity.getNote()).thenReturn(text);

        Mockito.when(cache.get(date.getTime())).thenReturn(activity);
        final LoadActivityJob job = new LoadActivityJob("Junit", record, cache, loader);
        final IProgressMonitor monitor = Mockito.mock(IProgressMonitor.class);
        // execute
        job.run(monitor);
        // assert
        final ITraining loaded = job.getLoaded();

        assertEquals("Activity geladen", text, loaded.getNote());
    }
}
