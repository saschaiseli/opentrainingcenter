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
import ch.opentrainingcenter.core.cache.Cache;
import ch.opentrainingcenter.core.cache.ICache;
import ch.opentrainingcenter.core.cache.TrainingCache;
import ch.opentrainingcenter.core.db.DatabaseAccessFactory;
import ch.opentrainingcenter.core.db.IDatabaseAccess;
import ch.opentrainingcenter.i18n.Messages;
import ch.opentrainingcenter.model.cache.TrainingsPlanCache;
import ch.opentrainingcenter.model.navigation.ConcreteHealth;
import ch.opentrainingcenter.model.planing.IPlanungModel;
import ch.opentrainingcenter.model.strecke.StreckeModel;
import ch.opentrainingcenter.transfer.IAthlete;
import ch.opentrainingcenter.transfer.IHealth;
import ch.opentrainingcenter.transfer.IPlanungWoche;
import ch.opentrainingcenter.transfer.IRoute;
import ch.opentrainingcenter.transfer.ITraining;

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
        monitor.subTask(Messages.InitialLoadRunnable_0);
        final List<ITraining> allImported = db.getAllImported(athlete, 10);
        final Cache cache = TrainingCache.getInstance();
        cache.addAll(allImported);
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
            monitor.subTask(Messages.InitialLoadRunnable_5 + i++);
            LOG.info("Strecke dem Cache hinzugefügt: " + route + " Strecke: " + strecke); //$NON-NLS-1$ //$NON-NLS-2$
        }
    }
}
