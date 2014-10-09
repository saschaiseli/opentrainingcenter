package ch.opentrainingcenter.client.views.overview;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTError;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseTrackAdapter;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.forms.widgets.TableWrapData;
import org.eclipse.ui.forms.widgets.TableWrapLayout;
import org.eclipse.ui.part.ViewPart;
import org.joda.time.DateTime;

import ch.opentrainingcenter.charts.single.ChartFactory;
import ch.opentrainingcenter.charts.single.ChartType;
import ch.opentrainingcenter.client.Activator;
import ch.opentrainingcenter.client.model.Units;
import ch.opentrainingcenter.client.ui.FormToolkitSupport;
import ch.opentrainingcenter.client.ui.tableviewer.LapInfoTableViewer;
import ch.opentrainingcenter.client.ui.tableviewer.SelectionProviderIntermediate;
import ch.opentrainingcenter.client.views.ApplicationContext;
import ch.opentrainingcenter.core.PreferenceConstants;
import ch.opentrainingcenter.core.cache.Cache;
import ch.opentrainingcenter.core.cache.IRecordListener;
import ch.opentrainingcenter.core.cache.RecordAdapter;
import ch.opentrainingcenter.core.cache.RouteCache;
import ch.opentrainingcenter.core.cache.SchuhCache;
import ch.opentrainingcenter.core.cache.TrainingCache;
import ch.opentrainingcenter.core.db.IDatabaseAccess;
import ch.opentrainingcenter.core.helper.DistanceHelper;
import ch.opentrainingcenter.core.helper.TimeHelper;
import ch.opentrainingcenter.core.lapinfo.LapInfoCreator;
import ch.opentrainingcenter.core.service.IDatabaseService;
import ch.opentrainingcenter.i18n.Messages;
import ch.opentrainingcenter.model.training.Wetter;
import ch.opentrainingcenter.transfer.IAthlete;
import ch.opentrainingcenter.transfer.ILapInfo;
import ch.opentrainingcenter.transfer.IRoute;
import ch.opentrainingcenter.transfer.IShoe;
import ch.opentrainingcenter.transfer.ITraining;
import ch.opentrainingcenter.transfer.Sport;
import ch.opentrainingcenter.transfer.factory.CommonTransferFactory;

public class SingleActivityViewPart extends ViewPart implements ISelectionProvider {

    public static final String ID = "ch.opentrainingcenter.client.views.singlerun"; //$NON-NLS-1$
    private static final Logger LOGGER = Logger.getLogger(SingleActivityViewPart.class);
    private final Cache cache = TrainingCache.getInstance();
    private final RouteCache routeCache = RouteCache.getInstance();
    private final ITraining training;
    private final IPreferenceStore store;

    private FormToolkit toolkit;
    private ScrolledForm form;
    private TableWrapData td;

    private IRecordListener<ITraining> listener;
    private final ChartFactory factory;
    private IRecordListener<IRoute> routeListener;
    private final IDatabaseAccess databaseAccess;
    private final List<ILapInfo> lapInfos;
    private ChartSectionSupport chartSectionSupport;
    private final List<ISelectionListener> listeners = new ArrayList<>();
    private final SelectionProviderIntermediate providerSynth, providerLap;
    private final Sport sport;

    public SingleActivityViewPart() {
        final IDatabaseService service = (IDatabaseService) PlatformUI.getWorkbench().getService(IDatabaseService.class);
        databaseAccess = service.getDatabaseAccess();
        final ApplicationContext context = ApplicationContext.getApplicationContext();
        final Long selectedId = context.getSelectedId();
        training = databaseAccess.getTrainingById(selectedId);
        lapInfos = training.getLapInfos();
        final IAthlete athlete = context.getAthlete();

        LOGGER.info(String.format("Training mit %s Trackpoints geladen", training.getTrackPoints().size())); //$NON-NLS-1$

        store = Activator.getDefault().getPreferenceStore();
        factory = new ChartFactory(store, training, athlete);
        sport = training.getSport();
        setTitleImage(Activator.getImageDescriptor(sport.getImageIcon()).createImage());
        setPartName(null);
        providerSynth = new SelectionProviderIntermediate();
        providerLap = new SelectionProviderIntermediate();
    }

    @Override
    public String getPartName() {
        return sport.getTranslated();
    }

