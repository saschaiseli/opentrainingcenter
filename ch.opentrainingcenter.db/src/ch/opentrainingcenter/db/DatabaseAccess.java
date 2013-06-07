package ch.opentrainingcenter.db;

import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.CoreException;

import ch.opentrainingcenter.core.db.DatabaseConnectionConfiguration;
import ch.opentrainingcenter.core.db.DbConnection;
import ch.opentrainingcenter.core.db.IDatabaseAccess;
import ch.opentrainingcenter.core.db.SqlException;
import ch.opentrainingcenter.db.internal.AthleteDao;
import ch.opentrainingcenter.db.internal.Dao;
import ch.opentrainingcenter.db.internal.DatabaseCreator;
import ch.opentrainingcenter.db.internal.DbScriptReader;
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

public class DatabaseAccess implements IDatabaseAccess {

    private static final Logger LOG = Logger.getLogger(DatabaseAccess.class);
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
    private DbConnection dbConnection;

    /**
     * Mit diesem Konstruktur wird mit der eclipse platform der vm args
     * parameters ausgelesen und ausgewertet.
     */
    public DatabaseAccess() {
        dbConnection = new DbConnection("org.h2.Driver", "org.hibernate.dialect.H2Dialect"); //$NON-NLS-1$ //$NON-NLS-2$
    }

    /**
     * Konstruktor für Tests
     */
    public DatabaseAccess(final IDao dao) {
        super();
        this.dao = dao;
        init();
    }

    @Override
    public String getName() {
        return "H2 Database"; //$NON-NLS-1$
    }

    @Override
    public Object create() throws CoreException {
        return new DatabaseAccess();
    }

    @SuppressWarnings("nls")
    @Override
    public void createDatabase() throws SqlException {
        try {
            databaseCreator.createDatabase(DbScriptReader.readDbScript("otc.sql"));
        } catch (final FileNotFoundException fnne) {
            throw new SqlException(fnne);
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
    public void init() {
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
    public void setConfiguration(final DatabaseConnectionConfiguration config) {
        this.config = config;
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

    @SuppressWarnings("nls")
    @Override
    public boolean validateConnection(final String url, final String user, final String pass) {

        boolean result = false;
        try {
            Class.forName(dbConnection.getDriver());
            final Connection con = DriverManager.getConnection(url, user, pass);
            con.createStatement();
            result = true;
            LOG.info("Connection to database '" + url + "' successfully");
        } catch (final ClassNotFoundException | SQLException e) {
            LOG.info("Connection to database '" + url + "' failed");
        }
        return result;
    }

    @Override
    public DbConnection getDbConnection() {
        return dbConnection;
    }

}