package ch.opentrainingcenter.client.commands.runtype;

import ch.opentrainingcenter.client.model.RunType;

public class PowerLongJog extends ChangeRunType {

    public static final String ID = "ch.opentrainingcenter.client.commands.PowerLongJog";

    @Override
    public RunType getType() {
        return RunType.POWER_LONG_JOG;
    }
}