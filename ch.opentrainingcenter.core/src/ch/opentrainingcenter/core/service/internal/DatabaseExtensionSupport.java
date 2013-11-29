package ch.opentrainingcenter.core.service.internal;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;

import ch.opentrainingcenter.core.db.IDatabaseConnection;

public final class DatabaseExtensionSupport {

    private static final Logger LOGGER = Logger.getLogger(DatabaseExtensionSupport.class);

    private DatabaseExtensionSupport() {

    }

    static Map<String, IDatabaseConnection> getDao(final IConfigurationElement[] confItems, final String extensionAttr) {
        LOGGER.info("Anzahl Configuration Elements: " + confItems.length); //$NON-NLS-1$
        final Map<String, IDatabaseConnection> result = new HashMap<>();
        for (final IConfigurationElement element : confItems) {
            try {
                LOGGER.info("Element: " + element.getName()); //$NON-NLS-1$
                LOGGER.info("Namespaceidentifier: " + element.getNamespaceIdentifier()); //$NON-NLS-1$
                final IDatabaseConnection db = (IDatabaseConnection) element.createExecutableExtension(extensionAttr);
                result.put(db.getName(), db);
                LOGGER.info("Extension gefunden."); //$NON-NLS-1$
            } catch (final CoreException e) {
                LOGGER.error("Extension nicht gefunden: ", e); //$NON-NLS-1$
            }
        }
        return Collections.unmodifiableMap(result);
    }
}
