package ch.opentrainingcenter.client.commands;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.ui.handlers.HandlerUtil;

import ch.opentrainingcenter.client.Activator;
import ch.opentrainingcenter.core.PreferenceConstants;
import ch.opentrainingcenter.core.geo.MapConverter;
import ch.opentrainingcenter.core.helper.TimeHelper;
import ch.opentrainingcenter.route.CompareRouteFactory;
import ch.opentrainingcenter.route.IKmlDumper;
import ch.opentrainingcenter.transfer.ITraining;
import ch.opentrainingcenter.transfer.Track;

public class ExportKml extends OtcAbstractHandler {
    private static final Logger LOGGER = Logger.getLogger(ExportKml.class);
    private final IKmlDumper kmlDumper;
    private final String path;

    public ExportKml() {
        this(Activator.getDefault().getPreferenceStore());
    }

    public ExportKml(final IPreferenceStore store) {
        super(store);
        path = store.getString(PreferenceConstants.KML_DEBUG_PATH);
        kmlDumper = CompareRouteFactory.createKmlDumper(path);
    }

    @Override
    public Object execute(final ExecutionEvent event) throws ExecutionException {
        final ISelection selection = HandlerUtil.getCurrentSelection(event);
        final List<?> records = ((StructuredSelection) selection).toList();

        for (final Object obj : records) {
            final ITraining record = (ITraining) obj;
            final Track track = MapConverter.convert(record);
            final String datum = TimeHelper.convertDateToString(record.getDatum());
            kmlDumper.dumpTrack(TimeHelper.convertDateToFileName(new Date(record.getDatum())), track);
            LOGGER.info(String.format("Der Track vom %s wurde nach %s geschrieben", datum, path)); //$NON-NLS-1$
        }
        return null;
    }
}
