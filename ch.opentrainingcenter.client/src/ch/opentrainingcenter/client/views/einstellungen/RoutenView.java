package ch.opentrainingcenter.client.views.einstellungen;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.ISelectionService;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.forms.events.ExpansionAdapter;
import org.eclipse.ui.forms.events.ExpansionEvent;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.forms.widgets.TableWrapData;
import org.eclipse.ui.forms.widgets.TableWrapLayout;
import org.eclipse.ui.part.ViewPart;

import ch.opentrainingcenter.client.views.ApplicationContext;
import ch.opentrainingcenter.core.db.DatabaseAccessFactory;
import ch.opentrainingcenter.core.db.IDatabaseAccess;
import ch.opentrainingcenter.core.helper.DistanceHelper;
import ch.opentrainingcenter.core.helper.TimeHelper;
import ch.opentrainingcenter.i18n.Messages;
import ch.opentrainingcenter.transfer.IAthlete;
import ch.opentrainingcenter.transfer.IRoute;
import ch.opentrainingcenter.transfer.ITraining;

public class RoutenView extends ViewPart implements ISelectionListener {

    public static final String ID = "ch.opentrainingcenter.client.views.einstellungen.routen"; //$NON-NLS-1$

    private static final Logger LOGGER = Logger.getLogger(RoutenView.class);

    private static final int SWT_TABLE_PATTERN = SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION | SWT.BORDER;

    private final IDatabaseAccess databaseAccess = DatabaseAccessFactory.getDatabaseAccess();

    private final List<IRoute> routen = new ArrayList<>();

    private final List<ITraining> tracks = new ArrayList<>();

    private Composite parent;

    private FormToolkit toolkit;

    private ScrolledForm form;

    private final IAthlete athlete;

    private IAthlete selectedAthlete;

    private TableWrapData td;

    private Section sectionRouten;

    private TableViewer viewerRouten, viewerTracks;

    public RoutenView() {
        athlete = ApplicationContext.getApplicationContext().getAthlete();
    }

    @Override
    public void createPartControl(final Composite parent) {
        LOGGER.info("create Route view"); //$NON-NLS-1$
        this.parent = parent;

        toolkit = new FormToolkit(this.parent.getDisplay());

        form = toolkit.createScrolledForm(this.parent);
        toolkit.decorateFormHeading(form.getForm());
        form.setText(Messages.RoutenView_0);

        final ISelectionService selectionService = getSite().getWorkbenchWindow().getSelectionService();
        selectionService.addSelectionListener(this);

        final TableWrapLayout layout = new TableWrapLayout();
        layout.numColumns = 1;
        layout.makeColumnsEqualWidth = false;

        final Composite body = form.getBody();
        body.setLayout(layout);

        td = new TableWrapData(TableWrapData.FILL_GRAB);
        body.setLayoutData(td);

        sectionRouten = toolkit.createSection(body, Section.DESCRIPTION | ExpandableComposite.TITLE_BAR | ExpandableComposite.TWISTIE
                | ExpandableComposite.EXPANDED);
        sectionRouten.addExpansionListener(new ExpansionAdapter() {
            @Override
            public void expansionStateChanged(final ExpansionEvent e) {
                form.reflow(true);
            }
        });
        sectionRouten.setExpanded(true);
        td = new TableWrapData(TableWrapData.FILL_GRAB);
        td.colspan = 1;
        td.grabHorizontal = true;
        sectionRouten.setLayoutData(td);
        sectionRouten.setText(Messages.RoutenView_1);
        sectionRouten.setDescription(Messages.RoutenView_2);

        final Composite composite = toolkit.createComposite(sectionRouten);
        GridLayoutFactory.swtDefaults().numColumns(2).equalWidth(true).applyTo(composite);
        GridDataFactory.swtDefaults().align(SWT.FILL, SWT.FILL).grab(true, true).applyTo(composite);

        final Composite compositeLeft = new Composite(composite, SWT.NONE);
        GridLayoutFactory.swtDefaults().applyTo(compositeLeft);
        GridDataFactory.swtDefaults().align(SWT.FILL, SWT.FILL).grab(true, true).applyTo(compositeLeft);

        createLeftComposite(compositeLeft);

        final Composite compositeRight = new Composite(composite, SWT.NONE);
        GridLayoutFactory.swtDefaults().applyTo(compositeRight);
        GridDataFactory.swtDefaults().align(SWT.FILL, SWT.FILL).grab(true, true).applyTo(compositeRight);

        createRightComposite(compositeRight);

        final Composite compositeUnten = new Composite(composite, SWT.NONE);
        // compositeUnten.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_MAGENTA));
        GridLayoutFactory.swtDefaults().applyTo(compositeUnten);
        GridDataFactory.swtDefaults().span(2, 1).align(SWT.FILL, SWT.FILL).grab(true, true).applyTo(compositeUnten);

        sectionRouten.setClient(composite);
    }

    private void createLeftComposite(final Composite compositeLeft) {
        final Label headLeft = new Label(compositeLeft, SWT.BOLD);
        headLeft.setText(Messages.RoutenView_3);

        viewerRouten = new TableViewer(compositeLeft, SWT_TABLE_PATTERN);
        createRouteColumns();

        final Table table = viewerRouten.getTable();
        defineTable(table);

        viewerRouten.setContentProvider(new ArrayContentProvider());
        viewerRouten.setInput(routen);

        // Layout the viewer
        final GridData gridData = new GridData();
        gridData.verticalAlignment = GridData.FILL;
        gridData.horizontalSpan = 1;
        gridData.grabExcessHorizontalSpace = true;
        gridData.grabExcessVerticalSpace = true;
        gridData.horizontalAlignment = GridData.FILL;
        gridData.minimumHeight = 300;
        viewerRouten.getControl().setLayoutData(gridData);
    }

