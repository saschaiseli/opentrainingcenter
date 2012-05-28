package ch.opentrainingcenter.client.commands;

import org.eclipse.core.commands.HandlerEvent;

import ch.opentrainingcenter.client.perspectives.MainPerspective;

public class SwitchToMain extends SwitchToPerspective {

    @Override
    String getPerspectiveId() {
        return MainPerspective.ID;
    }

    @Override
    boolean isSamePerspective(final String perspectiveId) {
        return MainPerspective.ID.equals(perspectiveId);
    }

    @Override
    boolean isPerspectiveValidToShow() {
        return true;
    }

    @Override
    protected void fireHandlerChanged(final HandlerEvent handlerEvent) {
        super.fireHandlerChanged(handlerEvent);
    }

}
