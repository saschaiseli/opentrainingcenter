package ch.opentrainingcenter.client.perspectives;

import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

import ch.opentrainingcenter.client.views.ahtlete.CreateAthleteView;

public class AthletePerspective implements IPerspectiveFactory {

    public static final String ID = "ch.opentrainingcenter.client.athletePerspective"; //$NON-NLS-1$

    @Override
    public void createInitialLayout(final IPageLayout layout) {
        final String editorArea = layout.getEditorArea();
        layout.setEditorAreaVisible(false);

        layout.addStandaloneView(CreateAthleteView.ID, false, IPageLayout.LEFT, 1f, editorArea);
        layout.getViewLayout(CreateAthleteView.ID).setCloseable(false);
    }
}
