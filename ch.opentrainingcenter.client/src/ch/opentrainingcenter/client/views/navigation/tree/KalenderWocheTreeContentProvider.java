package ch.opentrainingcenter.client.views.navigation.tree;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

import ch.opentrainingcenter.client.model.navigation.IKalenderWocheNavigationModel;
import ch.opentrainingcenter.client.model.navigation.INavigationParent;

public class KalenderWocheTreeContentProvider implements IStructuredContentProvider, ITreeContentProvider {
    IKalenderWocheNavigationModel model;

    @Override
    public void dispose() {
    }

    @Override
    public void inputChanged(final Viewer viewer, final Object oldInput, final Object newInput) {
        model = (IKalenderWocheNavigationModel) newInput;
    }

    @Override
    public Object[] getChildren(final Object parent) {
        if (parent instanceof INavigationParent) {
            final INavigationParent naviParent = (INavigationParent) parent;
            return naviParent.getChilds().toArray();
        }
        if (parent instanceof Integer) {
            return model.getWeeks((Integer) parent).toArray();
        }
        return null;
    }

    @Override
    public Object getParent(final Object element) {
        return null;
    }

    @Override
    public boolean hasChildren(final Object element) {
        if (element instanceof INavigationParent || element instanceof Integer) {
            return true;
        }
        return false;
    }

    @Override
    public Object[] getElements(final Object inputElement) {
        return model.getParents().toArray();
    }

}
