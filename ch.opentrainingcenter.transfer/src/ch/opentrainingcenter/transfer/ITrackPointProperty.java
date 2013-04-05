package ch.opentrainingcenter.transfer;

/**
 * Eigenschaften eines Punktes
 * 
 * @author sascha
 * 
 */
public interface ITrackPointProperty {

    /**
     * @return Distanz in Meter seit dem Start
     */
    double getDistance();

    /**
     * @param distance
     *            Distanz in Meter seit dem Start
     */
    void setDistance(double distance);

    /**
     * @return Puls
     */
    int getHeartBeat();

    /**
     * @param heartBeat
     *            Puls in Beat per Minute
     */
    void setHeartBeat(int heartBeat);

    /**
     * @return altitude
     */
    int getAltitude();

    /**
     * @param altitude
     *            altitude in meter
     */
    void setAltitude(int altitude);

    /**
     * @return time in milliseconds
     */
    long getZeit();

    /**
     * @param timeInMilliseconds
     */
    void setZeit(long timeInMilliseconds);

    /**
     * @return den geografischen Punkt
     */
    IStreckenPunkt getStreckenPunkt();

    /**
     * @param streckenPunkt
     *            den geografischen Punkt mit xCoord und yCoord in WGS 84
     */
    void setStreckenPunkt(final IStreckenPunkt streckenPunkt);

    int getId();

    void setId(int id);
}
