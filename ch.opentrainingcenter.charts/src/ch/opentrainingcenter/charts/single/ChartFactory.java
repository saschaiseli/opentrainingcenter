package ch.opentrainingcenter.charts.single;

import java.awt.Paint;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.Collections;
import java.util.List;

import javax.imageio.ImageIO;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.TableWrapData;
import org.jfree.chart.ChartMouseEvent;
import org.jfree.chart.ChartMouseListener;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.annotations.XYImageAnnotation;
import org.jfree.chart.event.ChartProgressEvent;
import org.jfree.chart.event.ChartProgressListener;
import org.jfree.chart.plot.IntervalMarker;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.XYDataset;
import org.jfree.experimental.chart.swt.ChartComposite;
import org.jfree.ui.RectangleAnchor;
import org.osgi.framework.Bundle;

import ch.opentrainingcenter.charts.activator.Activator;
import ch.opentrainingcenter.charts.single.creators.internal.ChartCreatorImpl;
import ch.opentrainingcenter.charts.single.creators.internal.DataSetCreatorImpl;
import ch.opentrainingcenter.core.PreferenceConstants;
import ch.opentrainingcenter.core.data.Pair;
import ch.opentrainingcenter.core.helper.ColorFromPreferenceHelper;
import ch.opentrainingcenter.transfer.IAthlete;
import ch.opentrainingcenter.transfer.ILapInfo;
import ch.opentrainingcenter.transfer.ITraining;

public class ChartFactory {

    private static final Logger LOGGER = Logger.getLogger(ChartFactory.class);

    private final DataSetCreatorImpl dataSetCreator;
    private final ChartCreatorImpl chartCreator;

    private final IPreferenceStore store;

    public ChartFactory(final IPreferenceStore store, final ITraining training, final IAthlete athlete) {
        this.store = store;
        dataSetCreator = new DataSetCreatorImpl(training);
        chartCreator = new ChartCreatorImpl(store, athlete);
    }

    public ISelectionChangedListener addChartToComposite(final Composite client, final ChartType type) {
        final XYDataset dataset;
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
        chart.setAntiAlias(true);

        final ChartComposite chartComposite = new ChartComposite(client, SWT.NONE, chart, true);

        final DynamicChartInfos infos = new DynamicChartInfos(dataset);

        chart.addProgressListener(new ChartProgressListener() {

            @Override
            public void chartProgress(final ChartProgressEvent event) {
                if (ChartProgressEvent.DRAWING_FINISHED == event.getType()) {
                    infos.reset();
                }
            }
        });

        chartComposite.addChartMouseListener(new ChartMouseListener() {

            @Override
            public void chartMouseMoved(final ChartMouseEvent event) {

                infos.setRenderInfos(chartComposite.getChartRenderingInfo());

                final java.awt.event.MouseEvent trigger = event.getTrigger();
                final Point point = trigger.getPoint();
                final Point translateScreenToJavaSWT = chartComposite.translateScreenToJavaSWT(point);
                final XYPlot plot = chartComposite.getChart().getXYPlot();
                plot.clearAnnotations();
                final int index = (int) translateScreenToJavaSWT.getX();
                final Pair<Number, Number> xy = infos.getXValue(index);
                if (xy != null) {
                    final String yValue = infos.getYValue(index);
                    AnnotationHelper.addCrossAnnotation(plot, xy, Double.MAX_VALUE, Double.MAX_VALUE, yValue);
                }
            }

            @SuppressWarnings("unused")
            private void addImageAnnotation(final XYPlot plot, final Pair<Number, Number> xy) {
                try {
                    final Bundle bundle = Platform.getBundle(Activator.PLUGIN_ID);
                    final Path path = new Path("icons/man_lj_32_32.gif"); //$NON-NLS-1$
                    final URL url = FileLocator.find(bundle, path, Collections.EMPTY_MAP);
                    final URL fileURL = FileLocator.toFileURL(url);
                    final BufferedImage bimg = ImageIO.read(fileURL);
                    final XYImageAnnotation a = new XYImageAnnotation(xy.getFirst().doubleValue(), xy.getSecond().doubleValue(), bimg, RectangleAnchor.CENTER);
                    plot.addAnnotation(a);
                } catch (final IOException e) {
                    LOGGER.error("Fehler beim Hinzufügen einer ImageAnnotation", e); //$NON-NLS-1$
                }
            }

            @Override
            public void chartMouseClicked(final ChartMouseEvent event) {
                infos.reset();
            }
        });
        final TableWrapData td = new TableWrapData(TableWrapData.FILL_GRAB);
        td.heightHint = 400;
        chartComposite.setLayoutData(td);
        return new TreeViewerListener(chartComposite);
    }

    class TreeViewerListener implements ISelectionChangedListener {

        private final ChartComposite chartComposite;

        TreeViewerListener(final ChartComposite chartComposite) {
            this.chartComposite = chartComposite;
        }

        @Override
        public void selectionChanged(final SelectionChangedEvent event) {
            final XYPlot plot = chartComposite.getChart().getXYPlot();
            plot.clearDomainMarkers();
            final StructuredSelection ss = (StructuredSelection) event.getSelection();
            if (!ss.isEmpty() && ss.getFirstElement() instanceof ILapInfo) {
                @SuppressWarnings("unchecked")
                final List<ILapInfo> lapInfos = ss.toList();
                final ILapInfo first = lapInfos.get(0);
                final ILapInfo last = lapInfos.get(lapInfos.size() - 1);

                final IntervalMarker marker = new IntervalMarker(first.getStart(), last.getEnd());
                final Paint paint = ColorFromPreferenceHelper.getColor(store, PreferenceConstants.CHART_COLOR_RANGE, 80);
                marker.setPaint(paint);
                plot.addDomainMarker(marker);
            }
        }

    }
}
