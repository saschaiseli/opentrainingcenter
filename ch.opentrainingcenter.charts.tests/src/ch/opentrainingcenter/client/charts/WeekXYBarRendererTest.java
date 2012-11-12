package ch.opentrainingcenter.client.charts;

import java.awt.Color;
import java.math.BigDecimal;

import org.eclipse.jface.preference.IPreferenceStore;
import org.jfree.data.xy.IntervalXYDataset;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import ch.opentrainingcenter.charts.bar.internal.WeekXYBarRenderer;
import ch.opentrainingcenter.core.PreferenceConstants;
import ch.opentrainingcenter.core.cache.ICache;
import ch.opentrainingcenter.model.planing.IPlanungModel;
import ch.opentrainingcenter.model.planing.KwJahrKey;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@SuppressWarnings("nls")
public class WeekXYBarRendererTest {
    private WeekXYBarRenderer renderer;
    private IntervalXYDataset dataset;
    private IPreferenceStore store;
    private ICache<KwJahrKey, IPlanungModel> cache;

    @SuppressWarnings("unchecked")
    @Before
    public void before() {
        dataset = Mockito.mock(IntervalXYDataset.class);
        store = Mockito.mock(IPreferenceStore.class);
        cache = Mockito.mock(ICache.class);
        Mockito.when(store.getString(PreferenceConstants.ZIEL_NICHT_ERFUELLT_COLOR)).thenReturn("125,120,115");
        Mockito.when(store.getString(PreferenceConstants.ZIEL_ERFUELLT_COLOR)).thenReturn("225,220,215");
        Mockito.when(store.getString(PreferenceConstants.ZIEL_NICHT_BEKANNT_COLOR)).thenReturn("123,123,123");
        final IPlanungModel model = Mockito.mock(IPlanungModel.class);
        Mockito.when(model.getKmProWoche()).thenReturn(Integer.valueOf(100));
        Mockito.when(cache.get((KwJahrKey) Mockito.any())).thenReturn(model);
    }

    @Test
    public void testConstructor() {
        renderer = new WeekXYBarRenderer(dataset, store, cache);
        assertNotNull(renderer);
    }

    @Test
    public void testGetPaintBelow() {
        // prepare
        renderer = new WeekXYBarRenderer(dataset, store, cache);
        final Number number = BigDecimal.valueOf(50);
        Mockito.when(dataset.getY(0, 0)).thenReturn(number);
        Mockito.when(dataset.getX(0, 0)).thenReturn(0);
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

        renderer = new WeekXYBarRenderer(dataset, store, cache);
        final Number number = BigDecimal.valueOf(150);
        Mockito.when(dataset.getY(0, 0)).thenReturn(number);
        Mockito.when(dataset.getX(0, 0)).thenReturn(0);
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

        renderer = new WeekXYBarRenderer(dataset, store, cache);
        Mockito.when(dataset.getY(0, 0)).thenReturn(null);
        Mockito.when(dataset.getX(0, 0)).thenReturn(0);
        // execute
        final Color paint = (Color) renderer.getItemPaint(0, 0);
        // assert
        assertEquals("Rotanteil über der Wochengrenze", 225, paint.getRed());
        assertEquals("Grünanteil über der Wochengrenze", 220, paint.getGreen());
        assertEquals("Blauanteil über der Wochengrenze", 215, paint.getBlue());
    }
}
