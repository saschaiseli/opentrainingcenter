package ch.opentrainingcenter.core.cache;

/**
 * Event Typ um mittzuteilen ob die Records hinzugefuegt, aktualisiert oder
 * geloescht wurden.
 */
public enum Event {
    /**
     * Hinzugefuegt
     */
    ADDED,
    /**
     * Aktualisiert
     */
    UPDATED,
    /**
     * Geloescht
     */
    DELETED;
}
