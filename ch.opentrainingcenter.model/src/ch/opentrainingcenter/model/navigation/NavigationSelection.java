package ch.opentrainingcenter.model.navigation;

import java.util.ArrayList;
import java.util.List;

import ch.opentrainingcenter.core.assertions.Assertions;
import ch.opentrainingcenter.transfer.ITraining;

public class NavigationSelection {

    /**
     * Die Selection muss immer von demselben Typ sein. Mit dem
     * SelectionPropertyTester wird garantiert, dass die Objekte immer von
     * derselben Instanz sind.
     */
    @SuppressWarnings("unchecked")
    public List<ITraining> convertSelectionToTrainings(final List<?> selection) {
        Assertions.isValid(selection == null, "Selection darf nicht null sein"); //$NON-NLS-1$
        Assertions.isValid(selection.isEmpty(), "Selection darf nicht leer sein"); //$NON-NLS-1$
        final List<ITraining> result = new ArrayList<>();
        if (isEachElementSameInstance(selection, INavigationParent.class)) {
            for (final INavigationParent parent : (List<INavigationParent>) selection) {
                final List<INavigationItem> childs = parent.getChilds();
                for (final INavigationItem child : childs) {
                    result.add(((ConcreteImported) child).getTraining());
                }
            }
        } else if (isEachElementSameInstance(selection, ConcreteImported.class)) {
            for (final ConcreteImported item : (List<ConcreteImported>) selection) {
                result.add(item.getTraining());
            }
        } else {
            throw new IllegalArgumentException("Selection ist weder vom Typ ConcreteImported noch vom INavigationParent"); //$NON-NLS-1$
        }
        return result;
    }

    private boolean isEachElementSameInstance(final List<?> selection, final Class<? extends Object> clazz) {
        boolean result = true;
        for (final Object object : selection) {
            if (!(clazz.isInstance(object))) {
                result = false;
                break;
            }
        }
        return result;
    }

}
