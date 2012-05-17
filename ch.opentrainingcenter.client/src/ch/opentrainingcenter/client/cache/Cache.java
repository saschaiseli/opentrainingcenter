package ch.opentrainingcenter.client.cache;

import java.util.Date;
import java.util.List;

import ch.opentrainingcenter.tcx.ActivityT;
import ch.opentrainingcenter.transfer.IImported;

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
     * Aktualisiert den Cache Eintrag mit der neuen Notiz.
     */
    void updateNote(final Date activityId, String note);

    void setSelection(final Object[] selectedItems);

    List<?> getSelection();

    void addAllImported(final List<IImported> records);

    boolean contains(final Date activityId);

}