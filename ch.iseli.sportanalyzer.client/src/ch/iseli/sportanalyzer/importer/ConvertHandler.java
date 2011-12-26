package ch.iseli.sportanalyzer.importer;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

public class ConvertHandler {

    public static final Logger logger = Logger.getLogger(ConvertHandler.class);

    private final Map<String, IConvert2Tcx> converters;

    public ConvertHandler() {
        this.converters = new HashMap<String, IConvert2Tcx>();
    }

    public void addConverter(final IConvert2Tcx converter) {
        converters.put(converter.getFilePrefix(), converter);
        logger.info("Adding Extension for Fileimporting: " + converter.getFilePrefix()); //$NON-NLS-1$
    }

    /**
     * @param fileToImport
     *            file welches importiert werden möchte
     * @return dem cpmverter, der dieses file brauch
     */
    public IConvert2Tcx getMatchingConverter(final File fileToImport) {
        final String name = fileToImport.getName();
        final String prefix = name.substring(name.indexOf('.') + 1, name.length());
        return converters.get(prefix);
    }

    /**
     * Gibt alle unterstützen File Suffixes zurück. Also zum Beispiel {"*.gmn", "*.fitnesslog"}
     */
    public List<String> getSupportedFileSuffixes() {
        final List<String> suffixes = new ArrayList<String>();
        for (final Map.Entry<String, IConvert2Tcx> entry : converters.entrySet()) {
            suffixes.add("*." + entry.getKey()); //$NON-NLS-1$
        }
        return suffixes;
    }
}
