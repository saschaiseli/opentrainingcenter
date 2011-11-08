package ch.opentrainingcenter.db.h2;

import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.hibernate.Query;

import ch.iseli.sportanalyzer.db.IImportedDao;

public class ImportedDao extends Dao implements IImportedDao {

    @Override
    public Object create() throws CoreException {
        return new ImportedDao();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<String> getImportedRecords(int idAthlete) {
        Query query = getSession().createQuery("select comments from Imported where id_fk_athlete=:idAthlete");
        query.setParameter("idAthlete", idAthlete);
        return query.list();
    }

}
