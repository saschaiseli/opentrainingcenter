package ch.opentrainingcenter.database.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Matchers.anyString;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SuppressWarnings("nls")
public class DatabaseCreatorTest {

    DatabaseCreator creator;
    private IConnectionConfig connectionConfig;
    private Session session;

    @Before
    public void setUp() throws Exception {
        connectionConfig = mock(ConnectionConfig.class);
        session = mock(Session.class);
        when(connectionConfig.getSession()).thenReturn(session);
        creator = new DatabaseCreator(connectionConfig);
    }

    @Test
    public void testCreateDatabase() {
        final List<String> sqlQueries = new ArrayList<>();
        sqlQueries.add("blabla");
        sqlQueries.add("blabla2");

        final SQLQuery query1 = mock(SQLQuery.class);
        final SQLQuery query2 = mock(SQLQuery.class);
        when(session.createSQLQuery("blabla")).thenReturn(query1);
        when(session.createSQLQuery("blabla2")).thenReturn(query2);

        creator.createDatabase(sqlQueries);

        verify(session, times(1)).createSQLQuery("blabla");
        verify(session, times(1)).createSQLQuery("blabla2");
        verify(query1, times(1)).executeUpdate();
        verify(query2, times(1)).executeUpdate();

        verify(connectionConfig, times(1)).commit();
        verify(session, times(1)).flush();
    }

    @Test
    public void testBackUpDatabase() {

        final SQLQuery query = mock(SQLQuery.class);
        when(session.createSQLQuery(anyString())).thenReturn(query);

        creator.backUpDatabase("path");

        verify(connectionConfig, times(1)).begin();

        verify(query).executeUpdate();
        verify(connectionConfig).commit();
        verify(session).flush();
    }
}
