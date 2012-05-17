package ch.opentrainingcenter.db.h2;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.CoreException;

import ch.opentrainingcenter.db.IDatabaseAccess;
import ch.opentrainingcenter.db.h2.internal.AthleteDao;
import ch.opentrainingcenter.db.h2.internal.Dao;
import ch.opentrainingcenter.db.h2.internal.DatabaseCreator;
import ch.opentrainingcenter.db.h2.internal.ImportDao;
import ch.opentrainingcenter.transfer.IAthlete;
import ch.opentrainingcenter.transfer.IImported;
import ch.opentrainingcenter.transfer.ITraining;

public class DatabaseAccess implements IDatabaseAccess {

    private final AthleteDao athleteDao;
    private final ImportDao importDao;
    private final DatabaseCreator databaseCreator;

    public DatabaseAccess() {
        final Dao dao = new Dao();
        athleteDao = new AthleteDao(dao);
        importDao = new ImportDao(dao);
        databaseCreator = new DatabaseCreator();
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
    public int importRecord(final int athleteId, final String fileName, final Date activityId, final ITraining overview, final int type) {
        return importDao.importRecord(athleteId, fileName, activityId, overview, type);
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

}
