package ch.opentrainingcenter.client.perspectives;

import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IViewLayout;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import ch.opentrainingcenter.client.perspectives.AthletePerspective;
import ch.opentrainingcenter.client.perspectives.OverviewPerspectiveFactory;
import ch.opentrainingcenter.client.perspectives.PerspectiveNavigation;
import ch.opentrainingcenter.client.perspectives.StatisticPerspectiveFactory;
import ch.opentrainingcenter.client.views.ahtlete.CreateAthleteView;

public class AthletePerspectiveTest {
    private AthletePerspective perspective;
    private IPageLayout layout;

    @Before
    public void before() {
        layout = Mockito.mock(IPageLayout.class);
    }

    @Test
    public void testInitialLayout() {
        // prepare
        perspective = new AthletePerspective();
        final IViewLayout viewLayout = Mockito.mock(IViewLayout.class);
        Mockito.when(layout.getViewLayout(CreateAthleteView.ID)).thenReturn(viewLayout);

        Mockito.when(layout.getEditorArea()).thenReturn("editorArea");

        // execute
        perspective.createInitialLayout(layout);
        // assert
        Mockito.verify(layout, Mockito.times(1)).setEditorAreaVisible(false);
        Mockito.verify(layout, Mockito.times(1)).addStandaloneView(CreateAthleteView.ID, false, IPageLayout.LEFT, 1f, "editorArea");

        Mockito.verify(viewLayout, Mockito.times(1)).setCloseable(false);

        Mockito.verify(layout, Mockito.times(1)).addPerspectiveShortcut(PerspectiveNavigation.ID);
        Mockito.verify(layout, Mockito.times(1)).addPerspectiveShortcut(OverviewPerspectiveFactory.ID);
        Mockito.verify(layout, Mockito.times(1)).addPerspectiveShortcut(StatisticPerspectiveFactory.ID);
        Mockito.verify(layout, Mockito.times(1)).addPerspectiveShortcut(AthletePerspective.ID);

    }
}
