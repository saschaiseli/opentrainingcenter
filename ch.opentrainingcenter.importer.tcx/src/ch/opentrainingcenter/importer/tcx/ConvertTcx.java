package ch.opentrainingcenter.importer.tcx;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.osgi.framework.Bundle;
import org.xml.sax.SAXException;

import ch.opentrainingcenter.core.importer.IConvert2Tcx;
import ch.opentrainingcenter.tcx.ActivityLapT;
import ch.opentrainingcenter.tcx.ActivityT;
import ch.opentrainingcenter.tcx.HeartRateInBeatsPerMinuteT;
import ch.opentrainingcenter.tcx.PositionT;
import ch.opentrainingcenter.tcx.TrackT;
import ch.opentrainingcenter.tcx.TrackpointT;
import ch.opentrainingcenter.tcx.TrainingCenterDatabaseT;
import ch.opentrainingcenter.transfer.ActivityExtension;
import ch.opentrainingcenter.transfer.CommonTransferFactory;
import ch.opentrainingcenter.transfer.IStreckenPunkt;
import ch.opentrainingcenter.transfer.ITrackPointProperty;
import ch.opentrainingcenter.transfer.ITraining;

public class ConvertTcx implements IConvert2Tcx {
    private static final Logger LOGGER = Logger.getLogger(ConvertTcx.class);
    private static final String RESOURCES_TCX_XSD = "resources/tcx.xsd";//$NON-NLS-1$
    protected static final String NO_DATA = "NO GPS DATA"; //$NON-NLS-1$

    private final String locationOfScript;

    /**
     * Contructor for Eclipse Extension
     */
    public ConvertTcx() {
        LOGGER.info("ConvertTcx erfolgreich instanziert....");//$NON-NLS-1$
        final Bundle bundle = Platform.getBundle(Activator.BUNDLE_ID);
        final Path path = new Path(RESOURCES_TCX_XSD);
        final URL url = FileLocator.find(bundle, path, Collections.EMPTY_MAP);
        URL fileUrl = null;
        try {
            fileUrl = FileLocator.toFileURL(url);
        } catch (final IOException e) {
            LOGGER.error("Fehler beim Instanzieren von ConvertTcx: " + e.getMessage());//$NON-NLS-1$
            throw new RuntimeException(e);
        }
        final File f = new File(fileUrl.getPath());
        locationOfScript = f.getAbsolutePath();
        LOGGER.info("ConvertTcx erfolgreich instanziert....fertig");//$NON-NLS-1$
    }

    @SuppressWarnings("unchecked")
    public TrainingCenterDatabaseT unmarshall(final File file) throws JAXBException, SAXException {
        final Unmarshaller unmarshaller = createUnmarshaller();

        final JAXBElement<TrainingCenterDatabaseT> jaxb = (JAXBElement<TrainingCenterDatabaseT>) unmarshaller.unmarshal(file);
        return jaxb.getValue();
    }

    private Unmarshaller createUnmarshaller() throws JAXBException, SAXException {
        final JAXBContext jc = JAXBContext.newInstance(TrainingCenterDatabaseT.class);
        final Unmarshaller unmarshaller = jc.createUnmarshaller();

        final SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);

        final Schema schema = schemaFactory.newSchema(new File(locationOfScript));
        unmarshaller.setSchema(schema);
        return unmarshaller;
    }

    @Override
    public ITraining convert(final File file) throws Exception {
        final TrainingCenterDatabaseT completeFile = unmarshall(file);
        final List<ActivityT> activities = completeFile.getActivities().getActivity();
        final ActivityT activity = activities.get(0);
        final ITraining training = create(activity);
        return training;
    }

    private ITraining create(final ActivityT activity) {
        // datum
        final XMLGregorianCalendar date = activity.getId();
        final Date dateOfStart = date.toGregorianCalendar().getTime();
        final List<ActivityLapT> laps = activity.getLap();
        final List<ITrackPointProperty> trackPoints = new ArrayList<ITrackPointProperty>();
        double distance = 0.0;
        double timeInSeconds = 0.0;
        int averageHeartRateBpm = 0;
        int maxHeartBeat = 0;
        int lapWithCardio = 0;
        double maximumSpeed = 0;
        int lapCount = 1;
        for (final ActivityLapT lap : laps) {
            final List<ITrackPointProperty> trackPointsOfLap = getTrackPointsOfLap(lap, lapCount);
            trackPoints.addAll(trackPointsOfLap);
            lapCount++;
            distance += lap.getDistanceMeters();
            if (lap.getMaximumSpeed() != null && maximumSpeed < lap.getMaximumSpeed()) {
                maximumSpeed = lap.getMaximumSpeed();
            }
            timeInSeconds += lap.getTotalTimeSeconds();
            if (!hasCardio(lap)) {
                continue;
            }
            lapWithCardio++;
            averageHeartRateBpm += lap.getAverageHeartRateBpm() != null ? lap.getAverageHeartRateBpm().getValue() : 0;
            if (maxHeartBeat < lap.getMaximumHeartRateBpm().getValue()) {
                maxHeartBeat = lap.getMaximumHeartRateBpm().getValue();
            }
            LOGGER.debug("lap: " + lap.getIntensity() + " distance: " + distance); //$NON-NLS-1$//$NON-NLS-2$
        }
        Integer avgHeartRate = null;
        if (lapWithCardio > 0) {
            avgHeartRate = averageHeartRateBpm / lapWithCardio;
        } else {
            avgHeartRate = 0;
        }
        final ActivityExtension activityExtension = new ActivityExtension();

        final ITraining training = CommonTransferFactory.createTraining(dateOfStart.getTime(), timeInSeconds, distance, avgHeartRate, maxHeartBeat,
                maximumSpeed, activityExtension);
        training.setTrackPoints(trackPoints);
        if (trackPoints.isEmpty()) {
            training.setNote(NO_DATA);
        }
        return training;
    }

    private List<ITrackPointProperty> getTrackPointsOfLap(final ActivityLapT lap, final int lapCount) {
        final List<ITrackPointProperty> property = new ArrayList<ITrackPointProperty>();
        final List<TrackT> tracks = lap.getTrack();
        for (final TrackT track : tracks) {
            final List<TrackpointT> trackpoints = track.getTrackpoint();
            for (final TrackpointT trackpoint : trackpoints) {
                final int altitude = trackpoint.getAltitudeMeters().intValue();
                final double distance = trackpoint.getDistanceMeters();
                final HeartRateInBeatsPerMinuteT heartRateBpm = trackpoint.getHeartRateBpm();
                int heartbeat = 0;
                if (heartRateBpm != null) {
                    heartbeat = heartRateBpm.getValue();
                }
                final long time = trackpoint.getTime().toGregorianCalendar().getTime().getTime();
                final PositionT position = trackpoint.getPosition();
                IStreckenPunkt geoPunkt = null;
                final double longitude = position.getLongitudeDegrees();
                final double latitude = position.getLatitudeDegrees();
                geoPunkt = CommonTransferFactory.createStreckenPunkt(distance, longitude, latitude);
                property.add(CommonTransferFactory.createTrackPointProperty(distance, heartbeat, altitude, time, lapCount, geoPunkt));
            }
        }
        return property;
    }

    private static boolean hasCardio(final ActivityLapT lap) {
        return lap.getMaximumHeartRateBpm() != null;
    }

    @Override
    public String getFilePrefix() {
        return "tcx"; //$NON-NLS-1$
    }

    @Override
    public String getName() {
        return "TCX (mit ANT+ aus der Uhr exportiert)"; //$NON-NLS-1$
    }

}
