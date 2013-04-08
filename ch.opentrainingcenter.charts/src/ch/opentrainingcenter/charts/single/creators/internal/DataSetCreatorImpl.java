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
import ch.opentrainingcenter.transfer.ITrackPointProperty;
import ch.opentrainingcenter.transfer.ITraining;

public class DataSetCreatorImpl implements DataSetCreator {

    private final ITraining training;

    public DataSetCreatorImpl(final ITraining training) {
        this.training = training;
    }

    @Override
    public XYDataset createDatasetHeart() {
        return createDataSet(ChartType.HEART_DISTANCE);
    }

    @Override
    public XYDataset createDatasetSpeed() {
        final List<PositionPace> positionPaces = new ArrayList<PositionPace>();
        final List<ITrackPointProperty> points = training.getTrackPoints();
        ITrackPointProperty previous = null;
        for (final ITrackPointProperty point : points) {
            if (previous != null) {
                final PositionPace speedPoint = getSpeedPoint(point, previous);
                if (speedPoint != null) {
                    positionPaces.add(speedPoint);
                }
            }
            previous = point;
        }
        final SpeedCompressor compressor = new SpeedCompressor(8);
        final List<PositionPace> compressSpeedDataPoints = compressor.compressSpeedDataPoints(positionPaces);
        final XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(SpeedChartSupport.putPointsInSerie(new XYSeries(Messages.SingleActivityViewPart18), compressSpeedDataPoints));

        return dataset;
    }

    private PositionPace getSpeedPoint(final ITrackPointProperty point, final ITrackPointProperty previous) {
        PositionPace positionPace = null;
        final double d2 = point.getDistance();
        final double d1 = previous.getDistance();
        final double t2 = point.getZeit() / 1000;
        final double t1 = previous.getZeit() / 1000;
        final double pace = SpeedCalculator.calculatePace(d1, d2, t1, t2);
        if (0 < pace && pace < 10) {
            positionPace = new PositionPace(point.getDistance(), pace);
        }
        return positionPace;
    }

    @Override
    public XYDataset createDatasetAltitude() {
        return createDataSet(ChartType.ALTITUDE_DISTANCE);
    }

    private XYDataset createDataSet(final ChartType type) {
        final List<ITrackPointProperty> points = training.getTrackPoints();
        final XYSeries series = new XYSeries(Messages.SingleActivityViewPart18);
        for (final ITrackPointProperty point : points) {
            addPoint(type, series, point);
        }
        final XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(series);

        return dataset;
    }

    private void addPoint(final ChartType type, final XYSeries serie, final ITrackPointProperty point) {
        switch (type) {
        case HEART_DISTANCE: {
            serie.add(point.getDistance(), point.getHeartBeat());
            break;
        }
        case ALTITUDE_DISTANCE: {
            serie.add(point.getDistance(), point.getAltitude());
            break;
        }
        case SPEED_DISTANCE: {
            throw new IllegalArgumentException("Do not use this method for speed "); //$NON-NLS-1$
        }
        }
    }
}
