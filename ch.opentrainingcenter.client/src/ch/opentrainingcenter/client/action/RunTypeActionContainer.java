package ch.opentrainingcenter.client.action;

import java.util.ArrayList;
import java.util.List;

import ch.opentrainingcenter.client.cache.Cache;
import ch.opentrainingcenter.client.model.RunType;
import ch.opentrainingcenter.db.IDatabaseAccess;

@Deprecated
public class RunTypeActionContainer {

    private final List<ChangeRunType> actions = new ArrayList<ChangeRunType>();

    public RunTypeActionContainer(final IDatabaseAccess databaseAccess, final Cache cache) {
        actions.add(new ChangeRunType(RunType.INT_INTERVALL, databaseAccess, cache));
        actions.add(new ChangeRunType(RunType.EXT_INTERVALL, databaseAccess, cache));
        actions.add(new ChangeRunType(RunType.LONG_JOG, databaseAccess, cache));
        actions.add(new ChangeRunType(RunType.POWER_LONG_JOG, databaseAccess, cache));
        actions.add(new ChangeRunType(RunType.TEMPO_JOG, databaseAccess, cache));
        actions.add(new ChangeRunType(RunType.NONE, databaseAccess, cache));
    }

    public List<ChangeRunType> getActions() {
        return actions;
    }

    public void update(final int selectedType) {
        for (final ChangeRunType crt : actions) {
            crt.setEnabled(!(crt.getType().getIndex() == selectedType));
        }
    }
}
