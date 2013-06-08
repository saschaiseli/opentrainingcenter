package ch.opentrainingcenter.core.importer;

import ch.opentrainingcenter.core.importer.internal.FileCopy;

public final class ImporterFactory {

    private ImporterFactory() {
    }

    public static IFileCopy createFileCopy() {
        return new FileCopy();
    }
}
