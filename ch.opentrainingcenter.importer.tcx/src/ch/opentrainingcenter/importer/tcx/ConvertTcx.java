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

import ch.opentrainingcenter.core.exceptions.ConvertException;
import ch.opentrainingcenter.core.helper.AltitudeCalculator;
import ch.opentrainingcenter.core.helper.AltitudeCalculator.Ascending;
import ch.opentrainingcenter.core.helper.DistanceHelper;
import ch.opentrainingcenter.core.importer.IConvert2Tcx;
import ch.opentrainingcenter.tcx.ActivityLapT;
import ch.opentrainingcenter.tcx.ActivityT;
import ch.opentrainingcenter.tcx.HeartRateInBeatsPerMinuteT;
import ch.opentrainingcenter.tcx.PositionT;
import ch.opentrainingcenter.tcx.SportT;
import ch.opentrainingcenter.tcx.TrackT;
import ch.opentrainingcenter.tcx.TrackpointT;
import ch.opentrainingcenter.tcx.TrainingCenterDatabaseT;
import ch.opentrainingcenter.transfer.HeartRate;
import ch.opentrainingcenter.transfer.ILapInfo;
import ch.opentrainingcenter.transfer.IStreckenPunkt;
import ch.opentrainingcenter.transfer.ITrackPointProperty;
import ch.opentrainingcenter.transfer.ITraining;
import ch.opentrainingcenter.transfer.RunData;
import ch.opentrainingcenter.transfer.Sport;
import ch.opentrainingcenter.transfer.factory.CommonTransferFactory;

public class ConvertTcx implements IConvert2Tcx {
    private static final Logger LOGGER = Logger.getLogger(ConvertTcx.class);
    private static final String RESOURCES_TCX_XSD = "resources/tcx.xsd"; //$NON-NLS-1$
    protected static final String NO_DATA = "NO GPS DATA"; //$NON-NLS-1$

    private final String locationOfSchema;
    private int error = 0;
    private int valid = 0;

    /**
     * Contructor for Eclipse Extension
     */
    public ConvertTcx() {
        LOGGER.info("ConvertTcx erfolgreich instanziert...."); //$NON-NLS-1$
        final Bundle bundle = Platform.getBundle(Activator.BUNDLE_ID);
        final Path path = new Path(RESOURCES_TCX_XSD);
        final URL url = FileLocator.find(bundle, path, Collections.emptyMap());
        URL fileUrl = null;
        try {
            fileUrl = FileLocator.toFileURL(url);
        } catch (final IOException e) {
            LOGGER.error(String.format("ConvertTcx failed. Schema '%s' nicht gefunden", url.toString())); //$NON-NLS-1$
        }
        final File f = new File(fileUrl.getPath());
        locationOfSchema = f.getAbsolutePath();
        LOGGER.info("ConvertTcx erfolgreich instanziert....fertig"); //$NON-NLS-1$
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

        final Schema schema = schemaFactory.newSchema(new File(locationOfSchema));
        unmarshaller.setSchema(schema);
        return unmarshaller;
    }

    @Override
    public ITraining convert(final File file) throws ConvertException {
        LOGGER.info("Start unmarshalling TCX File"); //$NON-NLS-1$
        error = 0;
        valid = 0;
        TrainingCenterDatabaseT completeFile;
        try {
            completeFile = unmarshall(file);
        } catch (JAXBException | SAXException e) {
            throw new ConvertException(e);
        }
        LOGGER.info("File unmarshalled"); //$NON-NLS-1$
        final List<ActivityT> activities = completeFile.getActivities().getActivity();
        final ActivityT activity = activities.get(0);
        return create(activity);
    }

