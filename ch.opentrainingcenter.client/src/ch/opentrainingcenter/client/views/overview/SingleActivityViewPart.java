package ch.opentrainingcenter.client.views.overview;

import java.awt.Color;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.forms.events.ExpansionAdapter;
import org.eclipse.ui.forms.events.ExpansionEvent;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.forms.widgets.TableWrapData;
import org.eclipse.ui.forms.widgets.TableWrapLayout;
import org.eclipse.ui.part.ViewPart;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.IntervalMarker;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.Range;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.experimental.chart.swt.ChartComposite;

import ch.opentrainingcenter.client.Activator;
import ch.opentrainingcenter.client.Messages;
import ch.opentrainingcenter.client.cache.Cache;
import ch.opentrainingcenter.client.cache.impl.TrainingCenterDataCache;
import ch.opentrainingcenter.client.charts.HeartIntervallCreator;
import ch.opentrainingcenter.client.helper.SpeedCalculator;
import ch.opentrainingcenter.client.helper.ZoneHelper;
import ch.opentrainingcenter.client.helper.ZoneHelper.Zone;
import ch.opentrainingcenter.client.model.ISimpleTraining;
import ch.opentrainingcenter.client.model.Units;
import ch.opentrainingcenter.tcx.ActivityLapT;
import ch.opentrainingcenter.tcx.ActivityT;
import ch.opentrainingcenter.tcx.HeartRateInBeatsPerMinuteT;
import ch.opentrainingcenter.tcx.TrackT;
import ch.opentrainingcenter.tcx.TrackpointT;

public class SingleActivityViewPart extends ViewPart {

    public static final String ID = "ch.opentrainingcenter.client.views.singlerun"; //$NON-NLS-1$
    private static final Logger LOGGER = Logger.getLogger(SingleActivityViewPart.class);
    private final Cache cache = TrainingCenterDataCache.getInstance();
    private final ISimpleTraining simpleTraining;
    private final ActivityT selected;
    private FormToolkit toolkit;
    private ScrolledForm form;
    private TableWrapData td;
    private final HeartIntervallCreator heartIntervall;

    public SingleActivityViewPart() {
        simpleTraining = cache.getSelectedOverview();
        selected = cache.get(simpleTraining.getDatum());
        setPartName(simpleTraining.getFormattedDate());
        heartIntervall = new HeartIntervallCreator(Activator.getDefault().getPreferenceStore());
    }

    /**
     * Verlauf der höhe
     */
    private void addAltitudeSection(final Composite body) {
        final Section altitude = toolkit.createSection(body, Section.DESCRIPTION | Section.TITLE_BAR | Section.TWISTIE | Section.EXPANDED);
        altitude.setExpanded(false);
        altitude.addExpansionListener(new ExpansionAdapter() {
            @Override
            public void expansionStateChanged(final ExpansionEvent e) {
                form.reflow(true);
            }
        });
        td = new TableWrapData(TableWrapData.FILL_GRAB);
        td.colspan = 1;
        td.grabHorizontal = true;
        td.grabVertical = true;
        altitude.setLayoutData(td);
        altitude.setText(Messages.SingleActivityViewPart_13);
        altitude.setDescription(Messages.SingleActivityViewPart_14);

        final Composite client = toolkit.createComposite(altitude);

        final TableWrapLayout layout = new TableWrapLayout();
        layout.numColumns = 2;
        layout.makeColumnsEqualWidth = false;
        client.setLayout(layout);

        final Label dauerLabel = toolkit.createLabel(client, Messages.SingleActivityViewPart_15);
        td = new TableWrapData();
        dauerLabel.setLayoutData(td);

        final JFreeChart chart = createChart(createDataset(ChartType.ALTITUDE_DISTANCE), ChartType.ALTITUDE_DISTANCE);
        final ChartComposite chartComposite = new ChartComposite(client, SWT.NONE, chart, true);
        td = new TableWrapData(TableWrapData.FILL_GRAB);
        td.heightHint = 400;
        chartComposite.setLayoutData(td);

        altitude.setClient(client);
    }

