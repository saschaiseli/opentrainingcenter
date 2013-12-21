package ch.opentrainingcenter.client.commands.runtype;

import ch.opentrainingcenter.core.cache.Cache;
import ch.opentrainingcenter.core.helper.RunType;
import ch.opentrainingcenter.core.service.IDatabaseService;

/**
 * Extensives Intervall.
 */
public class ExtIntervall extends ChangeRunType {

    public static final String ID = "ch.opentrainingcenter.client.commands.ExtIntervall"; //$NON-NLS-1$

    public ExtIntervall() {
        super();
    }

    /**
     * Konstruktor fuer Tests.
     */
    public ExtIntervall(final IDatabaseService service, final Cache cache) {
        super(service, cache);
    }

    @Override
    public RunType getType() {
        return RunType.EXT_INTERVALL;
    }
}
