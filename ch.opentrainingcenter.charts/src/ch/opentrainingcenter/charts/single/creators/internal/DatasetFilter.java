package ch.opentrainingcenter.charts.single.creators.internal;

import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

public final class DatasetFilter {

    private DatasetFilter() {
        // do not instanciate
    }

    public static XYDataset filter(XYDataset dataset) {
        final int seriesCount = dataset.getSeriesCount();
        if (seriesCount > 1) {
            final XYSeriesCollection newData = new XYSeriesCollection();
            final int itemCount = dataset.getItemCount(0);
            final XYSeries series = new XYSeries("A"); //$NON-NLS-1$
            for (int i = 0; i < itemCount; i++) {
                series.add(dataset.getX(0, i), dataset.getY(0, i));
            }
            newData.addSeries(series);
            dataset = newData;
        }
        return dataset;
    }
}
