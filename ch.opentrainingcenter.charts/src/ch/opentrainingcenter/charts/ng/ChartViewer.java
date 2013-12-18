package ch.opentrainingcenter.charts.ng;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.forms.widgets.TableWrapData;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYDataset;
import org.jfree.experimental.chart.swt.ChartComposite;

import ch.opentrainingcenter.charts.ng.data.ScatterDatasetCreator;
import ch.opentrainingcenter.core.charts.ScatterDataSupport;
import ch.opentrainingcenter.core.helper.TimeHelper;
import ch.opentrainingcenter.transfer.ITraining;

@SuppressWarnings("nls")
public class ChartViewer {

    public ChartViewer(final Composite parent, final List<ITraining> trainings) {

        final Map<String, Map<Integer, Integer>> all = new HashMap<>();
        for (final ITraining training : trainings) {
            final String name = TimeHelper.convertDateToString(training.getDatum());
            all.put(name, ScatterDataSupport.populate(training));
        }
        final XYDataset dataset = ScatterDatasetCreator.createDataset(all);

        final NumberAxis domainAxis = new NumberAxis("[bpm]");
        domainAxis.setAutoRangeIncludesZero(false);
        final NumberAxis rangeAxis = new NumberAxis("Anzahl");
        rangeAxis.setAutoRangeIncludesZero(false);

        final JFreeChart jfreechart = ChartFactory.createScatterPlot("", "[bpm]", "Anzahl", dataset, PlotOrientation.VERTICAL, true, true, false);

        final ChartComposite cc = new ChartComposite(parent, SWT.NONE, jfreechart);

        final TableWrapData td = new TableWrapData(TableWrapData.FILL_GRAB);
        td.heightHint = 800;
        cc.setLayoutData(td);
    }

    public Control getControl() {
        return null;
    }
}
