package ch.opentrainingcenter.importer.impl;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.jface.preference.IPreferenceStore;

import ch.opentrainingcenter.importer.ConvertContainer;
import ch.opentrainingcenter.importer.FindGarminFiles;
import ch.opentrainingcenter.importer.IConvert2Tcx;
import ch.opentrainingcenter.importer.IImportedConverter;
import ch.opentrainingcenter.tcx.ActivityT;
import ch.opentrainingcenter.transfer.IImported;

public class ImportedConverter implements IImportedConverter {

    public static final Logger LOGGER = Logger.getLogger(ImportedConverter.class);
    private final IPreferenceStore store;
    private final ConvertContainer cc;

    public ImportedConverter(final IPreferenceStore store, final ConvertContainer cc) {
        this.cc = cc;
        if (store == null) {
            throw new IllegalArgumentException("Store darf nicht null sein"); //$NON-NLS-1$
        }
        this.store = store;
    }

    @Override
    public ActivityT convertImportedToActivity(final IImported record) throws FileNotFoundException {
        final String fileName = record.getComments();
        final File file = FindGarminFiles.loadAllGPSFile(fileName, store);
        final List<ActivityT> converted = convertActivity(file);
        if (converted.isEmpty()) {
            throw new FileNotFoundException("Das File " + fileName + " konnte nicht gefunden werden"); //$NON-NLS-1$ //$NON-NLS-2$
        } else {
            return converted.get(0);
        }
    }

    private List<ActivityT> convertActivity(final File file) {
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
}
