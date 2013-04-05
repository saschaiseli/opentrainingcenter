package ch.opentrainingcenter.client.action.job;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Status;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import ch.opentrainingcenter.core.importer.IConvert2Tcx;
import ch.opentrainingcenter.transfer.ITraining;

@SuppressWarnings("nls")
public class BackupJobTest {

    private BackupJob job;

    private final Map<String, IConvert2Tcx> converters = new HashMap<String, IConvert2Tcx>();

    private IProgressMonitor monitor;

    private File destFolder;

    private File tmpFile;

    private File f;

    private File tmp;

    @Before
    public void setUp() throws IOException {
        tmp = File.createTempFile("setUp", "");
        destFolder = tmp.getParentFile();
        converters.put("gmn", new IConvert2Tcx() {

            @Override
            public String getName() {
                return null;
            }

            @Override
            public String getFilePrefix() {
                return "gmn";
            }

            @Override
            public List<ITraining> convertActivity(final File file) throws Exception {
                return null;
            }
        });
        monitor = new IProgressMonitor() {

            @Override
            public void worked(final int work) {
            }

            @Override
            public void subTask(final String name) {
            }

            @Override
            public void setTaskName(final String name) {
            }

            @Override
            public void setCanceled(final boolean value) {
            }

            @Override
            public boolean isCanceled() {
                return false;
            }

            @Override
            public void internalWorked(final double work) {
            }

            @Override
            public void done() {
            }

            @Override
            public void beginTask(final String name, final int totalWork) {
            }
        };

    }

    @After
    public void after() {
        tmpFile.deleteOnExit();
        tmp.deleteOnExit();
        destFolder.delete();
        if (f != null && f.exists()) {
            f.deleteOnExit();
        }
    }

    @Test
    public void testConstructor() throws IOException {
        tmpFile = File.createTempFile("testConstructor", ".gmn");
        job = new BackupJob("junit", destFolder.getAbsolutePath(), destFolder, converters);
        final String[] fileToCopy = job.getFileToCopy();
        assertNotNull("array mit den files zum kopieren darf nicht null sein", fileToCopy);
    }

    @Test
    public void testRun() throws IOException {
        tmpFile = File.createTempFile("testRun", ".gmn");
        job = new BackupJob("junit", destFolder.getAbsolutePath(), destFolder, converters);
        assertEquals(Status.OK_STATUS, job.run(monitor));
    }

    @Test
    public void testRunZipErstellt() throws IOException {
        tmpFile = File.createTempFile("testRunZipErstellt", ".gmn");
        job = new BackupJob("junit", destFolder.getAbsolutePath(), destFolder, converters);
        assertEquals(Status.OK_STATUS, job.run(monitor));

        final String zipFile = job.createZipFileName();
        f = new File(destFolder.getAbsolutePath(), zipFile);
        assertEquals("File muss erstellt worden sein:", true, f.exists());
    }
}
