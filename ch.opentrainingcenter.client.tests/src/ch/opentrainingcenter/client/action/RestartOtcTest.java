package ch.opentrainingcenter.client.action;

import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWindow;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SuppressWarnings("nls")
public class RestartOtcTest {
    private RestartOtc restart;
    private IWorkbenchWindow window;
    private IWorkbench workbench;

    @Before
    public void before() {
        window = mock(IWorkbenchWindow.class);
        workbench = mock(IWorkbench.class);
        restart = new RestartOtc(window, "Junit");
    }

    @Test
    public void testRun() {
        // prepare
        when(window.getWorkbench()).thenReturn(workbench);
        // execute
        restart.run();
        // assert
        verify(workbench, times(1)).restart();
    }

    @Test
    public void testDispose() {
        restart.dispose();
    }

    @Test
    public void testSelectionChanged() {
        restart.selectionChanged(null, null);
    }
}
