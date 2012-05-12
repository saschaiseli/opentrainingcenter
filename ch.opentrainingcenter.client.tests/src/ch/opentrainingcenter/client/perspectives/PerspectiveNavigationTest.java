package ch.opentrainingcenter.client.perspectives;

import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IViewLayout;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import ch.opentrainingcenter.client.cache.Cache;
import ch.opentrainingcenter.client.model.ISimpleTraining;
import ch.opentrainingcenter.client.views.best.BestRunsView;
import ch.opentrainingcenter.client.views.navigation.NavigationView;
import ch.opentrainingcenter.client.views.overview.SingleActivityViewPart;
import ch.opentrainingcenter.client.views.weeks.WeeklyOverview;

public class PerspectiveNavigationTest {
    private PerspectiveNavigation perspective;
    private IPageLayout layout;
    private Cache cache;

    @Before
    public void before() {
        layout = Mockito.mock(IPageLayout.class);
        cache = Mockito.mock(Cache.class);
    }

    @Test
    public void testInitialLayoutCacheNichtsSelektiert() {
        // prepare
        perspective = new PerspectiveNavigation(cache);
        final IViewLayout viewLayout = Mockito.mock(IViewLayout.class);
        Mockito.when(layout.getViewLayout(NavigationView.ID)).thenReturn(viewLayout);
        final IViewLayout viewLayoutWeek = Mockito.mock(IViewLayout.class);
        Mockito.when(layout.getViewLayout(WeeklyOverview.ID)).thenReturn(viewLayoutWeek);

        final IViewLayout viewLayoutBest = Mockito.mock(IViewLayout.class);
        Mockito.when(layout.getViewLayout(BestRunsView.ID)).thenReturn(viewLayoutBest);

        Mockito.when(layout.getEditorArea()).thenReturn("editorArea");

        final IFolderLayout folder = Mockito.mock(IFolderLayout.class);
        Mockito.when(layout.createFolder(PerspectiveNavigation.RIGHT_PART, IPageLayout.LEFT, 0.70f, "editorArea")).thenReturn(folder);
        // execute
        perspective.createInitialLayout(layout);
        // assert

        Mockito.verify(folder, Mockito.times(1)).addPlaceholder(SingleActivityViewPart.ID + PerspectiveNavigation.MULTI_VIEW);

        Mockito.verify(layout, Mockito.times(1)).addStandaloneView(WeeklyOverview.ID, false, IPageLayout.RIGHT, 0.10f, "editorArea");
        Mockito.verify(viewLayoutWeek, Mockito.times(1)).setCloseable(false);

        Mockito.verify(layout, Mockito.times(1)).addStandaloneView(BestRunsView.ID, false, IPageLayout.BOTTOM, 0.50f, WeeklyOverview.ID);
        Mockito.verify(viewLayoutBest, Mockito.times(1)).setCloseable(false);

        Mockito.verify(layout, Mockito.times(1)).addPerspectiveShortcut(PerspectiveNavigation.ID);
        Mockito.verify(layout, Mockito.times(1)).addPerspectiveShortcut(OverviewPerspectiveFactory.ID);
        Mockito.verify(layout, Mockito.times(1)).addPerspectiveShortcut(StatisticPerspectiveFactory.ID);
        Mockito.verify(layout, Mockito.times(1)).addPerspectiveShortcut(AthletePerspective.ID);
    }

    @Test
    public void testInitialLayoutCacheSelektiert() {
        // prepare
        perspective = new PerspectiveNavigation(cache);
        final IViewLayout viewLayout = Mockito.mock(IViewLayout.class);
        Mockito.when(layout.getViewLayout(NavigationView.ID)).thenReturn(viewLayout);
        final IViewLayout viewLayoutWeek = Mockito.mock(IViewLayout.class);
        Mockito.when(layout.getViewLayout(WeeklyOverview.ID)).thenReturn(viewLayoutWeek);

        final IViewLayout viewLayoutBest = Mockito.mock(IViewLayout.class);
        Mockito.when(layout.getViewLayout(BestRunsView.ID)).thenReturn(viewLayoutBest);

        Mockito.when(layout.getEditorArea()).thenReturn("editorArea");

        final IFolderLayout folder = Mockito.mock(IFolderLayout.class);
        Mockito.when(layout.createFolder(PerspectiveNavigation.RIGHT_PART, IPageLayout.LEFT, 0.70f, "editorArea")).thenReturn(folder);

        final ISimpleTraining simpleTraining = Mockito.mock(ISimpleTraining.class);
        Mockito.when(cache.getSelectedOverview()).thenReturn(simpleTraining);

        // execute
        perspective.createInitialLayout(layout);
        // assert

        Mockito.verify(folder, Mockito.times(1)).addView(SingleActivityViewPart.ID + PerspectiveNavigation.MULTI_VIEW);

        Mockito.verify(layout, Mockito.times(1)).addStandaloneView(WeeklyOverview.ID, false, IPageLayout.RIGHT, 0.10f, "editorArea");
        Mockito.verify(viewLayoutWeek, Mockito.times(1)).setCloseable(false);

        Mockito.verify(layout, Mockito.times(1)).addStandaloneView(BestRunsView.ID, false, IPageLayout.BOTTOM, 0.50f, WeeklyOverview.ID);
        Mockito.verify(viewLayoutBest, Mockito.times(1)).setCloseable(false);

        Mockito.verify(layout, Mockito.times(1)).addPerspectiveShortcut(PerspectiveNavigation.ID);
        Mockito.verify(layout, Mockito.times(1)).addPerspectiveShortcut(OverviewPerspectiveFactory.ID);
        Mockito.verify(layout, Mockito.times(1)).addPerspectiveShortcut(StatisticPerspectiveFactory.ID);
        Mockito.verify(layout, Mockito.times(1)).addPerspectiveShortcut(AthletePerspective.ID);
    }
}
