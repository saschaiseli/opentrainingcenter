package ch.opentrainingcenter.model.training;

import ch.opentrainingcenter.core.data.Pair;

public interface IGoldMedalModel {

    Pair<Long, String> EMPTY_PAIR = new Pair<>(null, "-"); //$NON-NLS-1$

    Pair<Long, String> getSchnellstePace(final Intervall intervall);

    void setSchnellstePace(final Intervall intervall, final Pair<Long, String> schnellstePace);

    /**
     * Prueft ob das neue Model (newModel) einen Record hat.
     * 
     * @param newModel
     *            das neue Model.
     * @return true, wenn der newModel Parameter irgendwo eine bessere Zeit hat,
     *         ansonsten false.
     */
    boolean hasNewRecord(IGoldMedalModel newModel);

    /**
     * Gibt den Record vom {@link GoldMedalTyp} zurück. Wenn noch keine Bestzeit
     * erfasst ist, wird ein {@link IGoldMedalModel#EMPTY_PAIR} zurückgegeben.
     * Es wird nie null zurückgegeben.
     */
    Pair<Long, String> getRecord(GoldMedalTyp typ);

    void setRecord(GoldMedalTyp typ, Pair<Long, String> record);
}