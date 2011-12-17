package ch.iseli.sportanalyzer.client.views.navigation;

import java.util.Collection;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

public class ViewContentProvider implements IStructuredContentProvider, ITreeContentProvider {

    @Override
    public void inputChanged(final Viewer v, final Object oldInput, final Object newInput) {
    }

    @Override
    public void dispose() {
    }

    @Override
    public Object[] getElements(final Object parent) {
        final Collection<?> l = (Collection<?>) parent;
        return l.toArray();
    }

    @Override
    public Object[] getChildren(final Object parentElement) {
        return null;
    }

    @Override
    public Object getParent(final Object element) {
        return null;
    }

    @Override
    public boolean hasChildren(final Object element) {
        return false;
    }

}
