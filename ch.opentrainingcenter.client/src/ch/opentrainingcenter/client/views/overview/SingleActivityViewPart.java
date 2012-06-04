package ch.opentrainingcenter.client.views.overview;

import java.util.Collection;
import java.util.Date;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseTrackListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.events.ExpansionAdapter;
import org.eclipse.ui.forms.events.ExpansionEvent;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.forms.widgets.TableWrapData;
import org.eclipse.ui.forms.widgets.TableWrapLayout;
import org.eclipse.ui.part.ViewPart;
import org.jfree.chart.JFreeChart;
import org.jfree.experimental.chart.swt.ChartComposite;
import org.jfree.util.Log;

import ch.opentrainingcenter.client.Activator;
import ch.opentrainingcenter.client.Messages;
import ch.opentrainingcenter.client.cache.Cache;
import ch.opentrainingcenter.client.cache.IRecordListener;
import ch.opentrainingcenter.client.cache.impl.TrainingCenterDataCache;
import ch.opentrainingcenter.client.charts.ChartCreator;
import ch.opentrainingcenter.client.charts.DataSetCreator;
import ch.opentrainingcenter.client.charts.internal.ChartCreatorImpl;
import ch.opentrainingcenter.client.charts.internal.DataSetCreatorImpl;
import ch.opentrainingcenter.client.helper.TimeHelper;
import ch.opentrainingcenter.client.model.ISimpleTraining;
import ch.opentrainingcenter.client.model.TrainingOverviewFactory;
import ch.opentrainingcenter.client.model.Units;
import ch.opentrainingcenter.client.model.Wetter;
import ch.opentrainingcenter.client.views.ApplicationContext;
import ch.opentrainingcenter.db.DatabaseAccessFactory;
import ch.opentrainingcenter.db.IDatabaseAccess;
import ch.opentrainingcenter.tcx.ActivityT;
import ch.opentrainingcenter.tcx.ExtensionsT;
import ch.opentrainingcenter.transfer.ActivityExtension;
import ch.opentrainingcenter.transfer.CommonTransferFactory;
import ch.opentrainingcenter.transfer.IImported;
import ch.opentrainingcenter.transfer.ITraining;
import ch.opentrainingcenter.transfer.IWeather;

public class SingleActivityViewPart extends ViewPart {

    public static final String ID = "ch.opentrainingcenter.client.views.singlerun"; //$NON-NLS-1$
    private static final Logger LOGGER = Logger.getLogger(SingleActivityViewPart.class);
    private final Cache cache = TrainingCenterDataCache.getInstance();
    private final IDatabaseAccess databaseAccess = DatabaseAccessFactory.getDatabaseAccess();
    private final ISimpleTraining simpleTraining;
    private final ActivityT activity;
    private FormToolkit toolkit;
    private ScrolledForm form;
    private TableWrapData td;
    private final DataSetCreator dataSetCreator;
    private final ChartCreator chartCreator;
    private IRecordListener listener;

    public SingleActivityViewPart() {
        final Date selectedId = ApplicationContext.getApplicationContext().getSelectedId();
        activity = cache.get(selectedId);
        simpleTraining = TrainingOverviewFactory.creatSimpleTraining(activity);
        dataSetCreator = new DataSetCreatorImpl(activity);
        chartCreator = new ChartCreatorImpl(cache, Activator.getDefault().getPreferenceStore());
        setPartName(simpleTraining.getFormattedDate());
    }

    @Override
    public void createPartControl(final Composite parent) {
        LOGGER.debug("create single activity view"); //$NON-NLS-1$
        toolkit = new FormToolkit(parent.getDisplay());
        form = toolkit.createScrolledForm(parent);
        form.setText(Messages.SingleActivityViewPart0 + TimeHelper.convertDateToString(simpleTraining.getDatum(), true));
        final Composite body = form.getBody();

        final TableWrapLayout layout = new TableWrapLayout();
        layout.numColumns = 2;
        body.setLayout(layout);

        addOverviewSection(body);
        addNoteSection(body);

        addMapSection(body);
        addHeartSection(body);
        addSpeedSection(body);
        addAltitudeSection(body);
    }

    @Override
    public void dispose() {
        cache.removeListener(listener);
        ApplicationContext.getApplicationContext().setSelectedId(null);
        super.dispose();
        toolkit.dispose();
    }

