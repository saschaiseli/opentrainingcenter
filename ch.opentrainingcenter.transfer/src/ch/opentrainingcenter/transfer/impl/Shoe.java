package ch.opentrainingcenter.transfer.impl;

import ch.opentrainingcenter.transfer.IAthlete;
import ch.opentrainingcenter.transfer.IShoe;

public class Shoe implements IShoe {
    private int id;
    private String schuhname;
    private String imageicon;
    private IAthlete athlete;

    @Override
    public int getId() {
        return id;
    }

    public void setId(final int id) {
        this.id = id;
    }

    @Override
    public String getSchuhname() {
        return schuhname;
    }

    public void setSchuhname(final String schuhname) {
        this.schuhname = schuhname;
    }

    public String getImageicon() {
        return imageicon;
    }

    public void setImageicon(final String imageicon) {
        this.imageicon = imageicon;
    }

    public IAthlete getAthlete() {
        return athlete;
    }

    public void setAthlete(final IAthlete athlete) {
        this.athlete = athlete;
    }

}
