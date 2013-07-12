package ch.opentrainingcenter.charts.single;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.Collections;

import javax.imageio.ImageIO;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.TableWrapData;
import org.jfree.chart.ChartMouseEvent;
import org.jfree.chart.ChartMouseListener;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.annotations.XYImageAnnotation;
import org.jfree.chart.event.ChartProgressEvent;
import org.jfree.chart.event.ChartProgressListener;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.XYDataset;
import org.jfree.experimental.chart.swt.ChartComposite;
import org.jfree.ui.RectangleAnchor;
import org.osgi.framework.Bundle;

import ch.opentrainingcenter.charts.activator.Activator;
import ch.opentrainingcenter.charts.single.creators.internal.ChartCreatorImpl;
import ch.opentrainingcenter.charts.single.creators.internal.DataSetCreatorImpl;
import ch.opentrainingcenter.core.data.Pair;
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

                    // experimental

                    // final Image image =
                    // Activator.getImageDescriptor("icons/man_lj_32_32.png").createImage();
                    // ImageUtil.
                    // BufferedImage bimg = new BufferedImage(11, 11,
                    // BufferedImage.TYPE_INT_ARGB);
                    // BufferedImage img = null;

                    // addImageAnnotation(plot, xy);

                    // final Graphics2D g2 = bimg.createGraphics();
                    // g2.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION,
                    // RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
                    // final RenderingHints rh = new
                    // RenderingHints(RenderingHints.KEY_ANTIALIASING,
                    // RenderingHints.VALUE_ANTIALIAS_ON);
                    // g2.setRenderingHints(rh);
                    //
                    // // clear
                    // g2.setComposite(AlphaComposite.getInstance(AlphaComposite.CLEAR));
                    // g2.fillRect(0, 0, 11, 11);

                    // reset composite
                    // g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));
                    // draw
                    // g2.setPaint(Color.RED);
                    // g2.fillOval(50, 50, 100, 100);

                    // // g2.setBackground(new Color(0, 0, 0, 0));
                    // //
                    // g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_IN,
                    // // 0.0f));
                    // g2.setColor(Color.magenta);
                    // g2.fill(circle);
                    // // Draw it

                    // g2.setComposite(original);
                    // g2.drawImage(bimg, null, 0, 0);
                    // g2.dispose();
                    // final Polygon polygon = new Polygon();
                    // polygon.addPoint(10, 0);
                    // polygon.addPoint(0, 10);
                    // polygon.addPoint(-10, 0);
                    // polygon.addPoint(-10, -10);
                    // g2.setBackground(Color.cyan);
                    // g2.drawPolygon(polygon);

                    // final Ellipse2D.Double shape = new
                    // Ellipse2D.Double(xy.getFirst().doubleValue() - 3d,
                    // xy.getSecond().doubleValue() - 3d, 2, 2);
                    // final int radius = 10;
                    // final int x = xy.getFirst().intValue();
                    // final int y = xy.getSecond().intValue();
                    // final XYPointerAnnotation pointAnnotation = new
                    // XYPointerAnnotation("Bam!", x, y, 0);
                    // chart.getXYPlot().addAnnotation(pointAnnotation);
                    //
                    // final XYShapeAnnotation shapeAnnotation = new
                    // XYShapeAnnotation(new Ellipse2D.Double(x - radius, y -
                    // radius, radius + radius, radius
                    // + radius));
                    // final XYShapeAnnotation shapeAnnotation = new
                    // XYShapeAnnotation(shape, new BasicStroke(0.5f),
                    // Color.BLACK, Color.GREEN);
                    // chartComposite.getChart().getXYPlot().addAnnotation(shapeAnnotation);

                    // chartComposite.getChart().getXYPlot().getRenderer().drawRangeLine(g2,
                    // plot, axis, dataArea, value, paint, stroke);
                    // chartComposite.getChart().getXYPlot().add
                }
            }

            @SuppressWarnings("unused")
            private void addImageAnnotation(final XYPlot plot, final Pair<Number, Number> xy) {
                try {
                    final Bundle bundle = Platform.getBundle(Activator.PLUGIN_ID);
                    final Path path = new Path("icons/man_lj_32_32.gif");
                    final URL url = FileLocator.find(bundle, path, Collections.EMPTY_MAP);
                    final URL fileURL = FileLocator.toFileURL(url);
                    final BufferedImage bimg = ImageIO.read(fileURL);
                    final XYImageAnnotation a = new XYImageAnnotation(xy.getFirst().doubleValue(), xy.getSecond().doubleValue(), bimg, RectangleAnchor.CENTER);
                    plot.addAnnotation(a);
                } catch (final IOException e) {
                    System.out.println("e: " + e.getMessage());
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
    }
}
