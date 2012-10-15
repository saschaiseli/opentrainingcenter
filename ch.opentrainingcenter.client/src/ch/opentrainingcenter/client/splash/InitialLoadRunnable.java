package ch.opentrainingcenter.client.splash;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.preference.IPreferenceStore;

import ch.opentrainingcenter.client.Activator;
import ch.opentrainingcenter.client.Messages;
import ch.opentrainingcenter.client.cache.Cache;
import ch.opentrainingcenter.client.cache.ICache;
import ch.opentrainingcenter.client.cache.impl.HealthCache;
import ch.opentrainingcenter.client.cache.impl.TrainingCenterDataCache;
import ch.opentrainingcenter.client.views.ApplicationContext;
import ch.opentrainingcenter.db.DatabaseAccessFactory;
import ch.opentrainingcenter.db.IDatabaseAccess;
import ch.opentrainingcenter.importer.ConvertContainer;
import ch.opentrainingcenter.importer.ExtensionHelper;
import ch.opentrainingcenter.importer.IImportedConverter;
import ch.opentrainingcenter.importer.ImporterFactory;
import ch.opentrainingcenter.tcx.ActivityT;
import ch.opentrainingcenter.transfer.IAthlete;
import ch.opentrainingcenter.transfer.IHealth;
import ch.opentrainingcenter.transfer.IImported;

public class InitialLoadRunnable implements IRunnableWithProgress {

    private final IPreferenceStore store = Activator.getDefault().getPreferenceStore();

    private static final Logger LOG = Logger.getLogger(InitialLoadRunnable.class);

    @Override
    public void run(final IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
        final IAthlete athlete = ApplicationContext.getApplicationContext().getAthlete();
        if (athlete != null) {

            final IDatabaseAccess databaseAccess = DatabaseAccessFactory.getDatabaseAccess();
            final List<IImported> allImported = databaseAccess.getAllImported(athlete, 10);
            final ConvertContainer cc = new ConvertContainer(ExtensionHelper.getConverters());
            final IImportedConverter fileLoader = ImporterFactory.createGpsFileLoader(store, cc);
            final Cache cache = TrainingCenterDataCache.getInstance();
            int i = 1;
            for (final IImported record : allImported) {
                ActivityT activity;
                try {
                    activity = fileLoader.convertImportedToActivity(record);
                    monitor.subTask(Messages.InitialLoadRunnable_0 + i);
                    i++;
                    cache.add(activity);
                    LOG.info("Record hinzugefügt"); //$NON-NLS-1$
                } catch (final Exception e) {
                    LOG.error("Fehler im initial load", e); //$NON-NLS-1$
                }
            }
            i = 0;
            final List<IHealth> healths = databaseAccess.getHealth(athlete);
            final ICache<Integer, IHealth> healthCache = HealthCache.getInstance();
            for (final IHealth health : healths) {
                healthCache.add(health);
                monitor.subTask("Gesundheitswerte laden: " + i++);
                LOG.info("Gesundheitswerte dem Cache hinzugefügt");
            }
        }
    }

}
