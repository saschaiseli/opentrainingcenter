package ch.opentrainingcenter.importer.fit;

import java.util.ArrayList;
import java.util.List;

import ch.opentrainingcenter.transfer.HeartRate;
import ch.opentrainingcenter.transfer.IStreckenPunkt;
import ch.opentrainingcenter.transfer.ITrackPointProperty;
import ch.opentrainingcenter.transfer.ITraining;
import ch.opentrainingcenter.transfer.RunData;
import ch.opentrainingcenter.transfer.factory.CommonTransferFactory;

import com.garmin.fit.Field;
import com.garmin.fit.Mesg;
import com.garmin.fit.MesgListener;
import com.garmin.fit.RecordMesg;
import com.garmin.fit.SessionMesg;

public class TrainingListener implements MesgListener {

    private static final String FILE_ID = "file_id";
    private static final String RECORD = "record";
    private static final String SESSION = "session";
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
        convertUnknown(mesg);
    }

    private ITrackPointProperty convertTrackPoint(final Mesg mesg) {
        final RecordMesg record = new RecordMesg(mesg);
        final double distance = record.getDistance();
        final double longitude = record.getPositionLong();
        final double latitude = record.getPositionLat();
        final int heartbeat = record.getHeartRate();
        final int altitude = record.getHeartRate();
        final long time = record.getTimestamp().getTimestamp();
        final IStreckenPunkt streckenPunkt = CommonTransferFactory.createStreckenPunkt(distance, longitude, latitude);
        return CommonTransferFactory.createTrackPointProperty(distance, heartbeat, altitude, time, 0, streckenPunkt);
    }

    private void convertUnknown(final Mesg mesg) {
        System.out.print("[" + mesg.getName() + "] ");
        for (final Field field : mesg.getFields()) {
            System.out.print(field.getName() + ": " + field.getDoubleValue() + " " + field.getUnits() + " / ");
        }
        System.out.println();
    }

    public ITraining getTraining() {
        final long dateOfStart = 1;
        final double timeInSeconds = 1;
        final double distanceInMeter = 1;
        final double maxSpeed = 1;
        final RunData runData = new RunData(dateOfStart, timeInSeconds, distanceInMeter, maxSpeed);
        final int average = 1;
        final int max = 1;
        final HeartRate heart = new HeartRate(average, max);

        final ITraining training = CommonTransferFactory.createTraining(runData, heart);
        training.setTrackPoints(trackpoints);
        return null;
    }

}
