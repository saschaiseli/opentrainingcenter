package ch.iseli.sportanalyzer.client.views.navigation;

import java.util.List;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

import ch.iseli.sportanalyzer.client.cache.TrainingCenterDatabaseTChild;
import ch.iseli.sportanalyzer.client.cache.TrainingCenterDatabaseTParent;

public class ViewContentProvider implements IStructuredContentProvider, ITreeContentProvider {

    @Override
    public void inputChanged(Viewer v, Object oldInput, Object newInput) {
    }

    @Override
    public void dispose() {
    }

    @Override
    public Object[] getElements(Object parent) {
        List<?> l = (List<?>) parent;
        return l.toArray();
    }

    @Override
    public Object[] getChildren(Object parentElement) {
        TrainingCenterDatabaseTParent parent = (TrainingCenterDatabaseTParent) parentElement;
        return parent.getChilds().toArray();
    }

    @Override
    public Object getParent(Object element) {
        TrainingCenterDatabaseTChild child = (TrainingCenterDatabaseTChild) element;
        return child.getParent();
    }

    @Override
    public boolean hasChildren(Object element) {
        return element instanceof TrainingCenterDatabaseTParent;
    }

}
