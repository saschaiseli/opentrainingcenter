package ch.iseli.sportanalyzer.importer;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;

import ch.iseli.sportanalyzer.client.Application;
import ch.iseli.sportanalyzer.client.Messages;
import ch.iseli.sportanalyzer.client.cache.TrainingCenterDataCache;
import ch.iseli.sportanalyzer.client.views.navigation.NavigationView;
import ch.iseli.sportanalyzer.db.DatabaseAccessFactory;
import ch.iseli.sportanalyzer.tcx.ActivityT;
import ch.opentrainingcenter.transfer.IAthlete;

public class ImportJob extends Job {

    private static final Logger logger = Logger.getLogger(NavigationView.class);

    private final IAthlete athlete;

    private final ConvertHandler convertHandler;

    public ImportJob(final String name, final IAthlete athlete) {
        super(name);
        this.athlete = athlete;
        final IConfigurationElement[] configurationElementsFor = Platform.getExtensionRegistry()
                .getConfigurationElementsFor(Application.IMPORT_EXTENSION_POINT);
        convertHandler = getConverterImplementation(configurationElementsFor);
    }

    @Override
    protected IStatus run(final IProgressMonitor monitor) {
        final Map<Date, String> importedRecords = DatabaseAccessFactory.getDatabaseAccess().getImportedRecords(athlete);
        final Map<Date, File> loadAllGPSFiles = FindGarminFiles.loadAllGPSFilesFromAthlete(importedRecords);
        monitor.beginTask(Messages.ImportJob_0, loadAllGPSFiles.size());
        try {
            final List<ActivityT> activitiesToStore = new ArrayList<ActivityT>();
            for (final Map.Entry<Date, File> entry : loadAllGPSFiles.entrySet()) {
                final File fileForConverting = entry.getValue();
                monitor.subTask(Messages.ImportJob_1 + fileForConverting);

                final List<ActivityT> activities = convertHandler.getMatchingConverter(fileForConverting).convertActivity(fileForConverting);
                for (final ActivityT activityT : activities) {
                    if (activityT.getId().toGregorianCalendar().getTime().equals(entry.getKey()) && !activitiesToStore.contains(activityT)) {
                        activitiesToStore.add(activityT);
                    }
                }
                monitor.worked(1);
            }
            TrainingCenterDataCache.getInstance().setSelectedProfile(athlete);
            TrainingCenterDataCache.getInstance().addAll(activitiesToStore);
        } catch (final Exception e) {
            logger.error(e.getMessage());
        }
        return Status.OK_STATUS;
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
