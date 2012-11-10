package ch.opentrainingcenter.importer;

import ch.opentrainingcenter.core.db.IDatabaseAccess;
import ch.opentrainingcenter.core.importer.ConvertContainer;
import ch.opentrainingcenter.core.importer.ImporterFactory;
import ch.opentrainingcenter.importer.impl.FileImport;
import ch.opentrainingcenter.transfer.IAthlete;

public class ImportFactory {

    private ImportFactory() {

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
     * @param fileCopy
     */
    public static IFileImport createFileImporter(final ConvertContainer cc, final IAthlete athlete, final IDatabaseAccess databaseAccess, final String backup) {
        return new FileImport(cc, athlete, databaseAccess, backup, ImporterFactory.createFileCopy());
    }
}
