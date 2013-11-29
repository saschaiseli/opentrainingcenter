package ch.opentrainingcenter.database.dao;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;

import org.hibernate.Query;
import org.hibernate.Session;
import org.junit.Before;
import org.junit.Test;

import ch.opentrainingcenter.transfer.IAthlete;

@SuppressWarnings("nls")
public class AthleteDaoTest {

    private AthleteDao dao;
    private IConnectionConfig config;
    private Session session;

    @Before
    public void setUp() throws Exception {
        config = mock(IConnectionConfig.class);
        session = mock(Session.class);
        when(config.getSession()).thenReturn(session);
        dao = new AthleteDao(config);

        final Query query = mock(Query.class);
        when(session.createQuery(anyString())).thenReturn(query);
        when(query.list()).thenReturn(new ArrayList<>());
    }

    @Test
    public void testGetAllAthletes() {

        dao.getAllAthletes();

        verify(config).begin();
        verify(session).flush();
        verify(config).commit();
    }

    @Test
    public void testGetAthleteInt() {
        dao.getAthlete(42);

        verify(config).begin();
        verify(session).flush();
        verify(config).commit();
    }

    @Test
    public void testGetAthleteString() {
        dao.getAthlete("");

        verify(config).begin();
        verify(session).flush();
        verify(config).commit();
    }

    @Test
    public void testSave() {
        final IAthlete athlete = mock(IAthlete.class);
        dao.save(athlete);
        // für das get auch noch
        verify(config, times(2)).begin();
        verify(session, times(2)).flush();
        verify(config, times(2)).commit();
    }

}
