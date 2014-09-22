package ch.opentrainingcenter.core.helper;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Map;

import org.apache.log4j.Logger;

import ch.opentrainingcenter.core.importer.ConvertContainer;
import ch.opentrainingcenter.core.importer.IConvert2Tcx;

public class GpsFileNameFilter implements FilenameFilter {

    private static final Logger LOG = Logger.getLogger(GpsFileNameFilter.class);

    private final ConvertContainer cc;

    public GpsFileNameFilter(final Map<String, IConvert2Tcx> converters) {
        cc = new ConvertContainer(converters);

    }

    @Override
    public boolean accept(final File dir, final String name) {
        for (final String suffix : cc.getSupportedFileSuffixes()) {
            final String endung = suffix.replace("*.", "");//$NON-NLS-1$ //$NON-NLS-2$
            if (name != null) {
                final String nameEndung = name.substring(name.indexOf('.') + 1, name.length());
                if (nameEndung.equalsIgnoreCase(endung)) {
                    LOG.info(String.format("'%s' wird exportiert", name)); //$NON-NLS-1$
                    return true;
                }
            }
        }
        LOG.debug(String.format("'%s' wird NICHT exportiert, da es dem Filter nicht entspricht", name)); //$NON-NLS-1$
        return false;
    }
}