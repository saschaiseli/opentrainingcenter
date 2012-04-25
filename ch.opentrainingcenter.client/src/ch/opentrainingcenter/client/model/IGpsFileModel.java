package ch.opentrainingcenter.client.model;

public interface IGpsFileModel {

    int getId();

    String getFileName();

    RunType getTyp();

    void setTyp(final RunType typ);

    boolean isImportFile();

    void setImportFile(final boolean importFile);

}