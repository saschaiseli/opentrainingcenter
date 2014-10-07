package ch.opentrainingcenter.database.dao;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import ch.opentrainingcenter.transfer.IAthlete;
import ch.opentrainingcenter.transfer.IRoute;

@SuppressWarnings("nls")
public class RouteDao {
    private static final Logger LOG = Logger.getLogger(RouteDao.class);

    private final IConnectionConfig dao;

    public RouteDao(final IConnectionConfig dao) {
        this.dao = dao;
    }

    public IRoute getById(final int idRoute) {
        final Session session = dao.getSession();
        dao.begin();
        final Criteria criteria = session.createCriteria(IRoute.class);
        criteria.add(Restrictions.eq("id", idRoute));
        final IRoute route;
        @SuppressWarnings("unchecked")
        final List<IRoute> routes = criteria.list();
        if (routes != null && !routes.isEmpty()) {
            route = routes.get(0);
        } else {
            route = null;
        }
        dao.commit();
        session.flush();
        return route;
    }

    /**
     * Gibt die Strecke zurück. Wenn nichts gefunden wird, wird null
     * zurückgegeben.
     * 
     * @param athlete
     */
    public IRoute getRoute(final String name, final IAthlete athlete) {
        LOG.info(String.format("lade Route mit dem namen %s und dem Athleten: %s", name, athlete));
        final Session session = dao.getSession();
        dao.begin();

        final Criteria criteria = session.createCriteria(IRoute.class);
        criteria.add(Restrictions.eq("name", name));
        criteria.add(Restrictions.eq("athlete", athlete));
        final IRoute route;
        @SuppressWarnings("unchecked")
        final List<IRoute> routes = criteria.list();
        if (routes != null && !routes.isEmpty()) {
            route = routes.get(0);
        } else {
            route = null;
        }
        dao.commit();
        session.flush();
        return route;
    }

    public int saveOrUpdate(final IRoute route) {
        final Session session = dao.getSession();
        dao.begin();
        session.saveOrUpdate(route);
        dao.commit();
        session.flush();
        return route.getId();
    }

    public List<IRoute> getRoute(final IAthlete athlete) {
        final Session session = dao.getSession();
        dao.begin();
        final Criteria criteria = session.createCriteria(IRoute.class);
        criteria.add(Restrictions.eq("athlete", athlete));

        @SuppressWarnings("unchecked")
        final List<IRoute> routen = criteria.list();
        dao.commit();
        session.flush();
        return routen;
    }

    public boolean exists(final String name, final IAthlete athlete) {
        final Session session = dao.getSession();
        dao.begin();
        final Criteria criteria = session.createCriteria(IRoute.class);
        criteria.add(Restrictions.eq("name", name));
        criteria.add(Restrictions.eq("athlete", athlete));
        @SuppressWarnings("unchecked")
        final List<IRoute> routen = criteria.list();
        dao.commit();
        session.flush();
        return !routen.isEmpty();
    }

    public void delete(final int id) {
        final Session session = dao.getSession();
        dao.begin();
        final Query query = session.createQuery("delete Route where id=:id"); //$NON-NLS-1$
        query.setParameter("id", id); //$NON-NLS-1$
        query.executeUpdate();
        dao.commit();
        session.flush();
    }

}
