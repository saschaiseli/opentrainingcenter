package ch.opentrainingcenter.database.dao;

import java.util.Date;
import java.util.List;

import ch.opentrainingcenter.core.db.IDatabaseAccess;
import ch.opentrainingcenter.transfer.IAthlete;
import ch.opentrainingcenter.transfer.IHealth;
import ch.opentrainingcenter.transfer.IPlanungWoche;
import ch.opentrainingcenter.transfer.IRoute;
import ch.opentrainingcenter.transfer.ITraining;
import ch.opentrainingcenter.transfer.IWeather;

/**
 * Implementation von {@link IDatabaseAccess}. Kapselt alle Datenbankzugriffe.
 * Hier könnte auch ein Cache zwischengeschaltet werden.
 * 
 */
public class CommonDao implements IDatabaseAccess {

    private final AthleteDao athleteDao;
    private final HealthDao healthDao;
    private final PlanungDao planungsDao;
    private final RouteDao routeDao;
    private final WeatherDao wetterDao;
    private final TrainingDao trainingDao;

    public CommonDao(final IConnectionConfig dao) {
        athleteDao = new AthleteDao(dao);
        healthDao = new HealthDao(dao);
        planungsDao = new PlanungDao(dao);
        routeDao = new RouteDao(dao);
        trainingDao = new TrainingDao(dao);
        wetterDao = new WeatherDao(dao);
    }

    @Override
    public final List<IAthlete> getAllAthletes() {
        return athleteDao.getAllAthletes();
    }

    @Override
    public final List<ITraining> getAllImported(final IAthlete athlete) {
        return trainingDao.getAllImported(athlete);
    }

    @Override
    public final List<ITraining> getAllImported(final IAthlete athlete, final int limit) {
        return trainingDao.getAllImported(athlete, limit);
    }

    @Override
    public final List<ITraining> getAllFromRoute(final IAthlete athlete, final IRoute route) {
        return trainingDao.getAllFromRoute(athlete, route);
    }

    @Override
    public final IAthlete getAthlete(final int id) {
        return athleteDao.getAthlete(id);
    }

    @Override
    public final IAthlete getAthlete(final String name) {
        return athleteDao.getAthlete(name);
    }

    @Override
    public final List<IHealth> getHealth(final IAthlete athlete) {
        return healthDao.getHealth(athlete);
    }

    @Override
    public final IHealth getHealth(final IAthlete athlete, final Date date) {
        return healthDao.getHealth(athlete, date);
    }

    @Override
    public final ITraining getTrainingById(final long key) {
        return trainingDao.getImportedRecord(key);
    }

    @Override
    public final ITraining getNewestRun(final IAthlete athlete) {
        return trainingDao.getNewestRun(athlete);
    }

    @Override
    public final List<IPlanungWoche> getPlanungsWoche(final IAthlete athlete) {
        return planungsDao.getPlanungsWoche(athlete);
    }

    @Override
    public final List<IPlanungWoche> getPlanungsWoche(final IAthlete athlete, final int jahr, final int kw) {
        return planungsDao.getPlanungsWoche(athlete, jahr, kw);
    }

    @Override
    public final List<IRoute> getRoute(final IAthlete athlete) {
        return routeDao.getRoute(athlete);
    }

    @Override
    public final IRoute getRoute(final String name, final IAthlete athlete) {
        return routeDao.getRoute(name, athlete);
    }

    @Override
    public final boolean existsRoute(final String name, final IAthlete athlete) {
        return routeDao.exists(name, athlete);
    }

    @Override
    public final List<IWeather> getWeather() {
        return wetterDao.getAllWeather();
    }

    @Override
    public final void removeHealth(final int id) {
        healthDao.remove(id);
    }

    @Override
    public final void removeImportedRecord(final long datum) {
        trainingDao.removeImportedRecord(datum);
    }

    @Override
    public final int save(final IAthlete athlete) {
        return athleteDao.save(athlete);
    }

    @Override
    public final int saveOrUpdate(final IHealth health) {
        return healthDao.saveOrUpdate(health);
    }

    @Override
    public final void saveOrUpdate(final IRoute route) {
        routeDao.saveOrUpdate(route);
    }

    @Override
    public final void saveOrUpdate(final List<IPlanungWoche> planung) {
        planungsDao.saveOrUpdate(planung);
    }

    @Override
    public final int saveTraining(final ITraining training) {
        return trainingDao.saveOrUpdate(training);
    }

    @Override
    public final void updateRecord(final ITraining record) {
        trainingDao.saveOrUpdate(record);
    }

    @Override
    public final void updateRecord(final ITraining record, final int index) {
        trainingDao.updateRecord(record, index);
    }

    @Override
    public final void updateRecordRoute(final ITraining record, final int idRoute) {
        trainingDao.updateRecordRoute(record, idRoute);
    }
}
