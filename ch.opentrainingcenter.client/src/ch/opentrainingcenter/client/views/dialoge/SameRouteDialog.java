package ch.opentrainingcenter.client.views.dialoge;

import java.util.List;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

import ch.opentrainingcenter.client.Activator;
import ch.opentrainingcenter.client.ui.open.OpenTrainingAction;
import ch.opentrainingcenter.client.views.IImageKeys;
import ch.opentrainingcenter.core.helper.DistanceHelper;
import ch.opentrainingcenter.core.helper.TimeHelper;
import ch.opentrainingcenter.i18n.Messages;
import ch.opentrainingcenter.transfer.ITraining;

/**
 * Zeigt Trainings mit potentiell derselben Route an.
 */
public class SameRouteDialog extends TitleAreaDialog {
    private final List<ITraining> trainings;
    private TableViewer viewer;
    private final ITraining ref;
    private final OpenTrainingAction action;

    public SameRouteDialog(final Shell parent, final ITraining referenzTraining, final List<ITraining> trainings) {
        super(parent);
        this.ref = referenzTraining;
        this.trainings = trainings;
        action = new OpenTrainingAction();
    }

    @Override
    protected Control createDialogArea(final Composite parent) {
        setTitle(Messages.SameRouteDialog_Title);
        setTitleImage(Activator.getImageDescriptor(IImageKeys.ROUTE_WIZ).createImage());
        final String datum = TimeHelper.convertDateToString(ref.getDatum());
        final String distanz = DistanceHelper.roundDistanceFromMeterToKm(ref.getLaengeInMeter());
        final String zeit = TimeHelper.convertSecondsToHumanReadableZeit(ref.getDauer());
        setMessage(NLS.bind(Messages.SameRouteDialog_Message, new Object[] { datum, distanz, zeit }));
        final Label separator = new Label(parent, SWT.SEPARATOR | SWT.HORIZONTAL);
        GridDataFactory.fillDefaults().align(SWT.FILL, SWT.TOP).grab(true, false).applyTo(separator);

        final Composite main = new Composite(parent, SWT.NONE);
        GridLayoutFactory.swtDefaults().applyTo(main);
        GridDataFactory.swtDefaults().align(SWT.FILL, SWT.FILL).grab(true, true).applyTo(main);

        viewer = new TableViewer(main, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION | SWT.BORDER);
        createColumns();

        final Table table = viewer.getTable();
        table.setHeaderVisible(true);
        table.setLinesVisible(true);

        viewer.setContentProvider(new ArrayContentProvider());
        viewer.setInput(trainings);
        updateButton();
        viewer.addSelectionChangedListener(new ISelectionChangedListener() {

            @Override
            public void selectionChanged(final SelectionChangedEvent event) {
                updateButton();
            }

        });
        // Layout the viewer
        final GridData gridData = new GridData();
        gridData.verticalAlignment = GridData.FILL;
        gridData.grabExcessHorizontalSpace = true;
        gridData.grabExcessVerticalSpace = true;
        gridData.minimumHeight = 140;
        gridData.horizontalAlignment = GridData.FILL;
        viewer.getControl().setLayoutData(gridData);
        return main;
    }

    private void createColumns() {
        final String[] titles = { Messages.Common_DATUM, Messages.Common_DISTANZ, Messages.Common_ZEIT, Messages.Common_BESCHREIBUNG };
        final int[] bounds = { 100, 100, 100, 180 };

        // Datum
        TableViewerColumn col = createTableViewerColumn(titles[0], bounds[0]);
        col.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public String getText(final Object element) {
                final ITraining training = (ITraining) element;
                return TimeHelper.convertDateToString(training.getDatum());
            }
        });

        // Distanz
        col = createTableViewerColumn(titles[1], bounds[1]);
        col.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public String getText(final Object element) {
                final ITraining training = (ITraining) element;
                return DistanceHelper.roundDistanceFromMeterToKm(training.getLaengeInMeter());
            }
        });

        // Zeit
        col = createTableViewerColumn(titles[2], bounds[2]);
        col.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public String getText(final Object element) {
                final ITraining training = (ITraining) element;
                return TimeHelper.convertSecondsToHumanReadableZeit(training.getDauer());
            }
        });

        // Beschreibung
        col = createTableViewerColumn(titles[3], bounds[3]);
        col.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public String getText(final Object element) {
                final ITraining training = (ITraining) element;
                String result;
                if (training.getNote() != null && training.getNote().length() > 40) {
                    result = training.getNote().substring(0, 39) + "..."; //$NON-NLS-1$
                } else {
                    result = training.getNote();
                }
                return result;
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

    private void updateButton() {
        final StructuredSelection selection = (StructuredSelection) viewer.getSelection();
        if (getButton(Dialog.OK) != null) {
            getButton(Dialog.OK).setEnabled(!selection.isEmpty());
        }
    }

    @Override
    protected void okPressed() {
        action.open(viewer);
        super.okPressed();
    }
}
