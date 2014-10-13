package ch.opentrainingcenter.model.training.internal;

import java.util.HashMap;
import java.util.Map;

import ch.opentrainingcenter.core.assertions.Assertions;
import ch.opentrainingcenter.core.data.Pair;
import ch.opentrainingcenter.model.training.GoldMedalTyp;
import ch.opentrainingcenter.model.training.IGoldMedalModel;
import ch.opentrainingcenter.model.training.Intervall;

public class GoldMedalModel implements IGoldMedalModel {

    private final Map<Intervall, Pair<Long, String>> SCHNELLSTE_PACES = new HashMap<>();

    private final Map<GoldMedalTyp, Pair<Long, String>> records = new HashMap<>();

    @Override
    public Pair<Long, String> getRecord(final GoldMedalTyp typ) {
        Assertions.notNull(typ);
        final Pair<Long, String> result = records.get(typ);
        return result != null ? result : EMPTY_PAIR;
    }

    @Override
    public void setRecord(final GoldMedalTyp typ, final Pair<Long, String> record) {
        Assertions.notNull(typ);
        records.put(typ, getValidatedPair(record));
    }

    @Override
    public Pair<Long, String> getSchnellstePace(final Intervall intervall) {
        Assertions.notNull(intervall);
        if (!SCHNELLSTE_PACES.containsKey(intervall)) {
            SCHNELLSTE_PACES.put(intervall, EMPTY_PAIR);
        }
        return SCHNELLSTE_PACES.get(intervall);
    }

    @Override
    public void setSchnellstePace(final Intervall intervall, final Pair<Long, String> schnellstePace) {
        Assertions.notNull(intervall);
        Assertions.notNull(schnellstePace);
        SCHNELLSTE_PACES.put(intervall, getValidatedPair(schnellstePace));
    }

    private Pair<Long, String> getValidatedPair(final Pair<Long, String> value) {
        if (value.getFirst() == null || value.getSecond() == null) {
            return EMPTY_PAIR;
        } else {
            return value;
        }
    }

    @Override
    public boolean hasNewRecord(final IGoldMedalModel newModel) {
        return false;
    }
}
