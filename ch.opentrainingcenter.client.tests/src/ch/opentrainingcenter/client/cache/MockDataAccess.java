package ch.opentrainingcenter.client.cache;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.CoreException;

import ch.opentrainingcenter.db.IDatabaseAccess;
import ch.opentrainingcenter.transfer.IAthlete;
import ch.opentrainingcenter.transfer.IImported;
import ch.opentrainingcenter.transfer.ITraining;

public class MockDataAccess implements IDatabaseAccess {

    private final Map<Date, IImported> mapOfImported = new HashMap<Date, IImported>();

    @Override
    public Object create() throws CoreException {
        return null;
    }

    @Override
    public void createDatabase() {
    }

    @Override
    public List<IAthlete> getAllAthletes() {
        return null;
    }

    @Override
    public List<IImported> getAllImported(final IAthlete athlete) {
        return null;
    }

    @Override
    public IAthlete getAthlete(final int id) {
        return null;
    }

    @Override
    public IAthlete getAthlete(final String name) {
        return null;
    }

    @Override
    public IImported getImportedRecord(final Date key) {
        return mapOfImported.get(key);
    }

    @Override
    public Map<Date, String> getImportedRecords(final IAthlete athlete) {
        return null;
    }

    @Override
    public int importRecord(final int athleteId, final String fileName, final Date activityId, final ITraining overview, final int type) {
        return 0;
    }

    @Override
    public void removeImportedRecord(final Date activityId) {

    }

    @Override
    public int save(final IAthlete athlete) {
        return 0;
    }

    @Override
    public void updateRecord(final IImported record, final int index) {

    }

    public void addIimported(final Date date, final IImported iimported) {
        mapOfImported.put(date, iimported);
    }
}
