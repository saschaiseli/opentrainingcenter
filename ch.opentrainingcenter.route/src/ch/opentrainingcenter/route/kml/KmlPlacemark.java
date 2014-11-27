package ch.opentrainingcenter.route.kml;

import java.util.List;

import ch.opentrainingcenter.core.data.SimplePair;

/**
 * <pre>
 * <Placemark>
 *     <name>Club house</name>
 *     <ExtendedData>
 *       <Data name="holeNumber">
 *         <value>1</value>
 *       </Data>
 *       <Data name="holeYardage">
 *         <value>234</value>
 *       </Data>
 *       <Data name="holePar">
 *         <value>4</value>
 *       </Data>
 *     </ExtendedData>
 *     <Point>
 *       <coordinates>-111.956,33.5043</coordinates>
 *     </Point>
 *   </Placemark>
 * </pre>
 *
 */
@SuppressWarnings("nls")
public class KmlPlacemark implements KmlItem {
    private static final String HEAD = "<Placemark><name>";
    private static final String HEAD_END = "</name><ExtendedData>";
    private static final String EXTENDED_DATA_END = "</ExtendedData><Point><coordinates>";
    private static final String END = "</coordinates></Point></Placemark>";
    private final StringBuilder content;

    public KmlPlacemark(final String name, final List<SimplePair<String>> placemarks, final String coordinates) {
        content = new StringBuilder(HEAD);
        content.append(name);
        content.append(HEAD_END);
        // extended content
        for (final SimplePair<String> pair : placemarks) {
            content.append("<Data name=\"").append(pair.getFirst()).append("\"><value>").append(pair.getSecond()).append("</value></Data>");
        }
        content.append(EXTENDED_DATA_END);
        content.append(coordinates);
        content.append(END);
    }

    @Override
    public String getKmlLine() {
        return content.toString();
    }

}
