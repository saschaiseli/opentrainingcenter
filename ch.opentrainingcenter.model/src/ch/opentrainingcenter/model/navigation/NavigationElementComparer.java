package ch.opentrainingcenter.model.navigation;

import org.eclipse.jface.viewers.IElementComparer;

import ch.opentrainingcenter.model.navigation.internal.KWTraining;
import ch.opentrainingcenter.model.navigation.internal.NavigationParent;

public class NavigationElementComparer implements IElementComparer {

    @Override
    public boolean equals(final Object a, final Object b) {
        final boolean result;
        if (a instanceof Integer && b instanceof Integer) {
            final Integer kwa = (Integer) a;
            final Integer kwb = (Integer) b;
            result = kwb.equals(kwa);
        } else if (a instanceof KWTraining && b instanceof KWTraining) {
            final KWTraining kwa = (KWTraining) a;
            final KWTraining kwb = (KWTraining) b;
            result = kwa.equals(kwb);
        } else if (a instanceof NavigationParent && b instanceof NavigationParent) {
            final NavigationParent first = (NavigationParent) a;
            final NavigationParent second = (NavigationParent) b;
            final KalenderWoche kwa = first.getKalenderWoche();
            final KalenderWoche kwb = second.getKalenderWoche();
            result = kwa.equals(kwb);
        } else if (a instanceof ConcreteImported && b instanceof ConcreteImported) {
            final ConcreteImported first = (ConcreteImported) a;
            final ConcreteImported second = (ConcreteImported) b;
            result = first.getDatum() == second.getDatum();
        } else {
            result = false;
        }
        return result;
    }

    @Override
    public int hashCode(final Object element) {
        final int hashCode;
        if (element instanceof KWTraining) {
            final KWTraining kwa = (KWTraining) element;
            hashCode = kwa.hashCode();
        } else if (element instanceof NavigationParent) {
            final NavigationParent first = (NavigationParent) element;
            final KalenderWoche kwa = first.getKalenderWoche();
            hashCode = kwa.hashCode();
        } else if (element instanceof ConcreteImported) {
            final ConcreteImported first = (ConcreteImported) element;
            hashCode = (int) first.getDatum();
        } else if (element instanceof Integer) {
            final Integer first = (Integer) element;
            hashCode = first.hashCode();
        } else {
            hashCode = 0;
        }
        return hashCode;
    }

}
