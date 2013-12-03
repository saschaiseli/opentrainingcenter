package ch.opentrainingcenter.transfer.impl;

import ch.opentrainingcenter.transfer.IAthlete;
import ch.opentrainingcenter.transfer.IRoute;
import ch.opentrainingcenter.transfer.ITraining;

public class Route implements java.io.Serializable, IRoute {

    private static final long serialVersionUID = 1L;

    private int id;
    private String name;
    private String beschreibung;
    private IAthlete athlete;

    private ITraining referenzTrack;

    public Route() {
    }

    public Route(final String name, final IAthlete athlete) {
        this.name = name;
        this.athlete = athlete;
    }

    public Route(final String name, final String beschreibung, final ITraining referenzTraining) {
        this.name = name;
        this.beschreibung = beschreibung;
        this.athlete = referenzTraining.getAthlete();
        this.referenzTrack = referenzTraining;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(final int id) {
        this.id = id;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public void setName(final String name) {
        this.name = name;
    }

    @Override
    public String getBeschreibung() {
        return this.beschreibung;
    }

    @Override
    public void setBeschreibung(final String beschreibung) {
        this.beschreibung = beschreibung;
    }

    @Override
    public void setAthlete(final IAthlete athlete) {
        this.athlete = athlete;
    }

    @Override
    public IAthlete getAthlete() {
        return athlete;
    }

    @Override
    public ITraining getReferenzTrack() {
        return referenzTrack;
    }

    @Override
    public void setReferenzTrack(final ITraining referenzTrack) {
        this.referenzTrack = referenzTrack;
    }

    @SuppressWarnings("nls")
    @Override
    public String toString() {
        return "Route [name=" + name + ", beschreibung=" + beschreibung + ", athlete=" + athlete + "]";
    }

}
