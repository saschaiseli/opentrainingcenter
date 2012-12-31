package ch.opentrainingcenter.transfer.impl;

import ch.opentrainingcenter.transfer.IRoute;

public class Route implements IRoute {

    private String name;
    private String beschreibung;
    private int id;

    /**
     * nur für hibernate
     */
    public Route() {
        this(null, null);
    }

    public Route(final String name, final String beschreibung) {
        this.name = name;
        this.beschreibung = beschreibung;
    }

    @Override
    public int getId() {
        return this.id;
    }

    @Override
    public void setId(final int id) {
        this.id = id;
    }

    @Override
    public void setName(final String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getBeschreibung() {
        return beschreibung;
    }

    @Override
    public void setBeschreibung(final String beschreibung) {
        this.beschreibung = beschreibung;
    }

    @Override
    public String toString() {
        return "Route [name=" + name + ", beschreibung=" + beschreibung + "]"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
    }

}
