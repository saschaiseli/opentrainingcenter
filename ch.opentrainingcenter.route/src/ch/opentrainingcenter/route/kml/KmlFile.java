package ch.opentrainingcenter.route.kml;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class KmlFile {

    private static final String HEAD = "<?xml version=\"1.0\" encoding=\"utf-8\" ?>\n<kml xmlns=\"http://www.opengis.net/kml/2.2\">\n<Document><Folder><name>"; //$NON-NLS-1$
    private static final String SCHEMA = "</name>\n<Schema name=\"Test\" id=\"Test\">\n<SimpleField name=\"Name\" type=\"string\"></SimpleField><SimpleField name=\"Description\" type=\"string\"></SimpleField><SimpleField name=\"name\" type=\"string\"></SimpleField><SimpleField name=\"number\" type=\"int\"></SimpleField><SimpleField name=\"comment\" type=\"string\"></SimpleField><SimpleField name=\"description\" type=\"string\"></SimpleField><SimpleField name=\"source\" type=\"string\"></SimpleField><SimpleField name=\"url\" type=\"string\"></SimpleField><SimpleField name=\"url name\" type=\"string\"></SimpleField></Schema>\n"; //$NON-NLS-1$
    private static final String END = "</Folder></Document></kml>"; //$NON-NLS-1$

    private final StringBuilder content = new StringBuilder(HEAD);
    private final List<KmlItem> items = new ArrayList<>();

    public KmlFile(final String name) {
        content.append(name).append(SCHEMA);
    }

    public void addKmlLine(final String lineName, final String color, final String coordinates) {
        items.add(new KmlLine(lineName, color, coordinates));
    }

    public void addPlacemark(final String name, final Map<String, String> extendedData, final String coordinates) {
        items.add(new KmlPlacemark(name, extendedData, coordinates));
    }

    public String getFile() {
        for (final KmlItem line : items) {
            content.append(line.getKmlLine());
        }
        content.append(END);
        return content.toString();
    }

}
