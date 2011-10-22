package ch.opentrainingcenter.db.h2;

import java.util.List;

import org.hibernate.Session;

import ch.iseli.sportanalyzer.db.IAthleteDao;
import ch.opentrainingcenter.transfer.IAthlete;

public class AthleteDaoImpl extends Dao implements IAthleteDao {

    public AthleteDaoImpl() {
    }

    @Override
    public IAthlete getAthleteByName(String name) {
        getSession();
        Session session = getSession();
        org.hibernate.Query createQuery = session.createQuery("from Athlete");
        List<?> list = createQuery.list();
        if (list != null && !list.isEmpty()) {
            return (IAthlete) list.get(0);
        }
        return null;
    }
}
