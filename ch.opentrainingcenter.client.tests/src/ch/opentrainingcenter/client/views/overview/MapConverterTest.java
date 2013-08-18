package ch.opentrainingcenter.client.views.overview;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.mockito.Mockito;

import ch.opentrainingcenter.model.geo.Track;
import ch.opentrainingcenter.model.geo.TrackPoint;
import ch.opentrainingcenter.transfer.IStreckenPunkt;
import ch.opentrainingcenter.transfer.ITrackPointProperty;
import ch.opentrainingcenter.transfer.ITraining;

@SuppressWarnings("nls")
public class MapConverterTest {

    private static final double DELTA = 0.000000001;

    @Test
    public void testGetFirstPoint() {
        final String firstPointToPan = MapConverter.getFirstPointToPan("[[25.774252,-80.190262],[18.466465,-66.118292], [46.954, 7.448]]");//$NON-NLS-1$
        assertEquals("25.774252,-80.190262", firstPointToPan);//$NON-NLS-1$
    }

    @Test
    public void testMapConvert() {
        final ITraining training = Mockito.mock(ITraining.class);
        final List<ITrackPointProperty> trackPoints = new ArrayList<ITrackPointProperty>();
        trackPoints.add(createTrackPoint(1.5, 7.77777, 46.7754));
        trackPoints.add(createTrackPoint(2.5, 7.87777, 46.8754));
        trackPoints.add(createTrackPoint(3.5, 7.97777, 46.9754));
        Mockito.when(training.getTrackPoints()).thenReturn(trackPoints);
        final String track = MapConverter.convertTrackpoints(training);

        assertEquals("[[46.7754,7.77777],[46.8754,7.87777],[46.9754,7.97777]]", track);
    }

    @Test
    public void testMapConverter() {
        final ITraining training = Mockito.mock(ITraining.class);
        final List<ITrackPointProperty> trackPoints = new ArrayList<ITrackPointProperty>();
        trackPoints.add(createTrackPoint(1.5, 7.77777, 46.7754));
        trackPoints.add(createTrackPoint(2.5, 7.87777, 46.8754));
        trackPoints.add(createTrackPoint(3.5, 7.97777, 46.9754));
        Mockito.when(training.getTrackPoints()).thenReturn(trackPoints);
        final Track track = MapConverter.convert(training);
        final List<TrackPoint> points = track.getPoints();
        assertEquals(3, points.size());
        assertPoint(points.get(0), 1.5, 7.77777, 46.7754);
        assertPoint(points.get(1), 2.5, 7.87777, 46.8754);
        assertPoint(points.get(2), 3.5, 7.97777, 46.9754);
    }

    private void assertPoint(final TrackPoint trackPoint, final double dist, final double longitude, final double latitude) {
        assertEquals("Distanz: ", dist, trackPoint.getDistance(), DELTA);
        assertEquals("Longitude: ", longitude, trackPoint.getyCoordinates(), DELTA);
        assertEquals("Latitude: ", latitude, trackPoint.getxCoordinates(), DELTA);
    }

    private ITrackPointProperty createTrackPoint(final double dist, final double longitude, final double latitude) {
        final ITrackPointProperty mock = Mockito.mock(ITrackPointProperty.class);
        Mockito.when(mock.getDistance()).thenReturn(dist);
        final IStreckenPunkt streckenPunkt = Mockito.mock(IStreckenPunkt.class);
        Mockito.when(streckenPunkt.getLongitude()).thenReturn(longitude);
        Mockito.when(streckenPunkt.getLatitude()).thenReturn(latitude);
        Mockito.when(mock.getStreckenPunkt()).thenReturn(streckenPunkt);
        return mock;
    }
}
