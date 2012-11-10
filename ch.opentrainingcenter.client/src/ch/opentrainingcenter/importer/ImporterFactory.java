package ch.opentrainingcenter.importer;

import org.eclipse.jface.preference.IPreferenceStore;

import ch.opentrainingcenter.core.db.IDatabaseAccess;
import ch.opentrainingcenter.importer.impl.FileCopy;
import ch.opentrainingcenter.importer.impl.FileImport;
import ch.opentrainingcenter.importer.impl.ImportedConverter;
import ch.opentrainingcenter.transfer.IAthlete;

public final class ImporterFactory {

    private ImporterFactory() {
    }

    public static IImportedConverter createGpsFileLoader(final IPreferenceStore store, final ConvertContainer cc) {
        return new ImportedConverter(store, cc);
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
        return new FileImport(cc, athlete, databaseAccess, backup, new FileCopy());
    }

    public static IFileCopy createFileCopy() {
        return new FileCopy();
    }
}