    @Override
    public void createPartControl(final Composite parent) {
        LOGGER.debug("create single activity view"); //$NON-NLS-1$
        toolkit = new FormToolkit(parent.getDisplay());
        chartSectionSupport = new ChartSectionSupport(toolkit, factory);

        form = toolkit.createScrolledForm(parent);

        toolkit.decorateFormHeading(form.getForm());

        form.setText(Messages.SingleActivityViewPart0 + TimeHelper.convertDateToString(training.getDatum(), true));
        final Composite body = form.getBody();

        final TableWrapLayout layout = new TableWrapLayout();
        layout.makeColumnsEqualWidth = true;
        layout.numColumns = 2;
        body.setLayout(layout);

        long time1 = DateTime.now().getMillis();
        addOverviewSection(body);
        long time2 = DateTime.now().getMillis();
        LOGGER.debug(String.format("Zeit zum Laden von Overview: %s [ms]", time2 - time1)); //$NON-NLS-1$

        addNoteSection(body);
        time1 = DateTime.now().getMillis();
        LOGGER.debug(String.format("Zeit zum Laden von addNoteSection: %s [ms]", time1 - time2)); //$NON-NLS-1$

        final ISelectionChangedListener heartListener = chartSectionSupport.createChartOnSection(body, ChartType.HEART_DISTANCE);
        providerLap.addSelectionChangedListener(heartListener);
        providerSynth.addSelectionChangedListener(heartListener);

        time1 = DateTime.now().getMillis();
        LOGGER.debug(String.format("Zeit zum Laden von addHeartSection: %s [ms]", time1 - time2)); //$NON-NLS-1$

        if (lapInfos.size() > 1) {
            addLapSection(body, providerLap);
        }
        if (store.getBoolean(PreferenceConstants.SYNTH_RUNDEN)) {
            addSynthLapSection(body, providerSynth);
        }

        if (!training.getTrackPoints().isEmpty()) {
            addMapSection(body);
        }
        time2 = DateTime.now().getMillis();
        LOGGER.debug(String.format("Zeit zum Laden von addMapSection: %s [ms]", time2 - time1)); //$NON-NLS-1$

        final ISelectionChangedListener speedListener = chartSectionSupport.createChartOnSection(body, ChartType.SPEED_DISTANCE);
        providerLap.addSelectionChangedListener(speedListener);
        providerSynth.addSelectionChangedListener(speedListener);

        time2 = DateTime.now().getMillis();
        LOGGER.debug(String.format("Zeit zum Laden von addSpeedSection: %s [ms]", time2 - time1)); //$NON-NLS-1$

        final ISelectionChangedListener distanceListener = chartSectionSupport.createChartOnSection(body, ChartType.ALTITUDE_DISTANCE);
        providerLap.addSelectionChangedListener(distanceListener);
        providerSynth.addSelectionChangedListener(distanceListener);
        time1 = DateTime.now().getMillis();
        logDebug(String.format("Zeit zum Laden von addAltitudeSection: %s [ms]", time1 - time2)); //$NON-NLS-1$

        getSite().setSelectionProvider(this);
    }

    private void logDebug(final String message, final Object... args) {
        if (args != null) {
            LOGGER.debug(String.format(message, args));
        } else {
            LOGGER.debug(message);
        }
    }

    private void addLapSection(final Composite body, final SelectionProviderIntermediate provider) {
        final TableViewer viewer = createSection(Messages.RUNDEN, Messages.DETAIL_RUNDEN, new ArrayList<>(training.getLapInfos()), body);
        provider.setSelectionProviderDelegate(viewer);
    }

    private void addSynthLapSection(final Composite body, final SelectionProviderIntermediate provider) {
        final LapInfoCreator creator = new LapInfoCreator(1000);
        final List<ILapInfo> input = new ArrayList<>(creator.createLapInfos(training));
        final TableViewer viewer = createSection(Messages.SingleActivityViewPart_SYNTH_RUNDEN, Messages.SingleActivityViewPart_SYNTH_RUNDEN_DESC, input, body);
        provider.setSelectionProviderDelegate(viewer);
    }

    private TableViewer createSection(final String text, final String description, final List<ILapInfo> input, final Composite body) {
        final Section lapSection = toolkit.createSection(body, FormToolkitSupport.SECTION_STYLE);
        lapSection.setExpanded(true);
        final TableWrapData tableWrapData = new TableWrapData(TableWrapData.FILL_GRAB);
        tableWrapData.colspan = 2;
        tableWrapData.grabHorizontal = true;
        tableWrapData.grabVertical = true;
        tableWrapData.maxHeight = 270;
        lapSection.setLayoutData(tableWrapData);

        lapSection.setText(text);
        lapSection.setDescription(description);

        final Composite client = toolkit.createComposite(lapSection);
        final TableWrapLayout layout = new TableWrapLayout();
        layout.numColumns = 1;
        layout.makeColumnsEqualWidth = false;
        client.setLayout(layout);

        final LapInfoTableViewer viewer = new LapInfoTableViewer(client, sport);
        viewer.createTableViewer(input, getSite());
        lapSection.setClient(client);
        return viewer;
    }

