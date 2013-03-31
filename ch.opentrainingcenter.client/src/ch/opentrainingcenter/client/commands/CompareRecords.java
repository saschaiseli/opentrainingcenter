package ch.opentrainingcenter.client.commands;

import java.util.ArrayList;
import java.util.List;

<<<<<<< HEAD
import org.apache.log4j.Logger;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.ui.handlers.HandlerUtil;

import ch.opentrainingcenter.client.views.overview.MapConverter;
import ch.opentrainingcenter.core.cache.Cache;
import ch.opentrainingcenter.core.cache.TrainingCenterDataCache;
import ch.opentrainingcenter.core.db.DatabaseAccessFactory;
import ch.opentrainingcenter.core.db.IDatabaseAccess;
import ch.opentrainingcenter.model.geo.Track;
import ch.opentrainingcenter.route.CompareRouteFactory;
import ch.opentrainingcenter.route.ICompareRoute;
import ch.opentrainingcenter.tcx.ActivityT;
import ch.opentrainingcenter.transfer.IImported;

public class CompareRecords extends AbstractHandler {

    public static final String ID = "ch.opentrainingcenter.client.commands.CompareRecords"; //$NON-NLS-1$

    private static final Logger LOGGER = Logger.getLogger(CompareRecords.class.getName());

    @Override
    public Object execute(final ExecutionEvent event) throws ExecutionException {
        final IDatabaseAccess db = DatabaseAccessFactory.getDatabaseAccess();
        final Cache cache = TrainingCenterDataCache.getInstance();

        final ISelection selection = HandlerUtil.getCurrentSelection(event);

        final List<?> records = ((StructuredSelection) selection).toList();

        final List<Track> tracks = new ArrayList<Track>();
        for (final Object obj : records) {
            final IImported record = (IImported) obj;
            final ActivityT activity = cache.get(record.getActivityId());
            tracks.add(MapConverter.convert(activity));
        }
        final ICompareRoute comp = CompareRouteFactory.getRouteComparator(true);
        final boolean compareRoute = comp.compareRoute(tracks.get(0), tracks.get(1));
        // MessageDialog.openInformation(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
        // "Vergleich", "result: " + compareRoute);
=======
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.ui.handlers.HandlerUtil;

import ch.opentrainingcenter.client.Activator;
import ch.opentrainingcenter.client.views.overview.MapConverter;
import ch.opentrainingcenter.core.PreferenceConstants;
import ch.opentrainingcenter.core.cache.Cache;
import ch.opentrainingcenter.core.cache.TrainingCenterDataCache;
import ch.opentrainingcenter.model.geo.Track;
import ch.opentrainingcenter.route.CompareRouteFactory;
import ch.opentrainingcenter.route.ICompareRoute;
import ch.opentrainingcenter.tcx.ActivityT;
import ch.opentrainingcenter.transfer.IImported;

public class CompareRecords extends AbstractHandler {

    public static final String ID = "ch.opentrainingcenter.client.commands.CompareRecords"; //$NON-NLS-1$
    private final IPreferenceStore store = Activator.getDefault().getPreferenceStore();

    @Override
    public Object execute(final ExecutionEvent event) throws ExecutionException {
        final Cache cache = TrainingCenterDataCache.getInstance();

        final ISelection selection = HandlerUtil.getCurrentSelection(event);

        final List<?> records = ((StructuredSelection) selection).toList();

        final List<Track> tracks = new ArrayList<Track>();
        for (final Object obj : records) {
            final IImported record = (IImported) obj;
            final ActivityT activity = cache.get(record.getActivityId());
            tracks.add(MapConverter.convert(activity));
        }
        final ICompareRoute comp = CompareRouteFactory.getRouteComparator(true, store.getString(PreferenceConstants.KML_DEBUG_PATH));
        comp.compareRoute(tracks.get(0), tracks.get(1));
>>>>>>> refs/heads/kmldumper
        return null;
    }

}
