package ch.opentrainingcenter.client.commands;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.osgi.util.NLS;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;
import org.joda.time.DateTime;

import ch.opentrainingcenter.client.Activator;
import ch.opentrainingcenter.client.views.ApplicationContext;
import ch.opentrainingcenter.core.PreferenceConstants;
import ch.opentrainingcenter.core.data.SimplePair;
import ch.opentrainingcenter.core.db.IDatabaseAccess;
import ch.opentrainingcenter.core.geo.MapConverter;
import ch.opentrainingcenter.core.helper.DistanceHelper;
import ch.opentrainingcenter.core.helper.TimeHelper;
import ch.opentrainingcenter.core.service.IDatabaseService;
import ch.opentrainingcenter.i18n.Messages;
import ch.opentrainingcenter.model.navigation.ConcreteImported;
import ch.opentrainingcenter.model.navigation.INavigationItem;
import ch.opentrainingcenter.model.navigation.INavigationParent;
import ch.opentrainingcenter.route.IKmlDumper;
import ch.opentrainingcenter.route.RouteFactory;
import ch.opentrainingcenter.transfer.ITraining;
import ch.opentrainingcenter.transfer.Track;

public class ExportKml extends OtcAbstractHandler {
    private static final Logger LOGGER = Logger.getLogger(ExportKml.class);
    private final IKmlDumper kmlDumper;
    private final String path;

    private final IDatabaseService service;
    private final ApplicationContext context;

    public ExportKml() {
        this(Activator.getDefault().getPreferenceStore());
    }

    public ExportKml(final IPreferenceStore store) {
        super(store);
        path = store.getString(PreferenceConstants.KML_DEBUG_PATH);
        kmlDumper = RouteFactory.createKmlDumper(path);
        service = (IDatabaseService) PlatformUI.getWorkbench().getService(IDatabaseService.class);
        context = ApplicationContext.getApplicationContext();
    }

    @Override
    public Object execute(final ExecutionEvent event) throws ExecutionException {
        final IDatabaseAccess databaseAccess = service.getDatabaseAccess();
        final ISelection selection = HandlerUtil.getCurrentSelection(event);
        final List<?> records = ((StructuredSelection) selection).toList();
        final List<ITraining> trainings = new ArrayList<>();
        for (final Object obj : records) {
            if (obj instanceof Integer) {
                final DateTime von = new DateTime((int) obj, 1, 1, 0, 0);
                final DateTime bis = new DateTime((int) obj, 12, 31, 23, 59);
                trainings.addAll(databaseAccess.getTrainingsByAthleteAndDate(context.getAthlete(), von, bis));
            }
            if (obj instanceof INavigationParent) {
                final INavigationParent parent = (INavigationParent) obj;
                final List<INavigationItem> childs = parent.getChilds();
                for (final INavigationItem child : childs) {
                    if (child instanceof ConcreteImported) {
                        trainings.add(((ConcreteImported) child).getTraining());
                    }
                }
            }

            if (obj instanceof ITraining) {
                trainings.add((ITraining) obj);
            }
        }
        final long start = DateTime.now().getMillis();
        for (final ITraining training : trainings) {
            final Track track = MapConverter.convert(training);
            final String label = NLS.bind(Messages.ExportKml_StreckeVom, TimeHelper.convertDateToString(training.getDatum()));

            final List<SimplePair<String>> extendedData = new ArrayList<>();
            extendedData.add(new SimplePair<String>(Messages.Common_DISTANZ, DistanceHelper.roundDistanceFromMeterToKm(training.getLaengeInMeter())
                    + Messages.Einheit_Kilometer));
            kmlDumper.dumpTrack(TimeHelper.convertDateToFileName(new Date(training.getDatum())), label, track, extendedData);
            LOGGER.info(String.format("Der Track vom %s wurde nach %s geschrieben", label, path)); //$NON-NLS-1$
        }
        LOGGER.info(String.format("Das Generieren der %s KML Files dauerte %s [ms]", trainings.size(), DateTime.now().getMillis() - start)); //$NON-NLS-1$
        return null;
    }
}
