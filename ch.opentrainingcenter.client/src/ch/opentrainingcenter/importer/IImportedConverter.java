package ch.opentrainingcenter.importer;

import ch.opentrainingcenter.tcx.ActivityT;
import ch.opentrainingcenter.transfer.IImported;

public interface IImportedConverter {

    /**
     * @param record
     * @return
     * @throws Exception
     */
    ActivityT convertImportedToActivity(final IImported record) throws Exception;

}