package ch.iseli.sportanalyzer.client.helper;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;

public class DaoHelper {

    private static final Logger log = Logger.getLogger(DaoHelper.class.getName());

    public static Object getDao(final IConfigurationElement[] confItems, final String extensionAttr) {
        log.info("Anzahl Configuration Elements: " + confItems.length);
        for (final IConfigurationElement element : confItems) {
            try {
                final String[] attributeNames = element.getAttributeNames();
                for (final String str : attributeNames) {
                    log.info("Attr name: '" + str + "'");
                }
                final Object createExecutableExtension = element.createExecutableExtension(extensionAttr);
                log.info("Extension gefunden...........................");
                return createExecutableExtension;
            } catch (final CoreException e) {
                log.error("Nicht gefunden: " + e.getMessage());
            }
        }
        return null;
    }
}
