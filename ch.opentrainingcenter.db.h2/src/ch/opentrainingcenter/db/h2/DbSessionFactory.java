package ch.opentrainingcenter.db.h2;

import org.eclipse.core.runtime.CoreException;

import ch.iseli.sportanalyzer.db.IDBSessionFactory;

public class DbSessionFactory implements IDBSessionFactory {

    @Override
    public Object create() throws CoreException {
        return new AthleteDaoImpl();
    }
}