    @Override
    public void setFocus() {
        form.setFocus();
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
        td = new TableWrapData(TableWrapData.FILL_GRAB);
        td.colspan = 1;
        overviewSection.setLayoutData(td);
        overviewSection.setText(Messages.SingleActivityViewPart1);
        overviewSection.setDescription(Messages.SingleActivityViewPart2);

        final Composite overViewComposite = toolkit.createComposite(overviewSection);
        final GridLayout layoutClient = new GridLayout(3, false);
        overViewComposite.setLayout(layoutClient);

        addLabelAndValue(overViewComposite, Messages.SingleActivityViewPart3, simpleTraining.getZeit(), Units.HOUR_MINUTE_SEC);
        addLabelAndValue(overViewComposite, Messages.SingleActivityViewPart4, simpleTraining.getLaengeInKilometer(), Units.KM);
        final int avgHeartRate = simpleTraining.getAvgHeartRate();
        if (avgHeartRate > 0) {
            addLabelAndValue(overViewComposite, Messages.SingleActivityViewPart5, String.valueOf(avgHeartRate), Units.BEATS_PER_MINUTE);
        } else {
            addLabelAndValue(overViewComposite, Messages.SingleActivityViewPart5, "-", Units.BEATS_PER_MINUTE); //$NON-NLS-1$
        }
        addLabelAndValue(overViewComposite, Messages.SingleActivityViewPart6, simpleTraining.getMaxHeartBeat(), Units.BEATS_PER_MINUTE);
        addLabelAndValue(overViewComposite, Messages.SingleActivityViewPart7, simpleTraining.getPace(), Units.PACE);
        addLabelAndValue(overViewComposite, Messages.SingleActivityViewPart8, simpleTraining.getMaxSpeed(), Units.PACE);
        overviewSection.setClient(overViewComposite);
    }

    private void addNoteSection(final Composite body) {
        final Section section = toolkit.createSection(body, Section.DESCRIPTION | Section.TITLE_BAR | Section.TWISTIE | Section.EXPANDED);
        section.addExpansionListener(new ExpansionAdapter() {
            @Override
            public void expansionStateChanged(final ExpansionEvent e) {
                form.reflow(true);
            }
        });
        section.setRedraw(true);
        section.setText("Bemerkungen");
        section.setDescription("Beschreibung von Zustand der Gesundheit, wie der Lauf erlebt wurde. Usw..");

        final Composite container = toolkit.createComposite(section);
        final GridLayout layout = new GridLayout(2, false);
        layout.verticalSpacing = 2;
        layout.horizontalSpacing = 10;
        container.setLayout(layout);

        final Label label = toolkit.createLabel(container, "");
        label.setText("Beschreibung : ");
        // label.setLayoutData(gd);

        final Text note = toolkit.createText(container, "", SWT.V_SCROLL | SWT.MULTI | SWT.BORDER);
        final GridData noteGd = new GridData();
        noteGd.grabExcessHorizontalSpace = true;
        noteGd.grabExcessVerticalSpace = true;
        noteGd.minimumHeight = 80;
        noteGd.minimumWidth = 400;
        note.setLayoutData(noteGd);
        final String notiz = simpleTraining.getNote();
        if (notiz != null) {
            note.setText(notiz);
        }

        note.addMouseTrackListener(new MouseTrackListener() {

            @Override
            public void mouseHover(final MouseEvent e) {

            }

            @Override
            public void mouseExit(final MouseEvent e) {
                final String text = note.getText();
                safeNote(text);
            }

            @Override
            public void mouseEnter(final MouseEvent e) {

            }
        });

        note.addFocusListener(new FocusListener() {

            @Override
            public void focusLost(final FocusEvent e) {
                // speichern
                final String text = note.getText();
                safeNote(text);
            }

            @Override
            public void focusGained(final FocusEvent e) {

            }
        });

        final Label labelWetter = toolkit.createLabel(container, "");
        labelWetter.setText("Wetter : ");
        // gd.minimumWidth = 10;
        // labelWetter.setLayoutData(gd);

        final Combo weatherCombo = new Combo(container, SWT.READ_ONLY);
        weatherCombo.setBounds(50, 50, 150, 65);

        weatherCombo.setItems(Wetter.getItems());
        weatherCombo.select(simpleTraining.getWetter().getIndex());
        weatherCombo.addSelectionListener(new SelectionListener() {

            @Override
            public void widgetSelected(final SelectionEvent e) {
                safeWeather(weatherCombo.getSelectionIndex());
            }

            @Override
            public void widgetDefaultSelected(final SelectionEvent e) {
            }
        });

        listener = new IRecordListener() {

            @Override
            public void recordChanged(final Collection<ActivityT> entry) {
                if (entry != null) {
                    final ActivityT act = entry.iterator().next();
                    if (act.getId().toGregorianCalendar().getTime().equals(simpleTraining.getDatum()) && act.getExtensions() != null) {
                        // nur wenn es dieser record ist!
                        final ExtensionsT extensions = act.getExtensions();
                        final Object any = extensions.getAny().get(0);
                        if (any != null) {
                            final ActivityExtension ae = (ActivityExtension) any;
                            note.setText(ae.getNote());
                        }

                    }
                }
                section.update();
            }

            @Override
            public void deleteRecord(final Collection<ActivityT> entry) {
            }
        };
        cache.addListener(listener);
        section.setClient(container);
    }

