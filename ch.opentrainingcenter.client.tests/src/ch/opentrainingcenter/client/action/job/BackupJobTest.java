package ch.opentrainingcenter.client.action.job;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Status;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import ch.opentrainingcenter.core.db.IDatabaseConnection;
import ch.opentrainingcenter.transfer.ITraining;

@SuppressWarnings("nls")
public class BackupJobTest {

    private BackupJob job;

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
    public void testRun() throws IOException {
        tmpFile = File.createTempFile("testRun", ".gmn");
        job = new BackupJob("junit", destFolder.getAbsolutePath(), destFolder, Collections.<ITraining> emptyList(), db);
        assertEquals(Status.OK_STATUS, job.run(monitor));
    }

    /**
     * wenn ein file nicht gefunden wird, wird trotzdem weiterexportiert und ein
     * OK zurueckgegeben.
     */
    @Test
    public void testRunWithElementFileNichtGefunden() throws IOException {
        tmpFile = File.createTempFile("testRun", ".gmn");
        final List<ITraining> trainings = new ArrayList<>();
        final ITraining training = mock(ITraining.class);
        when(training.getFileName()).thenReturn("testFile.tcx");
        trainings.add(training);
        job = new BackupJob("junit", destFolder.getAbsolutePath(), destFolder, trainings, db);
        assertEquals(Status.OK_STATUS, job.run(monitor));
    }

    @Test
    public void testRunZipErstellt() throws IOException {
        tmpFile = File.createTempFile("testRunZipErstellt", ".gmn");
        job = new BackupJob("junit", destFolder.getAbsolutePath(), destFolder, Collections.<ITraining> emptyList(), db);
        assertEquals(Status.OK_STATUS, job.run(monitor));

        final String zipFile = job.createZipFileName();
        f = new File(destFolder.getAbsolutePath(), zipFile);
        assertEquals("File muss erstellt worden sein:", true, f.exists());
    }
}
