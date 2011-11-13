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
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.experimental.chart.swt.ChartComposite;

import ch.iseli.sportanalyzer.client.cache.TrainingCenterDataCache;
import ch.iseli.sportanalyzer.client.model.TrainingOverview;
import ch.iseli.sportanalyzer.client.model.Units;
import ch.iseli.sportanalyzer.tcx.ActivityLapT;
import ch.iseli.sportanalyzer.tcx.ActivityT;
import ch.iseli.sportanalyzer.tcx.HeartRateInBeatsPerMinuteT;
import ch.iseli.sportanalyzer.tcx.TrackT;
import ch.iseli.sportanalyzer.tcx.TrackpointT;
import ch.iseli.sportanalyzer.tcx.TrainingCenterDatabaseT;

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
    public void createPartControl(Composite parent) {

        toolkit = new FormToolkit(parent.getDisplay());
        form = toolkit.createScrolledForm(parent);
        // form.setSize(1000, 2000);
        // gridlayout definieren

        TableWrapLayout layout = new TableWrapLayout();
        layout.numColumns = 1;
        layout.makeColumnsEqualWidth = false;

        Composite body = form.getBody();
        body.setLayout(layout);

        td = new TableWrapData(TableWrapData.FILL_GRAB);
        body.setLayoutData(td);
        form.setText("Lauf vom " + datumZeit);

        addOverviewSection(body);

        addHeartSection(body);
        addAltitudeSection(body);

    }

    private void addOverviewSection(Composite body) {
        Section overviewSection = toolkit.createSection(body, Section.DESCRIPTION | Section.TITLE_BAR | Section.TWISTIE | Section.EXPANDED);
        overviewSection.addExpansionListener(new ExpansionAdapter() {
            @Override
            public void expansionStateChanged(ExpansionEvent e) {
                form.reflow(true);
            }
        });
        td = new TableWrapData();
        td.colspan = 1;
        overviewSection.setLayoutData(td);
        overviewSection.setText("Übersicht");
        overviewSection.setDescription("Eine erste Übersicht auf den Lauf");

        Composite overViewComposite = toolkit.createComposite(overviewSection);
        GridLayout layoutClient = new GridLayout(3, false);
        overViewComposite.setLayout(layoutClient);

        addLabelAndValue(overViewComposite, "Dauer", trainingOverview.getDauer(), Units.HOUR_MINUTE_SEC);
        addLabelAndValue(overViewComposite, "Distanz", trainingOverview.getLaengeInKilometer(), Units.KM);
        addLabelAndValue(overViewComposite, "durchschnittliche Herzfrequenz", String.valueOf(trainingOverview.getAverageHeartBeat()), Units.BEATS_PER_MINUTE);
        addLabelAndValue(overViewComposite, "maximale Herzfrequenz", String.valueOf(trainingOverview.getMaxHeartBeat()), Units.BEATS_PER_MINUTE);
        addLabelAndValue(overViewComposite, "durchschnittliche Geschwindigkeit", String.valueOf(trainingOverview.getPace()), Units.PACE);
        addLabelAndValue(overViewComposite, "maximal Geschwindigkeit", String.valueOf(trainingOverview.getMaxSpeed()), Units.PACE);
        overviewSection.setClient(overViewComposite);
    }

    private void addAltitudeSection(Composite body) {
        Section altitude = toolkit.createSection(body, Section.DESCRIPTION | Section.TITLE_BAR | Section.TWISTIE | Section.EXPANDED);
        altitude.setExpanded(false);
        altitude.addExpansionListener(new ExpansionAdapter() {
            @Override
            public void expansionStateChanged(ExpansionEvent e) {
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

        Composite client = toolkit.createComposite(altitude);

        TableWrapLayout layout = new TableWrapLayout();
        layout.numColumns = 2;
        layout.makeColumnsEqualWidth = false;

        client.setLayout(layout);

        Label dauerLabel = toolkit.createLabel(client, "blabla 1");
        td = new TableWrapData();
        dauerLabel.setLayoutData(td);

        JFreeChart chart = createChart(createDataset(ChartType.ALTITUDE_DISTANCE), ChartType.ALTITUDE_DISTANCE);
        ChartComposite chartComposite = new ChartComposite(client, SWT.NONE, chart, true);
        td = new TableWrapData(TableWrapData.FILL_GRAB);
        td.heightHint = 400;
        chartComposite.setLayoutData(td);

        altitude.setClient(client);
    }

    private void addHeartSection(Composite body) {
        Section heartSection = toolkit.createSection(body, Section.DESCRIPTION | Section.TITLE_BAR | Section.TWISTIE | Section.EXPANDED);
        heartSection.addExpansionListener(new ExpansionAdapter() {
            @Override
            public void expansionStateChanged(ExpansionEvent e) {
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
        Composite client = toolkit.createComposite(heartSection);

        TableWrapLayout layout = new TableWrapLayout();
        layout.numColumns = 2;
        layout.makeColumnsEqualWidth = false;

        client.setLayout(layout);

        Label dauerLabel = toolkit.createLabel(client, "blabla 2");
        td = new TableWrapData();
        dauerLabel.setLayoutData(td);

        JFreeChart chart = createChart(createDataset(ChartType.HEART_DISTANCE), ChartType.HEART_DISTANCE);
        ChartComposite chartComposite = new ChartComposite(client, SWT.NONE, chart, true);
        td = new TableWrapData(TableWrapData.FILL_GRAB);
        td.heightHint = 400;
        chartComposite.setLayoutData(td);

        heartSection.setClient(client);
    }

    private void addLabelAndValue(Composite parent, String label, String value, Units unit) {
        // Label
        Label dauerLabel = toolkit.createLabel(parent, label + ": ");
        GridData gd = new GridData();
        gd.verticalIndent = 4;
        dauerLabel.setLayoutData(gd);

        // value
        Label dauer = toolkit.createLabel(parent, value);
        gd = new GridData();
        gd.horizontalAlignment = SWT.RIGHT;
        gd.horizontalIndent = 10;
        gd.verticalIndent = 4;
        dauer.setLayoutData(gd);

        // einheit
        Label einheit = toolkit.createLabel(parent, unit.getName());
        gd = new GridData();
        gd.horizontalAlignment = SWT.LEFT;
        gd.horizontalIndent = 10;
        gd.verticalIndent = 4;
        einheit.setLayoutData(gd);
    }

    private JFreeChart createChart(XYDataset dataset, ChartType type) {

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

        plot.setRenderer(renderer);

        // change the auto tick unit selection to integer units only...
        final NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());

        return chart;
    }

    private XYDataset createDataset(ChartType type) {

        TrainingCenterDatabaseT selected = cache.getSelected().getTrainingCenterDatabase();
        ActivityT activityT = selected.getActivities().getActivity().get(0);
        List<ActivityLapT> laps = activityT.getLap();
        final XYSeries series1 = new XYSeries("");
        for (ActivityLapT activityLapT : laps) {
            List<TrackT> tracks = activityLapT.getTrack();
            for (TrackT track : tracks) {
                List<TrackpointT> trackpoints = track.getTrackpoint();
                for (TrackpointT trackpoint : trackpoints) {
                    addPoint(type, series1, trackpoint);
                }
            }
        }

        final XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(series1);

        return dataset;
    }

    private void addPoint(final ChartType type, final XYSeries series1, TrackpointT trackpoint) {
        switch (type) {
        case HEART_DISTANCE: {
            Double m = trackpoint.getDistanceMeters();
            HeartRateInBeatsPerMinuteT bpm = trackpoint.getHeartRateBpm();
            if (m != null && bpm != null) {
                series1.add(trackpoint.getDistanceMeters().doubleValue(), trackpoint.getHeartRateBpm().getValue());
            }
            break;
        }
        case ALTITUDE_DISTANCE: {
            Double m = trackpoint.getDistanceMeters();
            Double alti = trackpoint.getAltitudeMeters();
            if (m != null && alti != null) {
                series1.add(trackpoint.getDistanceMeters().doubleValue(), alti.doubleValue());
            }
            break;
        }
        }

    }

    @Override
    public void setFocus() {
        form.setFocus();
    }

    /**
     * Disposes the toolkit
     */
    @Override
    public void dispose() {
        toolkit.dispose();
        super.dispose();
    }

}
