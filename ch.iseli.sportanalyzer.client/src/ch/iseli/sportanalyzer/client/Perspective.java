package ch.iseli.sportanalyzer.client;

import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

import ch.iseli.sportanalyzer.client.views.navigation.NavigationView;
import ch.iseli.sportanalyzer.client.views.overview.SingleActivityViewPart;

public class Perspective implements IPerspectiveFactory {

    /**
     * The ID of the perspective as specified in the extension.
     */
    public static final String ID = "ch.iseli.sportanalyzer.client.perspective";

    @Override
    public void createInitialLayout(final IPageLayout layout) {
        final String editorArea = layout.getEditorArea();
        layout.setEditorAreaVisible(false);

        layout.addStandaloneView(NavigationView.ID, false, IPageLayout.LEFT, 0.25f, editorArea);
        layout.getViewLayout(NavigationView.ID).setCloseable(false);

        final IFolderLayout folder = layout.createFolder("rightPart", IPageLayout.TOP, 0.75f, editorArea);
        folder.addPlaceholder(SingleActivityViewPart.ID + ":*");
        //
        // final IFolderLayout folderBottom = layout.createFolder("bottomPart", IPageLayout.BOTTOM, 0.25f, editorArea);
        // folderBottom.addPlaceholder(GoogleMapViewer.ID + ":*");
    }
}
