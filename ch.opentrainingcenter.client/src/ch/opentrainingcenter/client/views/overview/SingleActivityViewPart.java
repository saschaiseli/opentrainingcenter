package ch.opentrainingcenter.client.views.overview;

import java.util.Collection;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTError;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseTrackListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
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
import ch.opentrainingcenter.core.cache.TrainingCache;
import ch.opentrainingcenter.core.db.DatabaseAccessFactory;
import ch.opentrainingcenter.core.db.IDatabaseAccess;
import ch.opentrainingcenter.core.helper.TimeHelper;
import ch.opentrainingcenter.i18n.Messages;
import ch.opentrainingcenter.model.ModelFactory;
import ch.opentrainingcenter.model.strecke.StreckeModel;
import ch.opentrainingcenter.model.strecke.StreckeModelComparator;
import ch.opentrainingcenter.model.training.ISimpleTraining;
import ch.opentrainingcenter.model.training.Wetter;
import ch.opentrainingcenter.transfer.CommonTransferFactory;
import ch.opentrainingcenter.transfer.IAthlete;
import ch.opentrainingcenter.transfer.ITraining;
import ch.opentrainingcenter.transfer.IWeather;

public class SingleActivityViewPart extends ViewPart implements ISelectionProvider {

    public static final String ID = "ch.opentrainingcenter.client.views.singlerun"; //$NON-NLS-1$
    private static final Logger LOGGER = Logger.getLogger(SingleActivityViewPart.class);
    private final Cache cache = TrainingCache.getInstance();
    private final StreckeCache cacheStrecke = StreckeCache.getInstance();
    private final IDatabaseAccess databaseAccess = DatabaseAccessFactory.getDatabaseAccess();
    private final ISimpleTraining simpleTraining;
    private final ITraining training;
    private FormToolkit toolkit;
    private ScrolledForm form;
    private TableWrapData td;

    private IRecordListener<ITraining> listener;
    private final ChartFactory factory;
    private IRecordListener<StreckeModel> streckeListener;

    public SingleActivityViewPart() {
        final ApplicationContext context = ApplicationContext.getApplicationContext();
        final Long selectedId = context.getSelectedId();
        if (cache.get(selectedId) == null) {
            training = databaseAccess.getTrainingById(selectedId);
        } else {
            training = cache.get(selectedId);
        }
        final IAthlete athlete = context.getAthlete();
        simpleTraining = ModelFactory.convertToSimpleTraining(training);

        factory = new ChartFactory(Activator.getDefault().getPreferenceStore(), training, athlete);

        setPartName(simpleTraining.getFormattedDate());
    }

    @Override
    public void createPartControl(final Composite parent) {
        LOGGER.debug("create single activity view"); //$NON-NLS-1$
        toolkit = new FormToolkit(parent.getDisplay());
        form = toolkit.createScrolledForm(parent);

        toolkit.decorateFormHeading(form.getForm());

        form.setText(Messages.SingleActivityViewPart0 + TimeHelper.convertDateToString(simpleTraining.getDatum(), true));
        final Composite body = form.getBody();

        final TableWrapLayout layout = new TableWrapLayout();
        layout.makeColumnsEqualWidth = true;
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
        cacheStrecke.removeListener(streckeListener);
        ApplicationContext.getApplicationContext().setSelectedId(null);
        toolkit.dispose();
        super.dispose();
    }

    @Override
    public void setFocus() {
        form.setFocus();
    }

    private void addOverviewSection(final Composite body) {
        final Section overviewSection = toolkit.createSection(body, Section.DESCRIPTION | Section.TITLE_BAR | Section.TWISTIE | Section.EXPANDED);
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
        addLabelAndValue(overViewComposite, Messages.SingleActivityViewPart_4, "" + simpleTraining.getUpMeter(), Units.METER); //$NON-NLS-1$
        addLabelAndValue(overViewComposite, Messages.SingleActivityViewPart_8, "" + simpleTraining.getDownMeter(), Units.METER); //$NON-NLS-1$
        overviewSection.setClient(overViewComposite);
    }

    private void addNoteSection(final Composite body) {
        final Section section = toolkit.createSection(body, Section.DESCRIPTION | Section.TITLE_BAR | Section.TWISTIE | Section.EXPANDED);
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
                safeNote(note.getText());
            }

