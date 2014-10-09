package ch.opentrainingcenter.client.views.dialoge;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.preference.IPreferenceStore;
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
import ch.opentrainingcenter.client.views.IImageKeys;
import ch.opentrainingcenter.core.PreferenceConstants;
import ch.opentrainingcenter.i18n.Messages;
import ch.opentrainingcenter.model.ModelFactory;
import ch.opentrainingcenter.model.importer.IGpsFileModel;
import ch.opentrainingcenter.model.importer.IGpsFileModelWrapper;
import ch.opentrainingcenter.transfer.IRoute;
import ch.opentrainingcenter.transfer.IShoe;

public class RunTypeDialog extends TitleAreaDialog {

    private final List<IGpsFileModel> models = new ArrayList<IGpsFileModel>();

    private TableViewer viewer;

    private final List<IRoute> routen;

    private final List<IShoe> schuhe;

    private final IPreferenceStore preferenceStore;

    public RunTypeDialog(final Shell parentShell, final String[] fileNames, final List<IRoute> routen, final List<IShoe> schuhe) {
        super(parentShell);
        this.routen = routen;
        this.schuhe = schuhe;
        preferenceStore = Activator.getDefault().getPreferenceStore();
        initModel(fileNames);
    }

    private void initModel(final String[] fileNames) {
        final IRoute defRoute = routen.isEmpty() ? null : routen.get(0);
        final IShoe defShoe = schuhe.isEmpty() ? null : getDefaultSchuh();
        for (final String fileName : fileNames) {
            final IGpsFileModel model = ModelFactory.createGpsFileModel(fileName);
            model.setRoute(defRoute);
            model.setSchuh(defShoe);
            models.add(model);
        }
    }

    @Override
    protected Control createDialogArea(final Composite parent) {

        setTitle(Messages.RunTypeDialog0);
        setTitleImage(Activator.getImageDescriptor(IImageKeys.IMPORT_GPS_GROSS).createImage());

        final GridLayout layout = new GridLayout(2, false);
        parent.setLayout(layout);

        final Label label = new Label(parent, SWT.NONE);
        label.setText(Messages.RunTypeDialog1);
        label.setLayoutData(new GridData(GridData.GRAB_HORIZONTAL | GridData.HORIZONTAL_ALIGN_FILL));

        viewer = new TableViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION | SWT.BORDER);
        createColumns();

        final Table table = viewer.getTable();
        table.setHeaderVisible(true);
        table.setLinesVisible(true);

        viewer.setContentProvider(new ArrayContentProvider());

        viewer.setInput(models);

        // Layout the viewer
        final GridData gridData = new GridData();
        gridData.verticalAlignment = GridData.FILL;
        gridData.horizontalSpan = 5;
        gridData.grabExcessHorizontalSpace = true;
        gridData.grabExcessVerticalSpace = true;
        gridData.horizontalAlignment = GridData.FILL;
        viewer.getControl().setLayoutData(gridData);
        return viewer.getControl();
    }

    private void createColumns() {
        final String[] titles = { Messages.RunTypeDialog2, Messages.RunTypeDialog3, Messages.RunTypeDialog4, Messages.RunTypeDialog_0,
                Messages.RunTypeDialog_Schuhe };
        final int[] bounds = { 80, 250, 250, 100, 100 };

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
                return record.getTyp().getName();
            }
        });
        col.setEditingSupport(new RunTypeEditingSupport(viewer));

        // Strecke
        col = createTableViewerColumn(titles[3], bounds[3]);
        col.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public String getText(final Object element) {
                return routen.get(0).getName();
            }
        });
        col.setEditingSupport(new StreckeEditingSupport(viewer, routen));

        // Schuh

        col = createTableViewerColumn(titles[4], bounds[4]);
        col.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public String getText(final Object element) {
                return schuhe.isEmpty() ? "" : models.get(0).getSchuh().getSchuhname(); //$NON-NLS-1$
            }
        });
        final SchuhEditingSupport schuhEditingSupport = new SchuhEditingSupport(viewer, schuhe);
        col.setEditingSupport(schuhEditingSupport);
    }

    private IShoe getDefaultSchuh() {
        final String id = preferenceStore.getString(PreferenceConstants.DEFAULT_SCHUH_1);
        for (final IShoe shoe : schuhe) {
            if (String.valueOf(shoe.getId()).equals(id)) {
                return shoe;
            }
        }
        return null;
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
