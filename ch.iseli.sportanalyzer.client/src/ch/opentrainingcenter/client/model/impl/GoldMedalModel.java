package ch.opentrainingcenter.client.model.impl;

import ch.opentrainingcenter.client.model.IGoldMedalModel;
import ch.opentrainingcenter.transfer.IAthlete;

public class GoldMedalModel implements IGoldMedalModel {

    private final IAthlete athlete;

    public GoldMedalModel(final IAthlete athlete) {
        this.athlete = athlete;
    }
}
