package ch.opentrainingcenter.transfer;

import ch.opentrainingcenter.transfer.impl.Athlete;

public class Factory {
    public static IAthlete createAthlete(String name) {
        return new Athlete(name);
    }
}
