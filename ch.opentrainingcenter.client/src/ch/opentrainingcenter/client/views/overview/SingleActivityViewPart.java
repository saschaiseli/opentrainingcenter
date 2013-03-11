package ch.opentrainingcenter.client.views.overview;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.StructuredSelection;
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
import org.eclipse.swt.widgets.Display;
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

import ch.opentrainingcenter.charts.single.ChartFactory;
import ch.opentrainingcenter.charts.single.ChartType;
import ch.opentrainingcenter.client.Activator;
import ch.opentrainingcenter.client.cache.StreckeCache;
import ch.opentrainingcenter.client.model.Units;
import ch.opentrainingcenter.client.views.ApplicationContext;
import ch.opentrainingcenter.core.cache.Cache;
import ch.opentrainingcenter.core.cache.IRecordListener;
import ch.opentrainingcenter.core.cache.TrainingCenterDataCache;
import ch.opentrainingcenter.core.db.DatabaseAccessFactory;
import ch.opentrainingcenter.core.db.IDatabaseAccess;
import ch.opentrainingcenter.core.helper.TimeHelper;
import ch.opentrainingcenter.i18n.Messages;
import ch.opentrainingcenter.model.TrainingOverviewFactory;
import ch.opentrainingcenter.model.strecke.StreckeModel;
import ch.opentrainingcenter.model.training.ISimpleTraining;
import ch.opentrainingcenter.model.training.Wetter;
import ch.opentrainingcenter.tcx.ActivityT;
import ch.opentrainingcenter.tcx.ExtensionsT;
import ch.opentrainingcenter.transfer.ActivityExtension;
import ch.opentrainingcenter.transfer.CommonTransferFactory;
import ch.opentrainingcenter.transfer.IImported;
import ch.opentrainingcenter.transfer.ITraining;
import ch.opentrainingcenter.transfer.IWeather;

public class SingleActivityViewPart extends ViewPart implements ISelectionProvider {

    public static final String ID = "ch.opentrainingcenter.client.views.singlerun"; //$NON-NLS-1$
    private static final Logger LOGGER = Logger.getLogger(SingleActivityViewPart.class);
    private final Cache cache = TrainingCenterDataCache.getInstance();
    private final StreckeCache cacheStrecke = StreckeCache.getInstance();
    private final IDatabaseAccess databaseAccess = DatabaseAccessFactory.getDatabaseAccess();
    private final ISimpleTraining simpleTraining;
    private final ActivityT activity;
    private FormToolkit toolkit;
    private ScrolledForm form;
    private TableWrapData td;

    private IRecordListener<ActivityT> listener;
    private final ChartFactory factory;

