package ch.opentrainingcenter.core.cache;

import ch.opentrainingcenter.transfer.ActivityExtension;
import ch.opentrainingcenter.transfer.ITraining;

public interface Cache extends ICache<Long, ITraining> {

    /**
     * Synchronisiert das Wetter und die Notes (beide in der ActivityExtension)
     * 
     * 
     * @param activityId
     * @param extension
     */
    void updateExtension(final Long activityId, final ActivityExtension extension);

    void resetCache();

}