package ch.opentrainingcenter.database.dao;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import ch.opentrainingcenter.transfer.IAthlete;
import ch.opentrainingcenter.transfer.IShoe;

@SuppressWarnings("nls")
public class ShoeDao {
    private static final Logger LOG = Logger.getLogger(ShoeDao.class);

    private final IConnectionConfig dao;

    public ShoeDao(final IConnectionConfig dao) {
        this.dao = dao;
    }

    public List<IShoe> getShoes(final IAthlete athlete) {
        LOG.info(String.format("lade Schuhe von dem Athleten: %s", athlete));
        final Session session = dao.getSession();
        dao.begin();

        final Criteria criteria = session.createCriteria(IShoe.class);
        criteria.add(Restrictions.eq("athlete", athlete));
        @SuppressWarnings("unchecked")
        final List<IShoe> shoes = criteria.list();
        dao.commit();
        session.flush();
        return shoes;
    }
}
