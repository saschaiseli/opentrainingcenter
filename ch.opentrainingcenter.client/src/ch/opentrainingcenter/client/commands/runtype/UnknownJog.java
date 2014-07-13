package ch.opentrainingcenter.client.commands.runtype;

import ch.opentrainingcenter.transfer.TrainingType;

/**
 * Unbekannt.
 */
public class UnknownJog extends ChangeRunType {

    public static final String ID = "ch.opentrainingcenter.client.commands.Unknown"; //$NON-NLS-1$

    @Override
    public TrainingType getType() {
        return TrainingType.NONE;
    }
}