package ch.iseli.sportanalyzer.client.perspectives;

import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

import ch.iseli.sportanalyzer.client.views.navigation.NavigationView;
import ch.iseli.sportanalyzer.client.views.overview.SingleActivityViewPart;

public class Perspective implements IPerspectiveFactory {

    public static final String RIGHT_PART = "rightPart";

    public static final String ID = "ch.iseli.sportanalyzer.client.perspective";

    private static final String MULTI_VIEW = ":*";

    @Override
    public void createInitialLayout(final IPageLayout layout) {

        final String editorArea = layout.getEditorArea();
        layout.setEditorAreaVisible(false);

        layout.addStandaloneView(NavigationView.ID, false, IPageLayout.LEFT, 0.20f, editorArea);

        final IFolderLayout folderRight = layout.createFolder(RIGHT_PART, IPageLayout.RIGHT, 0.80f, editorArea);
        folderRight.addPlaceholder(SingleActivityViewPart.ID + MULTI_VIEW);

        layout.addPerspectiveShortcut(Perspective.ID);
        layout.addPerspectiveShortcut(OverviewPerspectiveFactory.ID);
        layout.addPerspectiveShortcut(StatisticPerspectiveFactory.ID);
        layout.addPerspectiveShortcut(AthletePerspective.ID);

    }
}
