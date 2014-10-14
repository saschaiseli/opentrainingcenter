package ch.opentrainingcenter.client.ui.tableviewer;

import ch.opentrainingcenter.transfer.IRoute;

public class RoutenTableModel {

    private final IRoute route;
    private final int anzahl;

    public RoutenTableModel(final IRoute route, final int anzahl) {
        this.route = route;
        this.anzahl = anzahl;
    }

    public IRoute getRoute() {
        return route;
    }

    public int getAnzahl() {
        return anzahl;
    }

}
