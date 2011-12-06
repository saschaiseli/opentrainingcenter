package ch.iseli.sportanalyzer.client.views.overview;

import java.awt.Color;
import java.util.List;

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
import org.jfree.chart.plot.IntervalMarker;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.experimental.chart.swt.ChartComposite;
import org.jfree.ui.Layer;
import org.jfree.ui.RectangleAnchor;
import org.jfree.ui.TextAnchor;

import ch.iseli.sportanalyzer.client.cache.TrainingCenterDataCache;
import ch.iseli.sportanalyzer.client.cache.TrainingCenterRecord;
import ch.iseli.sportanalyzer.client.helper.SpeedCalculator;
import ch.iseli.sportanalyzer.client.model.TrainingOverview;
import ch.iseli.sportanalyzer.client.model.Units;
import ch.iseli.sportanalyzer.tcx.ActivityLapT;
import ch.iseli.sportanalyzer.tcx.ActivityT;
import ch.iseli.sportanalyzer.tcx.HeartRateInBeatsPerMinuteT;
import ch.iseli.sportanalyzer.tcx.TrackT;
import ch.iseli.sportanalyzer.tcx.TrackpointT;

public class SingleActivityViewPart extends ViewPart {

    public static final String ID = "ch.iseli.sportanalyzer.client.views.singlerun";
    private static final Logger logger = Logger.getLogger(SingleActivityViewPart.class);
    private final TrainingCenterDataCache cache = TrainingCenterDataCache.getInstance();
    private final String datumZeit;
    private final TrainingOverview trainingOverview;
    private FormToolkit toolkit;
    private ScrolledForm form;
    private TableWrapData td;

    public SingleActivityViewPart() {
        trainingOverview = cache.getSelectedOverview();
        datumZeit = trainingOverview.getDatum();
        setPartName(datumZeit);
    }

    @Override
    public void createPartControl(final Composite parent) {
        logger.debug("create single activity view");
        toolkit = new FormToolkit(parent.getDisplay());
        form = toolkit.createScrolledForm(parent);
        // form.setSize(1000, 2000);
        // gridlayout definieren

        final TableWrapLayout layout = new TableWrapLayout();
        layout.numColumns = 1;
        layout.makeColumnsEqualWidth = false;

        final Composite body = form.getBody();
        body.setLayout(layout);

        td = new TableWrapData(TableWrapData.FILL_GRAB);
        body.setLayoutData(td);
        form.setText("Lauf vom " + datumZeit);

        addOverviewSection(body);
        addMapSection(body);
        addHeartSection(body);
        addSpeedSection(body);
        addAltitudeSection(body);

    }

    private void addOverviewSection(final Composite body) {
        final Section overviewSection = toolkit.createSection(body, Section.DESCRIPTION | Section.TITLE_BAR | Section.TWISTIE | Section.EXPANDED);
        overviewSection.addExpansionListener(new ExpansionAdapter() {
            @Override
            public void expansionStateChanged(final ExpansionEvent e) {
                form.reflow(true);
            }
        });
        td = new TableWrapData();
        td.colspan = 1;
        overviewSection.setLayoutData(td);
        overviewSection.setText("Übersicht");
        overviewSection.setDescription("Eine erste Übersicht auf den Lauf");

        final Composite overViewComposite = toolkit.createComposite(overviewSection);
        final GridLayout layoutClient = new GridLayout(3, false);
        overViewComposite.setLayout(layoutClient);

        addLabelAndValue(overViewComposite, "Dauer", trainingOverview.getDauer(), Units.HOUR_MINUTE_SEC);
        addLabelAndValue(overViewComposite, "Distanz", trainingOverview.getLaengeInKilometer(), Units.KM);
        addLabelAndValue(overViewComposite, "durchschnittliche Herzfrequenz", String.valueOf(trainingOverview.getAverageHeartBeat()), Units.BEATS_PER_MINUTE);
        addLabelAndValue(overViewComposite, "maximale Herzfrequenz", String.valueOf(trainingOverview.getMaxHeartBeat()), Units.BEATS_PER_MINUTE);
        addLabelAndValue(overViewComposite, "durchschnittliche Geschwindigkeit", String.valueOf(trainingOverview.getPace()), Units.PACE);
        addLabelAndValue(overViewComposite, "maximal Geschwindigkeit", String.valueOf(trainingOverview.getMaxSpeed()), Units.PACE);
        overviewSection.setClient(overViewComposite);
    }

    /**
     * Herz frequenz
     */
    private void addHeartSection(final Composite body) {
        final Section heartSection = toolkit.createSection(body, Section.DESCRIPTION | Section.TITLE_BAR | Section.TWISTIE | Section.EXPANDED);
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
        heartSection.setText("Herz");
        heartSection.setDescription("Herzfrequenz in Funktion der Distanz");
        //
        final Composite client = toolkit.createComposite(heartSection);

        final TableWrapLayout layout = new TableWrapLayout();
        layout.numColumns = 2;
        layout.makeColumnsEqualWidth = false;

        client.setLayout(layout);

        final Label dauerLabel = toolkit.createLabel(client, "blabla 2");
        td = new TableWrapData();
        dauerLabel.setLayoutData(td);

        final JFreeChart chart = createChart(createDataset(ChartType.HEART_DISTANCE), ChartType.HEART_DISTANCE);
        final ChartComposite chartComposite = new ChartComposite(client, SWT.NONE, chart, true);
        td = new TableWrapData(TableWrapData.FILL_GRAB);
        td.heightHint = 400;
        chartComposite.setLayoutData(td);

        heartSection.setClient(client);
    }

