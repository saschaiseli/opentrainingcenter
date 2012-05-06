package ch.opentrainingcenter.importer;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import ch.opentrainingcenter.client.cache.Cache;
import ch.opentrainingcenter.db.IDatabaseAccess;
import ch.opentrainingcenter.transfer.IAthlete;
import ch.opentrainingcenter.transfer.IImported;
import static org.junit.Assert.assertNotNull;

public class LoadJobTest {
    private LoadJob job;
    private IAthlete athlete;
    private IDatabaseAccess databaseAccess;
    private Cache cache;

    @Before
    public void before() {
        athlete = Mockito.mock(IAthlete.class);
        databaseAccess = Mockito.mock(IDatabaseAccess.class);
        cache = Mockito.mock(Cache.class);
    }

    @Test
    public void testConstructor() {
        job = new LoadJob("Junit", athlete, databaseAccess, cache);
        assertNotNull(job);
    }

    @Test
    public void testRun() {
        job = new LoadJob("Junit", athlete, databaseAccess, cache);
        final IProgressMonitor monitor = Mockito.mock(IProgressMonitor.class);
        final List<IImported> values = new ArrayList<IImported>();
        final IImported value = Mockito.mock(IImported.class);
        values.add(value);
        Mockito.when(databaseAccess.getAllImported(athlete)).thenReturn(values);
        // execute
        job.run(monitor);
        // assert
        Mockito.verify(cache, Mockito.times(1)).setSelectedProfile(athlete);
        Mockito.verify(cache, Mockito.times(1)).addAllImported(values);
    }
}
