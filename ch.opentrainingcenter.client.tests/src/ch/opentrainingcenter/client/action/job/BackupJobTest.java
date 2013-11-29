package ch.opentrainingcenter.client.action.job;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Status;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import ch.opentrainingcenter.core.db.IDatabaseConnection;
import ch.opentrainingcenter.core.exceptions.ConvertException;
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

    private IDatabaseConnection db;

    private File file;

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
            public ITraining convert(final File file) throws ConvertException {
                return null;
            }
        });
        monitor = new IProgressMonitor() {

            @Override
            public void worked(final int work) {
                // do nothing
            }

            @Override
            public void subTask(final String name) {
                // do nothing
            }

            @Override
            public void setTaskName(final String name) {
                // do nothing
            }

            @Override
            public void setCanceled(final boolean value) {
                // do nothing
            }

            @Override
            public boolean isCanceled() {
                return false;
            }

            @Override
            public void internalWorked(final double work) {
                // do nothing
            }

            @Override
            public void done() {
                // do nothing
            }

            @Override
            public void beginTask(final String name, final int totalWork) {
                // do nothing
            }
        };
        db = mock(IDatabaseConnection.class);
        file = File.createTempFile("backup", ".sql");
        when(db.backUpDatabase(Mockito.anyString())).thenReturn(file);
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
        job = new BackupJob("junit", destFolder.getAbsolutePath(), destFolder, converters, db);
        final String[] fileToCopy = job.getFileToCopy();
        assertNotNull("array mit den files zum kopieren darf nicht null sein", fileToCopy);
    }

    @Test
    public void testRun() throws IOException {
        tmpFile = File.createTempFile("testRun", ".gmn");
        job = new BackupJob("junit", destFolder.getAbsolutePath(), destFolder, converters, db);
        assertEquals(Status.OK_STATUS, job.run(monitor));
    }

    @Test
    public void testRunZipErstellt() throws IOException {
        tmpFile = File.createTempFile("testRunZipErstellt", ".gmn");
        job = new BackupJob("junit", destFolder.getAbsolutePath(), destFolder, converters, db);
        assertEquals(Status.OK_STATUS, job.run(monitor));

        final String zipFile = job.createZipFileName();
        f = new File(destFolder.getAbsolutePath(), zipFile);
        assertEquals("File muss erstellt worden sein:", true, f.exists());
    }
}
