package ch.opentrainingcenter.client.views.dialoge;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

import ch.opentrainingcenter.client.Activator;
import ch.opentrainingcenter.client.Messages;
import ch.opentrainingcenter.client.model.IGpsFileModel;
import ch.opentrainingcenter.client.model.IGpsFileModelWrapper;
import ch.opentrainingcenter.client.model.ModelFactory;
import ch.opentrainingcenter.client.views.IImageKeys;

public class RunTypeDialog extends TitleAreaDialog {

    private final List<IGpsFileModel> models = new ArrayList<IGpsFileModel>();

    private TableViewer viewer;

    public RunTypeDialog(final Shell parentShell, final String[] fileNames) {
        super(parentShell);

        initModel(fileNames);
    }

    private void initModel(final String[] fileNames) {
        for (final String fileName : fileNames) {
            models.add(ModelFactory.createGpsFileModel(fileName));
        }
    }

    @Override
    protected Control createDialogArea(final Composite parent) {

        setTitle(Messages.RunTypeDialog_0);
        // setMessage("message");
        setTitleImage(Activator.getImageDescriptor(IImageKeys.IMPORT_GPS_GROSS).createImage());

        final GridLayout layout = new GridLayout(2, false);
        parent.setLayout(layout);

        final Label label = new Label(parent, SWT.NONE);
        label.setText(Messages.RunTypeDialog_1);
        label.setLayoutData(new GridData(GridData.GRAB_HORIZONTAL | GridData.HORIZONTAL_ALIGN_FILL));

        viewer = new TableViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION | SWT.BORDER);
        createColumns();

        final Table table = viewer.getTable();
        table.setHeaderVisible(true);
        table.setLinesVisible(true);

        viewer.setContentProvider(new ArrayContentProvider());
        // Get the content for the viewer, setInput will call getElements in the
        // contentProvider

        viewer.setInput(models);

        // Layout the viewer
        final GridData gridData = new GridData();
        gridData.verticalAlignment = GridData.FILL;
        gridData.horizontalSpan = 5;
        gridData.grabExcessHorizontalSpace = true;
        gridData.grabExcessVerticalSpace = true;
        gridData.horizontalAlignment = GridData.FILL;
        viewer.getControl().setLayoutData(gridData);
        // Make the selection available to other views
        // workbench.getSite().setSelectionProvider(viewer);
        return viewer.getControl();
    }

    private void createColumns() {
        final String[] titles = { Messages.RunTypeDialog_2, Messages.RunTypeDialog_3, Messages.RunTypeDialog_4 };
        final int[] bounds = { 80, 250, 250 };

        // Flag
        TableViewerColumn col = createTableViewerColumn(titles[0], bounds[0]);
        col.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public String getText(final Object element) {
                return null;
            }

            @Override
            public Image getImage(final Object element) {
                if (((IGpsFileModel) element).isImportFile()) {
                    return Activator.getImageDescriptor("icons/checked.gif").createImage(); //$NON-NLS-1$
                } else {
                    return Activator.getImageDescriptor("icons/unchecked.gif").createImage(); //$NON-NLS-1$
                }
            }
        });
        col.setEditingSupport(new RunTypeImportEditingSupport(viewer));

        // FileNamen
        col = createTableViewerColumn(titles[1], bounds[1]);
        col.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public String getText(final Object element) {
                final IGpsFileModel record = (IGpsFileModel) element;
                return record.getFileName();
            }
        });

        // Typ
        col = createTableViewerColumn(titles[2], bounds[2]);
        col.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public String getText(final Object element) {
                final IGpsFileModel record = (IGpsFileModel) element;
                return record.getTyp().getTitle();
            }
        });
        col.setEditingSupport(new RunTypeEditingSupport(viewer));
    }

    public IGpsFileModelWrapper getModelWrapper() {
        return ModelFactory.createGpsFileModelWrapper(models);
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
}
