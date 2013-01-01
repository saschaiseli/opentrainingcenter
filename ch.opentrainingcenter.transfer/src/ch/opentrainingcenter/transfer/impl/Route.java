package ch.opentrainingcenter.transfer.impl;

import java.util.HashSet;
import java.util.Set;

import ch.opentrainingcenter.transfer.IAthlete;
import ch.opentrainingcenter.transfer.IImported;
import ch.opentrainingcenter.transfer.IRoute;

public class Route implements IRoute {

    private String name;
    private String beschreibung;
    private int id;
    private IAthlete athlete;
    private Set<IImported> importeds = new HashSet<IImported>(0);

    /**
     * nur für hibernate
     */
    public Route() {
        this(null, null, null);
    }

    public Route(final String name, final String beschreibung, final IAthlete athlete) {
        this.name = name;
        this.beschreibung = beschreibung;
        this.athlete = athlete;
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
    public IAthlete getAthlete() {
        return athlete;
    }

    @Override
    public void setAthlete(final IAthlete athlete) {
        this.athlete = athlete;
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
    public Set<IImported> getImporteds() {
        return this.importeds;
    }

    public void setImporteds(final Set<IImported> importeds) {
        this.importeds = importeds;
    }

    @Override
    public String toString() {
        return "Route [id=" + id + ", athlete=" + athlete + ", name=" + name + ", beschreibung=" + beschreibung + "]"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
    }
}
