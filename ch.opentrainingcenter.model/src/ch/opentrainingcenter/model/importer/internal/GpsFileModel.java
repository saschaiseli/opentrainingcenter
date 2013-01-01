package ch.opentrainingcenter.model.importer.internal;

import ch.opentrainingcenter.core.helper.RunType;
import ch.opentrainingcenter.model.importer.IGpsFileModel;
import ch.opentrainingcenter.model.strecke.StreckeModel;

public class GpsFileModel implements IGpsFileModel {

    private boolean importFile;
    private final String fileName;
    private RunType typ;
    private StreckeModel strecke;

    public GpsFileModel(final String fileName) {
        this.fileName = fileName;
        importFile = true;
        typ = RunType.NONE;
    }

    @Override
    public int getId() {
        return typ.getIndex();
    }

    @Override
    public String getFileName() {
        return fileName;
    }

    @Override
    public RunType getTyp() {
        return typ;
    }

    @Override
    public void setTyp(final RunType typ) {
        this.typ = typ;
    }

    @Override
    public boolean isImportFile() {
        return importFile;
    }

    @Override
    public void setImportFile(final boolean importFile) {
        this.importFile = importFile;
    }

    @Override
    public StreckeModel getRoute() {
        return strecke;
    }

    @Override
    public void setRoute(final StreckeModel strecke) {
        this.strecke = strecke;
    }
}
