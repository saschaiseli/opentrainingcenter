package ch.opentrainingcenter.client.commands.runtype;

import ch.opentrainingcenter.client.model.RunType;

public class ExtIntervall extends ChangeRunType {

    public static final String ID = "ch.opentrainingcenter.client.commands.ExtIntervall";

    @Override
    public RunType getType() {
        return RunType.EXT_INTERVALL;
    }
}
