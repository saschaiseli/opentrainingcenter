package ch.opentrainingcenter.client.perspectives;

import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

import ch.opentrainingcenter.client.views.ApplicationContext;
import ch.opentrainingcenter.client.views.best.BestRunsView;
import ch.opentrainingcenter.client.views.navigation.KalenderWocheNavigationView;
import ch.opentrainingcenter.client.views.overview.SingleActivityViewPart;
import ch.opentrainingcenter.client.views.planung.JahresplanungViewPart;
import ch.opentrainingcenter.client.views.planung.ShowJahresplanungViewPart;
import ch.opentrainingcenter.client.views.weeks.MonthlyOverview;
import ch.opentrainingcenter.client.views.weeks.WeeklyOverview;

public class MainPerspective implements IPerspectiveFactory {

    private static final float LEFT = 0.15f;
    private static final float MIDDLE = 0.75f;
    private static final float RIGHT = 0.15f;

    public static final String RIGHT_PART = "rightPart"; //$NON-NLS-1$

    public static final String ID = "ch.opentrainingcenter.client.perspective"; //$NON-NLS-1$

    static final String MULTI_VIEW = ":*"; //$NON-NLS-1$

    @Override
    public void createInitialLayout(final IPageLayout layout) {

        final String editorArea = layout.getEditorArea();
        layout.setEditorAreaVisible(false);

        layout.addView(KalenderWocheNavigationView.ID, IPageLayout.LEFT, LEFT, editorArea);
        layout.getViewLayout(KalenderWocheNavigationView.ID).setCloseable(false);

        final IFolderLayout folderMiddle = layout.createFolder(RIGHT_PART, IPageLayout.LEFT, MIDDLE, editorArea);

        if (ApplicationContext.getApplicationContext().getSelectedId() != null) {
            folderMiddle.addView(SingleActivityViewPart.ID + MULTI_VIEW);
        } else {
            folderMiddle.addPlaceholder(SingleActivityViewPart.ID + MULTI_VIEW);
        }

        folderMiddle.addPlaceholder(JahresplanungViewPart.ID);
        folderMiddle.addPlaceholder(ShowJahresplanungViewPart.ID);

        layout.addStandaloneView(WeeklyOverview.ID, false, IPageLayout.RIGHT, RIGHT, editorArea);
        layout.addStandaloneView(BestRunsView.ID, false, IPageLayout.BOTTOM, 0.5f, WeeklyOverview.ID);
        layout.addStandaloneView(MonthlyOverview.ID, false, IPageLayout.BOTTOM, 0.5f, WeeklyOverview.ID);

        layout.getViewLayout(BestRunsView.ID).setCloseable(false);
        layout.getViewLayout(WeeklyOverview.ID).setCloseable(false);
        layout.getViewLayout(MonthlyOverview.ID).setCloseable(false);

        layout.addPerspectiveShortcut(MainPerspective.ID);
        layout.addPerspectiveShortcut(TablePerspective.ID);
        layout.addPerspectiveShortcut(StatisticPerspective.ID);
        layout.addPerspectiveShortcut(EinstellungenPerspective.ID);

    }
}
