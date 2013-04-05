package ch.opentrainingcenter.charts.single;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.TableWrapData;
import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.XYDataset;
import org.jfree.experimental.chart.swt.ChartComposite;

import ch.opentrainingcenter.charts.single.creators.internal.ChartCreatorImpl;
import ch.opentrainingcenter.charts.single.creators.internal.DataSetCreatorImpl;
import ch.opentrainingcenter.transfer.IAthlete;
import ch.opentrainingcenter.transfer.ITraining;

public class ChartFactory {

    private final DataSetCreatorImpl dataSetCreator;
    private final ChartCreatorImpl chartCreator;

    public ChartFactory(final IPreferenceStore store, final ITraining training, final IAthlete athlete) {
        dataSetCreator = new DataSetCreatorImpl(training);
        chartCreator = new ChartCreatorImpl(store, athlete);
    }

    public void addChartToComposite(final Composite client, final ChartType type) {
        final XYDataset dataset;// = dataSetCreator.createDatasetHeart();
        switch (type) {
        case HEART_DISTANCE:
            dataset = dataSetCreator.createDatasetHeart();
            break;
        case ALTITUDE_DISTANCE:
            dataset = dataSetCreator.createDatasetAltitude();
            break;
        case SPEED_DISTANCE:
            dataset = dataSetCreator.createDatasetSpeed();
            break;
        default:
            dataset = dataSetCreator.createDatasetHeart();
        }
        final JFreeChart chart = chartCreator.createChart(dataset, type);
        final ChartComposite chartComposite = new ChartComposite(client, SWT.NONE, chart, true);
        final TableWrapData td = new TableWrapData(TableWrapData.FILL_GRAB);
        td.heightHint = 400;
        chartComposite.setLayoutData(td);
    }
}
