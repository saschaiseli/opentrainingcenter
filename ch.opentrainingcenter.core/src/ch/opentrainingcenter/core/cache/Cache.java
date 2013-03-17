package ch.opentrainingcenter.core.cache;

import java.util.Date;

import ch.opentrainingcenter.tcx.ActivityT;
import ch.opentrainingcenter.transfer.ActivityExtension;

public interface Cache extends ICache<Date, ActivityT> {

    /**
     * Synchronisiert das Wetter und die Notes (beide in der ActivityExtension)
     * 
     * 
     * @param activityId
     * @param extension
     */
    void updateExtension(final Date activityId, final ActivityExtension extension);

    void resetCache();

}