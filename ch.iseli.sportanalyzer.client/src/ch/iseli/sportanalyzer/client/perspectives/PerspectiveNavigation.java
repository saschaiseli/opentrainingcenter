package ch.iseli.sportanalyzer.client.perspectives;

import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

import ch.iseli.sportanalyzer.client.views.navigation.NavigationView;
import ch.iseli.sportanalyzer.client.views.overview.SingleActivityViewPart;

public class PerspectiveNavigation implements IPerspectiveFactory {

    public static final String RIGHT_PART = "rightPart"; //$NON-NLS-1$

    public static final String ID = "ch.iseli.sportanalyzer.client.perspective"; //$NON-NLS-1$

    private static final String MULTI_VIEW = ":*"; //$NON-NLS-1$

    @Override
    public void createInitialLayout(final IPageLayout layout) {

        final String editorArea = layout.getEditorArea();
        layout.setEditorAreaVisible(false);

        layout.addStandaloneView(NavigationView.ID, false, IPageLayout.LEFT, 0.20f, editorArea);

        final IFolderLayout folderRight = layout.createFolder(RIGHT_PART, IPageLayout.RIGHT, 0.80f, editorArea);
        folderRight.addPlaceholder(SingleActivityViewPart.ID + MULTI_VIEW);

        layout.addPerspectiveShortcut(PerspectiveNavigation.ID);
        layout.addPerspectiveShortcut(OverviewPerspectiveFactory.ID);
        layout.addPerspectiveShortcut(StatisticPerspectiveFactory.ID);
        layout.addPerspectiveShortcut(AthletePerspective.ID);

    }
}
