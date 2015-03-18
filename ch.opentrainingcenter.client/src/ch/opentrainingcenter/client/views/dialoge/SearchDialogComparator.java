package ch.opentrainingcenter.client.views.dialoge;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.swt.SWT;

import ch.opentrainingcenter.transfer.ITraining;

/**
 * @see http://www.vogella.com/tutorials/EclipseJFaceTableAdvanced/article.html#
 *      jfacetable_comparator
 */
public class SearchDialogComparator extends ViewerComparator {
    private int propertyIndex;
    private static final int DESCENDING = 1;
    private int direction = DESCENDING;

    public SearchDialogComparator() {
        this.propertyIndex = 0;
        direction = DESCENDING;
    }

    public int getDirection() {
        return direction == 1 ? SWT.DOWN : SWT.UP;
    }

    public void setColumn(final int column) {
        if (column == propertyIndex) {
            direction = 1 - direction;
        } else {
            propertyIndex = column;
            direction = DESCENDING;
        }
    }

    @Override
    public int compare(final Viewer viewer, final Object e1, final Object e2) {
        final ITraining p1 = (ITraining) e1;
        final ITraining p2 = (ITraining) e2;
        int rc = 0;
        switch (propertyIndex) {
        case 0:
            rc = Long.compare(p1.getDatum(), p2.getDatum());
            break;
        case 1:
            rc = Double.compare(p1.getLaengeInMeter(), p2.getLaengeInMeter());
            break;
        case 2:
            rc = Double.compare(p1.getDauer(), p2.getDauer());
            break;
        case 3:
            rc = Double.compare(p1.getDauer() / p1.getLaengeInMeter(), p2.getDauer() / p2.getLaengeInMeter());
            break;
        default:
            rc = 0;
        }
        if (direction == DESCENDING) {
            rc = -rc;
        }
        return rc;
    }
}
