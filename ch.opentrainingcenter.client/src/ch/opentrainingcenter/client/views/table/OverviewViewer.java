package ch.opentrainingcenter.client.views.table;

import java.util.Collection;

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
import org.eclipse.ui.part.ViewPart;

import ch.opentrainingcenter.client.Messages;
import ch.opentrainingcenter.client.cache.IRecordListener;
import ch.opentrainingcenter.client.cache.TrainingCenterDataCache;
import ch.opentrainingcenter.client.helper.DistanceHelper;
import ch.opentrainingcenter.client.helper.TimeHelper;
import ch.opentrainingcenter.tcx.ActivityT;
import ch.opentrainingcenter.transfer.IImported;
import ch.opentrainingcenter.transfer.ITraining;

public class OverviewViewer extends ViewPart {

    public static final String ID = "ch.opentrainingcenter.client.table.overview"; //$NON-NLS-1$
    private TableViewer viewer;

    public OverviewViewer() {
    }

    @Override
    public void createPartControl(final Composite parent) {
        final GridLayout layout = new GridLayout(2, false);
        parent.setLayout(layout);
        final Label searchLabel = new Label(parent, SWT.NONE);
        searchLabel.setText(Messages.OverviewViewer_0);
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
        // Get the content for the viewer, setInput will call getElements in the
        // contentProvider
        viewer.setInput(TrainingCenterDataCache.getInstance().getAllActivities());
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

        final TrainingCenterDataCache cache = TrainingCenterDataCache.getInstance();
        cache.addListener(new IRecordListener() {

            @Override
            public void recordChanged(final Collection<ActivityT> entry) {
                update();
            }

            @Override
            public void deleteRecord(final Collection<ActivityT> entry) {
                update();
            }

            private void update() {
                viewer.setInput(TrainingCenterDataCache.getInstance().getAllActivities());
                viewer.refresh();
            }
        });

    }

    // This will create the columns for the table
    private void createColumns() {
        final String[] titles = { Messages.OverviewViewer_1, Messages.OverviewViewer_2, Messages.OverviewViewer_3, Messages.OverviewViewer_4, Messages.OverviewViewer_5 };
        final int[] bounds = { 220, 100, 150, 120, 180 };

        // Datum
        TableViewerColumn col = createTableViewerColumn(titles[0], bounds[0]);
        col.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public String getText(final Object element) {
                final IImported record = (IImported) element;
                return TimeHelper.convertDateToString(record.getActivityId(), true);
            }
        });

        // Zeit
        col = createTableViewerColumn(titles[1], bounds[1]);
        col.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public String getText(final Object element) {
                final IImported record = (IImported) element;
                return TimeHelper.convertSecondsToHumanReadableZeit(record.getTraining().getDauerInSekunden());
            }
        });

        // distanz
        col = createTableViewerColumn(titles[2], bounds[2]);
        col.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public String getText(final Object element) {
                final IImported record = (IImported) element;
                return DistanceHelper.roundDistanceFromMeterToKm(record.getTraining().getLaengeInMeter());
            }
        });

        // pace
        col = createTableViewerColumn(titles[3], bounds[3]);
        col.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public String getText(final Object element) {
                final IImported record = (IImported) element;
                final ITraining training = record.getTraining();
                return DistanceHelper.calculatePace(training.getLaengeInMeter(), training.getDauerInSekunden());
            }
        });

        // Herzfrequenz
        col = createTableViewerColumn(titles[4], bounds[4]);
        col.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public String getText(final Object element) {
                final IImported record = (IImported) element;
                final ITraining training = record.getTraining();
                return String.valueOf(training.getAverageHeartBeat());
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
