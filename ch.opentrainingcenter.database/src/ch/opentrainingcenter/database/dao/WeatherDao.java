package ch.opentrainingcenter.database.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;

import ch.opentrainingcenter.transfer.IWeather;

@SuppressWarnings("nls")
public class WeatherDao {

    private final IConnectionConfig dao;

    public WeatherDao(final IConnectionConfig dao) {
        this.dao = dao;
    }

    @SuppressWarnings("unchecked")
    public List<IWeather> getAllWeather() {
        final Session session = dao.getSession();
        dao.begin();

        final Criteria criteria = session.createCriteria(IWeather.class);

        criteria.addOrder(Order.desc("id"));

        final List<IWeather> list = criteria.list();

        dao.commit();
        session.flush();

        return list;
    }
}