    /**
     * Herz frequenz
     */
    private void addHeartSection(final Composite body) {
        final Section heartSection = toolkit.createSection(body, Section.DESCRIPTION | Section.TITLE_BAR | Section.TWISTIE
                | Section.EXPANDED);
        heartSection.addExpansionListener(new ExpansionAdapter() {
            @Override
            public void expansionStateChanged(final ExpansionEvent e) {
                form.reflow(true);
            }
        });
        heartSection.setExpanded(false);
        td = new TableWrapData(TableWrapData.FILL_GRAB);
        td.colspan = 1;
        td.grabHorizontal = true;
        td.grabVertical = true;
        heartSection.setLayoutData(td);
        heartSection.setText(Messages.SingleActivityViewPart_9);
        heartSection.setDescription(Messages.SingleActivityViewPart_10);
        //
        final Composite client = toolkit.createComposite(heartSection);

        final TableWrapLayout layout = new TableWrapLayout();
        layout.numColumns = 2;
        layout.makeColumnsEqualWidth = false;

        client.setLayout(layout);

        final Label dauerLabel = toolkit.createLabel(client, ""); //$NON-NLS-1$
        td = new TableWrapData();
        dauerLabel.setLayoutData(td);

        final JFreeChart chart = createChart(createDataset(ChartType.HEART_DISTANCE), ChartType.HEART_DISTANCE);
        final ChartComposite chartComposite = new ChartComposite(client, SWT.NONE, chart, true);
        td = new TableWrapData(TableWrapData.FILL_GRAB);
        td.heightHint = 400;
        chartComposite.setLayoutData(td);

        heartSection.setClient(client);
    }

    private void addIntervallMarker(final XYPlot plot) {
        final Map<Zone, IntervalMarker> markers = heartIntervall.createMarker(cache.getSelectedProfile());
        plot.addRangeMarker(markers.get(ZoneHelper.Zone.AEROBE));
        plot.addRangeMarker(markers.get(ZoneHelper.Zone.SCHWELLE));
        plot.addRangeMarker(markers.get(ZoneHelper.Zone.ANAEROBE));
    }

    private void addLabelAndValue(final Composite parent, final String label, final String value, final Units unit) {
        // Label
        final Label dauerLabel = toolkit.createLabel(parent, label + ": "); //$NON-NLS-1$
        GridData gd = new GridData();
        gd.verticalIndent = 4;
        dauerLabel.setLayoutData(gd);

        // value
        final Label dauer = toolkit.createLabel(parent, value);
        gd = new GridData();
        gd.horizontalAlignment = SWT.RIGHT;
        gd.horizontalIndent = 10;
        gd.verticalIndent = 4;
        dauer.setLayoutData(gd);

        // einheit
        final Label einheit = toolkit.createLabel(parent, unit.getName());
        gd = new GridData();
        gd.horizontalAlignment = SWT.LEFT;
        gd.horizontalIndent = 10;
        gd.verticalIndent = 4;
        einheit.setLayoutData(gd);
    }

    private void addMapSection(final Composite body) {
        final Section mapSection = toolkit
                .createSection(body, Section.DESCRIPTION | Section.TITLE_BAR | Section.TWISTIE | Section.EXPANDED);
        mapSection.addExpansionListener(new ExpansionAdapter() {
            @Override
            public void expansionStateChanged(final ExpansionEvent e) {
                form.reflow(true);
            }
        });
        mapSection.setExpanded(false);

        td = new TableWrapData(TableWrapData.FILL_GRAB);
        td.colspan = 1;
        td.grabHorizontal = true;
        td.grabVertical = true;

        mapSection.setLayoutData(td);
        mapSection.setText(Messages.SingleActivityViewPart_16);
        mapSection.setDescription(Messages.SingleActivityViewPart_17);

        final Composite client = toolkit.createComposite(mapSection);
        // client.setBackground(getViewSite().getWorkbenchWindow().getShell().getDisplay().getSystemColor(SWT.COLOR_CYAN));
        final TableWrapLayout layout = new TableWrapLayout();
        layout.numColumns = 1;
        layout.topMargin = -60;
        layout.bottomMargin = 5;
        client.setLayout(layout);

        final String convertTrackpoints = MapConverter.convertTrackpoints(selected);
        final String firstPointToPan = MapConverter.getFirstPointToPan(convertTrackpoints);
        final MapViewer mapViewer = new MapViewer(client, SWT.NONE, convertTrackpoints, firstPointToPan);
        td = new TableWrapData(TableWrapData.FILL_GRAB);
        td.heightHint = 550;
        td.grabHorizontal = true;
        mapViewer.getComposite().setLayoutData(td);

        mapSection.setClient(client);
    }

