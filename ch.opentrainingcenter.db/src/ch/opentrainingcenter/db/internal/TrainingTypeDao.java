package ch.opentrainingcenter.db.internal;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;

import ch.opentrainingcenter.transfer.ITrainingType;

public class TrainingTypeDao {

    private final IDao dao;

    public TrainingTypeDao(final IDao dao) {
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
