package ch.opentrainingcenter.client.commands;

import ch.opentrainingcenter.client.perspectives.TablePerspective;

public class SwitchToTable extends SwitchToPerspective {

    @Override
    String getPerspectiveId() {
        return TablePerspective.ID;
    }

    @Override
    boolean isSamePerspective(final String perspectiveId) {
        return TablePerspective.ID.equals(perspectiveId);
    }

    @Override
    boolean isPerspectiveValidToShow() {
        return true;
    }

}
