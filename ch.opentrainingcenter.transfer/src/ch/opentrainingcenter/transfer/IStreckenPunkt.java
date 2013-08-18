package ch.opentrainingcenter.transfer;

/**
 * Definition eines Punktes auf der Karte (Long/Latitude)
 * 
 */
public interface IStreckenPunkt {

    double getLongitude();

    void setLongitude(double longitude);

    double getLatitude();

    void setLatitude(double latitude);
}
