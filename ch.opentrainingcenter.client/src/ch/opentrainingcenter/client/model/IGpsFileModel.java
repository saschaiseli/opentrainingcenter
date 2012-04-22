package ch.opentrainingcenter.client.model;

public interface IGpsFileModel {

    public abstract int getId();

    public abstract String getFileName();

    public abstract RunType getTyp();

    public abstract void setTyp(final RunType typ);

    public abstract boolean isImportFile();

    public abstract void setImportFile(final boolean importFile);

}