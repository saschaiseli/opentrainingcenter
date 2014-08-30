package ch.opentrainingcenter.core.db.criteria;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import ch.opentrainingcenter.transfer.ITraining;
import ch.opentrainingcenter.transfer.Sport;

public class SportCriteriaTest {

    private SportCriteria criteria;
    private ITraining training;

    @Before
    public void setUp() {
        training = mock(ITraining.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testException() {
        new SportCriteria(null);
    }

    @Test
    public void testEmptyList() {
        criteria = new SportCriteria(new HashSet<Sport>());
        when(training.getSport()).thenReturn(Sport.RUNNING);

        final boolean result = criteria.matches(training);

        assertFalse(result);
    }

    @Test
    public void testMatches() {
        final Set<Sport> sports = new HashSet<Sport>();
        sports.add(Sport.RUNNING);
        criteria = new SportCriteria(sports);
        when(training.getSport()).thenReturn(Sport.RUNNING);

        final boolean result = criteria.matches(training);

        assertTrue(result);
    }

    @Test
    public void testMatchesMultiple() {
        final Set<Sport> sports = new HashSet<Sport>();
        sports.add(Sport.RUNNING);
        sports.add(Sport.BIKING);
        sports.add(Sport.OTHER);
        criteria = new SportCriteria(sports);
        when(training.getSport()).thenReturn(Sport.RUNNING);

        assertTrue(criteria.matches(training));

        when(training.getSport()).thenReturn(Sport.BIKING);
        assertTrue(criteria.matches(training));

        when(training.getSport()).thenReturn(Sport.OTHER);
        assertTrue(criteria.matches(training));
    }
}
