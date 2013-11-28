package ch.opentrainingcenter.importer.gpx;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import ch.opentrainingcenter.transfer.ITrackPointProperty;

import com.topografix.gpx.ExtensionsType;
import com.topografix.gpx.WptType;

public class GpxConverterTest {
    GpxConverter converter;
    private GregorianCalendar gc;

    @Before
    public void setUp() throws DatatypeConfigurationException {
        converter = new GpxConverter();
        gc = new GregorianCalendar();

    }

    @Test
    public void testConvertWaypoint2TrackPoint_PreviousIsNull() throws DatatypeConfigurationException {
        final WptType wayPoint = createWayPoint(650, 46.737999, 7.967988, null, 100000L);

        final ITrackPointProperty trackPoint = converter.convertWaypoint2TrackPoint(wayPoint, null, 1);

        assertEquals(650, trackPoint.getAltitude());
        assertEquals(100000L, trackPoint.getZeit());
        assertEquals(0, trackPoint.getHeartBeat());
    }

    @Test
    public void testConvertWaypoint2TrackPoint_PreviousIsNull_mit_HeartBeat() throws DatatypeConfigurationException {
        final WptType wayPoint = createWayPoint(650, 46.737999, 7.967988, "42", 100000L); //$NON-NLS-1$

        final ITrackPointProperty trackPoint = converter.convertWaypoint2TrackPoint(wayPoint, null, 1);

        assertEquals(650, trackPoint.getAltitude());
        assertEquals(100000L, 0, trackPoint.getZeit());
        assertEquals(42, trackPoint.getHeartBeat());
    }

    @Test
    public void testConvertWaypoint2TrackPoint_MitHerz() throws DatatypeConfigurationException {
        final WptType previous = createWayPoint(550, 46.737999, 7.967988, "100", 100000L); //$NON-NLS-1$
        final WptType wayPoint = createWayPoint(650, 46.737999, 7.967988, null, 101000L);

        final ITrackPointProperty trackPoint = converter.convertWaypoint2TrackPoint(wayPoint, previous, 1);

        assertEquals(650, trackPoint.getAltitude());
        assertEquals(101000L, trackPoint.getZeit());
        assertEquals(0, trackPoint.getHeartBeat());
    }

    private WptType createWayPoint(final int altitude, final double lat, final double longitude, final String heart, final long millis)
            throws DatatypeConfigurationException {
        final WptType wayPoint = mock(WptType.class);
        when(wayPoint.getEle()).thenReturn(BigDecimal.valueOf(altitude));
        when(wayPoint.getLat()).thenReturn(BigDecimal.valueOf(lat));
        when(wayPoint.getLon()).thenReturn(BigDecimal.valueOf(longitude));
        final ExtensionsType extensionType = mock(ExtensionsType.class);
        if (heart != null) {
            final List<Object> extensions = new ArrayList<>();
            final Element el = mock(Element.class);
            final Node firstChild = mock(Node.class);
            when(firstChild.getNodeName()).thenReturn("gpxtpx:hr"); //$NON-NLS-1$
            when(firstChild.getTextContent()).thenReturn(heart);
            when(el.getFirstChild()).thenReturn(firstChild);
            extensions.add(el);
            when(extensionType.getAny()).thenReturn(extensions);
            when(wayPoint.getExtensions()).thenReturn(extensionType);
        }

        gc.setTime(new Date(millis));
        final DatatypeFactory df = DatatypeFactory.newInstance();
        final XMLGregorianCalendar datum = df.newXMLGregorianCalendar(gc);

        when(wayPoint.getTime()).thenReturn(datum);
        return wayPoint;
    }
}
