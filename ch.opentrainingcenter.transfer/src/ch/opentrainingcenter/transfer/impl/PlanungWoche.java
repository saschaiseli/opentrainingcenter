package ch.opentrainingcenter.transfer.impl;

import ch.opentrainingcenter.transfer.IAthlete;
import ch.opentrainingcenter.transfer.IPlanungWoche;

public class PlanungWoche implements IPlanungWoche {

    private int id;
    private IAthlete athlete;
    private int kw;
    private int jahr;
    private boolean active;
    private int kmProWoche;

    public PlanungWoche(final IAthlete athlete, final int kw, final int jahr, final int kmProWoche) {
        this(athlete, kw, jahr, kmProWoche, true);
    }

    public PlanungWoche(final IAthlete athlete, final int kw, final int jahr, final int kmProWoche, final boolean active) {
        this.athlete = athlete;
        this.kw = kw;
        this.jahr = jahr;
        this.kmProWoche = kmProWoche;
        this.active = active;
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
    public IAthlete getAthlete() {
        return athlete;
    }

    @Override
    public void setAthlete(final IAthlete athlete) {
        this.athlete = athlete;
    }

    @Override
    public int getKalenderWoche() {
        return kw;
    }

    @Override
    public void setKalenderWoche(final int kw) {
        this.kw = kw;
    }

    @Override
    public int getJahr() {
        return jahr;
    }

    @Override
    public void setJahr(final int jahr) {
        this.jahr = jahr;
    }

    @Override
    public boolean isActive() {
        return active;
    }

    @Override
    public void setActive(final boolean active) {
        this.active = active;
    }

    @Override
    public int getTargetKilometer() {
        return kmProWoche;
    }

    @Override
    public void setTargetKilometer(final int kmProWoche) {
        this.kmProWoche = kmProWoche;
    }

}
