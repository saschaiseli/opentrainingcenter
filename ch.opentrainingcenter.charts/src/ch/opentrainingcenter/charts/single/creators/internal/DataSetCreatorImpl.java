package ch.opentrainingcenter.charts.single.creators.internal;

import java.util.ArrayList;
import java.util.List;

import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import ch.opentrainingcenter.charts.bar.internal.PositionPace;
import ch.opentrainingcenter.charts.bar.internal.SpeedChartSupport;
import ch.opentrainingcenter.charts.bar.internal.SpeedCompressor;
import ch.opentrainingcenter.charts.single.ChartType;
import ch.opentrainingcenter.core.helper.SpeedCalculator;
import ch.opentrainingcenter.i18n.Messages;
import ch.opentrainingcenter.tcx.ActivityLapT;
import ch.opentrainingcenter.tcx.ActivityT;
import ch.opentrainingcenter.tcx.HeartRateInBeatsPerMinuteT;
import ch.opentrainingcenter.tcx.TrackT;
import ch.opentrainingcenter.tcx.TrackpointT;

public class DataSetCreatorImpl implements DataSetCreator {

    private final ActivityT selected;

    public DataSetCreatorImpl(final ActivityT selected) {
        this.selected = selected;
    }

    @Override
    public XYDataset createDatasetHeart() {
        return createDataSet(ChartType.HEART_DISTANCE);
    }

    @Override
    public XYDataset createDatasetSpeed() {
        final List<ActivityLapT> laps = selected.getLap();
        final List<PositionPace> positionPaces = new ArrayList<PositionPace>();
        for (final ActivityLapT activityLapT : laps) {
            final List<TrackT> tracks = activityLapT.getTrack();
            for (final TrackT track : tracks) {
                final List<TrackpointT> trackpoints = track.getTrackpoint();
                TrackpointT previousTrackPoint = null;
                for (final TrackpointT trackpoint : trackpoints) {
                    final PositionPace speedPoint = getSpeedPoint(trackpoint, previousTrackPoint);
                    if (speedPoint != null) {
                        positionPaces.add(speedPoint);
                    }
                    previousTrackPoint = trackpoint;
                }
            }
        }
        final SpeedCompressor compressor = new SpeedCompressor(8);
        final List<PositionPace> compressSpeedDataPoints = compressor.compressSpeedDataPoints(positionPaces);
        final XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(SpeedChartSupport.putPointsInSerie(new XYSeries(Messages.SingleActivityViewPart18), compressSpeedDataPoints));

        return dataset;
    }

    private PositionPace getSpeedPoint(final TrackpointT point, final TrackpointT previousPoint) {
        PositionPace positionPace = null;
        if (previousPoint != null && validateTrackPointVorSpeed(point) && validateTrackPointVorSpeed(previousPoint)) {
            final double d2 = point.getDistanceMeters();
            final double d1 = previousPoint.getDistanceMeters();
            final double t2 = point.getTime().toGregorianCalendar().getTimeInMillis() / 1000;
            final double t1 = previousPoint.getTime().toGregorianCalendar().getTimeInMillis() / 1000;
            final double pace = SpeedCalculator.calculatePace(d1, d2, t1, t2);
            if (0 < pace && pace < 10) {
                positionPace = new PositionPace(point.getDistanceMeters().doubleValue(), pace);
            }
        }
        return positionPace;
    }

    private boolean validateTrackPointVorSpeed(final TrackpointT point) {
        return point.getDistanceMeters() != null && point.getTime() != null;
    }

    @Override
    public XYDataset createDatasetAltitude() {
        return createDataSet(ChartType.ALTITUDE_DISTANCE);
    }

    private XYDataset createDataSet(final ChartType type) {
        final List<ActivityLapT> laps = selected.getLap();
        final XYSeries series = new XYSeries(Messages.SingleActivityViewPart18);
        for (final ActivityLapT activityLapT : laps) {
            final List<TrackT> tracks = activityLapT.getTrack();
            for (final TrackT track : tracks) {
                final List<TrackpointT> trackpoints = track.getTrackpoint();
                for (final TrackpointT trackpoint : trackpoints) {
                    addPoint(type, series, trackpoint);
                }
            }
        }
        final XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(series);

        return dataset;
    }

    private void addPoint(final ChartType type, final XYSeries serie, final TrackpointT point) {
        switch (type) {
        case HEART_DISTANCE: {
            final Double m = point.getDistanceMeters();
            final HeartRateInBeatsPerMinuteT bpm = point.getHeartRateBpm();
            if (m != null && bpm != null) {
                serie.add(point.getDistanceMeters().doubleValue(), point.getHeartRateBpm().getValue());
            }
            break;
        }
        case ALTITUDE_DISTANCE: {
            final Double m = point.getDistanceMeters();
            final Double alti = point.getAltitudeMeters();
            if (m != null && alti != null) {
                serie.add(point.getDistanceMeters().doubleValue(), alti.doubleValue());
            }
            break;
        }
        case SPEED_DISTANCE: {
            throw new IllegalArgumentException("Do not use this method for speed "); //$NON-NLS-1$
        }
        }
    }
}
