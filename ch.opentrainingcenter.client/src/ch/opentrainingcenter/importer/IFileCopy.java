package ch.opentrainingcenter.importer;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;

public interface IFileCopy {
    /**
     * Kopiert ein file in ein anderes das destination file wird nie
     * überschrieben werden.
     */
    void copyFile(File source, File destination) throws IOException;

    void copyFiles(final String sourceFolder, final String destinationFolder, final FilenameFilter filter) throws IOException;
}
