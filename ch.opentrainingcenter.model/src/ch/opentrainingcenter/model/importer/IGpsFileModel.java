package ch.opentrainingcenter.model.importer;

import ch.opentrainingcenter.transfer.IRoute;
import ch.opentrainingcenter.transfer.IShoe;
import ch.opentrainingcenter.transfer.TrainingType;

public interface IGpsFileModel {

    int getId();

    String getFileName();

    TrainingType getTyp();

    void setTyp(final TrainingType typ);

    boolean isImportFile();

    void setImportFile(final boolean importFile);

    IRoute getRoute();

    void setRoute(IRoute strecke);

    IShoe getSchuh();

    void setSchuh(IShoe shoe);

}