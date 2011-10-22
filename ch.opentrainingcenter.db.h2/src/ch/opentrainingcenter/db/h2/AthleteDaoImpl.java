package ch.opentrainingcenter.db.h2;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.metadata.ClassMetadata;

import ch.iseli.sportanalyzer.db.IAthleteDao;
import ch.opentrainingcenter.transfer.IAthlete;
import ch.opentrainingcenter.transfer.internal.Athlete;

public class AthleteDaoImpl extends Dao implements IAthleteDao {

    public AthleteDaoImpl() {
    }

    @Override
    public IAthlete getAthleteByName(String name) {
        // org.hibernate.Query createSQLQuery = session.createSQLQuery("select * from Athlete");
        // List<?> list = createSQLQuery.list();
        IAthlete athlete = new Athlete();
        Session session = getSession();
        SessionFactory sf = session.getSessionFactory();
        ClassMetadata classMetadata = sf.getClassMetadata(Athlete.class);
        org.hibernate.Query createHibernateQuery = session.createQuery("from Athlete");
        List<?> otherList = createHibernateQuery.list();

        if (otherList != null && !otherList.isEmpty()) {
            return (IAthlete) otherList.get(0);
        }

        try {
            begin();
            getSession().update(athlete);
            commit();
        } catch (HibernateException e) {
            rollback();
        }

        return null;
    }
}
