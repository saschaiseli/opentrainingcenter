package ch.iseli.sportanalyzer.db;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.IExecutableExtensionFactory;

import ch.opentrainingcenter.transfer.IAthlete;
import ch.opentrainingcenter.transfer.IImported;
import ch.opentrainingcenter.transfer.ITraining;

public interface IDatabaseAccess extends IExecutableExtensionFactory {

    public static final String EXTENSION_POINT_NAME = "classImportedDao"; //$NON-NLS-1$

    /**
     * Gibt eine Liste von Filenamen zurück die diesem Athleten gehören. Das Resultat ist nicht ein absoluter File Pfad sondern einfach der Filename. Die Id der Map ist das
     * Startdatum des Laufes. Dies ist sogleich die ID.
     * 
     * <pre>
     * z.b.: 201011201234.gmn
     * </pre>
     * 
     * @param athleteId
     *            id des sportlers.
     * @return eine liste mit den records von diesem sportler.
     */
    Map<Date, String> getImportedRecords(IAthlete athlete);

    /**
     * Gibt eine Liste von allen importierted Records zurück.
     * 
     * @param athlete
     *            der Athlete der die Records importierte
     * @return eine Liste von {@link IImported}
     */
    List<IImported> getAllImported(IAthlete athlete);

    /**
     * Importiert den Record in die Datenbank. Zweimaliges importieren wird ignoriert. Das heisst ein anderer Benutzer kann nicht denselben Record auch importieren. Massgebendes
     * Kriterium hierfür ist der Filename und die id der Actitvity.
     * 
     * @param athleteId
     *            die ID des Athleten
     * @param name
     *            der Filename des GPS Files.
     * @param activityId
     *            id der aktivität.
     * @return id des datenbankeintrages oder -1 wenn der record bereits in der datenbank war.
     */
    int importRecord(int athleteId, String fileName, Date activityId, ITraining overview);

    /**
     * @param key
     *            das datum des importierten records. das datum ist die id des laufes.
     * @return
     */
    IImported getImportedRecord(Date key);

    /**
     * Gibt den sportler mit
     * 
     * @param id
     * @return Gibt den sportler mit der angegeben id zurück oder null.
     */
    IAthlete getAthlete(int id);

    /**
     * Sucht einen Athleten mit dem angegeben Namen. Der Name muss exakt übereinstimmen, es gibt keine %LIKE% abfrage.
     * 
     * @param name
     *            den vollständigen namen.
     * @return einen Athleten oder null, wenn dieser nicht gefunden wird.
     */
    IAthlete getAthlete(String name);

    List<IAthlete> getAllAthletes();

    void removeImportedRecord(Date activityId);

    /**
     * Wenn die db nicht vorhanden ist, wird die ganze datenbank mit sql queries erstellt.
     */
    void createDatabase();

    /**
     * @param athlete
     * @return
     * @throws DatabaseException
     *             wenn das speichern fehlschlägt
     */
    int save(IAthlete athlete);

}