    private void addOverviewSection(final Composite body) {
        final Section overviewSection = toolkit.createSection(body, Section.DESCRIPTION | Section.TITLE_BAR | Section.TWISTIE
                | Section.EXPANDED);
        overviewSection.addExpansionListener(new ExpansionAdapter() {
            @Override
            public void expansionStateChanged(final ExpansionEvent e) {
                form.reflow(true);
            }
        });
        td = new TableWrapData();
        td.colspan = 1;
        overviewSection.setLayoutData(td);
        overviewSection.setText(Messages.SingleActivityViewPart_1);
        overviewSection.setDescription(Messages.SingleActivityViewPart_2);

        final Composite overViewComposite = toolkit.createComposite(overviewSection);
        final GridLayout layoutClient = new GridLayout(3, false);
        overViewComposite.setLayout(layoutClient);

        addLabelAndValue(overViewComposite, Messages.SingleActivityViewPart_3, simpleTraining.getZeit(), Units.HOUR_MINUTE_SEC);
        addLabelAndValue(overViewComposite, Messages.SingleActivityViewPart_4, simpleTraining.getLaengeInKilometer(), Units.KM);
        final int avgHeartRate = simpleTraining.getAvgHeartRate();
        if (avgHeartRate > 0) {
            addLabelAndValue(overViewComposite, Messages.SingleActivityViewPart_5, String.valueOf(avgHeartRate), Units.BEATS_PER_MINUTE);
        } else {
            addLabelAndValue(overViewComposite, Messages.SingleActivityViewPart_5, "-", Units.BEATS_PER_MINUTE); //$NON-NLS-1$
        }
        addLabelAndValue(overViewComposite, Messages.SingleActivityViewPart_6, simpleTraining.getMaxHeartBeat(), Units.BEATS_PER_MINUTE);
        addLabelAndValue(overViewComposite, Messages.SingleActivityViewPart_7, simpleTraining.getPace(), Units.PACE);
        addLabelAndValue(overViewComposite, Messages.SingleActivityViewPart_8, simpleTraining.getMaxSpeed(), Units.PACE);
        overviewSection.setClient(overViewComposite);
    }

    private void addPoint(final ChartType type, final XYSeries serie, final TrackpointT point, final TrackpointT previousPoint) {
        switch (type) {
        case HEART_DISTANCE: {
            final Double m = point.getDistanceMeters();
            final HeartRateInBeatsPerMinuteT bpm = point.getHeartRateBpm();
            if (m != null && bpm != null) {
                serie.add(point.getDistanceMeters().doubleValue(), point.getHeartRateBpm().getValue());
            }
            break;
        }
        case ALTITUDE_DISTANCE: {
            final Double m = point.getDistanceMeters();
            final Double alti = point.getAltitudeMeters();
            if (m != null && alti != null) {
                serie.add(point.getDistanceMeters().doubleValue(), alti.doubleValue());
            }
            break;
        }
        case SPEED_DISTANCE: {
            if (previousPoint != null && validateTrackPointVorSpeed(point) && validateTrackPointVorSpeed(previousPoint)) {
                final double d2 = point.getDistanceMeters();
                final double d1 = previousPoint.getDistanceMeters();
                final double t2 = point.getTime().toGregorianCalendar().getTimeInMillis() / 1000;
                final double t1 = previousPoint.getTime().toGregorianCalendar().getTimeInMillis() / 1000;
                final double pace = SpeedCalculator.calculatePace(d1, d2, t1, t2);
                if (0 < pace && pace < 10) {
                    serie.add(point.getDistanceMeters().doubleValue(), pace);
                }
            }
        }
        }
    }

    private void addSpeedSection(final Composite body) {
        final Section speedSection = toolkit.createSection(body, Section.DESCRIPTION | Section.TITLE_BAR | Section.TWISTIE
                | Section.EXPANDED);
        speedSection.addExpansionListener(new ExpansionAdapter() {
            @Override
            public void expansionStateChanged(final ExpansionEvent e) {
                form.reflow(true);
            }
        });
        speedSection.setExpanded(false);
        td = new TableWrapData(TableWrapData.FILL_GRAB);
        td.colspan = 1;
        td.grabHorizontal = true;
        td.grabVertical = true;
        speedSection.setLayoutData(td);
        speedSection.setText(Messages.SingleActivityViewPart_11);
        speedSection.setDescription(Messages.SingleActivityViewPart_12);
        //
        final Composite client = toolkit.createComposite(speedSection);

        final TableWrapLayout layout = new TableWrapLayout();
        layout.numColumns = 2;
        layout.makeColumnsEqualWidth = false;

        client.setLayout(layout);

        final Label dauerLabel = toolkit.createLabel(client, ""); //$NON-NLS-1$
        td = new TableWrapData();
        dauerLabel.setLayoutData(td);

        final JFreeChart chart = createChart(createDataset(ChartType.SPEED_DISTANCE), ChartType.SPEED_DISTANCE);
        final ChartComposite chartComposite = new ChartComposite(client, SWT.NONE, chart, true);
        td = new TableWrapData(TableWrapData.FILL_GRAB);
        td.heightHint = 400;
        chartComposite.setLayoutData(td);

        speedSection.setClient(client);
    }

