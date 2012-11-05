package ch.opentrainingcenter.transfer.impl;

import ch.opentrainingcenter.transfer.IAthlete;
import ch.opentrainingcenter.transfer.IPlanungWoche;

public class PlanungWoche implements java.io.Serializable, IPlanungWoche {

    private static final long serialVersionUID = 1L;
    private int id;
    private IAthlete athlete;
    private int kw;
    private int jahr;
    private int kmProWoche;
    private boolean interval;
    private int langerLauf;

    public PlanungWoche() {
        // für hibernate
    }

    public PlanungWoche(final IAthlete athlete, final int jahr, final int kw, final int kmProWoche, final boolean interval, final int langerLauf) {
        this.athlete = athlete;
        this.kw = kw;
        this.jahr = jahr;
        this.kmProWoche = kmProWoche;
        this.interval = interval;
        this.langerLauf = langerLauf;
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
    public int getLangerLauf() {
        return langerLauf;
    }

    @Override
    public void setLangerLauf(final int langerLauf) {
        this.langerLauf = langerLauf;
    }

    @Override
    public String toString() {
        return "PlanungWoche [id=" + id + ", athlete=" + athlete + ", kw=" + kw + ", jahr=" + jahr + ", kmProWoche=" + kmProWoche //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ 
                + ", interval=" + interval + "]"; //$NON-NLS-1$ //$NON-NLS-2$
    }

}
