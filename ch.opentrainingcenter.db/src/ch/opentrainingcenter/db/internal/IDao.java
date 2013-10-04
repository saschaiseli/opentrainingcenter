package ch.opentrainingcenter.db.internal;

import org.hibernate.Session;
import org.hibernate.Transaction;

import ch.opentrainingcenter.core.db.DatabaseConnectionConfiguration;
import ch.opentrainingcenter.db.USAGE;

public interface IDao {

    Transaction begin();

    void close();

    void commit();

    Session getSession();

    USAGE getUsage();

    void rollback();

    DatabaseConnectionConfiguration getConfig();

}