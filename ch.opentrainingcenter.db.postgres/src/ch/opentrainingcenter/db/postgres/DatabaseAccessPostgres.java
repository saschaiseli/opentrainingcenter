package ch.opentrainingcenter.db.postgres;

import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.List;

import org.eclipse.core.runtime.CoreException;

import ch.opentrainingcenter.core.assertions.Assertions;
import ch.opentrainingcenter.core.db.DatabaseConnectionConfiguration;
import ch.opentrainingcenter.core.db.DatabaseConnectionConfiguration.DB_MODE;
import ch.opentrainingcenter.core.db.IDatabaseAccess;
import ch.opentrainingcenter.core.db.SqlException;
import ch.opentrainingcenter.db.DatabaseAccess;
import ch.opentrainingcenter.db.USAGE;
import ch.opentrainingcenter.db.internal.AthleteDao;
import ch.opentrainingcenter.db.internal.Dao;
import ch.opentrainingcenter.db.internal.DatabaseCreator;
import ch.opentrainingcenter.db.internal.HealthDao;
import ch.opentrainingcenter.db.internal.IDao;
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

public class DatabaseAccessPostgres implements IDatabaseAccess {

    private AthleteDao athleteDao;
    private DatabaseCreator databaseCreator;
    private HealthDao healthDao;
    private PlanungDao planungsDao;
    private RouteDao routeDao;
    private WeatherDao wetterDao;
    private TrainingDao trainingDao;
    private boolean developing;
    private IDao dao;
    private DatabaseConnectionConfiguration config;

    /**
     * Mit diesem Konstruktur wird mit der eclipse platform der vm args
     * parameters ausgelesen und ausgewertet.
     */
    public DatabaseAccessPostgres() {

    }

    /**
     * Konstruktor für Tests
     */
    public DatabaseAccessPostgres(final IDao dao) {
        this.dao = dao;
        this.config = dao.getConfig();
        init();
    }

    @Override
    public void setConfiguration(final DatabaseConnectionConfiguration config) {
        Assertions.notNull(config);
        this.config = config;
    }

    @Override
    public void init() {
        Assertions.notNull(config);
        if (developing) {
            this.dao = new Dao(USAGE.DEVELOPING, config);
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
    public void createDatabase() throws SqlException {
        createDB();
        dao.begin();
        dao.getSession();
        try {
            databaseCreator.createDatabase(DbScriptReader.readDbScript("otc_postgres.sql"));
        } catch (final FileNotFoundException fnne) {
            throw new SqlException(fnne);
        }
    }

    private void createDB() {
        Connection conn = null;
        Statement stmt = null;
        try {
            Class.forName(config.getDriver(DB_MODE.ADMIN));
            conn = DriverManager.getConnection(config.getUrl(DB_MODE.ADMIN), config.getUsername(DB_MODE.ADMIN), config.getPassword(DB_MODE.ADMIN));
            stmt = conn.createStatement();
            final ResultSet user = stmt.executeQuery("SELECT COUNT(*) FROM pg_user WHERE usename='" + config.getUsername(DB_MODE.APPLICATION) + "'");
            user.next();
            final int count = user.getInt("count");
            if (count == 0) {
                stmt.execute("CREATE USER " + config.getUsername(DB_MODE.APPLICATION) + " WITH PASSWORD '" + config.getPassword(DB_MODE.APPLICATION) + "'");
            }
            final ResultSet db = stmt.executeQuery("SELECT count(*) from pg_database where datname='" + config.getDatabaseName(DB_MODE.APPLICATION) + "'");
            db.next();
            final int countDb = db.getInt("count");
            if (countDb == 0) {
                stmt.execute("CREATE DATABASE " + dao.getUsage().getDbName());
            }
            stmt.execute("ALTER DATABASE OTC_JUNIT OWNER TO " + config.getUsername(DB_MODE.APPLICATION));
            stmt.execute("ALTER SCHEMA PUBLIC OWNER TO " + config.getUsername(DB_MODE.APPLICATION));
        } catch (final SQLException se) {
            se.printStackTrace();
        } catch (final Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (stmt != null)
                    stmt.close();
            } catch (final SQLException se2) {
            }
            try {
                if (conn != null)
                    conn.close();
            } catch (final SQLException se) {
                se.printStackTrace();
            }
        }
    }

    @Override
    public List<IAthlete> getAllAthletes() {
        return athleteDao.getAllAthletes();
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
    public IAthlete getAthlete(final int id) {
        return athleteDao.getAthlete(id);
    }

    @Override
    public IAthlete getAthlete(final String name) {
        return athleteDao.getAthlete(name);
    }

    @Override
    public List<IHealth> getHealth(final IAthlete athlete) {
        return healthDao.getHealth(athlete);
    }

    @Override
    public IHealth getHealth(final IAthlete athlete, final Date date) {
        return healthDao.getHealth(athlete, date);
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
    public List<IPlanungWoche> getPlanungsWoche(final IAthlete athlete) {
        return planungsDao.getPlanungsWoche(athlete);
    }

    @Override
    public List<IPlanungWoche> getPlanungsWoche(final IAthlete athlete, final int jahr, final int kw) {
        return planungsDao.getPlanungsWoche(athlete, jahr, kw);
    }

    @Override
    public List<IRoute> getRoute(final IAthlete athlete) {
        return routeDao.getRoute(athlete);
    }

    @Override
    public IRoute getRoute(final String name, final IAthlete athlete) {
        return routeDao.getRoute(name, athlete);
    }

    @Override
    public List<IWeather> getWeather() {
        return wetterDao.getAllWeather();
    }

    @Override
    public void removeHealth(final int id) {
        healthDao.remove(id);
    }

    @Override
    public void removeImportedRecord(final long datum) {
        trainingDao.removeImportedRecord(datum);
    }

    @Override
    public int save(final IAthlete athlete) {
        return athleteDao.save(athlete);
    }

    @Override
    public int saveOrUpdate(final IHealth health) {
        return healthDao.saveOrUpdate(health);
    }

    @Override
    public void saveOrUpdate(final IRoute route) {
        routeDao.saveOrUpdate(route);
    }

    @Override
    public void saveOrUpdate(final List<IPlanungWoche> planung) {
        planungsDao.saveOrUpdate(planung);
    }

    @Override
    public int saveTraining(final ITraining training) {
        return trainingDao.saveOrUpdate(training);
    }

    @Override
    public void setDeveloping(final boolean developing) {
        this.developing = developing;
    }

    @Override
    public void updateRecord(final ITraining record) {
        trainingDao.saveOrUpdate(record);
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
    public String getName() {
        return "Postgres Database";
    }

}
