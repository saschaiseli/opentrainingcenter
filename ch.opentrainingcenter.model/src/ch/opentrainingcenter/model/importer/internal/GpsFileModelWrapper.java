package ch.opentrainingcenter.model.importer.internal;

import java.util.ArrayList;
import java.util.List;

import ch.opentrainingcenter.model.importer.IGpsFileModel;
import ch.opentrainingcenter.model.importer.IGpsFileModelWrapper;

public class GpsFileModelWrapper implements IGpsFileModelWrapper {

    private final List<IGpsFileModel> fileModelsForImport = new ArrayList<IGpsFileModel>();

    public GpsFileModelWrapper(final List<IGpsFileModel> fileModels) {
        if (fileModels != null) {
            for (final IGpsFileModel model : fileModels) {
                if (model.isImportFile()) {
                    fileModelsForImport.add(model);
                }
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
