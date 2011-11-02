package ch.opentrainingcenter.db.h2;

import java.util.List;

import ch.iseli.sportanalyzer.db.IAthleteDao;
import ch.opentrainingcenter.transfer.IAthlete;

public class AthleteDaoImpl extends Dao implements IAthleteDao {

    private org.hibernate.Query query;

    public AthleteDaoImpl() {
        super();
    }

    @Override
    public IAthlete getAthleteByName(String name) {
        query = getSession().createQuery("from Athlete where name=:theName");
        query.setParameter("theName", name);
        List<?> otherList = query.list();
        if (otherList != null && !otherList.isEmpty()) {
            return (IAthlete) otherList.get(0);
        }
        query = getSession().createQuery("from Athlete");
        query.list();
        return null;
    }
}
