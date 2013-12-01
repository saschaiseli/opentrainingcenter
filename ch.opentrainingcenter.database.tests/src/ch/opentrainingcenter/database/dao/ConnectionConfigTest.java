package ch.opentrainingcenter.database.dao;

import java.util.Properties;

import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.classic.Session;
import org.junit.Before;
import org.junit.Test;

import ch.opentrainingcenter.core.db.DatabaseConnectionConfiguration;
import ch.opentrainingcenter.core.db.DbConnection;
import ch.opentrainingcenter.database.USAGE;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyString;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@SuppressWarnings("nls")
public class ConnectionConfigTest {

    private ConnectionConfig config;
    private DatabaseConnectionConfiguration databaseConnectionConfiguration;
    private Configuration hibernateCfg;
    private SessionFactory sessionFactory;

    @Before
    public void setUp() throws Exception {

        final DbConnection dbConnection = new DbConnection("driver1", "dialect1", "url1", "username1", "password1");
        databaseConnectionConfiguration = new DatabaseConnectionConfiguration(dbConnection);

        hibernateCfg = mock(Configuration.class);
        sessionFactory = mock(SessionFactory.class);
        when(hibernateCfg.buildSessionFactory()).thenReturn(sessionFactory);
    }

    @Test
    public void testConnectionConfig() {
        config = new ConnectionConfig(USAGE.TEST, databaseConnectionConfiguration, hibernateCfg);
        final Properties properties = databaseConnectionConfiguration.getProperties();
        verify(hibernateCfg, times(1)).setProperties(properties);
        verify(hibernateCfg, times(1)).setProperty("current_session_context_class", "thread");
        verify(hibernateCfg, times(1)).setProperty("cache.provider_class", "org.hibernate.cache.NoCacheProvider");
        verify(hibernateCfg, times(1)).setProperty("hibernate.show_sql", String.valueOf(USAGE.TEST.isShowSql()));
        verify(hibernateCfg, times(1)).setProperty("hibernate.format_sql", String.valueOf(USAGE.TEST.isFormatSql()));
        verify(hibernateCfg, times(1)).setProperty("hibernate.connection.pool_size", "10");

        verify(hibernateCfg, times(1)).addResource("Athlete.hbm.xml", this.getClass().getClassLoader());
        verify(hibernateCfg, times(1)).addResource("Health.hbm.xml", this.getClass().getClassLoader());
        verify(hibernateCfg, times(1)).addResource("Weather.hbm.xml", this.getClass().getClassLoader());
        verify(hibernateCfg, times(1)).addResource("Training.hbm.xml", this.getClass().getClassLoader());
        verify(hibernateCfg, times(1)).addResource("Trainingtype.hbm.xml", this.getClass().getClassLoader());
        verify(hibernateCfg, times(1)).addResource("Tracktrainingproperty.hbm.xml", this.getClass().getClassLoader());
        verify(hibernateCfg, times(1)).addResource("Streckenpunkte.hbm.xml", this.getClass().getClassLoader());
        verify(hibernateCfg, times(1)).addResource("Planungwoche.hbm.xml", this.getClass().getClassLoader());
        verify(hibernateCfg, times(1)).addResource("Route.hbm.xml", this.getClass().getClassLoader());
        verify(hibernateCfg, times(1)).buildSessionFactory();
        verify(hibernateCfg, times(3)).getProperty(anyString());
        verifyNoMoreInteractions(hibernateCfg);
    }

    @Test
    public void testGetUsage() {
        config = new ConnectionConfig(USAGE.TEST, databaseConnectionConfiguration, hibernateCfg);
        assertEquals(USAGE.TEST, config.getUsage());
    }

    @Test
    public void testGetConfig() {
        config = new ConnectionConfig(USAGE.TEST, databaseConnectionConfiguration, hibernateCfg);
        assertEquals(databaseConnectionConfiguration, config.getConfig());
    }

    @Test
    public void testGetSessionOpen() {
        config = new ConnectionConfig(USAGE.TEST, databaseConnectionConfiguration, hibernateCfg);
        final Session session = mock(Session.class);
        when(sessionFactory.openSession()).thenReturn(session);

        org.hibernate.Session result = config.getSession();
        assertEquals(session, result);

        verify(sessionFactory, times(1)).openSession();
        when(session.isOpen()).thenReturn(true);
        result = config.getSession();
        assertEquals(session, result);
    }

    @Test
    public void testGetSessionClosed() {
        config = new ConnectionConfig(USAGE.TEST, databaseConnectionConfiguration, hibernateCfg);
        final Session session = mock(Session.class);
        when(sessionFactory.openSession()).thenReturn(session);

        org.hibernate.Session result = config.getSession();
        assertEquals(session, result);

        when(session.isOpen()).thenReturn(false);
        result = config.getSession();
        assertEquals(session, result);
        verify(sessionFactory, times(2)).openSession();
    }

    @Test
    public void testBegin() {
        config = new ConnectionConfig(USAGE.TEST, databaseConnectionConfiguration, hibernateCfg);
        final Session session = mock(Session.class);
        when(sessionFactory.openSession()).thenReturn(session);

        config.begin();

        verify(session, times(1)).beginTransaction();
        verifyNoMoreInteractions(session);
    }

    @Test
    public void testClose() {
        config = new ConnectionConfig(USAGE.TEST, databaseConnectionConfiguration, hibernateCfg);
        final Session session = mock(Session.class);
        when(sessionFactory.openSession()).thenReturn(session);

        config.close();

        verify(session, times(1)).close();
        verifyNoMoreInteractions(session);
    }

    @Test
    public void testCommit() {
        config = new ConnectionConfig(USAGE.TEST, databaseConnectionConfiguration, hibernateCfg);
        final Session session = mock(Session.class);
        final Transaction transaction = mock(Transaction.class);
        when(session.getTransaction()).thenReturn(transaction);
        when(sessionFactory.openSession()).thenReturn(session);

        config.commit();

        verify(transaction, times(1)).commit();
        verifyNoMoreInteractions(transaction);
    }

    @Test
    public void testRollback() {
        config = new ConnectionConfig(USAGE.TEST, databaseConnectionConfiguration, hibernateCfg);
        final Session session = mock(Session.class);
        final Transaction transaction = mock(Transaction.class);
        when(session.getTransaction()).thenReturn(transaction);
        when(sessionFactory.openSession()).thenReturn(session);

        config.rollback();

        verify(transaction, times(1)).rollback();
        verifyNoMoreInteractions(transaction);

    }
}
