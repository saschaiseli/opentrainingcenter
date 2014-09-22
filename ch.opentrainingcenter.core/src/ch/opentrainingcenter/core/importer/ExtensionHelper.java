package ch.opentrainingcenter.core.importer;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;

public final class ExtensionHelper {

    private static final String IMPORT_EXTENSION_POINT = "ch.opentrainingcenter.myimporter"; //$NON-NLS-1$

    private static final Logger LOGGER = Logger.getLogger(ExtensionHelper.class);

    private static final Map<String, IConvert2Tcx> CONVERTERS = new HashMap<String, IConvert2Tcx>();

    static {
        final IConfigurationElement[] extensions = Platform.getExtensionRegistry().getConfigurationElementsFor(IMPORT_EXTENSION_POINT);
        LOGGER.info("Beginne mit Extensions einzulesen......."); //$NON-NLS-1$
        for (final IConfigurationElement element : extensions) {
            try {
                final IConvert2Tcx tcx = (IConvert2Tcx) element.createExecutableExtension(IConvert2Tcx.PROPERETY);
                LOGGER.info("Beginne mit Extensions einzulesen.......: " + element.getAttribute(IConvert2Tcx.PROPERETY)); //$NON-NLS-1$
                CONVERTERS.put(tcx.getFilePrefix().toUpperCase(), tcx);
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
