package ch.opentrainingcenter.client.cache;

import java.util.Date;

import ch.opentrainingcenter.client.model.impl.SimpleTraining;
import ch.opentrainingcenter.tcx.ActivityT;
import ch.opentrainingcenter.transfer.ActivityExtension;

public interface Cache extends ICache<Date, ActivityT> {

    /**
     * Synchronisiert das Wetter und die Notes (beide in der ActivityExtension)
     * die {@link ActivityT} und {@link SimpleTraining}
     * 
     * @param activityId
     * @param extension
     */
    void updateExtension(final Date activityId, final ActivityExtension extension);

}