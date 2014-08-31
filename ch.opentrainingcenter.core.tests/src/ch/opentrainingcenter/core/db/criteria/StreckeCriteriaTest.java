package ch.opentrainingcenter.core.db.criteria;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;

import ch.opentrainingcenter.transfer.IRoute;
import ch.opentrainingcenter.transfer.ITraining;

public class StreckeCriteriaTest {

    private StreckeCriteria criteria;
    private ITraining training;

    @Before
    public void setUp() {
        training = mock(ITraining.class);
    }

    @Test
    public void testRouteUnbekannt() {
        when(training.getRoute()).thenReturn(null);
        criteria = new StreckeCriteria(0);

        assertTrue(criteria.matches(training));
    }

    @Test
    public void testKeineSelektionTraining() {
        when(training.getRoute()).thenReturn(null);
        criteria = new StreckeCriteria(-1);

        assertTrue(criteria.matches(training));
    }

    @Test
    public void testRouteReferenzRoutePasstNichtTraining() {
        final IRoute route = mock(IRoute.class);
        when(route.getId()).thenReturn(2);
        when(training.getRoute()).thenReturn(route);
        criteria = new StreckeCriteria(1);

        assertFalse(criteria.matches(training));
    }

    @Test
    public void testRouteReferenzRoutePasstTraining() {
        final IRoute route = mock(IRoute.class);
        when(route.getId()).thenReturn(1);
        when(training.getRoute()).thenReturn(route);
        criteria = new StreckeCriteria(1);

        assertTrue(criteria.matches(training));
    }
}
