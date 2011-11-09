package ch.iseli.sportanalyzer.db;

import java.util.List;

import org.eclipse.core.runtime.IExecutableExtensionFactory;

import ch.opentrainingcenter.transfer.impl.Athlete;

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
    List<String> getImportedRecords(Athlete athlete);

    /**
     * Setzt in der Datenbank den Record auf importiert.
     * 
     * @param athleteId
     *            die ID des Athleten
     * @param name
     *            der Filename des GPS Files.
     * @return die ID von der Datenbank.
     */
    void importRecord(Athlete athlete, String name);

    /**
     * Gibt den sportler mit
     * 
     * @param id
     * @return Gibt den sportler mit der angegeben id zurück oder null.
     */
    Athlete getAthlete(int id);
}
