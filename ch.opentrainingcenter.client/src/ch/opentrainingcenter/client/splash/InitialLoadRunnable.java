package ch.opentrainingcenter.client.splash;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.osgi.util.NLS;
import org.eclipse.ui.PlatformUI;
import org.joda.time.DateTime;

import ch.opentrainingcenter.client.cache.AthleteCache;
import ch.opentrainingcenter.client.cache.HealthCache;
import ch.opentrainingcenter.client.cache.StreckeCache;
import ch.opentrainingcenter.client.views.ApplicationContext;
import ch.opentrainingcenter.client.views.IImageKeys;
import ch.opentrainingcenter.core.cache.Cache;
import ch.opentrainingcenter.core.cache.ICache;
import ch.opentrainingcenter.core.cache.TrainingCache;
import ch.opentrainingcenter.core.db.IDatabaseAccess;
import ch.opentrainingcenter.core.helper.AltitudeCalculator;
import ch.opentrainingcenter.core.helper.AltitudeCalculator.Ascending;
import ch.opentrainingcenter.core.service.IDatabaseService;
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
    private IDatabaseAccess databaseAccess;

    @Override
    public void run(final IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
        final IDatabaseService service = (IDatabaseService) PlatformUI.getWorkbench().getService(IDatabaseService.class);
        databaseAccess = service.getDatabaseAccess();
        final IAthlete athlete = ApplicationContext.getApplicationContext().getAthlete();
        if (athlete != null) {
            loadFirstRecords(monitor, athlete, databaseAccess);
            loadAllHealths(monitor, athlete, databaseAccess);
            loadAllPlaene(monitor, athlete, databaseAccess);
            loadAllRouten(monitor, athlete, databaseAccess);
            doMaintenance(monitor, athlete, databaseAccess);
        }
        loadAllAthleten(monitor, databaseAccess);
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
            monitor.subTask(NLS.bind(Messages.InitialLoadRunnable_3, i++));
            LOG.info(Messages.InitialLoadRunnable_4);
        }
    }

    private void loadAllHealths(final IProgressMonitor monitor, final IAthlete athlete, final IDatabaseAccess db) {
        int i = 0;
        final List<IHealth> healths = db.getHealth(athlete);
        final ICache<Integer, ConcreteHealth> healthCache = HealthCache.getInstance();
        for (final IHealth health : healths) {
            healthCache.add(ch.opentrainingcenter.model.ModelFactory.createConcreteHealth(health, IImageKeys.CARDIO3232));
            monitor.subTask(NLS.bind(Messages.InitialLoadRunnable_1, i++));
            LOG.info(Messages.InitialLoadRunnable_2);
        }
    }

    private void loadAllRouten(final IProgressMonitor monitor, final IAthlete athlete, final IDatabaseAccess db) {
        int i = 0;
        final List<IRoute> routen = db.getRoute(athlete);
        final ICache<String, StreckeModel> cache = StreckeCache.getInstance();
        for (final IRoute route : routen) {
            final int referenzTrainingId = route.getReferenzTrack() != null ? route.getReferenzTrack().getId() : 0;
            final StreckeModel strecke = ch.opentrainingcenter.model.ModelFactory.createStreckeModel(route, athlete, referenzTrainingId);
            cache.add(strecke);
            monitor.subTask(NLS.bind(Messages.InitialLoadRunnable_5, i++));
            LOG.info(String.format("Strecke dem Cache hinzugefügt: %s Strecke: %s", route, strecke)); //$NON-NLS-1$ 
        }
    }

    private void loadAllAthleten(final IProgressMonitor monitor, final IDatabaseAccess db) {
        int i = 0;
        final ICache<String, IAthlete> cache = AthleteCache.getInstance();
        final List<IAthlete> athletes = db.getAllAthletes();
        for (final IAthlete athlete : athletes) {
            cache.add(athlete);
            monitor.subTask(NLS.bind("Athlete in Cache laden {0}", i++)); //$NON-NLS-1$
            LOG.info(String.format("Athlete dem Cache hinzugefügt: %s", athlete)); //$NON-NLS-1$ 
        }
    }

    private void doMaintenance(final IProgressMonitor monitor, final IAthlete athlete, final IDatabaseAccess db) {
        final List<ITraining> trainings = db.getAllImported(athlete);
        calculateHoehenmeter(monitor, db, trainings);

        final IRoute defaultRoute = db.getRoute(Messages.OTCKonstanten_0, athlete);
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