    private void createRightComposite(final Composite compositeRight) {
        final Label headRight = new Label(compositeRight, SWT.BOLD);
        headRight.setText(Messages.RoutenView_4);

        viewerTracks = new TableViewer(compositeRight, SWT_TABLE_PATTERN);
        createTrackColumns();

        final Table table = viewerTracks.getTable();
        defineTable(table);

        viewerTracks.setContentProvider(new ArrayContentProvider());
        viewerTracks.setInput(tracks);

        // Layout the viewer
        final GridData gridData = new GridData();
        gridData.verticalAlignment = GridData.FILL;
        gridData.horizontalSpan = 1;
        gridData.grabExcessHorizontalSpace = true;
        gridData.grabExcessVerticalSpace = true;
        gridData.horizontalAlignment = GridData.FILL;
        gridData.minimumHeight = 300;
        viewerTracks.getControl().setLayoutData(gridData);
    }

    private void defineTable(final Table table) {
        table.setHeaderVisible(true);
        table.setLinesVisible(true);
        table.setEnabled(athlete != null);
    }

    private void createTrackColumns() {
        final String[] titles = { Messages.RoutenView_5, Messages.RoutenView_6, Messages.RoutenView_7, Messages.RoutenView_8 };
        final int[] bounds = { 120, 80, 200, 100 };

        TableViewerColumn col = createTrackColumn(titles[0], bounds[0]);
        col.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public String getText(final Object element) {
                final ITraining training = (ITraining) element;
                return String.valueOf(TimeHelper.convertDateToString(new Date(training.getDatum())));
            }
        });

        col = createTrackColumn(titles[1], bounds[1]);
        col.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public String getText(final Object element) {
                final ITraining training = (ITraining) element;
                return DistanceHelper.roundDistanceFromMeterToKm(training.getLaengeInMeter());
            }
        });

        col = createTrackColumn(titles[2], bounds[2]);
        col.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public String getText(final Object element) {
                final ITraining training = (ITraining) element;
                return training.getNote();
            }
        });

        col = createTrackColumn(titles[3], bounds[3]);
        col.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public String getText(final Object element) {
                return "route"; //$NON-NLS-1$
            }
        });
    }

    private void createRouteColumns() {
        final String[] titles = { Messages.RoutenView_9, Messages.RoutenView_10, Messages.RoutenView_11, Messages.RoutenView_6, Messages.RoutenView_13 };
        final int[] bounds = { 40, 100, 200, 100, 40 };

        TableViewerColumn col = createRouteColumn(titles[0], bounds[0]);
        col.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public String getText(final Object element) {
                final IRoute route = (IRoute) element;
                return String.valueOf(route.getId());
            }
        });

        col = createRouteColumn(titles[1], bounds[1]);
        col.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public String getText(final Object element) {
                final IRoute route = (IRoute) element;
                return route.getName();
            }
        });

        col = createRouteColumn(titles[2], bounds[2]);
        col.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public String getText(final Object element) {
                final IRoute route = (IRoute) element;
                return route.getBeschreibung();
            }
        });

        col = createRouteColumn(titles[3], bounds[3]);
        col.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public String getText(final Object element) {
                return "2.123"; //$NON-NLS-1$
            }
        });

        col = createRouteColumn(titles[4], bounds[4]);
        col.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public String getText(final Object element) {
                return "n/a"; //$NON-NLS-1$
            }
        });
    }

    private TableViewerColumn createRouteColumn(final String title, final int bound) {
        final TableViewerColumn viewerColumn = new TableViewerColumn(viewerRouten, SWT.NONE);
        final TableColumn column = viewerColumn.getColumn();
        column.setText(title);
        column.setWidth(bound);
        column.setResizable(true);
        column.setMoveable(true);
        return viewerColumn;
    }

    private TableViewerColumn createTrackColumn(final String title, final int bound) {
        final TableViewerColumn viewerColumn = new TableViewerColumn(viewerTracks, SWT.NONE);
        final TableColumn column = viewerColumn.getColumn();
        column.setText(title);
        column.setWidth(bound);
        column.setResizable(true);
        column.setMoveable(true);
        return viewerColumn;
    }

    @Override
    public void setFocus() {
    }

    @Override
    public void dispose() {
        final ISelectionService s = getSite().getWorkbenchWindow().getSelectionService();
        s.removeSelectionListener(this);
        super.dispose();
    }

    @Override
    public void selectionChanged(final IWorkbenchPart part, final ISelection selection) {
        if (part instanceof UserView && selection instanceof IStructuredSelection) {
            final IStructuredSelection sel = (IStructuredSelection) selection;
            final Object firstElement = sel.getFirstElement();
            routen.clear();
            tracks.clear();
            if (firstElement instanceof IAthlete) {
                selectedAthlete = (IAthlete) firstElement;
                routen.addAll(databaseAccess.getRoute(selectedAthlete));
                tracks.addAll(databaseAccess.getAllImported(selectedAthlete));
                LOGGER.info(String.format("Neuer Athlete selektiert: %s mit %s Routen und %s Anzahl Tracks", firstElement, routen.size(), tracks.size())); //$NON-NLS-1$      
            }
            viewerRouten.refresh();
            viewerTracks.refresh();
        } else {
            LOGGER.info(String.format("Part: %s Selection: %s", part, selection)); //$NON-NLS-1$
        }
    }
}
