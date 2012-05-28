package ch.opentrainingcenter.client.commands;

import ch.opentrainingcenter.client.perspectives.StatisticPerspective;

public class SwitchToStatistic extends SwitchToPerspective {

    public static final String ID = "ch.opentrainingcenter.client.commands.SwitchToStatistic"; //$NON-NLS-1$

    @Override
    String getPerspectiveId() {
        return StatisticPerspective.ID;
    }

    @Override
    boolean isSamePerspective(final String perspectiveId) {
        return StatisticPerspective.ID.equals(perspectiveId);
    }

    @Override
    boolean isPerspectiveValidToShow() {
        return true;
    }

}
