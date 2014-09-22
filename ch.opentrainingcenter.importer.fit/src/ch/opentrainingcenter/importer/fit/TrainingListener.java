package ch.opentrainingcenter.importer.fit;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import ch.opentrainingcenter.core.assertions.Assertions;
import ch.opentrainingcenter.importer.fit.internal.ConvertGarminSemicircles;
import ch.opentrainingcenter.importer.fit.internal.ConvertGarminUtcTime;
import ch.opentrainingcenter.transfer.HeartRate;
import ch.opentrainingcenter.transfer.IStreckenPunkt;
import ch.opentrainingcenter.transfer.ITrackPointProperty;
import ch.opentrainingcenter.transfer.ITraining;
import ch.opentrainingcenter.transfer.RunData;
import ch.opentrainingcenter.transfer.Sport;
import ch.opentrainingcenter.transfer.factory.CommonTransferFactory;

import com.garmin.fit.Field;
import com.garmin.fit.Mesg;
import com.garmin.fit.MesgListener;
import com.garmin.fit.RecordMesg;
import com.garmin.fit.SessionMesg;

public class TrainingListener implements MesgListener {

    private static final Logger LOGGER = Logger.getLogger(TrainingListener.class);

    private static final String RECORD = "record"; //$NON-NLS-1$
    private static final String SESSION = "session"; //$NON-NLS-1$
    private final List<ITrackPointProperty> trackpoints = new ArrayList<>();
    private SessionMesg session;

    @Override
    public void onMesg(final Mesg mesg) {
        final String messageName = mesg.getName();
        if (RECORD.equals(messageName)) {
            trackpoints.add(convertTrackPoint(new RecordMesg(mesg)));
        } else if (SESSION.equals(messageName)) {
            session = new SessionMesg(mesg);
        }
        logMessage(mesg);
    }

    private ITrackPointProperty convertTrackPoint(final RecordMesg record) {
        final double distance = record.getDistance();
        final Integer positionLong = record.getPositionLong();
        final Integer positionLat = record.getPositionLat();
        final BigDecimal longDms = ConvertGarminSemicircles.convertSemicircleToDms(positionLong);
        final BigDecimal latDms = ConvertGarminSemicircles.convertSemicircleToDms(positionLat);

        final int heartbeat = record.getHeartRate() != null ? record.getHeartRate() : -1;
        final int altitude = record.getAltitude() != null ? record.getAltitude().intValue() : -1;
        final long time = ConvertGarminUtcTime.convertToLocalMillis(record.getTimestamp().getDate());
        final IStreckenPunkt streckenPunkt = CommonTransferFactory.createStreckenPunkt(distance, longDms.doubleValue(), latDms.doubleValue());
        return CommonTransferFactory.createTrackPointProperty(distance, heartbeat, altitude, time, 0, streckenPunkt);
    }

    @SuppressWarnings("nls")
    private void logMessage(final Mesg mesg) {
        LOGGER.info("[" + mesg.getName() + "] ");
        final StringBuilder message = new StringBuilder();
        for (final Field field : mesg.getFields()) {
            message.append(field.getName() + ": " + field.getDoubleValue() + " " + field.getUnits() + " / ");
        }
        LOGGER.info(message.toString());
    }

    public ITraining getTraining() {
        Assertions.notNull(session);
        final long dateOfStart = session.getStartTime().getTimestamp() * 1000;
        final double timeInSeconds = (session.getTimestamp().getTimestamp() * 1000 - dateOfStart) / 1000;
        final double distanceInMeter = session.getTotalDistance();
        final double maxSpeed = session.getMaxSpeed();
        final RunData runData = new RunData(dateOfStart, timeInSeconds, distanceInMeter, maxSpeed);
        final int average = session.getAvgHeartRate() != null ? session.getAvgHeartRate().intValue() : -1;
        final int max = session.getMaxHeartRate() != null ? session.getMaxHeartRate().intValue() : -1;
        final HeartRate heart = new HeartRate(average, max);
        final ITraining training = CommonTransferFactory.createTraining(runData, heart);
        training.setDatum(ConvertGarminUtcTime.convertToLocalMillis(session.getStartTime().getDate()));
        training.setTrackPoints(trackpoints);
        training.setDownMeter(session.getTotalDescent());
        training.setUpMeter(session.getTotalAscent());
        training.setSport(Sport.RUNNING);
        return training;
    }

}