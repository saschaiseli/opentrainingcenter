package ch.opentrainingcenter.model.importer;

import ch.opentrainingcenter.core.helper.RunType;
import ch.opentrainingcenter.model.strecke.StreckeModel;

public interface IGpsFileModel {

    int getId();

    String getFileName();

    RunType getTyp();

    void setTyp(final RunType typ);

    boolean isImportFile();

    void setImportFile(final boolean importFile);

    StreckeModel getRoute();

    void setRoute(StreckeModel strecke);

}