package ch.opentrainingcenter.db;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.IExecutableExtensionFactory;

import ch.opentrainingcenter.client.model.RunType;
import ch.opentrainingcenter.transfer.IAthlete;
import ch.opentrainingcenter.transfer.IHealth;
import ch.opentrainingcenter.transfer.IImported;
import ch.opentrainingcenter.transfer.ITraining;
import ch.opentrainingcenter.transfer.IWeather;

public interface IDatabaseAccess extends IExecutableExtensionFactory {

    String EXTENSION_POINT_NAME = "classImportedDao"; //$NON-NLS-1$

    /**
     * Wenn die db nicht vorhanden ist, wird die ganze datenbank mit sql queries
     * erstellt.
     */
    void createDatabase();

    List<IAthlete> getAllAthletes();

    /**
     * Gibt eine nach datum sortierte Liste von allen importierted Records
     * zurück.
     * 
     * @param athlete
     *            der Athlete der die Records importierte
     * @return eine Liste von {@link IImported}
     */
    List<IImported> getAllImported(IAthlete athlete);

    /**
     * Gibt eine nach datum sortierte Liste von allen importierted Records
     * zurück.
     * 
     * @param athlete
     *            der Athlete der die Records importierte
     * @return eine Liste von {@link IImported}
     */
    List<IImported> getAllImported(IAthlete athlete, int limit);

    /**
     * @return den neusten Lauf. Nicht der Lauf der zuletzt importiert wurde,
     *         sondern der Lauf, der zuletzt gemacht wurde.
     */
    IImported getNewestRun(IAthlete athlete);

    /**
     * Gibt den sportler mit
     * 
     * @param id
     * @return Gibt den sportler mit der angegeben id zurück oder null.
     */
    IAthlete getAthlete(int id);

    /**
     * Sucht einen Athleten mit dem angegeben Namen. Der Name muss exakt
     * übereinstimmen, es gibt keine %LIKE% abfrage.
     * 
     * @param name
     *            den vollständigen namen.
     * @return einen Athleten oder null, wenn dieser nicht gefunden wird.
     */
    IAthlete getAthlete(String name);

    /**
     * @param key
     *            das datum des importierten records. das datum ist die id des
     *            laufes.
     * @return
     */
    IImported getImportedRecord(Date key);

    /**
     * Gibt eine Liste von Filenamen zurück die diesem Athleten gehören. Das
     * Resultat ist nicht ein absoluter File Pfad sondern einfach der Filename.
     * Die Id der Map ist das Startdatum des Laufes. Dies ist sogleich die ID.
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
     * Importiert den Record in die Datenbank. Zweimaliges importieren wird
     * ignoriert. Das heisst ein anderer Benutzer kann nicht denselben Record
     * auch importieren. Massgebendes Kriterium hierfür ist der Filename und die
     * id der Actitvity.
     * 
     * @param athleteId
     *            die ID des Athleten
     * @param name
     *            der Filename des GPS Files.
     * @param activityId
     *            id der aktivität.
     * @param type
     *            der typ des trainings {@link RunType#getIndex()}
     * @return id des datenbankeintrages oder -1 wenn der record bereits in der
     *         datenbank war.
     */
    int importRecord(int athleteId, String fileName, Date activityId, ITraining overview, int type);

    void removeImportedRecord(Date activityId);

    /**
     * @param athlete
     * @return
     * @throws DatabaseException
     *             wenn das speichern fehlschlägt
     */
    int save(IAthlete athlete);

    /**
     * updated training type
     */
    void updateRecord(IImported record, int index);

    /**
     * updated ganzer record
     */
    void updateRecord(IImported record);

    /**
     * @return alle in der Datenbank verfügbaren wetter
     */
    List<IWeather> getWeather();

    /**
     * speichert den täglichen ruhepuls und gewicht. daten werden überschrieben.
     */
    void saveOrUpdate(IHealth health);

    /**
     * gibt die gesundheitsdaten des atheleten zu dem gegebenen datum zurück.
     * Wenn noch keine daten erfasst sind, wird null zurückgegeben.
     */
    IHealth getHealth(IAthlete athlete, Date date);
}
