package ch.opentrainingcenter.client.perspectives;

import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

import ch.opentrainingcenter.client.views.einstellungen.RoutenView;
import ch.opentrainingcenter.client.views.einstellungen.UserView;

public class EinstellungenPerspective implements IPerspectiveFactory {

    private static final float VIERTEL = 0.25f;
    public static final String ID = "ch.opentrainingcenter.client.einstellungen"; //$NON-NLS-1$

    @Override
    public void createInitialLayout(final IPageLayout layout) {
        final String editorArea = layout.getEditorArea();
        layout.setEditorAreaVisible(false);

        layout.addStandaloneView(UserView.ID, false, IPageLayout.LEFT, VIERTEL, editorArea);
        layout.getViewLayout(UserView.ID).setCloseable(false);

        layout.addStandaloneView(RoutenView.ID, false, IPageLayout.RIGHT, 1 - VIERTEL, editorArea);
        layout.getViewLayout(RoutenView.ID).setCloseable(false);

        layout.addPerspectiveShortcut(MainPerspective.ID);
        layout.addPerspectiveShortcut(EinstellungenPerspective.ID);
    }

}
