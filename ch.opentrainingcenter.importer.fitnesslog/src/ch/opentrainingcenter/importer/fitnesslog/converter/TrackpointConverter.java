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

public class TrackpointConverter implements Convert<Pt, TrackpointT> {

    private static final Logger logger = Logger.getLogger(TrackpointConverter.class);

    private final GregorianCalendar startTime;

    TrackpointConverter(final XMLGregorianCalendar startTime) {
        this.startTime = startTime.toGregorianCalendar();
    }

    @Override
    public TrackpointT convert(final Pt pt) {
        final TrackpointT point = new TrackpointT();
        point.setAltitudeMeters(pt.getEle().doubleValue());
        // point.setDistanceMeters(pt.getDist().doubleValue());
        final HeartRateInBeatsPerMinuteT beatsPerMinuteT = new HeartRateInBeatsPerMinuteT();
        beatsPerMinuteT.setValue(pt.getHr().shortValue());
        point.setHeartRateBpm(beatsPerMinuteT);
        final PositionT position = new PositionT();
        position.setLatitudeDegrees(pt.getLat().doubleValue());
        position.setLongitudeDegrees(pt.getLon().doubleValue());
        point.setPosition(position);

        try {
            final BigDecimal secondsAfterStart = pt.getTm();
            final XMLGregorianCalendar time = DatatypeFactory.newInstance().newXMLGregorianCalendar(startTime);
            final Duration duration = DatatypeFactory.newInstance().newDuration(secondsAfterStart.intValue() * 1000);
            time.add(duration);
            point.setTime(time);
        } catch (final DatatypeConfigurationException e) {
            logger.error(e);
        }
        return point;
    }
}
