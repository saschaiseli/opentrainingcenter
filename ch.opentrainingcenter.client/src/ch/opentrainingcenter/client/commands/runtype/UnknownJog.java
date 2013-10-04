package ch.opentrainingcenter.client.commands.runtype;

import ch.opentrainingcenter.core.helper.RunType;

/**
 * Unbekannt.
 */
public class UnknownJog extends ChangeRunType {

    public static final String ID = "ch.opentrainingcenter.client.commands.Unknown"; //$NON-NLS-1$

    @Override
    public RunType getType() {
        return RunType.NONE;
    }
}