package ch.opentrainingcenter.client.views.dialoge;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ComboBoxCellEditor;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.TableViewer;

import ch.opentrainingcenter.model.importer.IGpsFileModel;
import ch.opentrainingcenter.transfer.TrainingType;

class RunTypeEditingSupport extends EditingSupport {

    private final TableViewer viewer;

    public RunTypeEditingSupport(final TableViewer viewer) {
        super(viewer);
        this.viewer = viewer;
    }

    @Override
    protected void setValue(final Object element, final Object value) {
        final IGpsFileModel model = (IGpsFileModel) element;
        model.setTyp(TrainingType.getByIndex((Integer) value));
        getViewer().refresh();
    }

    @Override
    protected Object getValue(final Object element) {
        final IGpsFileModel model = (IGpsFileModel) element;
        return model.getTyp().getIndex();
    }

    @Override
    protected CellEditor getCellEditor(final Object element) {
        return new ComboBoxCellEditor(viewer.getTable(), TrainingType.getAllTypes());
    }

    @Override
    protected boolean canEdit(final Object element) {
        return true;
    }

}
