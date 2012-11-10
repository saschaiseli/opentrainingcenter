package ch.opentrainingcenter.model.importer;

import java.util.List;

public interface IGpsFileModelWrapper {

    /**
     * @return anzahl der files die importiert werden müssen.
     */
    int size();

    /**
     * @return liste mit {@link IGpsFileModel} die importiert werden sollen.
     */
    List<IGpsFileModel> getGpsFileModels();
}
