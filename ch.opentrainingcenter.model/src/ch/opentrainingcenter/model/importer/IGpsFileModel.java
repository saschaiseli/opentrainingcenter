package ch.opentrainingcenter.model.importer;

import ch.opentrainingcenter.core.helper.RunType;

public interface IGpsFileModel {

    int getId();

    String getFileName();

    RunType getTyp();

    void setTyp(final RunType typ);

    boolean isImportFile();

    void setImportFile(final boolean importFile);

}