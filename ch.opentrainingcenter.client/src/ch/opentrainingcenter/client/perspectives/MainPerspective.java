package ch.opentrainingcenter.client.perspectives;

import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

import ch.opentrainingcenter.client.views.ApplicationContext;
import ch.opentrainingcenter.client.views.best.BestRunsView;
import ch.opentrainingcenter.client.views.chart.ChartViewPart;
import ch.opentrainingcenter.client.views.navigation.KalenderWocheNavigationView;
import ch.opentrainingcenter.client.views.ngchart.DynamicChartViewPart;
import ch.opentrainingcenter.client.views.overview.SingleActivityViewPart;
import ch.opentrainingcenter.client.views.planung.JahresplanungViewPart;
import ch.opentrainingcenter.client.views.planung.ShowJahresplanungViewPart;
import ch.opentrainingcenter.client.views.routen.RoutenView;
import ch.opentrainingcenter.client.views.shoes.ShoeViewPart;
import ch.opentrainingcenter.client.views.weeks.MonthlyWeekOverview;

public class MainPerspective implements IPerspectiveFactory {

    private static final float LEFT = 0.15f;
    private static final float MIDDLE = 0.75f;
    public static final String RIGHT_PART = "rightPart"; //$NON-NLS-1$

    public static final String ID = "ch.opentrainingcenter.client.perspective"; //$NON-NLS-1$

    private static final String MULTI_VIEW = ":*"; //$NON-NLS-1$

    @Override
    public void createInitialLayout(final IPageLayout layout) {

        final String editorArea = layout.getEditorArea();
        layout.setEditorAreaVisible(false);

        layout.addView(KalenderWocheNavigationView.ID, IPageLayout.LEFT, LEFT, editorArea);
        layout.addStandaloneView(MonthlyWeekOverview.ID, false, IPageLayout.BOTTOM, 0.70F, KalenderWocheNavigationView.ID);

        layout.getViewLayout(KalenderWocheNavigationView.ID).setCloseable(false);

        final IFolderLayout folderMiddle = layout.createFolder(RIGHT_PART, IPageLayout.LEFT, MIDDLE, editorArea);

        if (ApplicationContext.getApplicationContext().getSelectedId() != null) {
            folderMiddle.addView(SingleActivityViewPart.ID + MULTI_VIEW);
        } else {
            folderMiddle.addPlaceholder(SingleActivityViewPart.ID + MULTI_VIEW);
        }
        folderMiddle.addPlaceholder(ShoeViewPart.ID);
        folderMiddle.addPlaceholder(RoutenView.ID);
        folderMiddle.addPlaceholder(JahresplanungViewPart.ID);
        folderMiddle.addPlaceholder(ShowJahresplanungViewPart.ID);
        folderMiddle.addPlaceholder(ChartViewPart.ID + MULTI_VIEW);
        folderMiddle.addPlaceholder(DynamicChartViewPart.ID);
        folderMiddle.addView(BestRunsView.ID);
        layout.getViewLayout(BestRunsView.ID).setCloseable(false);
        layout.getViewLayout(MonthlyWeekOverview.ID).setCloseable(false);

        layout.addPerspectiveShortcut(MainPerspective.ID);
        layout.addPerspectiveShortcut(EinstellungenPerspective.ID);

    }
}
