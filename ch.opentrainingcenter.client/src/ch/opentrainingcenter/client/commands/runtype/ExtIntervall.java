package ch.opentrainingcenter.client.commands.runtype;

import ch.opentrainingcenter.core.helper.RunType;

public class ExtIntervall extends ChangeRunType {

    public static final String ID = "ch.opentrainingcenter.client.commands.ExtIntervall"; //$NON-NLS-1$

    @Override
    public RunType getType() {
        return RunType.EXT_INTERVALL;
    }
}
