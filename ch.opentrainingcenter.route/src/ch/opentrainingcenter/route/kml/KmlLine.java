package ch.opentrainingcenter.route.kml;

/**
 * Erstellt eine Simple Line in der folgenden Form:
 * 
 * <pre>
 *   <Placemark>
 *      <name>TestRoute</name>
 *      <description>nix</description>
 *      <Style><LineStyle><color>ff0000ff</color></LineStyle><PolyStyle><fill>0</fill></PolyStyle></Style>
 *      <ExtendedData><SchemaData schemaUrl="#Test">
 *              <SimpleData name="Name">TestRoute</SimpleData>
 *              <SimpleData name="Description">nix</SimpleData>
 *              <SimpleData name="comment">nix</SimpleData>
 *              <SimpleData name="source">Digitized in QGIS</SimpleData>
 *              <SimpleData name="url">nix</SimpleData>
 *              <SimpleData name="url name">nix</SimpleData>
 *      </SchemaData></ExtendedData>
 *    <LineString><coordinates>7.43019129,46.94525943 7.43013446,46.94523915 7.42995945,46.94519866 7.42990639,46.94518299
 *   </coordinates></LineString>
 * </Placemark>
 * </pre>
 * 
 * @author sascha
 * 
 */
public class KmlLine {
    private static final String HEAD = "<Placemark><name>"; //$NON-NLS-1$
    private static final String HEAD_END = "</name><description>nix</description><Style><LineStyle><color>"; //$NON-NLS-1$
    private static final String EXTENDED_DATA = "</color></LineStyle><PolyStyle><fill>0</fill></PolyStyle></Style><ExtendedData><SchemaData schemaUrl=\"#Test\"><SimpleData name=\"Name\">TestRoute</SimpleData><SimpleData name=\"Description\">nix</SimpleData><SimpleData name=\"comment\">nix</SimpleData><SimpleData name=\"source\">Digitized in QGIS</SimpleData><SimpleData name=\"url\">nix</SimpleData><SimpleData name=\"url name\">nix</SimpleData></SchemaData></ExtendedData><LineString><coordinates>"; //$NON-NLS-1$
    private static final String END = "</coordinates></LineString></Placemark>"; //$NON-NLS-1$
    private final StringBuffer content;

    /**
     * @param name
     *            der linie
     * @param color
     *            farbe in form 'ff0000ff'
     * @param coordinates
     *            koordinaten longitude, latitude --> '7.43019129,46.94525943
     *            7.43013446,46.94523915'
     */
    public KmlLine(final String name, final String color, final String coordinates) {
        content = new StringBuffer(HEAD);
        content.append(name).append(HEAD_END).append(color).append(EXTENDED_DATA).append(coordinates).append(END);
    }

    /**
     * @return die line.
     */
    protected String getKmlLine() {
        return content.toString();
    }
}
