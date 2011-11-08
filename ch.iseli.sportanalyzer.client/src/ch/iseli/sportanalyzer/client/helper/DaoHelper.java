package ch.iseli.sportanalyzer.client.helper;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DaoHelper {

    private static final Logger log = LoggerFactory.getLogger(DaoHelper.class.getName());

    public static Object getDao(IConfigurationElement[] configurationElementsFor, String executableExtension) {
        for (final IConfigurationElement element : configurationElementsFor) {
            try {
                return element.createExecutableExtension(executableExtension);
            } catch (CoreException e) {
                log.info(e.getMessage());
            }
        }
        return null;
    }
}
