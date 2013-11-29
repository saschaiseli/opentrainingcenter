package ch.opentrainingcenter.database.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;

import ch.opentrainingcenter.transfer.ITrainingType;

public class TrainingTypeDao {

    private final IConnectionConfig dao;

    public TrainingTypeDao(final IConnectionConfig dao) {
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
