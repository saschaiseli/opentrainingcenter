package ch.iseli.sportanalyzer.importer;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;

import ch.iseli.sportanalyzer.client.cache.TrainingCenterDataCache;
import ch.iseli.sportanalyzer.client.cache.TrainingCenterRecord;
import ch.iseli.sportanalyzer.client.views.navigation.NavigationView;
import ch.iseli.sportanalyzer.db.DatabaseAccessFactory;
import ch.iseli.sportanalyzer.tcx.TrainingCenterDatabaseT;
import ch.opentrainingcenter.transfer.IAthlete;

public class ImportJob extends Job {

    private static final Logger logger = Logger.getLogger(NavigationView.class);

    private static final String CH_ISELI_SPORTANALYZER_MYIMPORTER = "ch.iseli.sportanalyzer.myimporter"; //$NON-NLS-1$

    final Map<Integer, TrainingCenterRecord> allRuns = new HashMap<Integer, TrainingCenterRecord>();
    private final IConvert2Tcx tcx;

    private final IAthlete athlete;

    public ImportJob(final String name, final IAthlete athlete) {
        super(name);
        this.athlete = athlete;
        final IConfigurationElement[] configurationElementsFor = Platform.getExtensionRegistry().getConfigurationElementsFor(CH_ISELI_SPORTANALYZER_MYIMPORTER);
        this.tcx = getConverterImplementation(configurationElementsFor);
    }

    @Override
    protected IStatus run(final IProgressMonitor monitor) {
        final Map<Integer, String> importedRecords = DatabaseAccessFactory.getDatabaseAccess().getImportedRecords(athlete);
        final Map<Integer, File> loadAllGPSFiles = FindGarminFiles.loadAllGPSFilesFromAthlete(importedRecords);
        monitor.beginTask("Lade GPS Daten", loadAllGPSFiles.size());
        try {
            for (final Map.Entry<Integer, File> entry : loadAllGPSFiles.entrySet()) {
                monitor.subTask("Importiere File: " + entry.getValue());
                final TrainingCenterDatabaseT record = tcx.convert(entry.getValue());
                allRuns.put(entry.getKey(), new TrainingCenterRecord(entry.getKey(), record));
                monitor.worked(1);
            }
            TrainingCenterDataCache.getInstance().setSelectedProfile(athlete);
            TrainingCenterDataCache.getInstance().addAll(allRuns);
        } catch (final Exception e) {
            logger.error(e.getMessage());
        }
        return Status.OK_STATUS;
    }

    private IConvert2Tcx getConverterImplementation(final IConfigurationElement[] configurationElementsFor) {
        for (final IConfigurationElement element : configurationElementsFor) {
            try {
                return (IConvert2Tcx) element.createExecutableExtension("class"); //$NON-NLS-1$
            } catch (final CoreException e) {
                System.err.println(e.getMessage());
            }
        }
        return null;
    }
}
