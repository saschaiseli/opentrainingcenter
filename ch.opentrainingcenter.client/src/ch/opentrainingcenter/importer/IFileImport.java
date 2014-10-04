package ch.opentrainingcenter.importer;

import org.eclipse.core.runtime.IProgressMonitor;

import ch.opentrainingcenter.model.importer.IGpsFileModelWrapper;

public interface IFileImport {

    /**
     * @param filterPath
     *            Pfad wo das File liegt
     * @param modelWrapper
     * @param monitor
     * @throws Exception
     */
    void importFile(final String filterPath, final IGpsFileModelWrapper modelWrapper, final IProgressMonitor monitor);
}