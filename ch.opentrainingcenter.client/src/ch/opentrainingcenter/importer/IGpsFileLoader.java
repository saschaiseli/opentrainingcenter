package ch.opentrainingcenter.importer;

import java.io.File;
import java.util.List;

import ch.opentrainingcenter.tcx.ActivityT;
import ch.opentrainingcenter.transfer.IImported;

public interface IGpsFileLoader {

    List<ActivityT> convertActivity(final File file) throws Exception;

    ActivityT convertActivity(final IImported record) throws Exception;

}