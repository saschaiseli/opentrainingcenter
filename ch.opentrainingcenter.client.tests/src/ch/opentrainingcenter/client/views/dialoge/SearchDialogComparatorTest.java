package ch.opentrainingcenter.client.views.dialoge;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;

import ch.opentrainingcenter.transfer.ITraining;

public class SearchDialogComparatorTest {

    private ITraining training1;
    private ITraining training2;
    private SearchDialogComparator comparator;

    @Before
    public void setUp() {
        comparator = new SearchDialogComparator();
        training1 = createTraining(10L, 10_000.5, 1000d);
        training2 = createTraining(11L, 10_001.5, 10d);
    }

    @Test
    public void testDatum() {

        comparator.setColumn(0);
        int compare = comparator.compare(null, training1, training2);
        assertEquals(-1, compare);

        comparator.setColumn(0);
        compare = comparator.compare(null, training1, training2);

        assertEquals(1, compare);
    }

    @Test
    public void testLaengeSort() {

        comparator.setColumn(1);
        int compare = comparator.compare(null, training1, training2);
        assertEquals(1, compare);

        comparator.setColumn(1);
        compare = comparator.compare(null, training1, training2);
        assertEquals(-1, compare);
    }

    @Test
    public void testDauer() {

        comparator.setColumn(2);
        int compare = comparator.compare(null, training1, training2);
        assertEquals(-1, compare);

        comparator.setColumn(2);
        compare = comparator.compare(null, training1, training2);
        assertEquals(1, compare);
    }

    @Test
    public void testPace() {

        comparator.setColumn(3);
        int compare = comparator.compare(null, training1, training2);
        assertEquals(-1, compare);

        comparator.setColumn(3);
        compare = comparator.compare(null, training1, training2);
        assertEquals(1, compare);
    }

    private ITraining createTraining(final long datum, final double laengeInMeter, final double dauerInSekunden) {
        final ITraining training = mock(ITraining.class);
        when(training.getDatum()).thenReturn(datum);
        when(training.getLaengeInMeter()).thenReturn(laengeInMeter);
        when(training.getDauer()).thenReturn(dauerInSekunden);
        return training;
    }

}
