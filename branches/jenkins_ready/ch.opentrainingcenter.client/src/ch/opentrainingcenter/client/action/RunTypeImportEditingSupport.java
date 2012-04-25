package ch.opentrainingcenter.client.action;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.CheckboxCellEditor;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;

import ch.opentrainingcenter.client.model.IGpsFileModel;

public class RunTypeImportEditingSupport extends EditingSupport {

    private final TableViewer viewer;

    public RunTypeImportEditingSupport(final TableViewer viewer) {
        super(viewer);
        this.viewer = viewer;
    }

    @Override
    protected CellEditor getCellEditor(final Object element) {
        return new CheckboxCellEditor(null, SWT.CHECK | SWT.READ_ONLY);

    }

    @Override
    protected boolean canEdit(final Object element) {
        return true;
    }

    @Override
    protected Object getValue(final Object element) {
        final IGpsFileModel model = (IGpsFileModel) element;
        return model.isImportFile();

    }

    @Override
    protected void setValue(final Object element, final Object value) {
        final IGpsFileModel model = (IGpsFileModel) element;
        model.setImportFile((Boolean) value);
        viewer.refresh();
    }

}
