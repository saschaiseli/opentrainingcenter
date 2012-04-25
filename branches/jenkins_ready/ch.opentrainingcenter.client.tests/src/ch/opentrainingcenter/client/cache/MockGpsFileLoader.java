package ch.opentrainingcenter.client.cache;

import java.io.File;
import java.util.List;

import ch.opentrainingcenter.importer.IGpsFileLoader;
import ch.opentrainingcenter.tcx.ActivityT;
import ch.opentrainingcenter.transfer.IImported;

public class MockGpsFileLoader implements IGpsFileLoader {

    @Override
    public ActivityT convertActivity(final IImported record) throws Exception {
        return null;
    }

    @Override
    public List<ActivityT> convertActivity(final File file) throws Exception {
        return null;
    }

}
