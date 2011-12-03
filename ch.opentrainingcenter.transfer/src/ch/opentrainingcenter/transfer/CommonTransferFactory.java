package ch.opentrainingcenter.transfer;

import ch.opentrainingcenter.transfer.impl.Athlete;
import ch.opentrainingcenter.transfer.impl.Imported;

public class CommonTransferFactory {
    private CommonTransferFactory() {

    }

    public static IImported createIImported() {
        return new Imported();
    }

    public static IAthlete createAthlete(final String name, final Integer age, final Integer maxHeartBeat) {
        return new Athlete(name, age, maxHeartBeat);
    }
}