    private void safeWeather(final int index) {
        final Wetter wetter = simpleTraining.getWetter();
        if (index != wetter.getIndex()) {
            // änderungen
            final IImported record = databaseAccess.getImportedRecord(activity.getId().toGregorianCalendar().getTime());
            if (record != null) {
                final Wetter currentWeather = Wetter.getRunType(index);
                final ITraining training = record.getTraining();
                training.setWeather(CommonTransferFactory.createWeather(currentWeather.getIndex()));
                Log.info("Das Wetter wurde geändert: " + training.getWeather());
                simpleTraining.setWetter(wetter);
            }
            update(record);
        }
    }

    private void safeNote(final String text) {
        if (!text.equals(simpleTraining.getNote())) {
            // änderungen
            final IImported record = databaseAccess.getImportedRecord(activity.getId().toGregorianCalendar().getTime());
            if (record != null) {
                record.getTraining().setNote(text);
                simpleTraining.setNote(text);
            }
            update(record);
        }
    }

    private void update(final IImported record) {
        databaseAccess.updateRecord(record);
        final ITraining training = record.getTraining();
        final String note = training.getNote();
        final IWeather weather = training.getWeather();
        final ActivityExtension extension = new ActivityExtension(note, weather);
        cache.updateExtension(record.getActivityId(), extension);
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
        td.colspan = 2;
        td.grabHorizontal = true;
        td.grabVertical = true;

        mapSection.setLayoutData(td);
        mapSection.setText(Messages.SingleActivityViewPart16);
        mapSection.setDescription(Messages.SingleActivityViewPart17);

        final Composite client = toolkit.createComposite(mapSection);
        // client.setBackground(getViewSite().getWorkbenchWindow().getShell().getDisplay().getSystemColor(SWT.COLOR_CYAN));
        final TableWrapLayout layout = new TableWrapLayout();
        layout.numColumns = 1;
        layout.topMargin = -60;
        layout.bottomMargin = 5;
        client.setLayout(layout);

        final String convertTrackpoints = MapConverter.convertTrackpoints(activity);
        final String firstPointToPan = MapConverter.getFirstPointToPan(convertTrackpoints);
        final MapViewer mapViewer = new MapViewer(client, SWT.NONE, convertTrackpoints, firstPointToPan);
        td = new TableWrapData(TableWrapData.FILL_GRAB);
        td.heightHint = 550;
        td.grabHorizontal = true;
        mapViewer.getComposite().setLayoutData(td);

        mapSection.setClient(client);
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
        heartSection.setExpanded(true);
        td = new TableWrapData(TableWrapData.FILL_GRAB);
        td.colspan = 2;
        td.grabHorizontal = true;
        td.grabVertical = true;
        heartSection.setLayoutData(td);
        heartSection.setText(Messages.SingleActivityViewPart9);
        heartSection.setDescription(Messages.SingleActivityViewPart10);
        //
        final Composite client = toolkit.createComposite(heartSection);

        final TableWrapLayout layout = new TableWrapLayout();
        layout.numColumns = 2;
        layout.makeColumnsEqualWidth = false;

        client.setLayout(layout);

        final JFreeChart chart = chartCreator.createChart(dataSetCreator.createDatasetHeart(), ChartType.HEART_DISTANCE);
        final ChartComposite chartComposite = new ChartComposite(client, SWT.NONE, chart, true);
        td = new TableWrapData(TableWrapData.FILL_GRAB);
        td.heightHint = 400;
        chartComposite.setLayoutData(td);

        heartSection.setClient(client);
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
        td.colspan = 2;
        td.grabHorizontal = true;
        td.grabVertical = true;
        speedSection.setLayoutData(td);
        speedSection.setText(Messages.SingleActivityViewPart11);
        speedSection.setDescription(Messages.SingleActivityViewPart12);
        //
        final Composite client = toolkit.createComposite(speedSection);

        final TableWrapLayout layout = new TableWrapLayout();
        layout.numColumns = 2;
        layout.makeColumnsEqualWidth = false;

        client.setLayout(layout);

        final JFreeChart chart = chartCreator.createChart(dataSetCreator.createDatasetSpeed(), ChartType.SPEED_DISTANCE);
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
        td.colspan = 2;
        td.grabHorizontal = true;
        td.grabVertical = true;
        altitude.setLayoutData(td);
        altitude.setText(Messages.SingleActivityViewPart13);
        altitude.setDescription(Messages.SingleActivityViewPart14);

        final Composite client = toolkit.createComposite(altitude);

        final TableWrapLayout layout = new TableWrapLayout();
        layout.numColumns = 2;
        layout.makeColumnsEqualWidth = false;
        client.setLayout(layout);

        final JFreeChart chart = chartCreator.createChart(dataSetCreator.createDatasetAltitude(), ChartType.ALTITUDE_DISTANCE);
        final ChartComposite chartComposite = new ChartComposite(client, SWT.NONE, chart, true);
        td = new TableWrapData(TableWrapData.FILL_GRAB);
        td.heightHint = 400;
        chartComposite.setLayoutData(td);

        altitude.setClient(client);
    }

    private Label addLabelAndValue(final Composite parent, final String label, final String value, final Units unit) {
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

        return dauer;
    }

}
