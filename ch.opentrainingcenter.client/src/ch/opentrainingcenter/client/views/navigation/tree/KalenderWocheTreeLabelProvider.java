package ch.opentrainingcenter.client.views.navigation.tree;

import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

import ch.opentrainingcenter.client.Activator;
import ch.opentrainingcenter.model.navigation.INavigationItem;
import ch.opentrainingcenter.model.navigation.INavigationParent;

public class KalenderWocheTreeLabelProvider extends ColumnLabelProvider {

    @Override
    public String getToolTipText(final Object element) {
        if (element instanceof INavigationParent) {
            return ((INavigationParent) element).getTooltip();
        } else if (element instanceof Integer) {
            return null;
        } else if (element instanceof INavigationItem) {
            return ((INavigationItem) element).getTooltip();
        } else {
            throw new IllegalArgumentException("Der Typ '" + element + "' ist nicht definiert"); //$NON-NLS-1$ //$NON-NLS-2$
        }
    }

    @Override
    public final String getText(final Object element) {
        if (element instanceof INavigationParent) {
            final INavigationParent parent = (INavigationParent) element;
            return parent.getName();
        } else if (element instanceof Integer) {
            return String.valueOf(element);
        } else if (element instanceof INavigationItem) {
            return getNavigationItemName((INavigationItem) element);
        } else {
            throw new IllegalArgumentException("Der Typ '" + element + "' ist nicht definiert"); //$NON-NLS-1$ //$NON-NLS-2$
        }
    }

    public String getNavigationItemName(final INavigationItem element) {
        final INavigationItem item = element;
        return item.getName();
    }

    @Override
    public final Image getImage(final Object element) {
        if (element instanceof INavigationParent || element instanceof Integer) {
            return PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_OBJ_FOLDER);
        }
        if (element instanceof INavigationItem) {
            return Activator.getImageDescriptor(((INavigationItem) element).getImage()).createImage();
        }
        return PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_OBJ_FILE);
    }
}
