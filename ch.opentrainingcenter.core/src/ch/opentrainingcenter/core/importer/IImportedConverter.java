package ch.opentrainingcenter.core.importer;

import ch.opentrainingcenter.transfer.ITraining;

public interface IImportedConverter {

    /**
     * @param record
     * @return
     * @throws Exception
     */
    ITraining convertImportedToActivity(final ITraining record) throws Exception;

}