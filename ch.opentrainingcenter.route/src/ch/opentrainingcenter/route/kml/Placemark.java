package ch.opentrainingcenter.route.kml;

public class Placemark {
    private final String label;
    private final String value;

    public Placemark(final String label, final String value) {
        super();
        this.label = label;
        this.value = value;
    }

    public String getLabel() {
        return label;
    }

    public String getValue() {
        return value;
    }

}
