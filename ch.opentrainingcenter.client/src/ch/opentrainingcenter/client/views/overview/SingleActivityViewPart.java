package ch.opentrainingcenter.client.views.overview;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTError;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
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
import ch.opentrainingcenter.client.ui.FormToolkitSupport;
import ch.opentrainingcenter.client.ui.tableviewer.LapInfoTableViewer;
import ch.opentrainingcenter.client.ui.tableviewer.SelectionProviderIntermediate;
import ch.opentrainingcenter.client.views.ApplicationContext;
import ch.opentrainingcenter.core.PreferenceConstants;
import ch.opentrainingcenter.core.cache.Cache;
import ch.opentrainingcenter.core.cache.IRecordListener;
import ch.opentrainingcenter.core.cache.RouteCache;
import ch.opentrainingcenter.core.cache.TrainingCache;
import ch.opentrainingcenter.core.db.IDatabaseAccess;
import ch.opentrainingcenter.core.geo.MapConverter;
import ch.opentrainingcenter.core.helper.TimeHelper;
import ch.opentrainingcenter.core.lapinfo.LapInfoCreator;
import ch.opentrainingcenter.core.service.IDatabaseService;
import ch.opentrainingcenter.i18n.Messages;
import ch.opentrainingcenter.transfer.IAthlete;
import ch.opentrainingcenter.transfer.ILapInfo;
import ch.opentrainingcenter.transfer.IRoute;
import ch.opentrainingcenter.transfer.ITraining;
import ch.opentrainingcenter.transfer.Sport;

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
    private final IAthlete athlete;

    // private final ICompareRoute comp;

    public SingleActivityViewPart() {
        LOGGER.info("SingleActivityViewPart wird instanziert"); //$NON-NLS-1$
        final IDatabaseService service = (IDatabaseService) PlatformUI.getWorkbench().getService(IDatabaseService.class);
        LOGGER.info("Database Service geladen"); //$NON-NLS-1$
        databaseAccess = service.getDatabaseAccess();
        final ApplicationContext context = ApplicationContext.getApplicationContext();
        final Long selectedId = context.getSelectedId();
        LOGGER.info(String.format("Lade selektiertes Training mit ID %s", selectedId)); //$NON-NLS-1$
        training = databaseAccess.getTrainingById(selectedId);
        LOGGER.info(String.format("Selektiertes Training mit ID %s geladen", selectedId)); //$NON-NLS-1$
        lapInfos = training.getLapInfos();
        LOGGER.info("LapInfos geladen"); //$NON-NLS-1$
        athlete = context.getAthlete();

        LOGGER.info(String.format("Training mit %s Trackpoints geladen", training.getTrackPoints().size())); //$NON-NLS-1$

        store = Activator.getDefault().getPreferenceStore();
        factory = new ChartFactory(store, training, athlete);
        // comp = CompareRouteFactory.getRouteComparator(true,
        // store.getString(PreferenceConstants.KML_DEBUG_PATH));

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
        // addOverviewSection(body);
        new OverviewSection(training).addOverviewSection(body, toolkit);
        long time2 = DateTime.now().getMillis();
        LOGGER.debug(String.format("Zeit zum Laden von Overview: %s [ms]", time2 - time1)); //$NON-NLS-1$

        // addNoteSection(body);
        new NoteSection(training, toolkit, databaseAccess).addNoteSection(body);
        // searchForReferenzRoute();
        time1 = DateTime.now().getMillis();
        LOGGER.debug(String.format("Zeit zum Laden von addNoteSection: %s [ms]", time1 - time2)); //$NON-NLS-1$

        final ISelectionChangedListener heartListener = chartSectionSupport.createChartOnSection(body, ChartType.HEART_DISTANCE, true);
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

        final ISelectionChangedListener speedListener = chartSectionSupport.createChartOnSection(body, ChartType.SPEED_DISTANCE, false);
        providerLap.addSelectionChangedListener(speedListener);
        providerSynth.addSelectionChangedListener(speedListener);

        time2 = DateTime.now().getMillis();
        LOGGER.debug(String.format("Zeit zum Laden von addSpeedSection: %s [ms]", time2 - time1)); //$NON-NLS-1$

        final ISelectionChangedListener distanceListener = chartSectionSupport.createChartOnSection(body, ChartType.ALTITUDE_DISTANCE, false);
        providerLap.addSelectionChangedListener(distanceListener);
        providerSynth.addSelectionChangedListener(distanceListener);
        time1 = DateTime.now().getMillis();
        logDebug(String.format("Zeit zum Laden von addAltitudeSection: %s [ms]", time1 - time2)); //$NON-NLS-1$

        getSite().setSelectionProvider(this);
    }

    // private void searchForReferenzRoute() {
    // Display.getDefault().asyncExec(new Runnable() {
    //
    // @Override
    // public void run() {
    // final List<IRoute> routen = databaseAccess.getRoute(athlete);
    // final List<ITraining> all = new ArrayList<ITraining>();
    // for (final IRoute route : routen) {
    // if (route.getReferenzTrack() != null) {
    // all.add(route.getReferenzTrack());
    // }
    // }
    // final SearchRecordJob job = new
    // SearchRecordJob(Messages.SingleActivityViewPart_SucheReferenzRoute,
    // training, all, comp);
    // job.schedule();
    //
    // job.addJobChangeListener(new JobChangeAdapter() {
    // @Override
    // public void done(final IJobChangeEvent event) {
    // super.done(event);
    // Display.getDefault().asyncExec(new Runnable() {
    //
    // @Override
    // public void run() {
    // final List<ITraining> sameRoute = job.getSameRoute();
    //                                LOGGER.info(String.format("Anzahl gleiche Routen: %s", sameRoute.size())); //$NON-NLS-1$
    // }
    // });
    // }
    // });
    // }
    // });
    // }

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
