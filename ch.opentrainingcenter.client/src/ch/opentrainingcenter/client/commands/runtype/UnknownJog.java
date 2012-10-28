package ch.opentrainingcenter.client.commands.runtype;

import ch.opentrainingcenter.client.model.RunType;

public class UnknownJog extends ChangeRunType {

    public static final String ID = "ch.opentrainingcenter.client.commands.Unknown";

    @Override
    public RunType getType() {
        return RunType.NONE;
    }
}