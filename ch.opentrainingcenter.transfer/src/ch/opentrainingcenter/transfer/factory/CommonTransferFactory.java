package ch.opentrainingcenter.transfer.factory;

import java.util.Date;

import org.joda.time.DateTime;

import ch.opentrainingcenter.i18n.Messages;
import ch.opentrainingcenter.transfer.IAthlete;
import ch.opentrainingcenter.transfer.IHealth;
import ch.opentrainingcenter.transfer.ILapInfo;
import ch.opentrainingcenter.transfer.IPlanungWoche;
import ch.opentrainingcenter.transfer.IRoute;
import ch.opentrainingcenter.transfer.IStreckenPunkt;
import ch.opentrainingcenter.transfer.ITrackPointProperty;
import ch.opentrainingcenter.transfer.ITraining;
import ch.opentrainingcenter.transfer.IWeather;
import ch.opentrainingcenter.transfer.impl.Athlete;
import ch.opentrainingcenter.transfer.impl.Health;
import ch.opentrainingcenter.transfer.impl.LapInfo;
import ch.opentrainingcenter.transfer.impl.Planungwoche;
import ch.opentrainingcenter.transfer.impl.Route;
import ch.opentrainingcenter.transfer.impl.Streckenpunkte;
import ch.opentrainingcenter.transfer.impl.Tracktrainingproperty;
import ch.opentrainingcenter.transfer.impl.Training;
import ch.opentrainingcenter.transfer.impl.Weather;

public final class CommonTransferFactory {

    private CommonTransferFactory() {

    }

    public static IAthlete createAthlete(final String name, final Integer maxHeartBeat) {
        return createAthlete(name, DateTime.now().toDate(), maxHeartBeat);
    }

    public static IAthlete createAthlete(final String name, final Date birthday, final Integer maxHeartBeat) {
        return new Athlete(name, birthday, maxHeartBeat);
    }

    public static IWeather createDefaultWeather() {
        return createWeather(9);
    }

    public static IWeather createWeather(final int id) {
        return new Weather(id);
    }

    public static IHealth createHealth(final IAthlete athlete, final Double weight, final Integer cardio, final Date dateofmeasure) {
        return new Health(athlete, weight, cardio, dateofmeasure);
    }

    public static ITraining createTraining(final long dateOfStart, final double timeInSeconds, final double distance, final int avgHeartRate,
            final int maxHeartBeat, final double maximumSpeed, final String note, final IWeather weather, final IRoute route) {
        return new Training(dateOfStart, timeInSeconds, distance, avgHeartRate, maxHeartBeat, maximumSpeed, note, weather, route);
    }

    /**
     * Factory
     * 
     * @param dateOfStart
     *            start datum
     * @param timeInSeconds
     *            zeit des laufes in sekunden
     * @param distance
     *            distanz des laufes in meter
     * @param avgHeartRate
     *            durchschnittliche herzrate
     * @param maxHeartBeat
     *            maximale Herzrate
     * @param maximumSpeed
     *            schnellste pace [min/km]
     * @return {@link ITraining}
     */
    public static ITraining createTraining(final long dateOfStart, final double timeInSeconds, final double distance, final int avgHeartRate,
            final int maxHeartBeat, final double maximumSpeed) {
        return new Training(dateOfStart, timeInSeconds, distance, avgHeartRate, maxHeartBeat, maximumSpeed, "", CommonTransferFactory //$NON-NLS-1$
                .createDefaultWeather(), null);
    }

    /**
     * @param athlete
     *            Athlete
     * @param jahr
     *            das Jahr
     * @param kw
     *            die Kalenderwoche
     * @param kmProWoche
     *            KM pro woche
     * @param interval
     *            ob es ein intervall war
     * @param langerLauf
     *            wie lange der längste lauf sein soll
     * @return
     */
    public static IPlanungWoche createIPlanungWoche(final IAthlete athlete, final int jahr, final int kw, final int kmProWoche, final boolean interval,
            final int langerLauf) {
        return new Planungwoche(athlete, jahr, kw, kmProWoche, interval, langerLauf);
    }

    public static IPlanungWoche createIPlanungWocheEmpty(final IAthlete athlete, final int jahr, final int kw) {
        return new Planungwoche(athlete, jahr, kw, 0, false, 0);
    }

    /**
     * @param name
     *            eindeutiger name der route
     * @param beschreibung
     *            beschreibung
     * @param referenzTraining
     *            geografische Referenz der Streckenpunkte
     * @return
     */
    public static IRoute createRoute(final String name, final String beschreibung, final ITraining referenzTraining) {
        return new Route(name, beschreibung, referenzTraining);
    }

    public static IRoute createDefaultRoute(final IAthlete athlete) {
        return new Route(Messages.OTCKonstanten_0, athlete);
    }

    /**
     * @param distance
     *            vom Start weg
     * @param longitude
     *            longitude
     * @param latitude
     *            latitude
     * @return {@link IStreckenPunkt}
     */
    public static IStreckenPunkt createStreckenPunkt(final double distance, final double longitude, final double latitude) {
        return new Streckenpunkte(longitude, latitude);
    }

    /**
     * @param distance
     *            vom Startweg
     * @param heartbeat
     *            herzschlag an diesem Punkt
     * @param altitude
     *            Höhe in Meter über Meer
     * @param time
     *            absolute zeit
     * @param lap
     *            Runden nummer
     * @param streckenPunkt
     *            {@link IStreckenPunkt}
     * @return {@link ITraining}
     */
    public static ITrackPointProperty createTrackPointProperty(final double distance, final int heartbeat, final int altitude, final long time, final int lap,
            final IStreckenPunkt streckenPunkt) {
        return new Tracktrainingproperty(distance, heartbeat, altitude, time, lap, streckenPunkt);
    }

    public static ILapInfo createLapInfo(final int lap, final int start, final int end, final long time, final int heartBeat, final String pace) {
        return new LapInfo(lap, start, end, time, heartBeat, pace);
    }
}
