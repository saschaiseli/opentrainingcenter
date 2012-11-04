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
    private boolean interval;

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
    public int getKw() {
        return kw;
    }

    @Override
    public void setKw(final int kw) {
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
    public int getKmProWoche() {
        return kmProWoche;
    }

    @Override
    public void setKmProWoche(final int kmProWoche) {
        this.kmProWoche = kmProWoche;
    }

    @Override
    public boolean isInterval() {
        return interval;
    }

    @Override
    public void setInterval(final boolean interval) {
        this.interval = interval;
    }

    @Override
    public String toString() {
        return "PlanungWoche [id=" + id + ", athlete=" + athlete + ", kw=" + kw + ", jahr=" + jahr + ", active=" + active + ", kmProWoche=" + kmProWoche
                + ", interval=" + interval + "]";
    }

}
