package ch.opentrainingcenter.model.importer.internal;

import ch.opentrainingcenter.model.importer.IGpsFileModel;
import ch.opentrainingcenter.transfer.IRoute;
import ch.opentrainingcenter.transfer.IShoe;
import ch.opentrainingcenter.transfer.TrainingType;

public class GpsFileModel implements IGpsFileModel {

    private boolean importFile;
    private final String fileName;
    private TrainingType typ;
    private IRoute route;
    private IShoe shoe;

    public GpsFileModel(final String fileName) {
        this.fileName = fileName;
        importFile = true;
        typ = TrainingType.NONE;
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
    public TrainingType getTyp() {
        return typ;
    }

    @Override
    public void setTyp(final TrainingType typ) {
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
    public IRoute getRoute() {
        return route;
    }

    @Override
    public void setRoute(final IRoute route) {
        this.route = route;
    }

    @Override
    public IShoe getSchuh() {
        return shoe;
    }

    @Override
    public void setSchuh(final IShoe shoe) {
        this.shoe = shoe;
    }
}
