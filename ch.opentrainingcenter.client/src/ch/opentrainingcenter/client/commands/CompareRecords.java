package ch.opentrainingcenter.client.commands;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.ui.handlers.HandlerUtil;

import ch.opentrainingcenter.client.Activator;
import ch.opentrainingcenter.core.PreferenceConstants;
import ch.opentrainingcenter.core.cache.Cache;
import ch.opentrainingcenter.core.cache.TrainingCache;
import ch.opentrainingcenter.core.geo.MapConverter;
import ch.opentrainingcenter.route.ICompareRoute;
import ch.opentrainingcenter.route.RouteFactory;
import ch.opentrainingcenter.transfer.ITraining;
import ch.opentrainingcenter.transfer.Track;

/**
 * Vergleicht selektierte Läufe auf ihre geografische Ähnlichkeit.
 */
public class CompareRecords extends AbstractHandler {

    public static final String ID = "ch.opentrainingcenter.client.commands.CompareRecords"; //$NON-NLS-1$
    private final IPreferenceStore store = Activator.getDefault().getPreferenceStore();

    @Override
    public Object execute(final ExecutionEvent event) throws ExecutionException {
        final Cache cache = TrainingCache.getInstance();

        final ISelection selection = HandlerUtil.getCurrentSelection(event);

        final List<?> records = ((StructuredSelection) selection).toList();

        final List<Track> tracks = new ArrayList<Track>();
        for (final Object obj : records) {
            final ITraining record = (ITraining) obj;
            final ITraining training = cache.get(record.getDatum());
            tracks.add(MapConverter.convert(training));
        }
        final ICompareRoute comp = RouteFactory.getRouteComparator(true, store.getString(PreferenceConstants.KML_DEBUG_PATH));
        comp.compareRoute(tracks.get(0), tracks.get(1));
        return null;
    }

}
