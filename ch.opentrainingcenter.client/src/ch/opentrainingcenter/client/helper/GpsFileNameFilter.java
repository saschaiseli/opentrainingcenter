package ch.opentrainingcenter.client.helper;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Map;

import ch.opentrainingcenter.importer.ConvertContainer;
import ch.opentrainingcenter.importer.IConvert2Tcx;

public class GpsFileNameFilter implements FilenameFilter {

    private final ConvertContainer cc;

    public GpsFileNameFilter(final Map<String, IConvert2Tcx> converters) {
        cc = new ConvertContainer(converters);

    }

    @Override
    public boolean accept(final File dir, final String name) {
        for (final String suffix : cc.getSupportedFileSuffixes()) {
            if (name != null && name.endsWith(suffix.replace("*.", ""))) { //$NON-NLS-1$ //$NON-NLS-2$
                return true;
            }
        }
        return false;
    }

}