    private ITraining create(final ActivityT activity) {
        // datum
        final Sport sport = translate(activity.getSport());
        final XMLGregorianCalendar date = activity.getId();
        final Date dateOfStart = date.toGregorianCalendar().getTime();
        final List<ActivityLapT> laps = activity.getLap();
        final List<ITrackPointProperty> trackPoints = new ArrayList<ITrackPointProperty>();
        final List<ILapInfo> lapInfos = new ArrayList<>();
        double distance = 0.0;
        double timeInSeconds = 0.0;
        int averageHeartRateBpm = 0;
        int maxHeartBeat = 0;
        int lapWithCardio = 0;
        double maximumSpeed = 0;
        int lapCount = 0;

        for (final ActivityLapT lap : laps) {
            LOGGER.info("Runde " + lapCount + " wird konvertiert"); //$NON-NLS-1$//$NON-NLS-2$
            final double lapDistance = lap.getDistanceMeters();
            final double totalTimeSeconds = lap.getTotalTimeSeconds();
            final String pace = DistanceHelper.calculatePace(lapDistance, totalTimeSeconds, Sport.RUNNING);
            final String gesch = DistanceHelper.calculatePace(lapDistance, totalTimeSeconds, Sport.BIKING);
            final HeartRateInBeatsPerMinuteT heart = lap.getAverageHeartRateBpm();
            int lapHeart = 0;
            if (heart != null) {
                lapHeart = heart.getValue();
            }
            lapInfos.add(CommonTransferFactory.createLapInfo(lapCount, (int) distance, (int) (lapDistance + distance), (int) (totalTimeSeconds * 1000),
                    lapHeart, pace, gesch));
            final List<ITrackPointProperty> trackPointsOfLap = getTrackPointsOfLap(lap, lapCount);
            trackPoints.addAll(trackPointsOfLap);
            lapCount++;
            distance += lapDistance;
            if (lap.getMaximumSpeed() != null && maximumSpeed < lap.getMaximumSpeed()) {
                maximumSpeed = lap.getMaximumSpeed();
            }
            timeInSeconds += totalTimeSeconds;
            if (!hasCardio(lap)) {
                continue;
            }
            lapWithCardio++;
            averageHeartRateBpm += lapHeart;
            if (maxHeartBeat < lap.getMaximumHeartRateBpm().getValue()) {
                maxHeartBeat = lap.getMaximumHeartRateBpm().getValue();
            }
            LOGGER.debug("lap: " + lap.getIntensity() + " distance: " + distance); //$NON-NLS-1$//$NON-NLS-2$
        }
        Integer avgHr = null;
        if (lapWithCardio > 0) {
            avgHr = averageHeartRateBpm / lapWithCardio;
        } else {
            avgHr = 0;
        }
        final RunData runData = new RunData(dateOfStart.getTime(), timeInSeconds, distance, maximumSpeed);
        final HeartRate heart = new HeartRate(avgHr, maxHeartBeat);
        final ITraining training = CommonTransferFactory.createTraining(runData, heart);
        training.setTrackPoints(trackPoints);
        training.setLapInfos(lapInfos);
        final Ascending ascending = AltitudeCalculator.calculateAscending(trackPoints);
        training.setUpMeter(ascending.getUp());
        training.setDownMeter(ascending.getDown());
        training.setSport(sport);
        if (trackPoints.isEmpty()) {
            training.setNote(NO_DATA);
        }
        final int total = error + valid;
        final int fehlerInProzent = (int) (100 * (error / (float) total));
        training.setGeoQuality(fehlerInProzent);
        LOGGER.info(String.format("Qualität der Geodaten: '%s' [prozent] fehlerhafte Geodaten (%s von %s sind fehlerhaft)", fehlerInProzent, error, total)); //$NON-NLS-1$
        return training;
    }

    private List<ITrackPointProperty> getTrackPointsOfLap(final ActivityLapT lap, final int lapCount) {
        final List<ITrackPointProperty> property = new ArrayList<ITrackPointProperty>();
        final List<TrackT> tracks = lap.getTrack();
        for (final TrackT track : tracks) {
            final List<TrackpointT> trackpoints = track.getTrackpoint();
            for (final TrackpointT trackpoint : trackpoints) {
                if (isTrackPointValid(trackpoint)) {
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
        }
        return property;
    }

    private boolean isTrackPointValid(final TrackpointT t) {
        if (t.getAltitudeMeters() == null || t.getDistanceMeters() == null || t.getPosition() == null) {
            LOGGER.warn("Ungültiger Trackpoint um '" + t.getTime() + "' gefunden"); //$NON-NLS-1$ //$NON-NLS-2$
            error++;
            return false;
        } else {
            valid++;
            return true;
        }
    }

    private Sport translate(final SportT in) {
        if (SportT.BIKING.equals(in)) {
            return Sport.BIKING;
        } else if (SportT.RUNNING.equals(in)) {
            return Sport.RUNNING;
        } else {
            return Sport.OTHER;
        }
    }

    private boolean hasCardio(final ActivityLapT lap) {
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