    private void addSpeedSection(final Composite body) {
        final Section speedSection = toolkit.createSection(body, Section.DESCRIPTION | Section.TITLE_BAR | Section.TWISTIE | Section.EXPANDED);
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
        speedSection.setText("Geschwindigkeit");
        speedSection.setDescription("Pace [min/km]");
        //
        final Composite client = toolkit.createComposite(speedSection);

        final TableWrapLayout layout = new TableWrapLayout();
        layout.numColumns = 2;
        layout.makeColumnsEqualWidth = false;

        client.setLayout(layout);

        final Label dauerLabel = toolkit.createLabel(client, "");
        td = new TableWrapData();
        dauerLabel.setLayoutData(td);

        final JFreeChart chart = createChart(createDataset(ChartType.SPEED_DISTANCE), ChartType.SPEED_DISTANCE);
        final ChartComposite chartComposite = new ChartComposite(client, SWT.NONE, chart, true);
        td = new TableWrapData(TableWrapData.FILL_GRAB);
        td.heightHint = 400;
        chartComposite.setLayoutData(td);

        speedSection.setClient(client);
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
        altitude.setText("Höhe");
        altitude.setDescription("Verlauf der Höhe");

        final Composite client = toolkit.createComposite(altitude);

        final TableWrapLayout layout = new TableWrapLayout();
        layout.numColumns = 2;
        layout.makeColumnsEqualWidth = false;
        client.setLayout(layout);

        final Label dauerLabel = toolkit.createLabel(client, "blabla 1");
        td = new TableWrapData();
        dauerLabel.setLayoutData(td);

        final JFreeChart chart = createChart(createDataset(ChartType.ALTITUDE_DISTANCE), ChartType.ALTITUDE_DISTANCE);
        final ChartComposite chartComposite = new ChartComposite(client, SWT.NONE, chart, true);
        td = new TableWrapData(TableWrapData.FILL_GRAB);
        td.heightHint = 400;
        chartComposite.setLayoutData(td);

        altitude.setClient(client);
    }

    private void addMapSection(final Composite body) {
        final Section mapSection = toolkit.createSection(body, Section.DESCRIPTION | Section.TITLE_BAR | Section.TWISTIE | Section.EXPANDED);
        mapSection.addExpansionListener(new ExpansionAdapter() {
            @Override
            public void expansionStateChanged(final ExpansionEvent e) {
                form.reflow(true);
            }
        });
        mapSection.setExpanded(true);

        td = new TableWrapData(TableWrapData.FILL_GRAB);
        td.colspan = 1;
        td.grabHorizontal = true;
        td.grabVertical = true;

        mapSection.setLayoutData(td);
        mapSection.setText("Karte");
        mapSection.setDescription("Lauf auf der Karte");

        final Composite client = toolkit.createComposite(mapSection);
        // client.setBackground(getViewSite().getWorkbenchWindow().getShell().getDisplay().getSystemColor(SWT.COLOR_CYAN));
        final TableWrapLayout layout = new TableWrapLayout();
        layout.numColumns = 1;
        layout.topMargin = -60;
        layout.bottomMargin = 5;
        client.setLayout(layout);

        final TrainingCenterRecord selected = TrainingCenterDataCache.getInstance().getSelected();
        final String convertTrackpoints = MapConverter.convertTrackpoints(selected);
        final String firstPointToPan = MapConverter.getFirstPointToPan(convertTrackpoints);
        final MapViewer mapViewer = new MapViewer(client, SWT.NONE, convertTrackpoints, firstPointToPan);
        td = new TableWrapData(TableWrapData.FILL_GRAB);
        td.heightHint = 550;
        td.grabHorizontal = true;
        mapViewer.getComposite().setLayoutData(td);

        mapSection.setClient(client);
    }

    private void addLabelAndValue(final Composite parent, final String label, final String value, final Units unit) {
        // Label
        final Label dauerLabel = toolkit.createLabel(parent, label + ": ");
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

    private JFreeChart createChart(final XYDataset dataset, final ChartType type) {

        final JFreeChart chart = ChartFactory.createXYLineChart(type.getTitel(), // chart title
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
        if (ChartType.HEART_DISTANCE.equals(type)) {
            final IntervalMarker target = new IntervalMarker(100, 150);
            target.setLabel("Anaerober Bereich");
            // target.setLabelFont(new Font("SansSerif", Font.ITALIC, 11));
            target.setLabelAnchor(RectangleAnchor.LEFT);
            target.setLabelTextAnchor(TextAnchor.CENTER_LEFT);
            target.setPaint(new Color(222, 222, 255, 128));
            plot.addRangeMarker(target, Layer.BACKGROUND);
        }
        plot.setRenderer(renderer);

        // change the auto tick unit selection to integer units only...
        final NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());

        return chart;
    }

    private XYDataset createDataset(final ChartType type) {

        final TrainingCenterRecord selected = cache.getSelected();
        final ActivityT activityT = selected.getTrainingCenterDatabase().getActivities().getActivity().get(0);
        final List<ActivityLapT> laps = activityT.getLap();
        final XYSeries series1 = new XYSeries("");
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
                final double vMeterProSekunde = SpeedCalculator.calculatePace(d1, d2, t1, t2);
                logger.info("vMeterProSekunde: " + vMeterProSekunde);
                if (vMeterProSekunde > 0) {
                    serie.add(point.getDistanceMeters().doubleValue(), vMeterProSekunde);
                }
            }
        }
        }
    }

    private boolean validateTrackPointVorSpeed(final TrackpointT point) {
        return point.getDistanceMeters() != null && point.getTime() != null;
    }

    @Override
    public void setFocus() {
        form.setFocus();
    }

    @Override
    public void dispose() {
        toolkit.dispose();
        super.dispose();
    }

}
