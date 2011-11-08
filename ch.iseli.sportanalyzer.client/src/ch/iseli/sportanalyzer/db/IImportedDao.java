package ch.iseli.sportanalyzer.db;

import java.util.List;

import org.eclipse.core.runtime.IExecutableExtensionFactory;

public interface IImportedDao extends IExecutableExtensionFactory {
    /**
     * Gibt eine Liste von Filenamen zurück die diesem Athleten gehören. Das Resultat ist nicht ein absoluter File Pfad sondern einfach der Filename.
     * 
     * <pre>
     * z.b.: 201011201234.gmn
     * </pre>
     * 
     * @param athleteId
     *            id des sportlers.
     * @return eine liste mit den records von diesem sportler.
     */
    List<String> getImportedRecords(int athleteId);
}
