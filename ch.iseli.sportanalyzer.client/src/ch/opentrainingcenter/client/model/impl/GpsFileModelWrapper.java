package ch.opentrainingcenter.client.model.impl;

import java.util.ArrayList;
import java.util.List;

import ch.opentrainingcenter.client.model.IGpsFileModel;
import ch.opentrainingcenter.client.model.IGpsFileModelWrapper;

public class GpsFileModelWrapper implements IGpsFileModelWrapper {

    private final List<IGpsFileModel> fileModelsForImport = new ArrayList<IGpsFileModel>();

    public GpsFileModelWrapper(final List<IGpsFileModel> fileModels) {
        for (final IGpsFileModel model : fileModels) {
            if (model.isImportFile()) {
                fileModelsForImport.add(model);
            }
        }

    }

    @Override
    public int size() {
        return fileModelsForImport.size();
    }

    @Override
    public List<IGpsFileModel> getGpsFileModels() {
        return fileModelsForImport;
    }

}
