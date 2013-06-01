package ch.opentrainingcenter.core.db;

import java.util.Date;
import java.util.List;

import org.eclipse.core.runtime.IExecutableExtensionFactory;

import ch.opentrainingcenter.transfer.IAthlete;
import ch.opentrainingcenter.transfer.IHealth;
import ch.opentrainingcenter.transfer.IPlanungWoche;
import ch.opentrainingcenter.transfer.IRoute;
import ch.opentrainingcenter.transfer.ITraining;
import ch.opentrainingcenter.transfer.IWeather;

public interface IDatabaseAccess extends IExecutableExtensionFactory {

    String EXTENSION_POINT_NAME = "classImportedDao"; //$NON-NLS-1$

    /**
     * @return den namen der datenbank
     */
    String getName();

    /**
     * Wenn die db nicht vorhanden ist, wird die ganze datenbank mit sql queries
     * erstellt.
     */
    void createDatabase() throws SqlException;

    /**
     * Setzt die Datenbankkonfiguration (driver, url, user, password,
     * dialect,..) Darf NICHT null sein
     */
    void setConfiguration(DatabaseConnectionConfiguration config);

    /**
     * Initialisiert den Database Access. Zu diesem Zeitpunkt muss die
     * Konfiguraiton definiert sein.
     */
    void init();

    List<IAthlete> getAllAthletes();

    /**
     * Gibt eine nach datum sortierte Liste von allen importierted Records
     * zurück.
     * 
     * @param athlete
     *            der Athlete der die Records importierte
     * @return eine Liste von {@link ITraining}
     */
    List<ITraining> getAllImported(IAthlete athlete);

    /**
     * Gibt eine nach datum sortierte Liste von allen importierted Records
     * zurück.
     * 
     * @param athlete
     *            der Athlete der die Records importierte
     * @return eine Liste von {@link ITraining}
     */
    List<ITraining> getAllImported(IAthlete athlete, int limit);

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
     * Gibt alle Gesundheitsdaten von dem Athleten zurück.
     */
    List<IHealth> getHealth(IAthlete athlete);

    /**
     * gibt die gesundheitsdaten des atheleten zu dem gegebenen datum zurück.
     * Wenn noch keine daten erfasst sind, wird null zurückgegeben.
     */
    IHealth getHealth(IAthlete athlete, Date date);

    /**
     * @param key
     *            das datum des importierten records. das datum ist die id des
     *            laufes.
     * @return
     */
    ITraining getImportedRecord(long key);

    /**
     * @return den neusten Lauf. Nicht der Lauf der zuletzt importiert wurde,
     *         sondern der Lauf, der zuletzt gemacht wurde.
     */
    ITraining getNewestRun(IAthlete athlete);

    List<IPlanungWoche> getPlanungsWoche(IAthlete athlete);

    /**
     * Gibt eine Liste mit den geplanten Trainings vom entsprechenden Athleten
     * zurück.
     * 
     * @param athlete
     *            Der Athlete
     * @param jahr
     *            das Jahr aus welchem die Pläne sind
     * @param kwStart
     *            Kalenderwoche des ersten Planes
     * @return eine Liste mit den Plänen, wenn nichts gefunden wurde, wird eine
     *         leere Liste zurückgegeben.
     */
    List<IPlanungWoche> getPlanungsWoche(IAthlete athlete, int jahr, int kwStart);

    /**
     * Liefert alle Strecken von dem Athleten
     */
    List<IRoute> getRoute(IAthlete athlete);

    /**
     * @param name
     *            eindeutige identifizierung der Route
     * @return die route oder null, wenn nichts unter diesem namen gefunden.
     */
    IRoute getRoute(String name, IAthlete athlete);

    /**
     * @return alle in der Datenbank verfügbaren wetter
     */
    List<IWeather> getWeather();

    /**
     * Löscht einen Records mit der angegebenen ID.
     */
    void removeHealth(int id);

    void removeImportedRecord(long datum);

    /**
     * @param athlete
     * @return
     * @throws DatabaseException
     *             wenn das speichern fehlschlägt
     */
    int save(IAthlete athlete);

    /**
     * speichert den täglichen ruhepuls und gewicht. daten werden überschrieben.
     * die id wird zurückgegeben.
     */
    int saveOrUpdate(IHealth health);

    /**
     * Speichert die Strecke ab.
     */
    void saveOrUpdate(IRoute route);

    void saveOrUpdate(List<IPlanungWoche> planung);

    int saveTraining(ITraining training);

    /**
     * Setzt das developing flag. So kann in der entwicklung zum beispiel eine
     * andere DB verwendet werden.
     */
    void setDeveloping(boolean developing);

    /**
     * updated ganzer record
     */
    void updateRecord(ITraining record);

    /**
     * updated training type
     */
    void updateRecord(ITraining record, int index);

    /**
     * updated training type
     */
    void updateRecordRoute(ITraining record, int idRoute);

}
