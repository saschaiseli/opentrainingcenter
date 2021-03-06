package ch.opentrainingcenter.core.importer;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConvertContainer {

    private final Map<String, IConvert2Tcx> converters;

    public ConvertContainer(final Map<String, IConvert2Tcx> converters) {
        this.converters = new HashMap<String, IConvert2Tcx>(converters);
    }

    public List<IConvert2Tcx> getAllConverter() {
        return new ArrayList<IConvert2Tcx>(converters.values());
    }

    /**
     * @param fileToImport
     *            file welches importiert werden möchte. Die Fileendung ist
     *            caseinsensitiv. Es wird gross sowie kleinschreibung getestet.
     * @return dem converter, der dieses file braucht
     */
    public IConvert2Tcx getMatchingConverter(final File fileToImport) {
        final String name = fileToImport.getName();
        final String prefix = name.substring(name.indexOf('.') + 1, name.length());
        final IConvert2Tcx result = converters.get(prefix);
        return result != null ? result : converters.get(prefix.toUpperCase());
    }

    /**
     * Gibt alle unterstützen File Suffixes zurück. Einmal UpperCase und einmal
     * LowerCase. Also zum Beispiel {"*.gmn, *.GMN,*.fitnesslog"}
     */
    public List<String> getSupportedFileSuffixes() {
        final List<String> suffixes = new ArrayList<String>();
        for (final Map.Entry<String, IConvert2Tcx> entry : converters.entrySet()) {
            final String key = entry.getKey();
            suffixes.add("*." + key.toLowerCase()); //$NON-NLS-1$
        }
        return suffixes;
    }
}
