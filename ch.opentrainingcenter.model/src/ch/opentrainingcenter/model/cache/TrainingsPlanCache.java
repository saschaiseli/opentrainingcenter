package ch.opentrainingcenter.model.cache;

import ch.opentrainingcenter.core.assertions.Assertions;
import ch.opentrainingcenter.core.cache.AbstractCache;
import ch.opentrainingcenter.model.planing.IPlanungModel;
import ch.opentrainingcenter.model.planing.KwJahrKey;

public final class TrainingsPlanCache extends AbstractCache<KwJahrKey, IPlanungModel> {

    private static final TrainingsPlanCache INSTANCE = new TrainingsPlanCache();

    private TrainingsPlanCache() {
        // do not create instance. this cache is a singleton
    }

    public static TrainingsPlanCache getInstance() {
        return INSTANCE;
    }

    @Override
    public KwJahrKey getKey(final IPlanungModel value) {
        Assertions.notNull(value);
        return new KwJahrKey(value.getJahr(), value.getKw());
    }

    @Override
    public String getName() {
        return "IPlanungModel"; //$NON-NLS-1$
    }
}
