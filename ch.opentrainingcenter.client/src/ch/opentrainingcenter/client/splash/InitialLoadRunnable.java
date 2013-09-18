package ch.opentrainingcenter.client.splash;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.joda.time.DateTime;

import ch.opentrainingcenter.client.cache.HealthCache;
import ch.opentrainingcenter.client.cache.StreckeCache;
import ch.opentrainingcenter.client.views.ApplicationContext;
import ch.opentrainingcenter.client.views.IImageKeys;
import ch.opentrainingcenter.core.cache.Cache;
import ch.opentrainingcenter.core.cache.ICache;
import ch.opentrainingcenter.core.cache.TrainingCache;
import ch.opentrainingcenter.core.data.OTCKonstanten;
import ch.opentrainingcenter.core.db.DatabaseAccessFactory;
import ch.opentrainingcenter.core.db.IDatabaseAccess;
import ch.opentrainingcenter.core.helper.AltitudeCalculator;
import ch.opentrainingcenter.core.helper.AltitudeCalculator.Ascending;
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
            doMaintenance(monitor, athlete, db);
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

    private void doMaintenance(final IProgressMonitor monitor, final IAthlete athlete, final IDatabaseAccess db) {
        final List<ITraining> trainings = db.getAllImported(athlete);
        calculateHoehenmeter(monitor, db, trainings);

        final IRoute defaultRoute = db.getRoute(OTCKonstanten.DEFAULT_ROUTE_NAME, athlete);
        setDefaultStrecke(monitor, db, trainings, defaultRoute);
    }

    private void setDefaultStrecke(final IProgressMonitor monitor, final IDatabaseAccess db, final List<ITraining> trainings, final IRoute defaultRoute) {
        int i = 0;
        for (final ITraining training : trainings) {
            if (training.getRoute() == null) {
                training.setRoute(defaultRoute);
                db.saveTraining(training);
                monitor.subTask(Messages.InitialLoadRunnable_6 + i);
                LOG.info("Setzte default Strecke bei Training: " + i); //$NON-NLS-1$ 
                i++;
            }
        }
    }

    private void calculateHoehenmeter(final IProgressMonitor monitor, final IDatabaseAccess db, final List<ITraining> trainings) {
        int i = 0;
        for (final ITraining training : trainings) {
            if (training.getUpMeter() == null && training.getTrackPoints() != null) {
                final DateTime start = DateTime.now();
                final Ascending ascending = AltitudeCalculator.calculateAscending(training.getTrackPoints());
                final DateTime end = DateTime.now();
                training.setUpMeter(ascending.getUp());
                training.setDownMeter(ascending.getDown());
                db.saveTraining(training);
                final long time = end.getMillis() - start.getMillis();
                monitor.subTask(Messages.InitialLoadRunnable_7 + i);
                LOG.info("Berechne Steigungen " + i + " Zeit: " + time); //$NON-NLS-1$ //$NON-NLS-2$ 
                i++;
            }
        }
    }
}
