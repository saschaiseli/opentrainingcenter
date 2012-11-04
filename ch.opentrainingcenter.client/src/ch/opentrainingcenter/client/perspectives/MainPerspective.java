package ch.opentrainingcenter.client.perspectives;

import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

import ch.opentrainingcenter.client.views.ApplicationContext;
import ch.opentrainingcenter.client.views.best.BestRunsView;
import ch.opentrainingcenter.client.views.navigation.KalenderWocheNavigationView;
import ch.opentrainingcenter.client.views.overview.SingleActivityViewPart;
import ch.opentrainingcenter.client.views.planung.JahresplanungViewPart;
import ch.opentrainingcenter.client.views.weeks.WeeklyOverview;

public class MainPerspective implements IPerspectiveFactory {

    public static final String RIGHT_PART = "rightPart"; //$NON-NLS-1$

    public static final String ID = "ch.opentrainingcenter.client.perspective"; //$NON-NLS-1$

    static final String MULTI_VIEW = ":*"; //$NON-NLS-1$

    @Override
    public void createInitialLayout(final IPageLayout layout) {

        final String editorArea = layout.getEditorArea();
        layout.setEditorAreaVisible(false);

        // layout.addStandaloneView(KalenderWocheNavigationView.ID, false,
        // IPageLayout.LEFT, 0.20f, editorArea);
        layout.addView(KalenderWocheNavigationView.ID, IPageLayout.LEFT, 0.15f, editorArea);
        layout.getViewLayout(KalenderWocheNavigationView.ID).setCloseable(false);

        final IFolderLayout folderMiddle = layout.createFolder(RIGHT_PART, IPageLayout.LEFT, 0.75f, editorArea);

        if (ApplicationContext.getApplicationContext().getSelectedId() != null) {
            folderMiddle.addView(SingleActivityViewPart.ID + MULTI_VIEW);
        } else {
            folderMiddle.addPlaceholder(SingleActivityViewPart.ID + MULTI_VIEW);
        }

        folderMiddle.addPlaceholder(JahresplanungViewPart.ID);

        layout.addStandaloneView(WeeklyOverview.ID, false, IPageLayout.RIGHT, 0.10f, editorArea);
        layout.getViewLayout(WeeklyOverview.ID).setCloseable(false);

        layout.addStandaloneView(BestRunsView.ID, false, IPageLayout.BOTTOM, 0.50f, WeeklyOverview.ID);
        layout.getViewLayout(BestRunsView.ID).setCloseable(false);

        layout.addPerspectiveShortcut(MainPerspective.ID);
        layout.addPerspectiveShortcut(TablePerspective.ID);
        layout.addPerspectiveShortcut(StatisticPerspective.ID);
        layout.addPerspectiveShortcut(AthletePerspective.ID);

    }
}
