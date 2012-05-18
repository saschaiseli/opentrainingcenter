package ch.opentrainingcenter.client.cache;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.widgets.ProgressBar;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import ch.opentrainingcenter.importer.IImportedConverter;
import ch.opentrainingcenter.tcx.ActivityT;
import ch.opentrainingcenter.transfer.IImported;

public class InitialLoadTest {

    private Cache cache;
    private IImportedConverter loader;
    private InitialLoad initialLoad;
    private ProgressBar fBar;

    @Before
    public void before() {
        cache = Mockito.mock(Cache.class);
        loader = Mockito.mock(IImportedConverter.class);
        fBar = Mockito.mock(ProgressBar.class);
        initialLoad = new InitialLoad(cache, loader);
    }

    @Test
    public void testEmpty() throws Exception {
        initialLoad.initalLoad(fBar, new ArrayList<IImported>());

        Mockito.verify(fBar, Mockito.times(1)).setMaximum(0);
        Mockito.verifyNoMoreInteractions(cache);
    }

    @Test
    public void testOne() throws Exception {
        // prepare
        final List<IImported> records = new ArrayList<IImported>();
        final IImported record = Mockito.mock(IImported.class);
        records.add(record);

        final ActivityT activity = Mockito.mock(ActivityT.class);
        Mockito.when(loader.convertImportedToActivity(record)).thenReturn(activity);
        // execute
        initialLoad.initalLoad(fBar, records);

        Mockito.verify(fBar, Mockito.times(1)).setMaximum(1);
        Mockito.verify(fBar, Mockito.times(1)).setSelection(1);
        Mockito.verify(cache, Mockito.times(1)).add(activity);
        Mockito.verifyNoMoreInteractions(cache);
    }
}
