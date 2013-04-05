package ch.opentrainingcenter.core.importer.internal;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;

import ch.opentrainingcenter.core.importer.ConvertContainer;
import ch.opentrainingcenter.core.importer.FindGarminFiles;
import ch.opentrainingcenter.core.importer.IConvert2Tcx;
import ch.opentrainingcenter.core.importer.IImportedConverter;
import ch.opentrainingcenter.transfer.ITraining;

public class ImportedConverter implements IImportedConverter {

    public static final Logger LOGGER = Logger.getLogger(ImportedConverter.class);
    private final ConvertContainer cc;
    private final String gpsFileLocation;

    public ImportedConverter(final ConvertContainer cc, final String gpsFileLocation) {
        this.cc = cc;
        this.gpsFileLocation = gpsFileLocation;
        if (gpsFileLocation == null) {
            throw new IllegalArgumentException("gpsFileLocation darf nicht null sein"); //$NON-NLS-1$
        }
    }

    @Override
    public ITraining convertImportedToActivity(final ITraining record) throws FileNotFoundException {
        final String fileName = record.getFileName();
        final File file = FindGarminFiles.loadAllGPSFile(fileName, gpsFileLocation);
        final List<ITraining> converted = convertActivity(file);
        if (converted.isEmpty()) {
            throw new FileNotFoundException("Das File " + fileName + " konnte nicht gefunden werden"); //$NON-NLS-1$ //$NON-NLS-2$
        } else {
            return converted.get(0);
        }
    }

    private List<ITraining> convertActivity(final File file) {
        final IConvert2Tcx converter = cc.getMatchingConverter(file);
        final List<ITraining> activities = new ArrayList<ITraining>();
        try {
            final List<ITraining> convertActivity = converter.convertActivity(file);
            activities.addAll(convertActivity);
        } catch (final Exception e1) {
            LOGGER.error("Fehler beim Importeren"); //$NON-NLS-1$
        }
        return Collections.unmodifiableList(activities);
    }
}