    private JFreeChart createChart(final XYDataset dataset, final ChartType type) {

        final JFreeChart chart = ChartFactory.createXYLineChart(type.getTitel(), // chart
                // title
                type.getxAchse(), // x axis label
                type.getyAchse(), // y axis label
                dataset, // data
                PlotOrientation.VERTICAL, false, // include legend
                true, // tooltips
                false // urls
                );

        // NOW DO SOME OPTIONAL CUSTOMISATION OF THE CHART...
        chart.setBackgroundPaint(Color.white);
        // get a reference to the plot for further customisation...
        final XYPlot plot = chart.getXYPlot();
        plot.setBackgroundPaint(Color.white);
        plot.setDomainGridlinePaint(Color.lightGray);
        plot.setRangeGridlinePaint(Color.lightGray);

        final XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
        renderer.setSeriesLinesVisible(0, true);
        renderer.setSeriesShapesVisible(0, false);
        setLowerAndUpperBounds(plot);
        if (ChartType.HEART_DISTANCE.equals(type)) {
            addIntervallMarker(plot);
        }
        if (ChartType.SPEED_DISTANCE.equals(type)) {
            setLowerAndUpperBounds(plot, 2.5, 10);
        }
        plot.setRenderer(renderer);

        // change the auto tick unit selection to integer units only...
        final NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());

        return chart;
    }

    private XYDataset createDataset(final ChartType type) {
        final List<ActivityLapT> laps = selected.getLap();
        final XYSeries series1 = new XYSeries(Messages.SingleActivityViewPart_18);
        for (final ActivityLapT activityLapT : laps) {
            final List<TrackT> tracks = activityLapT.getTrack();
            for (final TrackT track : tracks) {
                final List<TrackpointT> trackpoints = track.getTrackpoint();
                TrackpointT previousTrackPoint = null;
                for (final TrackpointT trackpoint : trackpoints) {
                    addPoint(type, series1, trackpoint, previousTrackPoint);
                    previousTrackPoint = trackpoint;
                }
            }
        }

        final XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(series1);

        return dataset;
    }

    @Override
    public void createPartControl(final Composite parent) {
        LOGGER.debug("create single activity view"); //$NON-NLS-1$
        toolkit = new FormToolkit(parent.getDisplay());
        form = toolkit.createScrolledForm(parent);

        final TableWrapLayout layout = new TableWrapLayout();
        layout.numColumns = 1;
        layout.makeColumnsEqualWidth = false;

        final Composite body = form.getBody();
        body.setLayout(layout);

        td = new TableWrapData(TableWrapData.FILL_GRAB);
        body.setLayoutData(td);
        form.setText(Messages.SingleActivityViewPart_0 + simpleTraining.getDatum());

        addOverviewSection(body);
        addMapSection(body);
        addHeartSection(body);
        addSpeedSection(body);
        addAltitudeSection(body);

    }

    @Override
    public void dispose() {
        toolkit.dispose();
        super.dispose();
    }

    @Override
    public void setFocus() {
        form.setFocus();
    }

    private void setLowerAndUpperBounds(final XYPlot plot) {
        final ValueAxis axis = plot.getRangeAxis();
        final Range dataRange = plot.getDataRange(axis);
        if (dataRange != null) {
            axis.setLowerBound(dataRange.getLowerBound() * 0.95);
            axis.setUpperBound(dataRange.getUpperBound() * 1.05);
        }
        plot.setRangeAxis(axis);
    }

    private void setLowerAndUpperBounds(final XYPlot plot, final double min, final double max) {
        final ValueAxis axis = plot.getRangeAxis();
        axis.setLowerBound(min);
        axis.setUpperBound(max);
        plot.setRangeAxis(axis);
    }

    private boolean validateTrackPointVorSpeed(final TrackpointT point) {
        return point.getDistanceMeters() != null && point.getTime() != null;
    }

}
