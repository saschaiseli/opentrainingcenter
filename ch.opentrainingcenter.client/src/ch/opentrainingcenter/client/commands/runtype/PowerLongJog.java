package ch.opentrainingcenter.client.commands.runtype;

import ch.opentrainingcenter.core.helper.RunType;

public class PowerLongJog extends ChangeRunType {

    public static final String ID = "ch.opentrainingcenter.client.commands.PowerLongJog"; //$NON-NLS-1$

    @Override
    public RunType getType() {
        return RunType.POWER_LONG_JOG;
    }
}