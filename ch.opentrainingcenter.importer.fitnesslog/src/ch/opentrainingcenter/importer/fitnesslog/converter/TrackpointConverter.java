package ch.opentrainingcenter.importer.fitnesslog.converter;

import java.math.BigDecimal;
import java.util.GregorianCalendar;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.Duration;
import javax.xml.datatype.XMLGregorianCalendar;

import org.apache.log4j.Logger;

import ch.iseli.sportanalyzer.tcx.HeartRateInBeatsPerMinuteT;
import ch.iseli.sportanalyzer.tcx.PositionT;
import ch.iseli.sportanalyzer.tcx.TrackpointT;
import ch.opentrainingcenter.importer.fitnesslog.model.Pt;

public class TrackpointConverter {

    private static final Logger logger = Logger.getLogger(TrackpointConverter.class);

    private final GregorianCalendar startTime;

    TrackpointConverter(final XMLGregorianCalendar startTime) {
        this.startTime = startTime.toGregorianCalendar();
    }

    /**
     * @param previousPoint
     *            vorheriger punkt oder null, wenn es der erste punkt ist
     * @param currentPoint
     *            aktueller punkt
     * @return den {@link TrackpointT}
     */
    public TrackpointT convert(final Pt previousPoint, final Pt currentPoint) {
        final TrackpointT point = new TrackpointT();
        point.setAltitudeMeters(currentPoint.getEle() != null ? currentPoint.getEle().doubleValue() : 0);
        // point.setDistanceMeters(pt.getDist().doubleValue());
        final HeartRateInBeatsPerMinuteT beatsPerMinuteT = new HeartRateInBeatsPerMinuteT();
        beatsPerMinuteT.setValue(currentPoint.getHr() != null ? currentPoint.getHr().shortValue() : 0);
        point.setHeartRateBpm(beatsPerMinuteT);
        final PositionT position = new PositionT();
        if (isPointValid(currentPoint)) {
            position.setLatitudeDegrees(currentPoint.getLat().doubleValue());
            position.setLongitudeDegrees(currentPoint.getLon().doubleValue());
        }
        point.setPosition(position);

        try {
            final BigDecimal secondsAfterStart = currentPoint.getTm();
            final XMLGregorianCalendar time = DatatypeFactory.newInstance().newXMLGregorianCalendar(startTime);
            final Duration duration = DatatypeFactory.newInstance().newDuration(secondsAfterStart.intValue() * 1000);
            time.add(duration);
            point.setTime(time);
            if (isPointValid(previousPoint) && isPointValid(currentPoint)) {
                double distance = distance(previousPoint.getLat().doubleValue(), previousPoint.getLon().doubleValue(), currentPoint.getLat().doubleValue(),
                        currentPoint.getLon().doubleValue());
                distance += previousPoint.getDist().doubleValue();
                point.setDistanceMeters(distance);
            } else {
                point.setDistanceMeters(0.0);
            }
        } catch (final DatatypeConfigurationException e) {
            logger.error(e);
        }
        return point;
    }

    private boolean isPointValid(final Pt point) {
        return point != null && point.getLat() != null && point.getLon() != null;
    }

    private double distance(final double lat1, final double lon1, final double lat2, final double lon2) {
        final double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        dist = dist * 1609.344;
        return (dist);
    }

    /* ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::: */
    /* :: This function converts decimal degrees to radians : */
    /* ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::: */
    private double deg2rad(final double deg) {
        return (deg * Math.PI / 180.0);
    }

    /* ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::: */
    /* :: This function converts radians to decimal degrees : */
    /* ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::: */
    private double rad2deg(final double rad) {
        return (rad * 180.0 / Math.PI);
    }
}
