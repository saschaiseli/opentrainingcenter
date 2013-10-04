package ch.opentrainingcenter.client.commands.runtype;

import ch.opentrainingcenter.core.helper.RunType;

/**
 * Intesives Intervall
 */
public class IntIntervall extends ChangeRunType {
    public static final String ID = "ch.opentrainingcenter.client.commands.IntIntervall"; //$NON-NLS-1$

    @Override
    public RunType getType() {
        return RunType.INT_INTERVALL;
    }
}