    @Override
    public void dispose() {
        cache.removeListener(listener);
        routeCache.removeListener(routeListener);
        ApplicationContext.getApplicationContext().setSelectedId(null);
        for (final ISelectionListener listener : listeners) {
            getSite().getWorkbenchWindow().getSelectionService().removeSelectionListener(listener);
        }
        toolkit.dispose();
        super.dispose();
    }

    @Override
    public void setFocus() {
        form.setFocus();
    }

    private void addOverviewSection(final Composite body) {
        final Section overviewSection = toolkit.createSection(body, FormToolkitSupport.SECTION_STYLE);
        overviewSection.setExpanded(true);
        td = new TableWrapData(TableWrapData.FILL_GRAB);
        td.colspan = 1;
        overviewSection.setLayoutData(td);
        overviewSection.setText(Messages.SingleActivityViewPart1);
        overviewSection.setDescription(Messages.SingleActivityViewPart2);

        final Composite overViewComposite = toolkit.createComposite(overviewSection);
        final GridLayout layoutClient = new GridLayout(3, false);
        overViewComposite.setLayout(layoutClient);
        final FormToolkitSupport support = new FormToolkitSupport(toolkit);
        final String readableTime = TimeHelper.convertSecondsToHumanReadableZeit(training.getDauer());
        support.addLabelAndValue(overViewComposite, Messages.SingleActivityViewPart3, readableTime, Units.HOUR_MINUTE_SEC);
        final String laengeInKm = DistanceHelper.roundDistanceFromMeterToKm(training.getLaengeInMeter());
        support.addLabelAndValue(overViewComposite, Messages.SingleActivityViewPart4, laengeInKm, Units.KM);
        final int avgHeartRate = training.getAverageHeartBeat();
        if (avgHeartRate > 0) {
            support.addLabelAndValue(overViewComposite, Messages.SingleActivityViewPart5, String.valueOf(avgHeartRate), Units.BEATS_PER_MINUTE);
        } else {
            support.addLabelAndValue(overViewComposite, Messages.SingleActivityViewPart5, "-", Units.BEATS_PER_MINUTE); //$NON-NLS-1$
        }
        support.addLabelAndValue(overViewComposite, Messages.SingleActivityViewPart6, String.valueOf(training.getMaxHeartBeat()), Units.BEATS_PER_MINUTE);

        final Units unit = getUnitFuerGeschwindigkeit();
        final String pace = DistanceHelper.calculatePace(training.getLaengeInMeter(), training.getDauer(), Sport.RUNNING);
        final String speed = DistanceHelper.calculatePace(training.getLaengeInMeter(), training.getDauer(), Sport.BIKING);
        support.addLabelAndValue(overViewComposite, Messages.SingleActivityViewPart7, pace, unit);
        support.addLabelAndValue(overViewComposite, Messages.SingleActivityViewPart8, speed, unit);
        support.addLabelAndValue(overViewComposite, Messages.SingleActivityViewPart_4, training.getUpMeter().toString(), Units.METER);
        support.addLabelAndValue(overViewComposite, Messages.SingleActivityViewPart_8, training.getDownMeter().toString(), Units.METER);
        overviewSection.setClient(overViewComposite);
    }

    private Units getUnitFuerGeschwindigkeit() {
        final Units unit;
        if (Sport.BIKING.equals(sport)) {
            unit = Units.GESCHWINDIGKEIT;
        } else {
            unit = Units.PACE;
        }
        return unit;
    }

