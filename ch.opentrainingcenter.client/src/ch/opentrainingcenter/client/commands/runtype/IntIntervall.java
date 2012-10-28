package ch.opentrainingcenter.client.commands.runtype;

import ch.opentrainingcenter.client.model.RunType;

public class IntIntervall extends ChangeRunType {
    public static final String ID = "ch.opentrainingcenter.client.commands.IntIntervall";

    @Override
    public RunType getType() {
        return RunType.INT_INTERVALL;
    }
}