    public SingleActivityViewPart() {
        final ApplicationContext context = ApplicationContext.getApplicationContext();
        final Date selectedId = context.getSelectedId();
        activity = cache.get(selectedId);
        simpleTraining = TrainingOverviewFactory.creatSimpleTraining(activity);

        factory = new ChartFactory(Activator.getDefault().getPreferenceStore(), activity, context.getAthlete());

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

        getSite().setSelectionProvider(this);
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
        final Section overviewSection = toolkit.createSection(body, Section.DESCRIPTION | Section.TITLE_BAR | Section.TWISTIE | Section.EXPANDED);
        overviewSection.addExpansionListener(new ExpansionAdapter() {
            @Override
            public void expansionStateChanged(final ExpansionEvent e) {
                form.reflow(true);
            }
        });
        overviewSection.setExpanded(true);
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
        section.setExpanded(true);
        section.setText(Messages.SingleActivityViewPart_0);
        section.setDescription(Messages.SingleActivityViewPart_1);

        final Composite container = toolkit.createComposite(section);
        final GridLayout layout = new GridLayout(2, false);
        layout.verticalSpacing = 2;
        layout.horizontalSpacing = 10;
        container.setLayout(layout);

        final Label label = toolkit.createLabel(container, ""); //$NON-NLS-1$
        label.setText(Messages.SingleActivityViewPart_3);
        // label.setLayoutData(gd);

        final Text note = toolkit.createText(container, "", SWT.V_SCROLL | SWT.MULTI | SWT.BORDER); //$NON-NLS-1$
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

        final Label labelWetter = toolkit.createLabel(container, ""); //$NON-NLS-1$
        labelWetter.setText(Messages.SingleActivityViewPart_6);
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

        listener = new IRecordListener<ActivityT>() {

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

        final Label labelStrecke = toolkit.createLabel(container, ""); //$NON-NLS-1$
        labelStrecke.setText("Strecke:");
        // gd.minimumWidth = 10;
        // labelWetter.setLayoutData(gd);

        final Combo streckeCombo = new Combo(container, SWT.READ_ONLY);
        streckeCombo.setBounds(50, 50, 150, 65);

        final List<StreckeModel> all = cacheStrecke.getAll();
        Collections.sort(all, new Comparator<StreckeModel>() {

            @Override
            public int compare(final StreckeModel a, final StreckeModel b) {
                return Integer.valueOf(a.getId()).compareTo(Integer.valueOf(b.getId()));
            }
        });
        final List<String> allNamen = new ArrayList<String>();
        final Map<Integer, Integer> mapDbIndex = new HashMap<Integer, Integer>();
        int i = 0;
        for (final StreckeModel strecke : all) {
            allNamen.add(strecke.getName());
            mapDbIndex.put(strecke.getId(), i);
            i++;
        }
        streckeCombo.setItems(allNamen.toArray(new String[1]));
        final StreckeModel strecke = simpleTraining.getStrecke();
        streckeCombo.select(strecke != null ? mapDbIndex.get(strecke.getId()) : 0);
        streckeCombo.addSelectionListener(new SelectionListener() {

            @Override
            public void widgetSelected(final SelectionEvent e) {
                final Combo source = (Combo) e.getSource();
                final String[] items = source.getItems();
                final String key = items[source.getSelectionIndex()];
                safeStrecke(key);
            }

            @Override
            public void widgetDefaultSelected(final SelectionEvent e) {
            }
        });

        cache.addListener(listener);
        section.setClient(container);
    }

    private void safeStrecke(final String key) {
        final IImported record = databaseAccess.getImportedRecord(activity.getId().toGregorianCalendar().getTime());
        final StreckeModel newModel = cacheStrecke.get(key);
        updateRoute(record, newModel.getId());
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
            Display.getDefault().asyncExec(new Runnable() {

                @Override
                public void run() {
                    update(record);
                }
            });
        }
    }

    private void updateRoute(final IImported record, final int idRoute) {
        databaseAccess.updateRecordRoute(record, idRoute);
        updateCache(databaseAccess.getImportedRecord(record.getActivityId()));
    }

    private void update(final IImported record) {
        databaseAccess.updateRecord(record);
        updateCache(record);
    }

    private void updateCache(final IImported record) {
        final ITraining training = record.getTraining();
        final String note = training.getNote();
        final IWeather weather = training.getWeather();
        final ActivityExtension extension = new ActivityExtension(note, weather, record.getRoute());
        cache.updateExtension(record.getActivityId(), extension);
    }

    private void addMapSection(final Composite body) {
        final Section mapSection = toolkit.createSection(body, Section.DESCRIPTION | Section.TITLE_BAR | Section.TWISTIE | Section.EXPANDED);
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
        final Section heartSection = toolkit.createSection(body, Section.DESCRIPTION | Section.TITLE_BAR | Section.TWISTIE | Section.EXPANDED);
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

        factory.addChartToComposite(client, ChartType.HEART_DISTANCE);

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

        factory.addChartToComposite(client, ChartType.SPEED_DISTANCE);

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

        // final JFreeChart chart =
        // chartCreator.createChart(dataSetCreator.createDatasetAltitude(),
        // ChartType.ALTITUDE_DISTANCE);
        // final ChartComposite chartComposite = new ChartComposite(client,
        // SWT.NONE, chart, true);
        // td = new TableWrapData(TableWrapData.FILL_GRAB);
        // td.heightHint = 400;
        // chartComposite.setLayoutData(td);

        factory.addChartToComposite(client, ChartType.ALTITUDE_DISTANCE);

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

    @Override
    public void addSelectionChangedListener(final ISelectionChangedListener l) {
        //        System.out.println("asd" + l); //$NON-NLS-1$
    }

    @Override
    public ISelection getSelection() {
        return new StructuredSelection(simpleTraining);
    }

    @Override
    public void removeSelectionChangedListener(final ISelectionChangedListener l) {
    }

    @Override
    public void setSelection(final ISelection selection) {

    }

}
