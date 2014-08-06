package ch.opentrainingcenter.model.training.filter;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;

import java.util.Date;

import org.junit.Test;

import ch.opentrainingcenter.model.training.ISimpleTraining;
import ch.opentrainingcenter.transfer.Sport;

public class FilterFactoryTest {

    @Test
    public void testFilterByDate() {
        final Date start = mock(Date.class);
        final Date end = mock(Date.class);

        final Filter<ISimpleTraining> filter = FilterFactory.createFilterByDate(start, end);

        assertNotNull(filter);
    }

    @Test
    public void testFilterBySport() {
        final Filter<ISimpleTraining> filter = FilterFactory.createFilterBySport(Sport.BIKING);

        assertNotNull(filter);
    }
}
