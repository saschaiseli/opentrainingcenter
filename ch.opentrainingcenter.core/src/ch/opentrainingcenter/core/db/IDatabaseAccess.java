package ch.opentrainingcenter.core.db;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.joda.time.DateTime;

import ch.opentrainingcenter.transfer.IAthlete;
import ch.opentrainingcenter.transfer.IHealth;
import ch.opentrainingcenter.transfer.IPlanungWoche;
import ch.opentrainingcenter.transfer.IRoute;
import ch.opentrainingcenter.transfer.IShoe;
import ch.opentrainingcenter.transfer.ITraining;
import ch.opentrainingcenter.transfer.IWeather;

/**
 * Datenbankzugriffe. Alle Zugriffe auf die DAOs.
 */
public interface IDatabaseAccess {

    List<IAthlete> getAllAthletes();

    /**
     * Gibt eine nach datum sortierte Liste von allen importierted Records
     * zurück.
     * 
     * @param athlete
     *            der Athlete der die Records importierte
     * @return eine Liste von {@link ITraining}
     */
    List<ITraining> getAllTrainings(IAthlete athlete);

    /**
     * Gibt eine Liste mit Trainings von dem Athleten welche grösser gleich und
     * kleiner gleich bis Datum sind.
     */
    List<ITraining> getTrainingsByAthleteAndDate(IAthlete athlete, DateTime von, DateTime bis);

    /**
     * Gibt eine Liste von Trainings mit derselben Route zurück.
     * 
     * @param athlete
     *            der Athlete der die Records importierte
     * @param route
     *            {@link IRoute}
     * @return ine Liste von {@link ITraining}
     */
    List<ITraining> getAllTrainingByRoute(IAthlete athlete, final IRoute route);

    /**
     * @param key
     *            das datum des importierten records. das datum ist die id des
     *            laufes.
     * @return
     */
    ITraining getTrainingById(long key);

    /**
     * @return den neusten Lauf. Nicht der Lauf der zuletzt importiert wurde,
     *         sondern der Lauf, der zuletzt gemacht wurde.
     */
    ITraining getNewestTraining(IAthlete athlete);

    int getTotalLaengeInMeter(IShoe shoe);

    int saveOrUpdate(ITraining training);

    void saveOrUpdateAll(Collection<ITraining> trainings);

    /**
     * updated training type
     */
    void updateTrainingType(ITraining record, int index);

    // ------------------------------------------------------------------------------------------

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
     * Liefert alle Schuhe von dem Athleten.
     */
    List<IShoe> getShoes(IAthlete athlete);

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

    void removeTrainingByDate(long datum);

    /**
     * @param athlete
     * @return
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

    boolean existsRoute(final String name, final IAthlete athlete);

    boolean existsSchuh(IAthlete athlete, String schuhName);

    void saveOrUpdate(IShoe shoe);

    void deleteShoe(int id);

    IRoute getRoute(int idRoute);

    void deleteRoute(int id);

}