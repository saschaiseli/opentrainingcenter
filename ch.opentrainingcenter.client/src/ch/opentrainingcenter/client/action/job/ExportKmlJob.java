package ch.opentrainingcenter.client.action.job;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.osgi.util.NLS;
import org.joda.time.DateTime;

import ch.opentrainingcenter.core.data.SimplePair;
import ch.opentrainingcenter.core.db.IDatabaseAccess;
import ch.opentrainingcenter.core.geo.MapConverter;
import ch.opentrainingcenter.core.helper.DistanceHelper;
import ch.opentrainingcenter.core.helper.TimeHelper;
import ch.opentrainingcenter.i18n.Messages;
import ch.opentrainingcenter.route.IKmlDumper;
import ch.opentrainingcenter.transfer.IAthlete;
import ch.opentrainingcenter.transfer.ITraining;
import ch.opentrainingcenter.transfer.Track;

public class ExportKmlJob extends Job {

    private final static Logger LOGGER = Logger.getLogger(ExportKmlJob.class);
    private final IKmlDumper kmlDumper;
    private final List<?> records;
    private final NavigationSelectionSupport<ITraining> selectionSupport;

    public ExportKmlJob(final String name, final IKmlDumper kmlDumper, final List<?> records, final IDatabaseAccess databaseAccess, final IAthlete athlete) {
        super(name);
        this.kmlDumper = kmlDumper;
        this.records = records;
        selectionSupport = new NavigationSelectionSupport<>(databaseAccess, athlete, ITraining.class);
    }

    @Override
    protected IStatus run(final IProgressMonitor monitor) {
        final List<ITraining> trainings = selectionSupport.getSelectedTrainings(records);
        monitor.beginTask(Messages.ExportKmlJob_BeginTask, trainings.size());
        final long start = DateTime.now().getMillis();
        for (final ITraining training : trainings) {
            final Track track = MapConverter.convert(training);
            final String label = NLS.bind(Messages.ExportKml_StreckeVom, TimeHelper.convertDateToString(training.getDatum()));
            final List<SimplePair<String>> data = new ArrayList<>();
            final String distance = DistanceHelper.roundDistanceFromMeterToKm(training.getLaengeInMeter());
            final SimplePair<String> pair = new SimplePair<String>(Messages.Common_DISTANZ, distance + Messages.Einheit_Kilometer);
            data.add(pair);
            kmlDumper.dumpTrack(TimeHelper.convertDateToFileName(new Date(training.getDatum())), label, track, data);
            final String message = NLS.bind(Messages.ExportKmlJob_Task, label, kmlDumper.getKmlPath());
            LOGGER.info(message);
            monitor.setTaskName(message);
            monitor.worked(1);
        }
        LOGGER.info(String.format("Das Generieren der %s KML Files dauerte %s [ms]", trainings.size(), DateTime.now().getMillis() - start)); //$NON-NLS-1$
        return Status.OK_STATUS;
    }
}
