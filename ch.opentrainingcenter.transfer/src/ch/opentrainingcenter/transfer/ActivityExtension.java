package ch.opentrainingcenter.transfer;

/**
 * In der Klasse {@link ActivityT} kann eine {@link ExtensionsT} hinzugefügt
 * werden. Dies ist im wesentlich einfach eine Liste von Objects. Die
 * {@link ActivityExtension} ist solches Objekt, das dort verwendet werden kann.
 * Daten welche zu dem Lauf gehören, aber nicht im XML / TCX oder was auch immer
 * abgespeichert werden, können hier abgelegt werden. Das Model muss dann aber
 * auch immer synchron mit der Datenbank sein. --> Im Cache muss dies beim
 * Updaten immer synchron mit der {@link ActivityT} und dem SimpleTraining
 * gehalten werden.
 * 
 */
public class ActivityExtension {

    private final String note;

    private final IWeather weather;

    private final IRoute route;

    /**
     * Default konstruktor mit unbekanntem Wetter und einer leeren Notiz.
     */
    public ActivityExtension() {
        this("", CommonTransferFactory.createDefaultWeather(), null); //$NON-NLS-1$
    }

    public ActivityExtension(final String note, final IWeather weather, final IRoute route) {
        this.note = note;
        this.weather = weather;
        this.route = route;
    }

    public String getNote() {
        return note;
    }

    public IWeather getWeather() {
        return weather;
    }

    public IRoute getRoute() {
        return route;
    }

    @SuppressWarnings("nls")
    @Override
    public String toString() {
        return "ActivityExtension [note=" + note + ", weather=" + weather + ", route=" + route + "]";
    }
}
