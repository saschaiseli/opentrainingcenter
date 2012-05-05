package ch.opentrainingcenter.client.charts;

import java.awt.Color;
import java.math.BigDecimal;

import org.eclipse.jface.preference.IPreferenceStore;
import org.jfree.chart.plot.IntervalMarker;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.IntervalXYDataset;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import ch.opentrainingcenter.client.PreferenceConstants;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class WeekXYBarRendererTest {
    private WeekXYBarRenderer renderer;
    private IntervalXYDataset dataset;
    private XYPlot plot;
    private IPreferenceStore store;

    @Before
    public void before() {
        dataset = Mockito.mock(IntervalXYDataset.class);
        plot = Mockito.mock(XYPlot.class);
        store = Mockito.mock(IPreferenceStore.class);

        Mockito.when(store.getString(PreferenceConstants.KM_PER_WEEK_COLOR_BELOW)).thenReturn("125,120,115");
        Mockito.when(store.getString(PreferenceConstants.KM_PER_WEEK_COLOR_ABOVE)).thenReturn("225,220,215");
        Mockito.when(store.getInt(PreferenceConstants.KM_PER_WEEK)).thenReturn(100);
    }

    @Test
    public void testConstructor() {
        renderer = new WeekXYBarRenderer(dataset, plot, store);
        assertNotNull(renderer);
    }

    @Test
    public void testConstructorAddRangeMarker() {

        renderer = new WeekXYBarRenderer(dataset, plot, store);
        Mockito.verify(plot, Mockito.times(1)).addRangeMarker((IntervalMarker) Mockito.any());
    }

    @Test
    public void testGetPaintBelow() {
        // prepare
        renderer = new WeekXYBarRenderer(dataset, plot, store);
        final Number number = BigDecimal.valueOf(50);
        Mockito.when(dataset.getY(0, 0)).thenReturn(number);
        // execute
        final Color paint = (Color) renderer.getItemPaint(0, 0);
        // assert
        assertEquals("Rotanteil unter der Wochengrenze", 125, paint.getRed());
        assertEquals("Grünanteil unter der Wochengrenze", 120, paint.getGreen());
        assertEquals("Blauanteil unter der Wochengrenze", 115, paint.getBlue());
    }

    @Test
    public void testGetPaintAbove() {
        // prepare

        renderer = new WeekXYBarRenderer(dataset, plot, store);
        final Number number = BigDecimal.valueOf(150);
        Mockito.when(dataset.getY(0, 0)).thenReturn(number);
        // execute
        final Color paint = (Color) renderer.getItemPaint(0, 0);
        // assert
        assertEquals("Rotanteil über der Wochengrenze", 225, paint.getRed());
        assertEquals("Grünanteil über der Wochengrenze", 220, paint.getGreen());
        assertEquals("Blauanteil über der Wochengrenze", 215, paint.getBlue());
    }

    @Test
    public void testGetPaintNull() {
        // prepare

        renderer = new WeekXYBarRenderer(dataset, plot, store);
        Mockito.when(dataset.getY(0, 0)).thenReturn(null);
        // execute
        final Color paint = (Color) renderer.getItemPaint(0, 0);
        // assert
        assertEquals("Rotanteil über der Wochengrenze", 225, paint.getRed());
        assertEquals("Grünanteil über der Wochengrenze", 220, paint.getGreen());
        assertEquals("Blauanteil über der Wochengrenze", 215, paint.getBlue());
    }
}
