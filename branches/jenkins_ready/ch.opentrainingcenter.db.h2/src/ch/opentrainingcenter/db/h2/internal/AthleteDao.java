package ch.opentrainingcenter.db.h2.internal;

import java.io.Serializable;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import ch.opentrainingcenter.transfer.IAthlete;

public class AthleteDao {

    private final Dao dao;

    public AthleteDao(final Dao dao) {
        this.dao = dao;
    }

    public List<IAthlete> getAllAthletes() {
        final Session session = dao.getSession();
        dao.begin();
        final Query query = session.createQuery("from Athlete");//$NON-NLS-1$
        @SuppressWarnings("unchecked")
        final List<IAthlete> list = query.list();
        dao.commit();
        session.flush();
        return list;
    }

    public final IAthlete getAthlete(final int id) {
        final Session session = dao.getSession();
        dao.begin();
        final Query query = session.createQuery("from Athlete where id=:idAthlete");//$NON-NLS-1$
        query.setParameter("idAthlete", id);//$NON-NLS-1$
        IAthlete athlete = null;
        if (query.list() != null && query.list().size() == 1) {
            athlete = (IAthlete) query.list().get(0);
        }
        dao.commit();
        session.flush();
        return athlete;
    }

    public IAthlete getAthlete(final String name) {
        final Session session = dao.getSession();
        dao.begin();
        final Query query = session.createQuery("from Athlete where name=:name");//$NON-NLS-1$
        query.setParameter("name", name);//$NON-NLS-1$
        IAthlete athlete = null;
        if (query.list() != null && query.list().size() == 1) {
            athlete = (IAthlete) query.list().get(0);
        }
        dao.commit();
        session.flush();
        return athlete;
    }

    public final int save(final IAthlete athlete) {
        final Session session = dao.getSession();
        dao.begin();
        final Serializable save = session.save(athlete);
        dao.commit();
        session.flush();
        return (Integer) save;
    }

}
