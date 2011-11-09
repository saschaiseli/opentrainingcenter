package ch.iseli.sportanalyzer.client.cache;

import org.eclipse.swt.graphics.Image;

import ch.iseli.sportanalyzer.client.Activator;
import ch.iseli.sportanalyzer.client.views.cardio.CardioView;
import ch.iseli.sportanalyzer.client.views.sealevel.SealevelView;
import ch.iseli.sportanalyzer.client.views.speed.SpeedView;

public enum ChildTyp {
    SPEED(SpeedView.ID, SpeedView.IMAGE), CARDIO(CardioView.ID, CardioView.IMAGE), LEVEL_ABOUT_SEA(SealevelView.ID, "");

    private final String viewId;

    private final Image image;

    private ChildTyp(String viewId, String imagePath) {
        this.viewId = viewId;
        this.image = Activator.getImageDescriptor(imagePath).createImage();

    }

    public String getViewId() {
        return viewId;
    }

    public Image getImage() {
        return image;
    }
}
