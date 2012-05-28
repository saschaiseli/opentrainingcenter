package ch.opentrainingcenter.client.commands;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.IPerspectiveDescriptor;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.WorkbenchException;
import org.eclipse.ui.handlers.HandlerUtil;

public abstract class SwitchToPerspective extends AbstractHandler {

    @Override
    public Object execute(final ExecutionEvent event) throws ExecutionException {
        final IWorkbench workbench = PlatformUI.getWorkbench();
        final IWorkbenchWindow window = workbench.getActiveWorkbenchWindow();
        final IPerspectiveDescriptor desc = workbench.getPerspectiveRegistry().findPerspectiveWithId(getPerspectiveId());
        try {
            workbench.showPerspective(desc.getId(), window);
        } catch (final WorkbenchException e) {
            MessageDialog.openInformation(HandlerUtil.getActiveWorkbenchWindow(event).getShell(), "Info", "Info for you");
        }
        return null;
    }

    /**
     * @return die ID der Perspective Factory
     */
    abstract String getPerspectiveId();

    /**
     * Testet ob die active Page von der selben Perspective ist.
     */
    abstract boolean isSamePerspective(String perspectiveId);

    /**
     * @return true, wenn alle daten vorhanden sind um die perspective
     *         anzuzeigen
     */
    abstract boolean isPerspectiveValidToShow();
}
