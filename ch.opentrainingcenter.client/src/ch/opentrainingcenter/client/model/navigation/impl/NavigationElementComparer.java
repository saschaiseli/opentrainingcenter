package ch.opentrainingcenter.client.model.navigation.impl;

import org.eclipse.jface.viewers.IElementComparer;

import ch.opentrainingcenter.client.model.KalenderWoche;

public class NavigationElementComparer implements IElementComparer {

    @Override
    public boolean equals(final Object a, final Object b) {
        if (a instanceof KWTraining && b instanceof KWTraining) {
            final KWTraining kwa = (KWTraining) a;
            final KWTraining kwb = (KWTraining) b;
            return kwa.equals(kwb);
        }
        if (a instanceof NavigationParent && b instanceof NavigationParent) {
            final NavigationParent first = (NavigationParent) a;
            final NavigationParent second = (NavigationParent) b;
            final KalenderWoche kwa = first.getKalenderWoche();
            final KalenderWoche kwb = second.getKalenderWoche();
            return kwa.equals(kwb);
        }
        if (a instanceof ConcreteImported && b instanceof ConcreteImported) {
            final ConcreteImported first = (ConcreteImported) a;
            final ConcreteImported second = (ConcreteImported) b;
            return first.getActivityId().equals(second.getActivityId());
        }
        return false;
    }

    @Override
    public int hashCode(final Object element) {
        if (element instanceof KWTraining) {
            final KWTraining kwa = (KWTraining) element;
            return kwa.hashCode();
        }
        if (element instanceof NavigationParent) {
            final NavigationParent first = (NavigationParent) element;
            final KalenderWoche kwa = first.getKalenderWoche();
            return kwa.hashCode();
        }
        if (element instanceof ConcreteImported) {
            final ConcreteImported first = (ConcreteImported) element;
            return first.getActivityId().hashCode();
        }
        return 0;
    }

}
