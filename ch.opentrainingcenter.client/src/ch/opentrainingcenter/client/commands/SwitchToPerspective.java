package ch.opentrainingcenter.client.commands;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.IPerspectiveDescriptor;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.WorkbenchException;
import org.eclipse.ui.handlers.HandlerUtil;

import ch.opentrainingcenter.client.Activator;

/**
 * Abstrakter Handler um in eine andere Perspektive zu wechseln.
 */
public abstract class SwitchToPerspective extends OtcAbstractHandler {

    public SwitchToPerspective() {
        super(Activator.getDefault().getPreferenceStore());
    }

    @Override
    public Object execute(final ExecutionEvent event) throws ExecutionException {
        final IWorkbench workbench = PlatformUI.getWorkbench();
        final IWorkbenchWindow window = workbench.getActiveWorkbenchWindow();
        final IPerspectiveDescriptor desc = workbench.getPerspectiveRegistry().findPerspectiveWithId(getPerspectiveId());
        try {
            workbench.showPerspective(desc.getId(), window);
        } catch (final WorkbenchException e) {
            MessageDialog.openInformation(HandlerUtil.getActiveWorkbenchWindow(event).getShell(), "Info", "Info for you"); //$NON-NLS-1$ //$NON-NLS-2$
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
    public boolean isPerspectiveValidToShow() {
        return true;
    }
}
