package ch.opentrainingcenter.client.commands;

import ch.opentrainingcenter.client.perspectives.MainPerspective;

/**
 * Handler um zur Main Perspektive zu wechseln.
 */
public class SwitchToMain extends SwitchToPerspective {

    @Override
    String getPerspectiveId() {
        return MainPerspective.ID;
    }

    @Override
    boolean isSamePerspective(final String perspectiveId) {
        return MainPerspective.ID.equals(perspectiveId);
    }
}
