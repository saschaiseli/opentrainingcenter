package ch.opentrainingcenter.client.model.planing;

import java.util.List;

public interface IPastPlanungModel {
    /**
     * Es werden nur die vergangenen Kalender Wochen berücksichtigt. Die
     * aktuelle Woche ist die letzte die noch erscheint.
     * 
     * @return eine Liste mit den auf eine Planung gemappten und aufsumierten
     *         Läufen. Die Liste kann nicht mehr verändert werden.
     */
    List<IPastPlanung> getPastPlanungen();
}
