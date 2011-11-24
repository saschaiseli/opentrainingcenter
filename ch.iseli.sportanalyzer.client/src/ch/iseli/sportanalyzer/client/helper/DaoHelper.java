package ch.iseli.sportanalyzer.client.helper;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;

public class DaoHelper {

    private static final Logger log = Logger.getLogger(DaoHelper.class.getName());

    public static Object getDao(IConfigurationElement[] confItems, String extensionAttr) {
        log.info("Anzahl Configuration Elements: " + confItems.length);
        for (final IConfigurationElement element : confItems) {
            try {
                log.info("plugin extension suchen: " + extensionAttr);
                String[] attributeNames = element.getAttributeNames();
                for (String str : attributeNames) {
                    log.info("Attr name: '" + str + "'");
                }
                return element.createExecutableExtension(extensionAttr);
            } catch (CoreException e) {
                log.error("Nicht gefunden: " + e.getMessage());
            }
        }
        return null;
    }
}
