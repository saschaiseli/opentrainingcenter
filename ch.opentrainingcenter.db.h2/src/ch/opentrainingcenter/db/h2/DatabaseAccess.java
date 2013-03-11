package ch.opentrainingcenter.db.h2;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.CoreException;

import ch.opentrainingcenter.core.db.IDatabaseAccess;
import ch.opentrainingcenter.db.h2.internal.AthleteDao;
import ch.opentrainingcenter.db.h2.internal.Dao;
import ch.opentrainingcenter.db.h2.internal.DatabaseCreator;
import ch.opentrainingcenter.db.h2.internal.HealthDao;
import ch.opentrainingcenter.db.h2.internal.ImportDao;
import ch.opentrainingcenter.db.h2.internal.PlanungDao;
import ch.opentrainingcenter.db.h2.internal.RouteDao;
import ch.opentrainingcenter.db.h2.internal.WeatherDao;
import ch.opentrainingcenter.transfer.IAthlete;
import ch.opentrainingcenter.transfer.IHealth;
import ch.opentrainingcenter.transfer.IImported;
import ch.opentrainingcenter.transfer.IPlanungWoche;
import ch.opentrainingcenter.transfer.IRoute;
import ch.opentrainingcenter.transfer.ITraining;
import ch.opentrainingcenter.transfer.IWeather;

public class DatabaseAccess implements IDatabaseAccess {

    private final AthleteDao athleteDao;
    private final DatabaseCreator databaseCreator;
    private final HealthDao healthDao;
    private final ImportDao importDao;
    private final PlanungDao planungsDao;
    private final RouteDao routeDao;
    private final WeatherDao wetterDao;

    public DatabaseAccess() {
        final Dao dao = new Dao();
        athleteDao = new AthleteDao(dao);
        databaseCreator = new DatabaseCreator();
        healthDao = new HealthDao(dao);
        importDao = new ImportDao(dao);
        planungsDao = new PlanungDao(dao);
        routeDao = new RouteDao(dao);
        wetterDao = new WeatherDao(dao);
    }

    @Override
    public Object create() throws CoreException {
        return new DatabaseAccess();
    }

    @Override
    public Map<Date, String> getImportedRecords(final IAthlete athlete) {
        return importDao.getImportedRecords(athlete);
    }

    @Override
    public List<IImported> getAllImported(final IAthlete athlete) {
        return importDao.getAllImported(athlete);
    }

    @Override
    public List<IImported> getAllImported(final IAthlete athlete, final int limit) {
        return importDao.getAllImported(athlete, limit);
    }

    @Override
    public int importRecord(final int athleteId, final String fileName, final Date activityId, final ITraining overview, final int type, final int routeId) {
        return importDao.importRecord(athleteId, fileName, activityId, overview, type, routeId);
    }

    @Override
    public IImported getImportedRecord(final Date key) {
        return importDao.getImportedRecord(key);
    }

    @Override
    public IImported getNewestRun(final IAthlete athlete) {
        return importDao.getNewestRun(athlete);
    }

    @Override
    public void removeImportedRecord(final Date activityId) {
        importDao.removeImportedRecord(activityId);
    }

    @Override
    public void updateRecord(final IImported record, final int index) {
        importDao.updateRecord(record, index);
    }

    @Override
    public void updateRecordRoute(final IImported record, final int idRoute) {
        importDao.updateRecordRoute(record, idRoute);
    }

    @Override
    public void updateRecord(final IImported record) {
        importDao.updateRecord(record);
    }

    @Override
    public List<IAthlete> getAllAthletes() {
        return athleteDao.getAllAthletes();
    }

    @Override
    public IAthlete getAthlete(final int id) {
        return athleteDao.getAthlete(id);
    }

    @Override
    public IAthlete getAthlete(final String name) {
        return athleteDao.getAthlete(name);
    }

    @Override
    public int save(final IAthlete athlete) {
        return athleteDao.save(athlete);
    }

    @Override
    public void createDatabase() {
        databaseCreator.createDatabase();
    }

    @Override
    public List<IWeather> getWeather() {
        return wetterDao.getAllWeather();
    }

    @Override
    public int saveOrUpdate(final IHealth health) {
        return healthDao.saveOrUpdate(health);
    }

    @Override
    public IHealth getHealth(final IAthlete athlete, final Date date) {
        return healthDao.getHealth(athlete, date);
    }

    @Override
    public List<IHealth> getHealth(final IAthlete athlete) {
        return healthDao.getHealth(athlete);
    }

    @Override
    public void removeHealth(final int id) {
        healthDao.remove(id);
    }

    @Override
    public List<IPlanungWoche> getPlanungsWoche(final IAthlete athlete, final int jahr, final int kw) {
        return planungsDao.getPlanungsWoche(athlete, jahr, kw);
    }

    @Override
    public void saveOrUpdate(final List<IPlanungWoche> planung) {
        planungsDao.saveOrUpdate(planung);
    }

    @Override
    public List<IPlanungWoche> getPlanungsWoche(final IAthlete athlete) {
        return planungsDao.getPlanungsWoche(athlete);
    }

    @Override
    public IRoute getRoute(final String name, final IAthlete athlete) {
        return routeDao.getRoute(name, athlete);
    }

    @Override
    public List<IRoute> getRoute(final IAthlete athlete) {
        return routeDao.getRoute(athlete);
    }

    @Override
    public void saveOrUpdate(final IRoute route) {
        routeDao.saveOrUpdate(route);
    }

}
