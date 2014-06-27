package ch.opentrainingcenter.client.views.navigation.tree;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.ui.IViewActionDelegate;
import org.eclipse.ui.IViewPart;

import ch.opentrainingcenter.client.views.navigation.KalenderWocheNavigationView;

public class CollapseAll implements IViewActionDelegate {

    private TreeViewer treeViewer;

    @Override
    public void run(final IAction action) {
        treeViewer.collapseAll();
        treeViewer.refresh();
    }

    @Override
    public void selectionChanged(final IAction action, final ISelection selection) {
    }

    @Override
    public void init(final IViewPart view) {
        treeViewer = ((KalenderWocheNavigationView) view).getTreeViewer();
    }

}
