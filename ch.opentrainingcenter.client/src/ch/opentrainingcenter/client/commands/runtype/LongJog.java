package ch.opentrainingcenter.client.commands.runtype;

import ch.opentrainingcenter.transfer.TrainingType;

/**
 * Langer Dauerlauf
 */
public class LongJog extends ChangeRunType {

    public static final String ID = "ch.opentrainingcenter.client.commands.LongJog"; //$NON-NLS-1$

    @Override
    public TrainingType getType() {
        return TrainingType.LONG_JOG;
    }
}