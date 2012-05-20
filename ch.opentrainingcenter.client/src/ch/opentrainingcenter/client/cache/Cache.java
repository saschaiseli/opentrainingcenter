package ch.opentrainingcenter.client.cache;

import java.util.Date;
import java.util.List;

import ch.opentrainingcenter.client.model.impl.SimpleTraining;
import ch.opentrainingcenter.tcx.ActivityT;
import ch.opentrainingcenter.transfer.ActivityExtension;

public interface Cache {

    /**
     * Methode um importierte Runs im cache abzulegen.
     * 
     * @param activities
     *            eine Liste von Aktivitäten.
     */
    void add(final ActivityT activity);

    /**
     * Methode um importierte Runs im cache abzulegen.
     * 
     * @param activities
     *            eine Liste von Aktivitäten.
     */
    void addAll(final List<ActivityT> activities);

    ActivityT get(final Date activityId);

    /**
     * Entfernt alle Records mit den angegebenen Ids.
     * 
     * @param ids
     */
    void remove(final List<Date> ids);

    /**
     * Alle Listener notifizieren
     */
    void update();

    void addListener(final IRecordListener listener);

    void removeListener(final IRecordListener listener);

    /**
     * Synchronisiert das Wetter und die Notes (beide in der ActivityExtension)
     * die {@link ActivityT} und {@link SimpleTraining}
     * 
     * @param activityId
     * @param extension
     */
    void updateExtension(final Date activityId, final ActivityExtension extension);

    boolean contains(final Date activityId);

}