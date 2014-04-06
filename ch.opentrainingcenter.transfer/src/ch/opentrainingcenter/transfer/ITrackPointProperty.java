package ch.opentrainingcenter.transfer;

/**
 * Eigenschaften eines Punktes. Distanz zum Start, Herzschlag, Höhe, Zeit, in
 * welcher Runde,....
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
     * @return absolute zeit in milliseconds
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

    /**
     * @return Nummer der Runde. Wenn nicht Rundenbasiert gemessen wird, ist der
     *         Wert immer 1
     */
    int getLap();

    /**
     * @param lap
     *            die Nummer der Runde. (beginnt bei 1)
     */
    void setLap(int lap);

    void setTraining(ITraining training);

    ITraining getTraining();
}
