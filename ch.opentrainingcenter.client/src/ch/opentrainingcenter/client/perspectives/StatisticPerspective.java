package ch.opentrainingcenter.client.perspectives;

import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

import ch.opentrainingcenter.client.views.statistics.StatisticView;

public class StatisticPerspective implements IPerspectiveFactory {

    public static final String ID = "ch.opentrainingcenter.client.StatisticPerspective"; //$NON-NLS-1$

    @Override
    public void createInitialLayout(final IPageLayout layout) {
        final String editorArea = layout.getEditorArea();
        layout.setEditorAreaVisible(false);

        layout.addStandaloneView(StatisticView.ID, false, IPageLayout.LEFT, 1f, editorArea);
        layout.getViewLayout(StatisticView.ID).setCloseable(false);

        layout.addPerspectiveShortcut(MainPerspective.ID);
        layout.addPerspectiveShortcut(TablePerspective.ID);
        layout.addPerspectiveShortcut(StatisticPerspective.ID);
        layout.addPerspectiveShortcut(EinstellungenPerspective.ID);
    }

}