    private void addNoteSection(final Composite body) {
        final Section section = toolkit.createSection(body, FormToolkitSupport.SECTION_STYLE);
        section.setExpanded(true);
        section.setText(Messages.SingleActivityViewPart_0);
        section.setDescription(Messages.SingleActivityViewPart_1);

        final Composite container = toolkit.createComposite(section);
        GridLayoutFactory.fillDefaults().numColumns(2).applyTo(container);
        GridDataFactory.swtDefaults().applyTo(container);

        final Label label = toolkit.createLabel(container, ""); //$NON-NLS-1$
        label.setText(Messages.SingleActivityViewPart_3);

        final Text note = toolkit.createText(container, "", SWT.V_SCROLL | SWT.MULTI | SWT.BORDER); //$NON-NLS-1$
        GridDataFactory.fillDefaults().grab(true, true).minSize(SWT.DEFAULT, 100).align(SWT.FILL, SWT.FILL).applyTo(note);

        final String notiz = training.getNote();
        if (notiz != null) {
            note.setText(notiz);
        }

        note.addMouseTrackListener(new MouseTrackAdapter() {

            @Override
            public void mouseExit(final MouseEvent e) {
                if (!training.getNote().equals(note.getText())) {
                    training.setNote(note.getText());
                    update(training);
                }
            }
        });

        note.addFocusListener(new FocusAdapter() {

            @Override
            public void focusLost(final FocusEvent e) {
                if (!training.getNote().equals(note.getText())) {
                    training.setNote(note.getText());
                    update(training);
                }
            }
        });

        final Label labelWetter = toolkit.createLabel(container, ""); //$NON-NLS-1$
        labelWetter.setText(Messages.SingleActivityViewPart_6);

        final Combo comboWetter = new Combo(container, SWT.READ_ONLY);
        comboWetter.setBounds(50, 50, 150, 65);

        comboWetter.setItems(Wetter.getItems());
        comboWetter.select(training.getWeather().getId());
        comboWetter.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(final SelectionEvent e) {
                if (training.getWeather().getId() != (comboWetter.getSelectionIndex())) {
                    training.setWeather(CommonTransferFactory.createWeather(comboWetter.getSelectionIndex()));
                    update(training);
                }
            }
        });
        GridDataFactory.fillDefaults().grab(true, true).align(SWT.FILL, SWT.FILL).applyTo(comboWetter);

        final Label labelStrecke = toolkit.createLabel(container, ""); //$NON-NLS-1$
        labelStrecke.setText(Messages.STRECKE);

        final ComboViewer comboStrecke = new ComboViewer(container);
        comboStrecke.setContentProvider(ArrayContentProvider.getInstance());
        comboStrecke.setLabelProvider(new LabelProvider() {
            @Override
            public String getText(final Object element) {
                String label = ""; //$NON-NLS-1$
                if (element instanceof IRoute) {
                    final IRoute route = (IRoute) element;
                    label = route.getName();
                }
                return label;
            }
        });
        GridDataFactory.fillDefaults().grab(true, true).align(SWT.FILL, SWT.FILL).applyTo(comboStrecke.getControl());

        final List<IRoute> all = routeCache.getAll();
        comboStrecke.setInput(all);
        comboStrecke.addSelectionChangedListener(new ISelectionChangedListener() {

            @Override
            public void selectionChanged(final SelectionChangedEvent event) {
                if (!event.getSelection().isEmpty()) {
                    final StructuredSelection selection = (StructuredSelection) event.getSelection();
                    final IRoute route = (IRoute) selection.getFirstElement();
                    training.setRoute(route);
                    update(training);
                }
            }
        });
        routeListener = new IRecordListener<IRoute>() {

            @Override
            public void recordChanged(final Collection<IRoute> entry) {
                updateCombo(entry);
            }

            @Override
            public void deleteRecord(final Collection<IRoute> entry) {
                updateCombo(entry);
            }

            private void updateCombo(final Collection<IRoute> entry) {
                final ISelection sel = comboStrecke.getSelection();
                if (sel.isEmpty()) {
                    return;
                }
                comboStrecke.setInput(routeCache.getAll());
                if (!entry.isEmpty() && entry.size() > 1) {
                    // update der liste
                    comboStrecke.setSelection(sel);
                    comboStrecke.refresh();
                } else if (!entry.isEmpty() && entry.size() == 1) {
                    // update von training
                    final IRoute next = entry.iterator().next();
                    final StructuredSelection selection = new StructuredSelection(next);
                    if (next.getReferenzTrack() != null && next.getReferenzTrack().getId() == training.getId()) {
                        comboStrecke.setSelection(selection);
                        comboStrecke.getCombo().setEnabled(false);
                    } else {
                        // select old selection
                        comboStrecke.setSelection(sel);
                        comboStrecke.refresh();
                    }
                } else {
                    // select old selection
                    comboStrecke.setSelection(sel);
                }
            }
        };
        // ------------------------------- Schuhe
        final Label labelSchuhe = toolkit.createLabel(container, ""); //$NON-NLS-1$
        labelSchuhe.setText(Messages.SingleActivityViewPart_Schuhe);

        final ComboViewer comboSchuhe = new ComboViewer(container);
        comboSchuhe.setContentProvider(ArrayContentProvider.getInstance());
        comboSchuhe.setLabelProvider(new LabelProvider() {
            @Override
            public String getText(final Object element) {
                final IShoe shoe = (IShoe) element;
                return shoe.getSchuhname();
            }
        });
        GridDataFactory.fillDefaults().grab(true, true).align(SWT.FILL, SWT.FILL).applyTo(comboSchuhe.getControl());
        final List<IShoe> allShoes = SchuhCache.getInstance().getAll();
        comboSchuhe.setInput(allShoes);

        if (training.getShoe() != null) {
            final StructuredSelection selection = new StructuredSelection(training.getShoe());
            comboSchuhe.setSelection(selection);
        }

        comboSchuhe.addSelectionChangedListener(new ISelectionChangedListener() {

            @Override
            public void selectionChanged(final SelectionChangedEvent event) {
                if (!event.getSelection().isEmpty()) {
                    final StructuredSelection selection = (StructuredSelection) event.getSelection();
                    final IShoe shoe = (IShoe) selection.getFirstElement();
                    training.setShoe(shoe);
                    update(training);
                }
            }
        });
        // -------------------------------

        listener = new RecordAdapter<ITraining>() {

            @Override
            public void recordChanged(final Collection<ITraining> entry) {
                Display.getDefault().asyncExec(new Runnable() {

                    @Override
                    public void run() {
                        if (entry != null) {
                            final ITraining act = entry.iterator().next();
                            if (act.getDatum() == training.getDatum()) {
                                // nur wenn es dieser record ist!
                                if (training.getNote() != null) {
                                    note.setText(training.getNote());
                                }
                            }
                        }
                        section.update();
                    }
                });

            }
        };

        routeCache.addListener(routeListener);
        cache.addListener(listener);

        if (training.getRoute() != null) {
            comboStrecke.setSelection(new StructuredSelection(training.getRoute()));
        } else {
            comboStrecke.setSelection(new StructuredSelection(all.get(0)));
        }
        comboStrecke.refresh();
        handleStreckenCombo(training.getRoute(), comboStrecke);
        section.setClient(container);
    }

    private void handleStreckenCombo(final IRoute route, final ComboViewer comboStrecke) {
        if (isReferenzTrack(training, route)) {
            comboStrecke.getCombo().setEnabled(false);
        }
    }

    private boolean isReferenzTrack(final ITraining training, final IRoute route) {
        return route != null && route.getReferenzTrack() != null && route.getReferenzTrack().getId() == training.getId()
                && !Messages.OTCKonstanten_0.equals(route.getName());
    }

    private void update(final ITraining record) {
        Display.getDefault().asyncExec(new Runnable() {

            @Override
            public void run() {
                databaseAccess.saveOrUpdate(record);
            }
        });

    }

    private void addMapSection(final Composite body) {
        final Section mapSection = toolkit.createSection(body, FormToolkitSupport.SECTION_STYLE);
        mapSection.setExpanded(true);
        td = new TableWrapData(TableWrapData.FILL_GRAB);
        td.colspan = 2;
        td.grabHorizontal = true;
        td.grabVertical = true;

        mapSection.setLayoutData(td);
        mapSection.setText(Messages.SingleActivityViewPart16);
        mapSection.setDescription(Messages.SingleActivityViewPart17);

        final Composite client = toolkit.createComposite(mapSection);
        client.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_CYAN));
        final TableWrapLayout layout = new TableWrapLayout();
        layout.numColumns = 1;
        layout.topMargin = -60;
        layout.bottomMargin = 5;
        client.setLayout(layout);

        // final MapController controller = new MapController();
        // final MapView map = new MapView(client, controller);
        // map.setTileFactory(new OpenStreetBrowserFactory());
        //
        // map.redraw();
        // map.queueRedraw();
        // final GeoPoint position = new GeoPoint(7.481, 46.95);
        // map.setMapCenter(position);

        final String convertTrackpoints = MapConverter.convertTrackpoints(training);
        final String firstPointToPan = MapConverter.getFirstPointToPan(convertTrackpoints);
        final MapViewer mapViewer = new MapViewer(client, SWT.NONE, convertTrackpoints, firstPointToPan);
        td = new TableWrapData(TableWrapData.FILL_GRAB);
        td.heightHint = 550;
        td.grabHorizontal = true;
        try {
            mapViewer.getComposite().setLayoutData(td);
            mapSection.setClient(client);
        } catch (final SWTException | SWTError e) {
            LOGGER.error("Map kann nicht angezeigt werden", e); //$NON-NLS-1$
        }
    }

    @Override
    public void addSelectionChangedListener(final ISelectionChangedListener l) {
    }

    @Override
    public ISelection getSelection() {
        return new StructuredSelection(training);
    }

    @Override
    public void removeSelectionChangedListener(final ISelectionChangedListener l) {
    }

    @Override
    public void setSelection(final ISelection selection) {

    }

}
