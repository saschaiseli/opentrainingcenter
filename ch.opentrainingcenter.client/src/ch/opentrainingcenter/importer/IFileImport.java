package ch.opentrainingcenter.importer;

import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;

import ch.opentrainingcenter.model.importer.IGpsFileModelWrapper;
import ch.opentrainingcenter.transfer.ITraining;

public interface IFileImport {

    /**
     * @param filterPath
     *            Pfad wo das File liegt
     * @param modelWrapper
     * @param monitor
     * @return
     * @throws Exception
     */
    List<ITraining> importFile(final String filterPath, final IGpsFileModelWrapper modelWrapper, final IProgressMonitor monitor) throws Exception;
}