package ch.opentrainingcenter.client.preferences;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Collections;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.core.runtime.preferences.DefaultScope;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.osgi.framework.Bundle;

import ch.opentrainingcenter.client.Application;
import ch.opentrainingcenter.client.PreferenceConstants;
import ch.opentrainingcenter.importer.ConvertContainer;
import ch.opentrainingcenter.importer.ExtensionHelper;
import ch.opentrainingcenter.importer.IConvert2Tcx;

/**
 * Class used to initialize default preference values.
 */
public class PreferenceInitializer extends AbstractPreferenceInitializer {

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer#
     * initializeDefaultPreferences()
     */
    @Override
    public void initializeDefaultPreferences() {
        final IEclipsePreferences defaults = DefaultScope.INSTANCE.getNode(Application.ID);
        final Bundle bundle = Platform.getBundle(Application.ID);
        final IPath path = new Path(""); //$NON-NLS-1$
        final URL url = FileLocator.find(bundle, path, Collections.EMPTY_MAP);

        URL fileUrl = null;
        try {
            fileUrl = FileLocator.toFileURL(url);
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
        final File f = new File(fileUrl.getPath());
        defaults.put(PreferenceConstants.GPS_FILE_LOCATION, System.getProperty("user.home")); //$NON-NLS-1$
        defaults.put(PreferenceConstants.GPS_FILE_LOCATION_PROG, f.getAbsolutePath());
        defaults.putInt(PreferenceConstants.SB, 95);
        defaults.putInt(PreferenceConstants.EXTDL, 75);
        defaults.putInt(PreferenceConstants.INTDL, 80);
        defaults.putInt(PreferenceConstants.EXTINTERVALL, 89);
        defaults.putInt(PreferenceConstants.RECOM, 65);
        defaults.put(PreferenceConstants.RECOM_COLOR, "219,76,82"); //$NON-NLS-1$
        defaults.putInt(PreferenceConstants.GA1, 75);
        defaults.put(PreferenceConstants.GA1_COLOR, "96,134,216"); //$NON-NLS-1$
        defaults.putInt(PreferenceConstants.GA12, 85);
        defaults.put(PreferenceConstants.GA12_COLOR, "144,238,144"); //$NON-NLS-1$
        defaults.putInt(PreferenceConstants.GA2, 95);
        defaults.put(PreferenceConstants.GA2_COLOR, "144,238,144"); //$NON-NLS-1$
        defaults.putInt(PreferenceConstants.WSA, 100);
        defaults.put(PreferenceConstants.WSA_COLOR, "144,238,144"); //$NON-NLS-1$

        defaults.put(PreferenceConstants.BACKUP_FILE_LOCATION, System.getProperty("user.home") + File.separator + ".otc/backup"); //$NON-NLS-1$ //$NON-NLS-2$
        final ConvertContainer cc = new ConvertContainer(ExtensionHelper.getConverters());
        for (final IConvert2Tcx tcx : cc.getAllConverter()) {
            defaults.putBoolean(PreferenceConstants.FILE_SUFFIX_FOR_BACKUP + tcx.getFilePrefix(), true);
        }

        // training target
        defaults.putInt(PreferenceConstants.KM_PER_WEEK, 40);

        defaults.put(PreferenceConstants.DISTANCE_CHART_COLOR, "120,39,93"); //$NON-NLS-1$
        defaults.put(PreferenceConstants.DISTANCE_HEART_COLOR, "0,97,73"); //$NON-NLS-1$

        defaults.put(PreferenceConstants.KM_PER_WEEK_COLOR_ABOVE, "120,39,93"); //$NON-NLS-1$
        defaults.put(PreferenceConstants.KM_PER_WEEK_COLOR_BELOW, "0,97,73"); //$NON-NLS-1$
    }
}
