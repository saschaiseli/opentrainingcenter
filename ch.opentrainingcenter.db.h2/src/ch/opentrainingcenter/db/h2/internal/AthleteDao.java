package ch.opentrainingcenter.db.h2.internal;

import java.io.Serializable;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import ch.opentrainingcenter.transfer.IAthlete;

public class AthleteDao extends Dao {

    public AthleteDao() {
    }

    public List<IAthlete> getAllAthletes() {
        final Session session = getSession();
        begin();
        final Query query = session.createQuery("from Athlete");
        @SuppressWarnings("unchecked")
        final List<IAthlete> list = query.list();
        commit();
        session.flush();
        return list;
    }

    public final IAthlete getAthlete(final int id) {
        final Session session = getSession();
        begin();
        final Query query = session.createQuery("from Athlete where id=:idAthlete");
        query.setParameter("idAthlete", id);
        IAthlete athlete = null;
        if (query.list() != null && query.list().size() == 1) {
            athlete = (IAthlete) query.list().get(0);
        }
        commit();
        session.flush();
        return athlete;
    }

    public final int save(final IAthlete athlete) {
        final Session session = getSession();
        begin();
        final Serializable save = session.save(athlete);
        commit();
        session.flush();
        return (Integer) save;
    }

}
