package ch.opentrainingcenter.client.commands;

import ch.opentrainingcenter.client.perspectives.EinstellungenPerspective;

public class SwitchToAthlete extends SwitchToPerspective {

    public static final String ID = "ch.opentrainingcenter.client.commands.SwitchToAthlete"; //$NON-NLS-1$

    @Override
    String getPerspectiveId() {
        return EinstellungenPerspective.ID;
    }

    @Override
    boolean isSamePerspective(final String perspectiveId) {
        return EinstellungenPerspective.ID.equals(perspectiveId);
    }

    @Override
    boolean isPerspectiveValidToShow() {
        return true;
    }

}
