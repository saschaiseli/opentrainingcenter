package ch.opentrainingcenter.client.model.impl;

import ch.opentrainingcenter.client.model.IGpsFileModel;
import ch.opentrainingcenter.client.model.RunType;

public class GpsFileModel implements IGpsFileModel {

    private boolean importFile;
    private final String fileName;
    private RunType typ;

    public GpsFileModel(final String fileName) {
        this.fileName = fileName;
        importFile = true;
        typ = RunType.NONE;
    }

    @Override
    public int getId() {
        return typ.getIndex();
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.opentrainingcenter.client.model.impl.IGpsFileModel#getFileName()
     */
    @Override
    public String getFileName() {
        return fileName;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.opentrainingcenter.client.model.impl.IGpsFileModel#getTyp()
     */
    @Override
    public RunType getTyp() {
        return typ;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.opentrainingcenter.client.model.impl.IGpsFileModel#setTyp(ch.
     * opentrainingcenter.client.model.RunType)
     */
    @Override
    public void setTyp(final RunType typ) {
        this.typ = typ;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.opentrainingcenter.client.model.impl.IGpsFileModel#isImportFile()
     */
    @Override
    public boolean isImportFile() {
        return importFile;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.opentrainingcenter.client.model.impl.IGpsFileModel#setImportFile(boolean
     * )
     */
    @Override
    public void setImportFile(final boolean importFile) {
        this.importFile = importFile;
    }

}
