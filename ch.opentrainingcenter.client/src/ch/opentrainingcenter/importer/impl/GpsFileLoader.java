package ch.opentrainingcenter.importer.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;

import ch.opentrainingcenter.importer.ConvertContainer;
import ch.opentrainingcenter.importer.ExtensionHelper;
import ch.opentrainingcenter.importer.FindGarminFiles;
import ch.opentrainingcenter.importer.IConvert2Tcx;
import ch.opentrainingcenter.importer.IGpsFileLoader;
import ch.opentrainingcenter.tcx.ActivityT;
import ch.opentrainingcenter.transfer.IImported;

public class GpsFileLoader implements IGpsFileLoader {

    public static final Logger LOGGER = Logger.getLogger(GpsFileLoader.class);

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.opentrainingcenter.importer.IGpsFileLoader#convertActivity(java.io
     * .File)
     */
    @Override
    public List<ActivityT> convertActivity(final File file) {
        final ConvertContainer cc = new ConvertContainer(ExtensionHelper.getConverters());
        final IConvert2Tcx converter = cc.getMatchingConverter(file);
        final List<ActivityT> activities = new ArrayList<ActivityT>();
        try {
            final List<ActivityT> convertActivity = converter.convertActivity(file);
            activities.addAll(convertActivity);
        } catch (final Exception e1) {
            LOGGER.error("Fehler beim Importeren"); //$NON-NLS-1$
        }
        return Collections.unmodifiableList(activities);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.opentrainingcenter.importer.IGpsFileLoader#convertActivity(ch.
     * opentrainingcenter.transfer.IImported)
     */
    @Override
    public ActivityT convertActivity(final IImported record) {
        final String fileName = record.getComments();
        final File file = FindGarminFiles.loadAllGPSFilesFromAthlete(fileName);
        return convertActivity(file).get(0);
    }
}
