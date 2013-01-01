package ch.opentrainingcenter.client.views.dialoge;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.TableViewer;

public class StreckeEditingSupport extends EditingSupport {
    private final TableViewer viewer;

    public StreckeEditingSupport(final TableViewer viewer) {
        super(viewer);
        this.viewer = viewer;
    }

    @Override
    protected CellEditor getCellEditor(final Object element) {
        return null;
    }

    @Override
    protected boolean canEdit(final Object element) {
        return false;
    }

    @Override
    protected Object getValue(final Object element) {
        return null;
    }

    @Override
    protected void setValue(final Object element, final Object value) {
    }

}
