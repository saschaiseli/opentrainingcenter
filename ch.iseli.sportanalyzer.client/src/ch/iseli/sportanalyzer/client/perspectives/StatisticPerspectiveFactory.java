package ch.iseli.sportanalyzer.client.perspectives;

import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

import ch.iseli.sportanalyzer.client.views.statistics.StatisticView;

public class StatisticPerspectiveFactory implements IPerspectiveFactory {

    public final static String ID = "ch.iseli.sportanalyzer.client.StatisticPerspective";

    @Override
    public void createInitialLayout(final IPageLayout layout) {
        final String editorArea = layout.getEditorArea();
        layout.setEditorAreaVisible(false);

        layout.addStandaloneView(StatisticView.ID, false, IPageLayout.LEFT, 1f, editorArea);
        layout.getViewLayout(StatisticView.ID).setCloseable(false);
    }

}
