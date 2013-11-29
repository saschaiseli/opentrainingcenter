package ch.opentrainingcenter.database;

import ch.opentrainingcenter.core.db.DatabaseConnectionConfiguration;
import ch.opentrainingcenter.core.db.DbConnection;
import ch.opentrainingcenter.core.db.IDatabaseAccess;
import ch.opentrainingcenter.core.db.IDatabaseConnection;
import ch.opentrainingcenter.database.dao.CommonDao;
import ch.opentrainingcenter.database.dao.DatabaseCreator;
import ch.opentrainingcenter.database.dao.IConnectionConfig;

/**
 * Delegiert die DB Access an die entsprechenden DAOs
 */
public abstract class AbstractDatabaseAccess implements IDatabaseConnection {

    protected boolean developing;
    protected DatabaseConnectionConfiguration config;
    protected CommonDao commonDao;
    protected DatabaseCreator databaseCreator;

    protected void createDaos(final IConnectionConfig dao) {
        databaseCreator = new DatabaseCreator(dao);
        commonDao = new CommonDao(dao);
    }

    @Override
    public final DbConnection getDbConnection() {
        return config.getDbConnection();
    }

    @Override
    public final void setConfiguration(final DatabaseConnectionConfiguration config) {
        this.config = config;
    }

    @Override
    public final void setDeveloping(final boolean developing) {
        this.developing = developing;
    }

    @Override
    public IDatabaseAccess getDataAccess() {
        return commonDao;
    }
}
