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

    /**
     * Default konstruktor mit unbekanntem Wetter und einer leeren Notiz.
     */
    public ActivityExtension() {
        this.note = "";
        this.weather = CommonTransferFactory.createDefaultWeather();
    }

    public ActivityExtension(final String note, final IWeather weather) {
        this.note = note;
        this.weather = weather;
    }

    public String getNote() {
        return note;
    }

    public IWeather getWeather() {
        return weather;
    }

    @Override
    public String toString() {
        return "ActivityExtension [note=" + note + ", weather=" + weather.getWetter() + "]";
    }
}
