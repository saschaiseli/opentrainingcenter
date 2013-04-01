package ch.opentrainingcenter.transfer.impl;

import java.util.List;

import ch.opentrainingcenter.transfer.IAthlete;
import ch.opentrainingcenter.transfer.IStrecke;
import ch.opentrainingcenter.transfer.IStreckenPunkt;

public class Strecke implements IStrecke {

    private int id;
    private String name;
    private List<IStreckenPunkt> streckenPunkte;
    private IAthlete athlete;

    public Strecke() {
        // für hibernate
    }

    public Strecke(final String name, final List<IStreckenPunkt> streckenPunkte, final IAthlete athlete) {
        this.name = name;
        this.streckenPunkte = streckenPunkte;
        this.athlete = athlete;
    }

    public int getId() {
        return id;
    }

    public void setId(final int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
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
    public List<IStreckenPunkt> getStreckenPunkte() {
        return streckenPunkte;
    }

    @Override
    public void setStreckenPunkte(final List<IStreckenPunkt> streckenPunkte) {
        this.streckenPunkte = streckenPunkte;
    }

}
