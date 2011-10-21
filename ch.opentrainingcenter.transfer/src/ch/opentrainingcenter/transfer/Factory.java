package ch.opentrainingcenter.transfer;

import ch.opentrainingcenter.transfer.internal.Athlete;

public class Factory {
    public static IAthlete createAthlete(String name) {
        return new Athlete(name);
    }
}
