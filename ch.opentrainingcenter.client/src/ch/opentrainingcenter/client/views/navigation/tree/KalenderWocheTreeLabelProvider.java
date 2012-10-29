package ch.opentrainingcenter.client.views.navigation.tree;

import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

import ch.opentrainingcenter.client.Activator;
import ch.opentrainingcenter.client.model.navigation.INavigationItem;
import ch.opentrainingcenter.client.model.navigation.INavigationParent;

public class KalenderWocheTreeLabelProvider extends LabelProvider {
    @Override
    public String getText(final Object element) {
        if (element instanceof INavigationParent) {
            final INavigationParent parent = (INavigationParent) element;
            return parent.getName();
        } else {
            final INavigationItem item = (INavigationItem) element;
            return item.getName();
        }
    }

    @Override
    public Image getImage(final Object element) {
        if (element instanceof INavigationParent) {
            return PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_OBJ_FOLDER);
        }
        if (element instanceof INavigationItem) {
            return Activator.getImageDescriptor(((INavigationItem) element).getImage()).createImage();
        }
        return PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_OBJ_FILE);
    }
}
