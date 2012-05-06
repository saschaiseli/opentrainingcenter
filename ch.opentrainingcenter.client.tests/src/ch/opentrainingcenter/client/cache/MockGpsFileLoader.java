package ch.opentrainingcenter.client.cache;

import ch.opentrainingcenter.importer.IImportedConverter;
import ch.opentrainingcenter.tcx.ActivityT;
import ch.opentrainingcenter.transfer.IImported;

public class MockGpsFileLoader implements IImportedConverter {

    private ActivityT activity;

    @Override
    public ActivityT convertImportedToActivity(final IImported record) throws Exception {
        return activity;
    }

    public void setActivity(final ActivityT activity) {
        this.activity = activity;
    }

}
