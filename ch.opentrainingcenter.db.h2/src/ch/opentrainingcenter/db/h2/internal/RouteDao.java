package ch.opentrainingcenter.db.h2.internal;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import ch.opentrainingcenter.transfer.IRoute;

public class RouteDao {
    private static final Logger LOG = Logger.getLogger(RouteDao.class);

    private final Dao dao;

    public RouteDao(final Dao dao) {
        this.dao = dao;
    }

    /**
     * Gibt die Strecke zurück. Wenn nichts gefunden wird, wird null
     * zurückgegeben.
     */
    public IRoute getRoute(final String name) {
        LOG.info("load Route mit dem namen: " + name); //$NON-NLS-1$
        final Session session = dao.getSession();
        dao.begin();
        final Criteria criteria = session.createCriteria(IRoute.class);
        criteria.add(Restrictions.eq("name", name)); //$NON-NLS-1$
        final IRoute route;
        @SuppressWarnings("unchecked")
        final List<IRoute> routes = criteria.list();
        if (routes != null && routes.size() > 0) {
            route = routes.get(0);
        } else {
            route = null;
        }
        dao.commit();
        session.flush();
        return route;
    }

    public int saveOrUpdate(final IRoute route) {

        IRoute exists = getRoute(route.getName());
        if (exists != null) {
            LOG.info("Strecke überschreiben alt: " + exists + " neu: " + route); //$NON-NLS-1$ //$NON-NLS-2$
            exists.setBeschreibung(route.getBeschreibung());
        } else {
            LOG.info("Neue Strecke abspeichern: " + route); //$NON-NLS-1$
            exists = route;
        }
        final Session session = dao.getSession();
        dao.begin();
        session.saveOrUpdate(exists);
        dao.commit();
        session.flush();
        final int id = getRoute(route.getName()).getId();
        return id;
    }
}
