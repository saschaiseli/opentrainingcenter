package ch.opentrainingcenter.db.h2.internal;

import java.io.Serializable;

import org.hibernate.Session;

import ch.opentrainingcenter.transfer.IStrecke;

/**
 * DAO um Referenzstrecken abzuspeichern
 * 
 * @author sascha
 * 
 */
public class StreckeDao {

    private final Dao dao;

    public StreckeDao(final Dao dao) {
        this.dao = dao;
    }

    public final int save(final IStrecke strecke) {
        final Session session = dao.getSession();
        dao.begin();
        final Serializable save = session.save(strecke);
        dao.commit();
        session.flush();
        return (Integer) save;
    }
}
