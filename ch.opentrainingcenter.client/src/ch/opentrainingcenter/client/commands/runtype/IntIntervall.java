package ch.opentrainingcenter.client.commands.runtype;

import ch.opentrainingcenter.transfer.TrainingType;

/**
 * Intesives Intervall
 */
public class IntIntervall extends ChangeRunType {
    public static final String ID = "ch.opentrainingcenter.client.commands.IntIntervall"; //$NON-NLS-1$

    @Override
    public TrainingType getType() {
        return TrainingType.INT_INTERVALL;
    }
}
