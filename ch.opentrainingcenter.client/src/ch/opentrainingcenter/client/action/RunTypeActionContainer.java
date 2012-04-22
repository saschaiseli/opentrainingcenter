package ch.opentrainingcenter.client.action;

import java.util.ArrayList;
import java.util.List;

import ch.opentrainingcenter.client.model.RunType;

public class RunTypeActionContainer {

    private static final List<ChangeRunType> actions = new ArrayList<ChangeRunType>();

    static {
        actions.add(new ChangeRunType(RunType.INT_INTERVALL));
        actions.add(new ChangeRunType(RunType.EXT_INTERVALL));
        actions.add(new ChangeRunType(RunType.LONG_JOG));
        actions.add(new ChangeRunType(RunType.POWER_LONG_JOG));
        actions.add(new ChangeRunType(RunType.TEMPO_JOG));
        actions.add(new ChangeRunType(RunType.NONE));
    }

    public static List<ChangeRunType> getActions() {
        return actions;
    }

    public static void update(final int selectedType) {
        for (final ChangeRunType crt : actions) {
            crt.setEnabled(!(crt.getType().getIndex() == selectedType));
        }
    }
}
