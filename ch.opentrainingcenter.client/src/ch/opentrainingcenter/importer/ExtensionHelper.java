package ch.opentrainingcenter.importer;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;

import ch.opentrainingcenter.client.Application;

public final class ExtensionHelper {

    public static final Logger LOGGER = Logger.getLogger(ExtensionHelper.class);

    private static final Map<String, IConvert2Tcx> CONVERTERS = new HashMap<String, IConvert2Tcx>();

    static {
        final IConfigurationElement[] extensions = Platform.getExtensionRegistry().getConfigurationElementsFor(Application.IMPORT_EXTENSION_POINT);
        LOGGER.info("Beginne mit Extensions einzulesen......."); //$NON-NLS-1$
        for (final IConfigurationElement element : extensions) {
            try {
                final IConvert2Tcx tcx = (IConvert2Tcx) element.createExecutableExtension(IConvert2Tcx.PROPERETY);
                LOGGER.info("Beginne mit Extensions einzulesen.......: " + element.getAttribute(IConvert2Tcx.PROPERETY)); //$NON-NLS-1$
                getConverters().put(tcx.getFilePrefix(), tcx);
            } catch (final CoreException e) {
                LOGGER.error(e.getMessage());
            }
        }
    }

    private ExtensionHelper() {
    }

    public static Map<String, IConvert2Tcx> getConverters() {
        return CONVERTERS;
    }

}
