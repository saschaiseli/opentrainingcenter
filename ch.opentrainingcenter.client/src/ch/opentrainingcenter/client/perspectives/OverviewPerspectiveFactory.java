package ch.opentrainingcenter.client.perspectives;

import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

import ch.opentrainingcenter.client.views.table.OverviewViewer;

public class OverviewPerspectiveFactory implements IPerspectiveFactory {

    public static final String ID = "ch.opentrainingcenter.client.OverviewPerspective"; //$NON-NLS-1$

    @Override
    public void createInitialLayout(final IPageLayout layout) {
        final String editorArea = layout.getEditorArea();
        layout.setEditorAreaVisible(false);

        layout.addStandaloneView(OverviewViewer.ID, false, IPageLayout.LEFT, 1f, editorArea);
        layout.getViewLayout(OverviewViewer.ID).setCloseable(false);

        layout.addPerspectiveShortcut(PerspectiveNavigation.ID);
        layout.addPerspectiveShortcut(OverviewPerspectiveFactory.ID);
        layout.addPerspectiveShortcut(StatisticPerspectiveFactory.ID);
        layout.addPerspectiveShortcut(AthletePerspective.ID);
    }

}
