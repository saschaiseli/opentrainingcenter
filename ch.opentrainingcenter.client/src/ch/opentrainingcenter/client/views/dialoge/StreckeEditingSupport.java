package ch.opentrainingcenter.client.views.dialoge;

import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ComboBoxCellEditor;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.TableViewer;

import ch.opentrainingcenter.model.importer.IGpsFileModel;
import ch.opentrainingcenter.transfer.IRoute;

public class StreckeEditingSupport extends EditingSupport {

    private static final Logger LOGGER = Logger.getLogger(StreckeEditingSupport.class);

    private final TableViewer viewer;
    private final String[] routenNamen;
    private final List<IRoute> routen;

    public StreckeEditingSupport(final TableViewer viewer, final List<IRoute> routen) {
        super(viewer);
        this.viewer = viewer;
        this.routen = routen;
        routenNamen = new String[routen.size()];
        int i = 0;
        for (final IRoute route : routen) {
            routenNamen[i] = route.getName();
            i++;
        }
    }

    @Override
    protected CellEditor getCellEditor(final Object element) {
        return new ComboBoxCellEditor(viewer.getTable(), routenNamen);
    }

    @Override
    protected boolean canEdit(final Object element) {
        return true;
    }

    @Override
    protected Object getValue(final Object element) {
        LOGGER.debug(String.format("getValue element '%s'", element)); //$NON-NLS-1$
        final IGpsFileModel model = (IGpsFileModel) element;
        int index = 0;
        if (model.getRoute() != null) {
            index = model.getRoute().getId();
        }
        LOGGER.debug(String.format("getValue index gefunden: '%s'", index)); //$NON-NLS-1$
        return index;
    }

    @Override
    protected void setValue(final Object element, final Object value) {
        LOGGER.debug(String.format("setValue element '%s' value '%s'", element, value)); //$NON-NLS-1$
        final IGpsFileModel model = (IGpsFileModel) element;
        model.setRoute(routen.get((Integer) value));
        viewer.refresh();
    }
}
