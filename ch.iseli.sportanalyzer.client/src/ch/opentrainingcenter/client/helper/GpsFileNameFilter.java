package ch.opentrainingcenter.client.helper;

import java.io.File;
import java.io.FilenameFilter;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;

import ch.opentrainingcenter.client.Application;
import ch.opentrainingcenter.importer.ConvertHandler;
import ch.opentrainingcenter.importer.IConvert2Tcx;

public class GpsFileNameFilter implements FilenameFilter {

    private static final Logger logger = Logger.getLogger(GpsFileNameFilter.class.getName());
    private final List<String> supportedFileSuffixes;

    public GpsFileNameFilter() {
        final IConfigurationElement[] extensions = Platform.getExtensionRegistry().getConfigurationElementsFor(Application.IMPORT_EXTENSION_POINT);
        logger.info("Anzahl Extensions: " + extensions.length); //$NON-NLS-1$
        final ConvertHandler handler = getConverterImplementation(extensions);
        supportedFileSuffixes = handler.getSupportedFileSuffixes();
    }

    @Override
    public boolean accept(final File dir, final String name) {
        for (final String suffix : supportedFileSuffixes) {
            if (name != null && name.endsWith(suffix.replace("*.", ""))) { //$NON-NLS-1$ //$NON-NLS-2$
                return true;
            }
        }
        return false;
    }

    private ConvertHandler getConverterImplementation(final IConfigurationElement[] configurationElementsFor) {
        final ConvertHandler handler = new ConvertHandler();
        logger.info("Beginne mit Extensions einzulesen......."); //$NON-NLS-1$
        for (final IConfigurationElement element : configurationElementsFor) {
            try {
                final IConvert2Tcx tcx = (IConvert2Tcx) element.createExecutableExtension(IConvert2Tcx.PROPERETY);
                logger.info("Beginne mit Extensions einzulesen.......: " + element.getAttribute(IConvert2Tcx.PROPERETY)); //$NON-NLS-1$
                handler.addConverter(tcx);
            } catch (final CoreException e) {
                logger.error(e.getMessage());
            }
        }
        return handler;
    }
}
