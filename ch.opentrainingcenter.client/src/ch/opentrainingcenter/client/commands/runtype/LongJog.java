package ch.opentrainingcenter.client.commands.runtype;

import ch.opentrainingcenter.client.model.RunType;

public class LongJog extends ChangeRunType {

    public static final String ID = "ch.opentrainingcenter.client.commands.LongJog"; //$NON-NLS-1$

    @Override
    public RunType getType() {
        return RunType.LONG_JOG;
    }
}