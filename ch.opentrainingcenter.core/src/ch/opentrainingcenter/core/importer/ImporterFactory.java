package ch.opentrainingcenter.core.importer;

import ch.opentrainingcenter.core.importer.internal.FileCopy;
import ch.opentrainingcenter.core.importer.internal.ImportedConverter;

public final class ImporterFactory {

    private ImporterFactory() {
    }

    public static IImportedConverter createGpsFileLoader(final ConvertContainer cc, final String gpsFileLocation) {
        return new ImportedConverter(cc, gpsFileLocation);
    }

    public static IFileCopy createFileCopy() {
        return new FileCopy();
    }
}
