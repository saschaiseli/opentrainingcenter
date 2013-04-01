package ch.opentrainingcenter.transfer.impl;

import ch.opentrainingcenter.transfer.IStreckenPunkt;

public class StreckenPunkt implements IStreckenPunkt {

    private int id;
    private int idx;
    private double distance;
    private double longitude;
    private double latitude;
    private Strecke strecke;

    public StreckenPunkt() {
        // für hibernate
    }

    public StreckenPunkt(final double distance, final double longitude, final double latitude) {
        super();
        this.distance = distance;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    @Override
    public double getDistance() {
        return distance;
    }

    @Override
    public void setDistance(final double distance) {
        this.distance = distance;

    }

    @Override
    public double getLongitude() {
        return longitude;
    }

    @Override
    public void setLongitude(final double longitude) {
        this.longitude = longitude;

    }

    @Override
    public double getLatitude() {
        return latitude;
    }

    @Override
    public void setLatitude(final double latitude) {
        this.latitude = latitude;
    }

    public Strecke getStrecke() {
        return strecke;
    }

    public void setStrecke(final Strecke strecke) {
        this.strecke = strecke;
    }

    public int getId() {
        return id;
    }

    public void setId(final int id) {
        this.id = id;
    }

    public int getIdx() {
        return idx;
    }

    public void setIdx(final int idx) {
        this.idx = idx;
    }
}
