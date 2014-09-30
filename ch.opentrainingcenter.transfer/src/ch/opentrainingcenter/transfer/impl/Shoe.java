package ch.opentrainingcenter.transfer.impl;

import java.util.Date;

import ch.opentrainingcenter.transfer.IAthlete;
import ch.opentrainingcenter.transfer.IShoe;

public class Shoe implements java.io.Serializable, IShoe {

    private static final long serialVersionUID = 1L;

    private int id;
    private String schuhname;
    private String imageicon;
    private int preis;
    private Date kaufdatum;
    private IAthlete athlete;

    public Shoe() {
        // fuer hibernate
    }

    public Shoe(final IAthlete athlete, final String schuhname, final String imageicon, final int preis, final Date kaufdatum) {
        this.athlete = athlete;
        this.schuhname = schuhname;
        this.imageicon = imageicon;
        this.preis = preis;
        this.kaufdatum = kaufdatum;
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
    public String getSchuhname() {
        return schuhname;
    }

    @Override
    public void setSchuhname(final String schuhname) {
        this.schuhname = schuhname;
    }

    @Override
    public String getImageicon() {
        return imageicon;
    }

    @Override
    public void setImageicon(final String imageicon) {
        this.imageicon = imageicon;
    }

    @Override
    public int getPreis() {
        return preis;
    }

    @Override
    public void setPreis(final int preis) {
        this.preis = preis;
    }

    @Override
    public Date getKaufdatum() {
        return kaufdatum;
    }

    @Override
    public void setKaufdatum(final Date kaufdatum) {
        this.kaufdatum = kaufdatum;
    }

    @Override
    public IAthlete getAthlete() {
        return athlete;
    }

    @Override
    public void setAthlete(final IAthlete athlete) {
        this.athlete = athlete;
    }

    @SuppressWarnings("nls")
    @Override
    public String toString() {
        return "Shoe [id=" + id + ", schuhname=" + schuhname + ", imageicon=" + imageicon + ", preis=" + preis + ", kaufdatum=" + kaufdatum + ", athlete="
                + athlete + "]";
    }
}
