package ch.opentrainingcenter.database.dao;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Query;
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

    public boolean exists(final IAthlete athlete, final String schuhName) {
        final Session session = dao.getSession();
        dao.begin();
        final Criteria criteria = session.createCriteria(IShoe.class);
        criteria.add(Restrictions.eq("athlete", athlete));
        criteria.add(Restrictions.eq("schuhname", schuhName));
        @SuppressWarnings("unchecked")
        final List<IShoe> schuhe = criteria.list();
        dao.commit();
        session.flush();
        return !schuhe.isEmpty();
    }

    public int saveOrUpdate(final IShoe schuh) {
        IShoe exists = getShoe(schuh.getId());
        if (exists != null) {
            LOG.info("Schuh überschreiben alt: " + exists + " neu: " + schuh); //$NON-NLS-2$
            exists.setImageicon(schuh.getImageicon());
            exists.setSchuhname(schuh.getSchuhname());
            exists.setKaufdatum(schuh.getKaufdatum());
            exists.setPreis(schuh.getPreis());
        } else {
            LOG.info("Neuer Schuh abspeichern: " + schuh);
            exists = schuh;
        }
        final Session session = dao.getSession();
        dao.begin();
        session.saveOrUpdate(exists);
        dao.commit();
        session.flush();
        return exists.getId();
    }

    public IShoe getShoe(final int id) {
        LOG.info(String.format("lade Schuh mit der id '%s'", id));
        final Session session = dao.getSession();
        dao.begin();

        final Criteria criteria = session.createCriteria(IShoe.class);
        criteria.add(Restrictions.eq("id", id));
        final IShoe schuh;
        @SuppressWarnings("unchecked")
        final List<IShoe> schuhe = criteria.list();
        if (schuhe != null && !schuhe.isEmpty()) {
            schuh = schuhe.get(0);
        } else {
            schuh = null;
        }
        dao.commit();
        session.flush();
        return schuh;
    }

    public void delete(final int id) {
        final Session session = dao.getSession();
        dao.begin();
        final Query query = session.createQuery("delete Shoe where id=:id"); //$NON-NLS-1$
        query.setParameter("id", id); //$NON-NLS-1$
        query.executeUpdate();
        dao.commit();
        session.flush();
    }
}
