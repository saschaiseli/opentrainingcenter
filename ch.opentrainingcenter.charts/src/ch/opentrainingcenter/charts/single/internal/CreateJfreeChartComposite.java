package ch.opentrainingcenter.charts.single.internal;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.TableWrapData;
import org.jfree.chart.JFreeChart;
import org.jfree.experimental.chart.swt.ChartComposite;

import ch.opentrainingcenter.charts.single.ChartType;
import ch.opentrainingcenter.charts.single.creators.ChartCreator;
import ch.opentrainingcenter.charts.single.creators.internal.DataSetCreator;

public class CreateJfreeChartComposite {

    public void addChart(final Composite parent, final DataSetCreator dataSetCreator, final ChartCreator chartCreator, TableWrapData td) {
        final JFreeChart chart = chartCreator.createChart(dataSetCreator.createDatasetHeart(), ChartType.HEART_DISTANCE);
        final ChartComposite chartComposite = new ChartComposite(parent, SWT.NONE, chart, true);
        td = new TableWrapData(TableWrapData.FILL_GRAB);
        td.heightHint = 400;
        chartComposite.setLayoutData(td);
    }

}
