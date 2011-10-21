package ch.opentrainingcenter.transfer;

public class GarminFile {
    /**
     * Der Name ist eindeutig und somit die ID eines Datensatzes.
     */
    private String  fileName;
    private Athlete athlete;
    private boolean imported = false;

    public Athlete getAthlete() {
        return athlete;
    }

    public void setAthlete(Athlete athlete) {
        this.athlete = athlete;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public boolean isImported() {
        return imported;
    }

    public void setImported(boolean imported) {
        this.imported = imported;
    }

}
