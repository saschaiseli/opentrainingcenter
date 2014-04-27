package ch.opentrainingcenter.database.dao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;

import ch.opentrainingcenter.core.cache.TrainingCache;
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
    private static final Logger LOGGER = Logger.getLogger(CommonDao.class);
    private final AthleteDao athleteDao;
    private final HealthDao healthDao;
    private final PlanungDao planungsDao;
    private final RouteDao routeDao;
    private final WeatherDao wetterDao;
    private final TrainingDao trainingDao;
    private final TrainingCache cache;
    private final boolean useCache = true;

    public CommonDao(final IConnectionConfig dao) {
        athleteDao = new AthleteDao(dao);
        healthDao = new HealthDao(dao);
        planungsDao = new PlanungDao(dao);
        routeDao = new RouteDao(dao);
        trainingDao = new TrainingDao(dao);
        wetterDao = new WeatherDao(dao);
        cache = TrainingCache.getInstance();
    }

    public CommonDao(final AthleteDao athleteDao, final HealthDao healthDao, final PlanungDao planungsDao, final RouteDao routeDao, final WeatherDao wetterDao,
            final TrainingDao trainingDao, final TrainingCache cache) {
        this.athleteDao = athleteDao;
        this.healthDao = healthDao;
        this.planungsDao = planungsDao;
        this.routeDao = routeDao;
        this.wetterDao = wetterDao;
        this.trainingDao = trainingDao;
        this.cache = cache;
    }

    // -------------------------------TRAINING--------------------------------------------------
    @Override
    public final List<ITraining> getAllTrainings(final IAthlete athlete) {
        if (useCache) {
            if (cache.size() == 0) {
                cache.addAll(trainingDao.getAllTrainings(athlete));
                LOGGER.info(String.format("load %s records from database", cache.size())); //$NON-NLS-1$
            }
            return cache.getAll(athlete);
        } else {
            return trainingDao.getAllTrainings(athlete);
        }
    }

    @Override
    public List<ITraining> getTrainingsByAthleteAndDate(final IAthlete athlete, final DateTime von, final DateTime bis) {
        LOGGER.info("Load records direct from database"); //$NON-NLS-1$
        return trainingDao.getTrainingsByAthleteAndDate(athlete, von, bis);
    }

    @Override
    public final List<ITraining> getAllTrainingByRoute(final IAthlete athlete, final IRoute route) {
        LOGGER.info("Load records direct from database"); //$NON-NLS-1$
        return trainingDao.getAllTrainingsByRoute(athlete, route);
    }

    @Override
    public final int saveOrUpdate(final ITraining training) {
        final int id = trainingDao.saveOrUpdate(training);
        if (useCache) {
            final List<ITraining> models = new ArrayList<>();
            models.add(training);
            cache.addAll(models);
        }
        return id;
    }

    @Override
    public final void saveOrUpdateAll(final Collection<ITraining> trainings) {
        final List<ITraining> models = new ArrayList<>();
        for (final ITraining training : trainings) {
            trainingDao.saveOrUpdate(training);
            if (useCache) {
                models.add(training);
            }
        }
        if (!models.isEmpty()) {
            cache.addAll(models);
        }
    }

    @Override
    public final void updateTrainingType(final ITraining training, final int typeIndex) {
        trainingDao.updateTrainingType(training, typeIndex);
        if (useCache) {
            final List<ITraining> models = new ArrayList<>();
            models.add(trainingDao.getTrainingByDate(training.getDatum()));
            cache.addAll(models);
        }
    }

    @Override
    public final void updateTrainingRoute(final ITraining training, final int idRoute) {
        trainingDao.updateTrainingRoute(training, idRoute);
        if (useCache) {
            final List<ITraining> models = new ArrayList<>();
            models.add(trainingDao.getTrainingByDate(training.getDatum()));
            cache.addAll(models);
        }
    }

    @Override
    public final ITraining getTrainingById(final long datum) {
        if (useCache) {
            ITraining training = cache.get(datum);
            if (training == null) {
                training = trainingDao.getTrainingByDate(datum);
                if (training != null) {
                    final List<ITraining> models = new ArrayList<>();
                    models.add(training);
                    cache.addAll(models);
                }
            }
            return training;
        } else {
            return trainingDao.getTrainingByDate(datum);
        }

    }

    @Override
    public final ITraining getNewestTraining(final IAthlete athlete) {
        return trainingDao.getNewestTraining(athlete);
    }

    @Override
    public final void removeTrainingByDate(final long datum) {
        trainingDao.removeTrainingByDate(datum);
        if (useCache) {
            cache.remove(datum);
        }
    }

    // -------------------------------ATHLETE--------------------------------------------------
    @Override
    public final List<IAthlete> getAllAthletes() {
        return athleteDao.getAllAthletes();
    }

    @Override
    public final IAthlete getAthlete(final int id) {
        return athleteDao.getAthlete(id);
    }

    @Override
    public final int save(final IAthlete athlete) {
        return athleteDao.save(athlete);
    }

    @Override
    public final IAthlete getAthlete(final String name) {
        return athleteDao.getAthlete(name);
    }

    // -------------------------------healthDao--------------------------------------------------
    @Override
    public final List<IHealth> getHealth(final IAthlete athlete) {
        return healthDao.getHealth(athlete);
    }

    @Override
    public final IHealth getHealth(final IAthlete athlete, final Date date) {
        return healthDao.getHealth(athlete, date);
    }

    @Override
    public final void removeHealth(final int id) {
        healthDao.remove(id);
    }

    @Override
    public final int saveOrUpdate(final IHealth health) {
        return healthDao.saveOrUpdate(health);
    }

    // -------------------------------healthDao--------------------------------------------------
    @Override
    public final List<IPlanungWoche> getPlanungsWoche(final IAthlete athlete) {
        return planungsDao.getPlanungsWoche(athlete);
    }

    @Override
    public final List<IPlanungWoche> getPlanungsWoche(final IAthlete athlete, final int jahr, final int kw) {
        return planungsDao.getPlanungsWoche(athlete, jahr, kw);
    }

    @Override
    public final void saveOrUpdate(final List<IPlanungWoche> planung) {
        planungsDao.saveOrUpdate(planung);
    }

    // -------------------------------routeDao--------------------------------------------------
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
    public final void saveOrUpdate(final IRoute route) {
        routeDao.saveOrUpdate(route);
    }

    // -------------------------------wetterDao--------------------------------------------------
    @Override
    public final List<IWeather> getWeather() {
        return wetterDao.getAllWeather();
    }

}