            @Override
            public void mouseEnter(final MouseEvent e) {

            }
        });

        note.addFocusListener(new FocusListener() {

            @Override
            public void focusLost(final FocusEvent e) {
                safeNote(note.getText());
            }

            @Override
            public void focusGained(final FocusEvent e) {

            }
        });

        final Label labelWetter = toolkit.createLabel(container, ""); //$NON-NLS-1$
        labelWetter.setText(Messages.SingleActivityViewPart_6);

        final Combo comboWetter = new Combo(container, SWT.READ_ONLY);
        comboWetter.setBounds(50, 50, 150, 65);

        comboWetter.setItems(Wetter.getItems());
        comboWetter.select(simpleTraining.getWetter().getIndex());
        comboWetter.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(final SelectionEvent e) {
                safeWeather(comboWetter.getSelectionIndex());
            }
        });
        GridDataFactory.fillDefaults().grab(true, true).align(SWT.FILL, SWT.FILL).applyTo(comboWetter);

        final Label labelStrecke = toolkit.createLabel(container, ""); //$NON-NLS-1$
        labelStrecke.setText(Messages.SingleActivityViewPart_2);

        final ComboViewer comboStrecke = new ComboViewer(container);
        comboStrecke.setContentProvider(ArrayContentProvider.getInstance());
        comboStrecke.setLabelProvider(new LabelProvider() {
            @Override
            public String getText(final Object element) {
                String label = ""; //$NON-NLS-1$
                if (element instanceof StreckeModel) {
                    final StreckeModel route = (StreckeModel) element;
                    label = route.getName();
                }
                return label;
            }
        });
        GridDataFactory.fillDefaults().grab(true, true).align(SWT.FILL, SWT.FILL).applyTo(comboStrecke.getControl());

        final List<StreckeModel> all = cacheStrecke.getSortedElements(new StreckeModelComparator());
        comboStrecke.setInput(all);
        final StreckeModel strecke = simpleTraining.getStrecke();

        comboStrecke.addSelectionChangedListener(new ISelectionChangedListener() {

            @Override
            public void selectionChanged(final SelectionChangedEvent event) {
                if (!event.getSelection().isEmpty()) {
                    safeStrecke((StreckeModel) ((StructuredSelection) event.getSelection()).getFirstElement());
                }
            }
        });
        streckeListener = new IRecordListener<StreckeModel>() {

            @Override
            public void recordChanged(final Collection<StreckeModel> entry) {
                updateCombo(entry);
            }

            @Override
            public void deleteRecord(final Collection<StreckeModel> entry) {
                updateCombo(entry);
            }

            private void updateCombo(final Collection<StreckeModel> entry) {
                final ISelection sel = comboStrecke.getSelection();
                if (sel.isEmpty()) {
                    return;
                }
                comboStrecke.setInput(cacheStrecke.getSortedElements(new StreckeModelComparator()));
                if (!entry.isEmpty() && entry.size() > 1) {
                    // update der liste
                    comboStrecke.setSelection(sel);
                    comboStrecke.refresh();
                } else if (!entry.isEmpty() && entry.size() == 1) {
                    // update von training
                    final StreckeModel next = entry.iterator().next();
                    final StructuredSelection selection = new StructuredSelection(next);
                    if (next.getReferenzTrainingId() == training.getId()) {
                        comboStrecke.setSelection(selection);
                        comboStrecke.refresh();
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

        listener = new IRecordListener<ITraining>() {

            @Override
            public void recordChanged(final Collection<ITraining> entry) {
                if (entry != null) {
                    final ITraining act = entry.iterator().next();
                    if (act.getDatum() == simpleTraining.getDatum().getTime()) {
                        // nur wenn es dieser record ist!
                        note.setText(simpleTraining.getNote());
                        // comboStrecke.setSelection(new
                        // StructuredSelection(simpleTraining.getStrecke()));
                    }
                }
                section.update();
            }

            @Override
            public void deleteRecord(final Collection<ITraining> entry) {
            }
        };

        cacheStrecke.addListener(streckeListener);
        cache.addListener(listener);

        if (strecke != null) {
            comboStrecke.setSelection(new StructuredSelection(strecke));
        } else {
            comboStrecke.setSelection(new StructuredSelection(all.get(0)));
        }
        comboStrecke.refresh();

        section.setClient(container);
    }

    private void safeStrecke(final StreckeModel model) {

        final ITraining record = databaseAccess.getTrainingById(training.getDatum());
        updateRoute(record, model.getId());
    }

    private void safeWeather(final int index) {
        final Wetter wetter = simpleTraining.getWetter();
        if (index != wetter.getIndex()) {
            // änderungen
            final ITraining record = databaseAccess.getTrainingById(training.getDatum());
            if (record != null) {
                final Wetter currentWeather = Wetter.getRunType(index);
                training.setWeather(CommonTransferFactory.createWeather(currentWeather.getIndex()));
                simpleTraining.setWetter(wetter);
            }
            update(record);
        }
    }

    private void safeNote(final String text) {
        if (!text.equals(simpleTraining.getNote())) {
            // änderungen
            final ITraining record = databaseAccess.getTrainingById(training.getDatum());
            if (record != null) {
                record.setNote(text);
                simpleTraining.setNote(text);
            }
            update(record);
        }
    }

    private void updateRoute(final ITraining record, final int idRoute) {
        Display.getDefault().asyncExec(new Runnable() {

            @Override
            public void run() {
                databaseAccess.updateRecordRoute(record, idRoute);
                updateCache(databaseAccess.getTrainingById(record.getDatum()));

            }
        });
    }

    private void update(final ITraining record) {
        Display.getDefault().asyncExec(new Runnable() {

            @Override
            public void run() {
                databaseAccess.updateRecord(record);
                updateCache(record);

            }
        });

    }

    private void updateCache(final ITraining record) {
        final String note = record.getNote();
        final IWeather weather = record.getWeather();
        cache.update(record.getDatum(), note, weather, record.getRoute());
    }

    private void addMapSection(final Composite body) {
        final Section mapSection = toolkit.createSection(body, Section.DESCRIPTION | Section.TITLE_BAR | Section.TWISTIE | Section.EXPANDED);
        mapSection.setExpanded(false);
        td = new TableWrapData(TableWrapData.FILL_GRAB);
        td.colspan = 2;
        td.grabHorizontal = true;
        td.grabVertical = true;

        mapSection.setLayoutData(td);
        mapSection.setText(Messages.SingleActivityViewPart16);
        mapSection.setDescription(Messages.SingleActivityViewPart17);

        final Composite client = toolkit.createComposite(mapSection);
        final TableWrapLayout layout = new TableWrapLayout();
        layout.numColumns = 1;
        layout.topMargin = -60;
        layout.bottomMargin = 5;
        client.setLayout(layout);

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

    /**
     * Herz frequenz
     */
    private void addHeartSection(final Composite body) {
        final Section heartSection = toolkit.createSection(body, Section.DESCRIPTION | Section.TITLE_BAR | Section.TWISTIE | Section.EXPANDED);
        heartSection.setExpanded(true);
        td = new TableWrapData(TableWrapData.FILL_GRAB);
        td.colspan = 2;
        td.grabHorizontal = true;
        td.grabVertical = true;
        heartSection.setLayoutData(td);
        heartSection.setText(Messages.SingleActivityViewPart9);
        heartSection.setDescription(Messages.SingleActivityViewPart10);

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
        speedSection.setExpanded(false);
        td = new TableWrapData(TableWrapData.FILL_GRAB);
        td.colspan = 2;
        td.grabHorizontal = true;
        td.grabVertical = true;
        speedSection.setLayoutData(td);
        speedSection.setText(Messages.SingleActivityViewPart11);
        speedSection.setDescription(Messages.SingleActivityViewPart12);

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

        factory.addChartToComposite(client, ChartType.ALTITUDE_DISTANCE);

        altitude.setClient(client);
    }

    private Label addLabelAndValue(final Composite parent, final String label, final String value, final Units unit) {
        // Label
        final Label dauerLabel = toolkit.createLabel(parent, label + ": "); //$NON-NLS-1$
        GridDataFactory.swtDefaults().applyTo(dauerLabel);

        // value
        final Label dauer = toolkit.createLabel(parent, value);
        GridDataFactory.swtDefaults().indent(10, 4).align(SWT.RIGHT, SWT.CENTER).applyTo(dauer);

        // einheit
        final Label einheit = toolkit.createLabel(parent, unit.getName());
        GridDataFactory.swtDefaults().indent(10, 4).align(SWT.LEFT, SWT.CENTER).applyTo(einheit);

        return dauer;
    }

    @Override
    public void addSelectionChangedListener(final ISelectionChangedListener l) {

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
