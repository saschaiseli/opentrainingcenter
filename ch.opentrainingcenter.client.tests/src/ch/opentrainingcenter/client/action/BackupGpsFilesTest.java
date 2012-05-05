package ch.opentrainingcenter.client.action;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class BackupGpsFilesTest {
    private BackupGpsFiles backupGpsFiles;
    private MyJob myJob;

    private class MyJob extends Job {
        private boolean isJobStarted = false;

        public MyJob(final String name) {
            super(name);
        }

        @Override
        protected IStatus run(final IProgressMonitor monitor) {
            isJobStarted = true;
            return Status.OK_STATUS;
        }

        public boolean isJobStarted() {
            return isJobStarted;
        }
    }

    @Before
    public void setUp() {
        myJob = new MyJob("junit");
    }

    @Test
    public void testID() {
        backupGpsFiles = new BackupGpsFiles("tooltip", myJob);
        assertEquals(BackupGpsFiles.ID, backupGpsFiles.getId());
    }

    @Test
    public void testTooltip() {
        backupGpsFiles = new BackupGpsFiles("tooltip", myJob);
        assertEquals("tooltip", backupGpsFiles.getToolTipText());
    }

    @Test(timeout = 10000)
    public void testRun() throws InterruptedException {
        backupGpsFiles = new BackupGpsFiles("tooltip", myJob);
        backupGpsFiles.run();
        Thread.sleep(1000);
        assertTrue("Job wurde gestarted", myJob.isJobStarted());
    }
}
