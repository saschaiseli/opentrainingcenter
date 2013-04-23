package ch.opentrainingcenter.db;

import java.util.Date;
import java.util.List;

import org.eclipse.core.runtime.CoreException;

import ch.opentrainingcenter.core.db.IDatabaseAccess;
import ch.opentrainingcenter.db.internal.AthleteDao;
import ch.opentrainingcenter.db.internal.Dao;
import ch.opentrainingcenter.db.internal.Dao.USAGE;
import ch.opentrainingcenter.db.internal.DatabaseCreator;
import ch.opentrainingcenter.db.internal.HealthDao;
import ch.opentrainingcenter.db.internal.PlanungDao;
import ch.opentrainingcenter.db.internal.RouteDao;
import ch.opentrainingcenter.db.internal.TrainingDao;
import ch.opentrainingcenter.db.internal.WeatherDao;
import ch.opentrainingcenter.transfer.IAthlete;
import ch.opentrainingcenter.transfer.IHealth;
import ch.opentrainingcenter.transfer.IPlanungWoche;
import ch.opentrainingcenter.transfer.IRoute;
import ch.opentrainingcenter.transfer.ITraining;
import ch.opentrainingcenter.transfer.IWeather;

public class DatabaseAccess implements IDatabaseAccess {

    private AthleteDao athleteDao;
    private DatabaseCreator databaseCreator;
    private HealthDao healthDao;
    private PlanungDao planungsDao;
    private RouteDao routeDao;
    private WeatherDao wetterDao;
    private TrainingDao trainingDao;
    private boolean developing;
    private Dao dao;

    /**
     * Mit diesem Konstruktur wird mit der eclipse platform der vm args
     * parameters ausgelesen und ausgewertet.
     */
    public DatabaseAccess() {

    }

    /**
     * Konstruktor für Tests
     */
    public DatabaseAccess(final Dao dao) {
        this.dao = dao;
        init();
    }

    @Override
    public void setDeveloping(final boolean developing) {
        this.developing = developing;
    }

    @Override
    public void init() {
        if (developing) {
            this.dao = new Dao(USAGE.DEVELOPING);
        }
        athleteDao = new AthleteDao(dao);
        databaseCreator = new DatabaseCreator(dao);
        healthDao = new HealthDao(dao);
        planungsDao = new PlanungDao(dao);
        routeDao = new RouteDao(dao);
        trainingDao = new TrainingDao(dao);
        wetterDao = new WeatherDao(dao);
    }

    @Override
    public Object create() throws CoreException {
        return new DatabaseAccess();
    }

    @Override
    public List<ITraining> getAllImported(final IAthlete athlete) {
        return trainingDao.getAllImported(athlete);
    }

    @Override
    public List<ITraining> getAllImported(final IAthlete athlete, final int limit) {
        return trainingDao.getAllImported(athlete, limit);
    }

    @Override
    public ITraining getImportedRecord(final long key) {
        return trainingDao.getImportedRecord(key);
    }

    @Override
    public ITraining getNewestRun(final IAthlete athlete) {
        return trainingDao.getNewestRun(athlete);
    }

    @Override
    public void removeImportedRecord(final long datum) {
        trainingDao.removeImportedRecord(datum);
    }

    @Override
    public void updateRecord(final ITraining record, final int index) {
        trainingDao.updateRecord(record, index);
    }

    @Override
    public void updateRecordRoute(final ITraining record, final int idRoute) {
        trainingDao.updateRecordRoute(record, idRoute);
    }

    @Override
    public void updateRecord(final ITraining record) {
        trainingDao.saveOrUpdate(record);
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

    @Override
    public int saveTraining(final ITraining training) {
        return trainingDao.saveOrUpdate(training);
    }

}
