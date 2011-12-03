package ch.opentrainingcenter.db.h2;

import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.CoreException;

import ch.iseli.sportanalyzer.db.IDatabaseAccess;
import ch.opentrainingcenter.db.h2.internal.AthleteDao;
import ch.opentrainingcenter.db.h2.internal.DatabaseCreator;
import ch.opentrainingcenter.db.h2.internal.ImportDao;
import ch.opentrainingcenter.transfer.IAthlete;

public class DatabaseAccess implements IDatabaseAccess {

    private final AthleteDao athleteDao;
    private final ImportDao importDao;
    private final DatabaseCreator databaseCreator;

    public DatabaseAccess() {
        athleteDao = new AthleteDao();
        importDao = new ImportDao();
        databaseCreator = new DatabaseCreator();
    }

    @Override
    public Object create() throws CoreException {
        return new DatabaseAccess();
    }

    @Override
    public Map<Integer, String> getImportedRecords(final IAthlete athlete) {
        return importDao.getImportedRecords(athlete);
    }

    @Override
    public int importRecord(final IAthlete athlete, final String name) {
        return importDao.importRecord(athlete, name);
    }

    @Override
    public void removeImportedRecord(final Integer id) {
        importDao.removeImportedRecord(id);
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
    public int save(final IAthlete athlete) {
        return athleteDao.save(athlete);
    }

    @Override
    public void createDatabase() {
        databaseCreator.createDatabase();
    }

}
