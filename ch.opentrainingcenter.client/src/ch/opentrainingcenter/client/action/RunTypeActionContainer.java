package ch.opentrainingcenter.client.action;

import java.util.ArrayList;
import java.util.List;

import ch.opentrainingcenter.client.model.RunType;

public class RunTypeActionContainer {

    private static final List<ChangeRunType> ACTIONS = new ArrayList<ChangeRunType>();

    static {
        ACTIONS.add(new ChangeRunType(RunType.INT_INTERVALL));
        ACTIONS.add(new ChangeRunType(RunType.EXT_INTERVALL));
        ACTIONS.add(new ChangeRunType(RunType.LONG_JOG));
        ACTIONS.add(new ChangeRunType(RunType.POWER_LONG_JOG));
        ACTIONS.add(new ChangeRunType(RunType.TEMPO_JOG));
        ACTIONS.add(new ChangeRunType(RunType.NONE));
    }

    private RunTypeActionContainer() {

    }

    public static List<ChangeRunType> getActions() {
        return ACTIONS;
    }

    public static void update(final int selectedType) {
        for (final ChangeRunType crt : ACTIONS) {
            crt.setEnabled(!(crt.getType().getIndex() == selectedType));
        }
    }
}
