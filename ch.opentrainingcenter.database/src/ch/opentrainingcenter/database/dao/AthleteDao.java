package ch.opentrainingcenter.database.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import ch.opentrainingcenter.transfer.IAthlete;

public class AthleteDao {

    private final IConnectionConfig dao;

    public AthleteDao(final IConnectionConfig dao) {
        this.dao = dao;
    }

    public List<IAthlete> getAllAthletes() {
        final Session session = dao.getSession();
        dao.begin();
        final Query query = session.createQuery("from Athlete"); //$NON-NLS-1$
        @SuppressWarnings("unchecked")
        final List<IAthlete> list = query.list();
        dao.commit();
        session.flush();
        return list;
    }

    public final IAthlete getAthlete(final int id) {
        final Session session = dao.getSession();
        dao.begin();
        final Query query = session.createQuery("from Athlete where id=:idAthlete"); //$NON-NLS-1$
        query.setParameter("idAthlete", id); //$NON-NLS-1$
        IAthlete athlete = null;
        @SuppressWarnings("unchecked")
        final List<IAthlete> athletes = query.list();
        if (athletes != null && athletes.size() == 1) {
            athlete = athletes.get(0);
        }
        dao.commit();
        session.flush();
        return athlete;
    }

    public IAthlete getAthlete(final String name) {
        final Session session = dao.getSession();
        dao.begin();
        final Query query = session.createQuery("from Athlete where name=:name"); //$NON-NLS-1$
        query.setParameter("name", name); //$NON-NLS-1$
        IAthlete athlete = null;
        @SuppressWarnings("unchecked")
        final List<IAthlete> athletes = query.list();
        if (athletes != null && athletes.size() == 1) {
            athlete = athletes.get(0);
        }
        dao.commit();
        session.flush();
        return athlete;
    }

    public final int save(final IAthlete athlete) {
        IAthlete exists = getAthlete(athlete.getName());
        if (exists != null) {
            exists.setBirthday(athlete.getBirthday());
            exists.setMaxHeartRate(athlete.getMaxHeartRate());
            athlete.setId(exists.getId());
        } else {
            exists = athlete;
        }
        final Session session = dao.getSession();
        dao.begin();
        session.saveOrUpdate(exists);
        dao.commit();
        session.flush();
        return exists.getId();
    }

}
