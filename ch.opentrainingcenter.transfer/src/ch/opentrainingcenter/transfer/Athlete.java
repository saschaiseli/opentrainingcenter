package ch.opentrainingcenter.transfer;

public class Athlete {
    private final String vorname;
    private final String nachname;

    public Athlete(String vorname, String nachname) {
        super();
        this.vorname = vorname;
        this.nachname = nachname;
    }

    public String getVorname() {
        return vorname;
    }

    public String getNachname() {
        return nachname;
    }

    @Override
    public String toString() {
        return vorname;
    }
}
