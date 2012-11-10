package ch.opentrainingcenter.client.action.job;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mockito;

import ch.opentrainingcenter.core.cache.Cache;
import ch.opentrainingcenter.core.importer.ConvertContainer;
import ch.opentrainingcenter.core.importer.IConvert2Tcx;
import ch.opentrainingcenter.importer.IFileImport;
import ch.opentrainingcenter.model.importer.IGpsFileModel;
import ch.opentrainingcenter.model.importer.IGpsFileModelWrapper;
import ch.opentrainingcenter.tcx.ActivityT;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ImportManualJobTest {
    private ImportManualJob job;
    private IGpsFileModelWrapper modelWrapper;
    private Cache cache;
    private String filterPath;
    private ConvertContainer cc;
    private IFileImport importer;

    @Before
    public void before() throws IOException {
        cc = Mockito.mock(ConvertContainer.class);
        modelWrapper = Mockito.mock(IGpsFileModelWrapper.class);
        filterPath = "";
        cache = Mockito.mock(Cache.class);
        importer = Mockito.mock(IFileImport.class);
    }

    @Test
    public void testConstructor() {

        job = new ImportManualJob("junit", modelWrapper, filterPath, importer, cache);
        assertNotNull(job);
    }

    @Test
    @Ignore
    public void testRun() {
        job = new ImportManualJob("junit", modelWrapper, filterPath, importer, cache);

        final IProgressMonitor monitor = Mockito.mock(IProgressMonitor.class);
        final IStatus status = job.run(monitor);

        assertEquals(Status.OK_STATUS, status);
    }

    @Test(timeout = 3000)
    @Ignore
    public void testRunMitFileModel() throws Exception {
        job = new ImportManualJob("junit", modelWrapper, filterPath, importer, cache);

        final List<IGpsFileModel> models = new ArrayList<IGpsFileModel>();
        final IGpsFileModel model = Mockito.mock(IGpsFileModel.class);
        models.add(model);
        Mockito.when(model.getFileName()).thenReturn("gmn");

        Mockito.when(modelWrapper.getGpsFileModels()).thenReturn(models);

        final IConvert2Tcx convert = Mockito.mock(IConvert2Tcx.class);
        Mockito.when(cc.getMatchingConverter(new File(""))).thenReturn(convert);
        final List<ActivityT> activities = new ArrayList<ActivityT>();
        final ActivityT element = Mockito.mock(ActivityT.class);
        activities.add(element);
        Mockito.when(convert.convertActivity(new File(""))).thenReturn(activities);

        final IProgressMonitor monitor = Mockito.mock(IProgressMonitor.class);
        Mockito.when(importer.importFile(filterPath, modelWrapper, monitor)).thenReturn(new ArrayList<ActivityT>());

        final IStatus status = job.run(monitor);

        assertEquals(Status.OK_STATUS, status);
    }
}
