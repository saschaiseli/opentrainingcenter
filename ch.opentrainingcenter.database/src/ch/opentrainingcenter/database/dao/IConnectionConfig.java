package ch.opentrainingcenter.database.dao;

import org.hibernate.Session;
import org.hibernate.Transaction;

import ch.opentrainingcenter.core.db.DatabaseConnectionConfiguration;
import ch.opentrainingcenter.database.USAGE;

public interface IConnectionConfig {

    Transaction begin();

    void close();

    void commit();

    Session getSession();

    USAGE getUsage();

    void rollback();

    DatabaseConnectionConfiguration getConfig();

}