package ch.opentrainingcenter.db.h2.internal;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;

import ch.opentrainingcenter.transfer.ITrainingType;

public class TrainingTypeDao {

    private final Dao dao;

    public TrainingTypeDao(final Dao dao) {
        this.dao = dao;
    }

    public List<ITrainingType> getTrainingType() {
        final Session session = dao.getSession();
        dao.begin();
        final Criteria criteria = session.createCriteria(ITrainingType.class);
        @SuppressWarnings("unchecked")
        final List<ITrainingType> records = criteria.list();
        dao.commit();
        session.flush();
        return records;
    }

}
