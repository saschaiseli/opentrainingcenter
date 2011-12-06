package ch.iseli.sportanalyzer.client;

import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

import ch.iseli.sportanalyzer.client.views.table.OverviewViewer;

public class OverviewPerspectiveFactory implements IPerspectiveFactory {

    public static final String ID = "ch.iseli.sportanalyzer.client.OverviewPerspective";

    @Override
    public void createInitialLayout(final IPageLayout layout) {
        final String editorArea = layout.getEditorArea();
        layout.setEditorAreaVisible(false);

        layout.addStandaloneView(OverviewViewer.ID, false, IPageLayout.LEFT, 1f, editorArea);
        layout.getViewLayout(OverviewViewer.ID).setCloseable(false);

    }

}
