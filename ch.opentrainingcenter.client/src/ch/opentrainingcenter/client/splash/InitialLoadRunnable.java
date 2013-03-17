package ch.opentrainingcenter.client.splash;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.preference.IPreferenceStore;

import ch.opentrainingcenter.client.Activator;
import ch.opentrainingcenter.client.cache.HealthCache;
import ch.opentrainingcenter.client.cache.StreckeCache;
import ch.opentrainingcenter.client.views.ApplicationContext;
import ch.opentrainingcenter.client.views.IImageKeys;
import ch.opentrainingcenter.core.PreferenceConstants;
import ch.opentrainingcenter.core.cache.Cache;
import ch.opentrainingcenter.core.cache.ICache;
import ch.opentrainingcenter.core.cache.TrainingCenterDataCache;
import ch.opentrainingcenter.core.db.DatabaseAccessFactory;
import ch.opentrainingcenter.core.db.IDatabaseAccess;
import ch.opentrainingcenter.core.importer.ConvertContainer;
import ch.opentrainingcenter.core.importer.ExtensionHelper;
import ch.opentrainingcenter.core.importer.IImportedConverter;
import ch.opentrainingcenter.core.importer.ImporterFactory;
import ch.opentrainingcenter.i18n.Messages;
import ch.opentrainingcenter.model.cache.TrainingsPlanCache;
import ch.opentrainingcenter.model.navigation.ConcreteHealth;
import ch.opentrainingcenter.model.planing.IPlanungModel;
import ch.opentrainingcenter.model.strecke.StreckeModel;
import ch.opentrainingcenter.tcx.ActivityT;
import ch.opentrainingcenter.transfer.IAthlete;
import ch.opentrainingcenter.transfer.IHealth;
import ch.opentrainingcenter.transfer.IImported;
import ch.opentrainingcenter.transfer.IPlanungWoche;
import ch.opentrainingcenter.transfer.IRoute;

public class InitialLoadRunnable implements IRunnableWithProgress {

    private final IPreferenceStore store = Activator.getDefault().getPreferenceStore();

    private static final Logger LOG = Logger.getLogger(InitialLoadRunnable.class);

    @Override
    public void run(final IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
        final IAthlete athlete = ApplicationContext.getApplicationContext().getAthlete();
        if (athlete != null) {

            final IDatabaseAccess db = DatabaseAccessFactory.getDatabaseAccess();
            loadFirstRecords(monitor, athlete, db);
            loadAllHealths(monitor, athlete, db);
            loadAllPlaene(monitor, athlete, db);
            loadAllRouten(monitor, athlete, db);
        }
    }

    private void loadFirstRecords(final IProgressMonitor monitor, final IAthlete athlete, final IDatabaseAccess db) {
        final List<IImported> allImported = db.getAllImported(athlete, 10);
        final ConvertContainer cc = new ConvertContainer(ExtensionHelper.getConverters());
        final IImportedConverter fileLoader = ImporterFactory.createGpsFileLoader(cc, store.getString(PreferenceConstants.GPS_FILE_LOCATION_PROG));
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
    }

    private void loadAllPlaene(final IProgressMonitor monitor, final IAthlete athlete, final IDatabaseAccess db) {
        int i = 0;
        final List<IPlanungWoche> planungsWoche = db.getPlanungsWoche(athlete);
        final TrainingsPlanCache planCache = TrainingsPlanCache.getInstance();
        for (final IPlanungWoche plan : planungsWoche) {
            final IPlanungModel model = ch.opentrainingcenter.model.ModelFactory.createPlanungModel(athlete, plan.getJahr(), plan.getKw(),
                    plan.getKmProWoche(), plan.isInterval(), plan.getLangerLauf());
            planCache.add(model);
            monitor.subTask(Messages.InitialLoadRunnable_3 + i++);
            LOG.info(Messages.InitialLoadRunnable_4);
        }
    }

    private void loadAllHealths(final IProgressMonitor monitor, final IAthlete athlete, final IDatabaseAccess db) {
        int i = 0;
        final List<IHealth> healths = db.getHealth(athlete);
        final ICache<Integer, ConcreteHealth> healthCache = HealthCache.getInstance();
        for (final IHealth health : healths) {
            healthCache.add(ch.opentrainingcenter.model.ModelFactory.createConcreteHealth(health, IImageKeys.CARDIO3232));
            monitor.subTask(Messages.InitialLoadRunnable_1 + i++);
            LOG.info(Messages.InitialLoadRunnable_2);
        }
    }

    private void loadAllRouten(final IProgressMonitor monitor, final IAthlete athlete, final IDatabaseAccess db) {
        int i = 0;
        final List<IRoute> routen = db.getRoute(athlete);
        final ICache<String, StreckeModel> cache = StreckeCache.getInstance();
        for (final IRoute route : routen) {
            final StreckeModel strecke = ch.opentrainingcenter.model.ModelFactory.createStreckeModel(route, athlete);
            cache.add(strecke);
            monitor.subTask("Strecken laden: " + i++);
            LOG.info("Strecke dem Cache hinzugefügt: " + route + " Strecke: " + strecke); //$NON-NLS-1$ //$NON-NLS-2$
        }
    }
}
