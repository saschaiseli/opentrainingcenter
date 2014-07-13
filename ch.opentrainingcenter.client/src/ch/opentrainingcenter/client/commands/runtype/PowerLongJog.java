package ch.opentrainingcenter.client.commands.runtype;

import ch.opentrainingcenter.transfer.TrainingType;

/**
 * Erweiterter Dauerlauf. Letztes viertel etwas schneller.
 */
public class PowerLongJog extends ChangeRunType {

    public static final String ID = "ch.opentrainingcenter.client.commands.PowerLongJog"; //$NON-NLS-1$

    @Override
    public TrainingType getType() {
        return TrainingType.POWER_LONG_JOG;
    }
}