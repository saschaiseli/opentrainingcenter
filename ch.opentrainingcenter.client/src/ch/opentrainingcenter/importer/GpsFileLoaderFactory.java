package ch.opentrainingcenter.importer;

import ch.opentrainingcenter.db.IDatabaseAccess;
import ch.opentrainingcenter.importer.impl.FileImport;
import ch.opentrainingcenter.importer.impl.GpsFileLoader;
import ch.opentrainingcenter.transfer.IAthlete;

public final class GpsFileLoaderFactory {

    private GpsFileLoaderFactory() {
    }

    public static IGpsFileLoader createGpsFileLoader() {
        return new GpsFileLoader();
    }

    /**
     * @param cc
     *            {@link ConvertContainer}
     * @param athlete
     *            der Sportler oder die Sportlerin
     * @param dbAccess
     *            Access auf die Datenbank
     * @param backup
     *            Ort, wo die importieren Files hinkopiert werden
     */
    public static IFileImport createFileImporter(final ConvertContainer cc, final IAthlete athlete, final IDatabaseAccess databaseAccess,
            final String backup) {
        return new FileImport(cc, athlete, databaseAccess, backup);
    }
}
