package ch.opentrainingcenter.charts.single;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.TableWrapData;
import org.jfree.chart.JFreeChart;
import org.jfree.experimental.chart.swt.ChartComposite;

import ch.opentrainingcenter.charts.single.creators.internal.ChartCreatorImpl;
import ch.opentrainingcenter.charts.single.creators.internal.DataSetCreatorImpl;
import ch.opentrainingcenter.tcx.ActivityT;
import ch.opentrainingcenter.transfer.IAthlete;

public class ChartFactory {

    private final DataSetCreatorImpl dataSetCreator;
    private final ChartCreatorImpl chartCreator;

    public ChartFactory(final IPreferenceStore store, final ActivityT activity, final IAthlete athlete) {
        dataSetCreator = new DataSetCreatorImpl(activity);
        chartCreator = new ChartCreatorImpl(store, athlete);
    }

    public void addChartToComposite(final Composite client, final ChartType type) {
        final JFreeChart chart = chartCreator.createChart(dataSetCreator.createDatasetHeart(), type);
        final ChartComposite chartComposite = new ChartComposite(client, SWT.NONE, chart, true);
        final TableWrapData td = new TableWrapData(TableWrapData.FILL_GRAB);
        td.heightHint = 400;
        chartComposite.setLayoutData(td);
    }

}
