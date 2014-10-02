package ch.opentrainingcenter.client.views.dialoge;

import java.util.List;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ComboBoxCellEditor;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.widgets.Table;

import ch.opentrainingcenter.model.importer.IGpsFileModel;
import ch.opentrainingcenter.transfer.IShoe;

public class SchuhEditingSupport extends EditingSupport {

    private final List<IShoe> schuhe;
    private final String[] routenNamen;
    private final Table table;

    public SchuhEditingSupport(final TableViewer viewer, final List<IShoe> schuhe) {
        super(viewer);
        table = viewer.getTable();
        this.schuhe = schuhe;
        routenNamen = new String[schuhe.size()];
        int i = 0;
        for (final IShoe strecke : schuhe) {
            routenNamen[i] = strecke.getSchuhname();
            i++;
        }
    }

    @Override
    protected CellEditor getCellEditor(final Object element) {
        return new ComboBoxCellEditor(table, routenNamen);
    }

    @Override
    protected boolean canEdit(final Object element) {
        return true;
    }

    @Override
    protected Object getValue(final Object element) {
        final IGpsFileModel model = (IGpsFileModel) element;
        int index = 0;
        if (model.getSchuh() != null) {
            index = model.getSchuh().getId();
        }
        return index;
    }

    @Override
    protected void setValue(final Object element, final Object value) {
        final IGpsFileModel model = (IGpsFileModel) element;
        model.setSchuh(schuhe.get((Integer) value));
        getViewer().refresh();
    }

}
