package ch.opentrainingcenter.client.property;

import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.core.expressions.PropertyTester;

import ch.opentrainingcenter.model.navigation.ConcreteImported;
import ch.opentrainingcenter.model.navigation.INavigationParent;

public class SelectionPropertyTester extends PropertyTester {

    private static final Logger LOGGER = Logger.getLogger(SelectionPropertyTester.class);

    public static final String PROPERTY_NAMESPACE = "ch.opentrainingcenter.client.selection"; //$NON-NLS-1$
    public static final String PROPERTY_SELECTION = "navigationSelection"; //$NON-NLS-1$

    public SelectionPropertyTester() {
    }

    @Override
    public boolean test(final Object receiver, final String property, final Object[] args, final Object expectedValue) {
        boolean result = false;
        @SuppressWarnings("unchecked")
        final List<Object> input = (List<Object>) receiver;
        if (!input.isEmpty()) {
            final Object object = input.get(0);
            if (!isValidInstance(object)) {
                result = false;
                LOGGER.debug(String.format("Falsche Instanz %s", object)); //$NON-NLS-1$
            } else {
                final Class<? extends Object> clazz = object.getClass();
                result = true;
                for (final Object obj : input) {
                    if (!clazz.isInstance(obj)) {
                        result = false;
                        LOGGER.debug(String.format("Nicht identische Instanz %s", object)); //$NON-NLS-1$
                        break;
                    }
                }
            }
        }
        LOGGER.debug(String.format("Result: %s", result)); //$NON-NLS-1$
        return result;
    }

    private boolean isValidInstance(final Object object) {
        return object instanceof INavigationParent || object instanceof ConcreteImported;
    }

}
