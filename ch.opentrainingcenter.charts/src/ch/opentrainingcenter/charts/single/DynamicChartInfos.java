package ch.opentrainingcenter.charts.single;

import java.awt.geom.Rectangle2D;
import java.util.Calendar;
import java.util.Locale;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.jfree.chart.ChartRenderingInfo;
import org.jfree.chart.entity.EntityCollection;
import org.jfree.chart.entity.XYItemEntity;
import org.jfree.data.xy.XYDataset;

import ch.opentrainingcenter.core.assertions.Assertions;
import ch.opentrainingcenter.core.data.Pair;
import ch.opentrainingcenter.core.helper.TimeHelper;

/**
 * Übersetzt X-Coordinaten von JFreeCharts in Werte aus dem {@link XYDataset}.
 * 
 */
public class DynamicChartInfos {

    private final TreeMap<Double, Pair<Number, Number>> result = new TreeMap<>();
    private final TreeMap<Double, Number> time = new TreeMap<>();
    private final Calendar cal = Calendar.getInstance(Locale.getDefault());
    private ChartRenderingInfo renderInfos;

    private final XYDataset dataset;

    public DynamicChartInfos(final XYDataset dataset) {
        this.dataset = dataset;
    }

    public void setRenderInfos(final ChartRenderingInfo renderInfos) {
        this.renderInfos = renderInfos;
    }

    /**
     * Zugewiesener Wert zurückgeben. Zuweisungs Algorithmus wird nur ausgeführt
     * wenn noch nie aufgerufen oder nach einem {@link #reset()}.
     * 
     * @param key
     *            x-Coordinate auf dem Chart. Bereits mit
     *            translateScreenToJavaSWT übersetzt.
     * @return den wert der grösser als Key ist wenn keiner gefunden wird, wird
     *         null zurückgegeben.
     */
    public Pair<Number, Number> getXValue(final int key) {
        Assertions.notNull(renderInfos, "Renderinfos dürfen nicht null sein"); //$NON-NLS-1$
        if (result.isEmpty()) {
            assignValuesToMap();
        }
        final Entry<Double, Pair<Number, Number>> higherEntry = result.higherEntry((double) key);
        Pair<Number, Number> value = null;
        if (higherEntry != null) {
            value = higherEntry.getValue();
        }
        return value;
    }

    private void assignValuesToMap() {
        final EntityCollection entities = renderInfos.getEntityCollection();
        long startTime = Long.MIN_VALUE;
        for (final Object item : entities.getEntities()) {
            if (item instanceof XYItemEntity) {
                final XYItemEntity xyItem = (XYItemEntity) item;
                final Rectangle2D rect = xyItem.getArea().getBounds2D();

                final int itemIndex = xyItem.getItem();
                final int seriesIndex = xyItem.getSeriesIndex();
                final Number xValue = dataset.getX(seriesIndex, itemIndex);
                final Number yValue = dataset.getY(seriesIndex, itemIndex);
                result.put(rect.getX(), new Pair<Number, Number>(xValue, yValue));
                startTime = addTimeIfPresent(startTime, rect, itemIndex);
            }
        }
    }

    private long addTimeIfPresent(final long startTime, final Rectangle2D rect, final int itemIndex) {
        if (dataset.getSeriesCount() > 1) {
            // ansonsten wurde im dataset die zeit nicht hinzugefügt
            long runningTime;
            final Number y = dataset.getY(1, itemIndex);
            if (y != null) {
                final long currentStart;
                if (startTime == Long.MIN_VALUE) {
                    currentStart = y.longValue();
                } else {
                    currentStart = startTime;
                }
                runningTime = y.longValue() - currentStart;
                time.put(rect.getX(), runningTime);
                return currentStart;
            }
        }
        return startTime;
    }

    /**
     * Gibt die Zeit zurück die bei diesem Element verstrichen ist. Wenn keine
     * Zeit im Dataset ist, wird null zurückgegeben.
     */
    public String getYValue(final int key) {
        Assertions.notNull(renderInfos, "Renderinfos dürfen nicht null sein"); //$NON-NLS-1$
        if (time.isEmpty()) {
            assignValuesToMap();
        }
        final Entry<Double, Number> higherEntry = time.higherEntry((double) key);
        Number value = null;
        if (higherEntry != null) {
            value = higherEntry.getValue();
        }
        final String yValue;
        if (value != null) {
            cal.setTimeInMillis(value.longValue());
            yValue = TimeHelper.convertTimeToString(value.longValue());
        } else {
            yValue = null;
        }
        return yValue;
    }

    /**
     * Setzt die zugewiesenen Werte zurück. Diese Methode sollte zum Beispiel
     * nach einem Zoom im Chart aufgerufen werden.
     */
    public void reset() {
        result.clear();
        time.clear();
    }
}
