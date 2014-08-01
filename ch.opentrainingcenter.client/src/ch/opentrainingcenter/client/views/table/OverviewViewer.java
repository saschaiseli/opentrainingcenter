package ch.opentrainingcenter.client.views.table;

import java.util.Collection;
import java.util.Date;

import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

import ch.opentrainingcenter.client.views.ApplicationContext;
import ch.opentrainingcenter.core.cache.Cache;
import ch.opentrainingcenter.core.cache.IRecordListener;
import ch.opentrainingcenter.core.cache.TrainingCache;
import ch.opentrainingcenter.core.db.IDatabaseAccess;
import ch.opentrainingcenter.core.helper.DistanceHelper;
import ch.opentrainingcenter.core.helper.TimeHelper;
import ch.opentrainingcenter.core.service.IDatabaseService;
import ch.opentrainingcenter.i18n.Messages;
import ch.opentrainingcenter.transfer.ITraining;

public class OverviewViewer extends ViewPart {

    public static final String ID = "ch.opentrainingcenter.client.table.overview"; //$NON-NLS-1$
    private final Cache cache;
    private TableViewer viewer;
    private final IDatabaseAccess databaseAccess;

    public OverviewViewer() {
        cache = TrainingCache.getInstance();
        final IDatabaseService service = (IDatabaseService) PlatformUI.getWorkbench().getService(IDatabaseService.class);
        databaseAccess = service.getDatabaseAccess();
    }

    @Override
    public void createPartControl(final Composite parent) {
        final GridLayout layout = new GridLayout(2, false);
        parent.setLayout(layout);
        final Label searchLabel = new Label(parent, SWT.NONE);
        searchLabel.setText(Messages.OverviewViewer0);
        searchLabel.setLayoutData(new GridData(GridData.GRAB_HORIZONTAL | GridData.HORIZONTAL_ALIGN_FILL));
        createViewer(parent);
    }

    private void createViewer(final Composite parent) {
        viewer = new TableViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION | SWT.BORDER);
        createColumns();
        final Table table = viewer.getTable();
        table.setHeaderVisible(true);
        table.setLinesVisible(true);

        viewer.setContentProvider(new ArrayContentProvider());

        final MenuManager menuManager = new MenuManager("KontextMenu"); //$NON-NLS-1$
        table.setMenu(menuManager.createContextMenu(table));

        getSite().registerContextMenu("ch.opentrainingcenter.client.somepopup", menuManager, viewer); //$NON-NLS-1$
        getSite().setSelectionProvider(viewer);

        getSite().registerContextMenu(menuManager, viewer);

        // Get the content for the viewer, setInput will call getElements in the
        // contentProvider
        viewer.setInput(databaseAccess.getAllTrainings(ApplicationContext.getApplicationContext().getAthlete()));
        // Make the selection available to other views
        getSite().setSelectionProvider(viewer);
        // Set the sorter for the table

        // Layout the viewer
        final GridData gridData = new GridData();
        gridData.verticalAlignment = GridData.FILL;
        gridData.horizontalSpan = 2;
        gridData.grabExcessHorizontalSpace = true;
        gridData.grabExcessVerticalSpace = true;
        gridData.horizontalAlignment = GridData.FILL;
        viewer.getControl().setLayoutData(gridData);

        cache.addListener(new IRecordListener<ITraining>() {

            @Override
            public void recordChanged(final Collection<ITraining> entry) {
                update();
            }

            @Override
            public void deleteRecord(final Collection<ITraining> entry) {
                update();
            }

            private void update() {
                viewer.setInput(databaseAccess.getAllTrainings(ApplicationContext.getApplicationContext().getAthlete()));
                viewer.refresh();
            }
        });

    }

    // This will create the columns for the table
    private void createColumns() {
        final String[] titles = { Messages.OverviewViewer1, Messages.OverviewViewer2, Messages.OverviewViewer3, Messages.OverviewViewer4,
                Messages.OverviewViewer5 };
        final int[] bounds = { 220, 100, 150, 120, 180 };

        // Datum
        TableViewerColumn col = createTableViewerColumn(titles[0], bounds[0]);
        col.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public String getText(final Object element) {
                final ITraining record = (ITraining) element;
                return TimeHelper.convertDateToString(new Date(record.getDatum()), true);
            }
        });

        // Zeit
        col = createTableViewerColumn(titles[1], bounds[1]);
        col.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public String getText(final Object element) {
                final ITraining record = (ITraining) element;
                return TimeHelper.convertSecondsToHumanReadableZeit(record.getDauer());
            }
        });

        // distanz
        col = createTableViewerColumn(titles[2], bounds[2]);
        col.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public String getText(final Object element) {
                final ITraining record = (ITraining) element;
                return DistanceHelper.roundDistanceFromMeterToKm(record.getLaengeInMeter());
            }
        });

        // pace
        col = createTableViewerColumn(titles[3], bounds[3]);
        col.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public String getText(final Object element) {
                final ITraining record = (ITraining) element;
                return DistanceHelper.calculatePace(record.getLaengeInMeter(), record.getDauer(), record.getSport());
            }
        });

        // Herzfrequenz
        col = createTableViewerColumn(titles[4], bounds[4]);
        col.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public String getText(final Object element) {
                final ITraining record = (ITraining) element;
                return String.valueOf(record.getAverageHeartBeat());
            }
        });

    }

    private TableViewerColumn createTableViewerColumn(final String title, final int bound) {
        final TableViewerColumn viewerColumn = new TableViewerColumn(viewer, SWT.NONE);
        final TableColumn column = viewerColumn.getColumn();
        column.setText(title);
        column.setWidth(bound);
        column.setResizable(true);
        column.setMoveable(true);
        return viewerColumn;

    }

    public TableViewer getViewer() {
        return viewer;
    }

    @Override
    public void setFocus() {
        viewer.getControl().setFocus();
    }
}
