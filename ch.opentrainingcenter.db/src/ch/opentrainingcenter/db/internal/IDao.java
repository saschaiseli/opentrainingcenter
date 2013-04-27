package ch.opentrainingcenter.db.internal;

import org.hibernate.Session;
import org.hibernate.Transaction;

import ch.opentrainingcenter.db.USAGE;

public interface IDao {

    public abstract Transaction begin();

    public abstract void close();

    public abstract void commit();

    public abstract Session getSession();

    public abstract USAGE getUsage();

    public abstract void rollback();

}