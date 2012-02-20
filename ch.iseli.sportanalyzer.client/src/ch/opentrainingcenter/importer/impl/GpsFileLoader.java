package ch.opentrainingcenter.importer.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;

import ch.opentrainingcenter.client.Application;
import ch.opentrainingcenter.importer.ConvertHandler;
import ch.opentrainingcenter.importer.FindGarminFiles;
import ch.opentrainingcenter.importer.IConvert2Tcx;
import ch.opentrainingcenter.importer.IGpsFileLoader;
import ch.opentrainingcenter.tcx.ActivityT;
import ch.opentrainingcenter.transfer.IImported;

public class GpsFileLoader implements IGpsFileLoader {

    public static final Logger logger = Logger.getLogger(GpsFileLoader.class);

    private final ConvertHandler convertHandler;

    public GpsFileLoader() {
        final IConfigurationElement[] configurationElementsFor = Platform.getExtensionRegistry()
                .getConfigurationElementsFor(Application.IMPORT_EXTENSION_POINT);
        convertHandler = getConverterImplementation(configurationElementsFor);
    }

    /* (non-Javadoc)
     * @see ch.opentrainingcenter.importer.IGpsFileLoader#convertActivity(ch.opentrainingcenter.transfer.IImported)
     */
    @Override
    public ActivityT convertActivity(final IImported record) throws Exception {
        final String fileName = record.getComments();
        final File file = FindGarminFiles.loadAllGPSFilesFromAthlete(fileName);
        return convertActivity(file).get(0);
    }

    /* (non-Javadoc)
     * @see ch.opentrainingcenter.importer.IGpsFileLoader#convertActivity(java.io.File)
     */
    @Override
    public List<ActivityT> convertActivity(final File file) throws Exception {
        final IConvert2Tcx converter = convertHandler.getMatchingConverter(file);
        final List<ActivityT> activities = new ArrayList<ActivityT>();
        try {
            final List<ActivityT> convertActivity = converter.convertActivity(file);
            activities.addAll(convertActivity);
        } catch (final Exception e1) {
            logger.error("Fehler beim Importeren"); //$NON-NLS-1$
        }
        return Collections.unmodifiableList(activities);
    }

    private ConvertHandler getConverterImplementation(final IConfigurationElement[] configurationElementsFor) {
        final ConvertHandler handler = new ConvertHandler();
        for (final IConfigurationElement element : configurationElementsFor) {
            try {
                final IConvert2Tcx tcx = (IConvert2Tcx) element.createExecutableExtension(IConvert2Tcx.PROPERETY);
                handler.addConverter(tcx);
            } catch (final CoreException e) {
                logger.error(e.getMessage());
            }
        }
        return handler;
    }

}
