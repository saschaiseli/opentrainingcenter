package ch.opentrainingcenter.client.commands;

import ch.opentrainingcenter.client.perspectives.AthletePerspective;

public class SwitchToAthlete extends SwitchToPerspective {

    public static final String ID = "ch.opentrainingcenter.client.commands.SwitchToAthlete"; //$NON-NLS-1$

    @Override
    String getPerspectiveId() {
        return AthletePerspective.ID;
    }

    @Override
    boolean isSamePerspective(final String perspectiveId) {
        return AthletePerspective.ID.equals(perspectiveId);
    }

    @Override
    boolean isPerspectiveValidToShow() {
        return true;
    }

}
