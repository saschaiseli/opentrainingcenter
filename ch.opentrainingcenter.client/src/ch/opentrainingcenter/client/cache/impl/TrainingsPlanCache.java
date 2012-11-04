package ch.opentrainingcenter.client.cache.impl;

import ch.opentrainingcenter.client.model.planing.impl.KwJahrKey;
import ch.opentrainingcenter.client.model.planing.impl.PlanungModel;

public class TrainingsPlanCache extends AbstractCache<KwJahrKey, PlanungModel> {

    private static TrainingsPlanCache INSTANCE = new TrainingsPlanCache();

    private TrainingsPlanCache() {
        // do not create instance. this cache is a singleton
    }

    public static TrainingsPlanCache getInstance() {
        return INSTANCE;
    }

    @Override
    public KwJahrKey getKey(final PlanungModel value) {
        return new KwJahrKey(value.getJahr(), value.getKw());
    }
}
