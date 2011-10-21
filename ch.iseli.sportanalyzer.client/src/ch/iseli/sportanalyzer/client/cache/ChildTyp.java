package ch.iseli.sportanalyzer.client.cache;

import ch.iseli.sportanalyzer.client.views.CardioView;
import ch.iseli.sportanalyzer.client.views.SealevelView;
import ch.iseli.sportanalyzer.client.views.SpeedView;

public enum ChildTyp {
    SPEED(SpeedView.ID), CARDIO(CardioView.ID), LEVEL_ABOUT_SEA(SealevelView.ID);

    private final String viewId;

    private ChildTyp(String viewId) {
        this.viewId = viewId;

    }

    public String getViewId() {
        return viewId;
    }
}
