package ch.iseli.sportanalyzer.client.views.navigation;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

import ch.iseli.sportanalyzer.client.Activator;
import ch.iseli.sportanalyzer.client.PreferenceConstants;
import ch.iseli.sportanalyzer.client.action.DeleteImportedRecord;
import ch.iseli.sportanalyzer.client.cache.IRecordListener;
import ch.iseli.sportanalyzer.client.cache.TrainingCenterDataCache;
import ch.iseli.sportanalyzer.client.cache.TrainingCenterRecord;
import ch.iseli.sportanalyzer.client.helper.DistanceHelper;
import ch.iseli.sportanalyzer.client.helper.TimeHelper;
import ch.iseli.sportanalyzer.client.views.overview.SingleActivityViewPart;
import ch.iseli.sportanalyzer.db.DatabaseAccessFactory;
import ch.iseli.sportanalyzer.importer.IConvert2Tcx;
import ch.iseli.sportanalyzer.tcx.ActivityLapT;
import ch.iseli.sportanalyzer.tcx.ActivityT;
import ch.iseli.sportanalyzer.tcx.IntensityT;
import ch.iseli.sportanalyzer.tcx.TrainingCenterDatabaseT;
import ch.opentrainingcenter.transfer.IAthlete;

public class NavigationView extends ViewPart {

    public static final String ID = "ch.iseli.sportanalyzer.client.navigationView";

    private static final Logger logger = Logger.getLogger(NavigationView.class);

    private TreeViewer viewer;

    private final TrainingCenterDataCache cache = TrainingCenterDataCache.getInstance();

    /**
     * This is a callback that will allow us to create the viewer and initialize it.
     */
    @Override
    public void createPartControl(final Composite parent) {

        final String athleteId = Activator.getDefault().getPreferenceStore().getString(PreferenceConstants.ATHLETE_NAME);

        final int id = Integer.parseInt(athleteId);
        final IAthlete athlete = DatabaseAccessFactory.getDatabaseAccess().getAthlete(id);

        viewer = new TreeViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);
        // viewer = new ListViewer(parent);
        viewer.setContentProvider(new ViewContentProvider());
        viewer.setLabelProvider(new ViewLabelProvider(parent));

        final MenuManager menuMgr = new MenuManager("#PopupMenu"); //$NON-NLS-1$
        menuMgr.setRemoveAllWhenShown(true);
        menuMgr.addMenuListener(new IMenuListener() {
            @Override
            public void menuAboutToShow(final IMenuManager manager) {
                manager.add(new DeleteImportedRecord());
            }
        });

        final Menu menu = menuMgr.createContextMenu(viewer.getTree());
        viewer.getTree().setMenu(menu);
        getSite().registerContextMenu(menuMgr, viewer);

        viewer.addDoubleClickListener(new IDoubleClickListener() {

            @Override
            public void doubleClick(final DoubleClickEvent event) {
                final IStructuredSelection selection = (IStructuredSelection) event.getSelection();
                final Object first = selection.getFirstElement();
                if (first instanceof TrainingCenterRecord) {
                    openSingleRunView((TrainingCenterRecord) first);
                }
            }

            private void openSingleRunView(final TrainingCenterRecord first) {
                cache.setSelectedRun(first);

                try {
                    final String hash = String.valueOf(cache.getSelected().toString().hashCode());
                    PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
                            .showView(SingleActivityViewPart.ID, hash, IWorkbenchPage.VIEW_ACTIVATE);
                } catch (final PartInitException e) {
                    e.printStackTrace();
                }
            }
        });

        viewer.addSelectionChangedListener(new ISelectionChangedListener() {

            @Override
            public void selectionChanged(final SelectionChangedEvent event) {
                final IStructuredSelection selection = (IStructuredSelection) event.getSelection();
                final Object first = selection.getFirstElement();
                if (first instanceof TrainingCenterRecord) {
                    final TrainingCenterRecord trainingCenterDatabase = (TrainingCenterRecord) first;
                    cache.setSelection(selection.toArray());
                    writeToStatusLine(trainingCenterDatabase);
                } else {
                    writeToStatusLine("");
                    cache.setSelectedRun(null);
                }
            }

            private void writeToStatusLine(final String message) {
                getViewSite().getActionBars().getStatusLineManager().setMessage(message);
            }

            private void writeToStatusLine(final TrainingCenterRecord selectedRun) {
                writeToStatusLine("Lauf vom "
                        + TimeHelper.convertGregorianDateToString(selectedRun.getTrainingCenterDatabase().getActivities().getActivity().get(0).getId(), false)
                        + " " + getOverview(selectedRun));
            }
        });

        // load garmin data
        final IConfigurationElement[] configurationElementsFor = Platform.getExtensionRegistry().getConfigurationElementsFor(
                "ch.iseli.sportanalyzer.myimporter");
        final IConvert2Tcx tcx = getConverterImplementation(configurationElementsFor);
        final Map<Integer, TrainingCenterRecord> allRuns = new HashMap<Integer, TrainingCenterRecord>();
        final Map<Integer, File> allFiles = new HashMap<Integer, File>();
        if (tcx != null) {

            final Map<Integer, String> importedRecords = DatabaseAccessFactory.getDatabaseAccess().getImportedRecords(athlete);
            // Start the Job
            final Map<Integer, File> loadAllGPSFiles = tcx.loadAllGPSFilesFromAthlete(importedRecords);
            if (loadAllGPSFiles == null || loadAllGPSFiles.isEmpty()) {
                getViewSite().getActionBars().getStatusLineManager().setMessage("Keine GPS Files gefunden. Files müssen noch importiert werden.");
            }
            allFiles.putAll(loadAllGPSFiles);
            final Job job = new Job("Lade GPS Daten") {
                @Override
                protected IStatus run(final IProgressMonitor monitor) {
                    // Set total number of work units
                    monitor.beginTask("Lade GPS Daten", allFiles.size());
                    try {
                        for (final Map.Entry<Integer, File> entry : allFiles.entrySet()) {
                            final TrainingCenterDatabaseT record = tcx.convert(entry.getValue());
                            allRuns.put(entry.getKey(), new TrainingCenterRecord(entry.getKey(), record));
                            monitor.worked(1);
                        }
                        Display.getDefault().asyncExec(new Runnable() {

                            @Override
                            public void run() {
                                try {
                                    TrainingCenterDataCache.getInstance().addAll(allRuns);
                                    viewer.setInput(cache.getAllRuns());
                                } catch (final Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    } catch (final Exception e1) {
                        e1.printStackTrace();
                    }
                    return Status.OK_STATUS;
                }
            };
            job.schedule();
        }
        final TrainingCenterDataCache cache = TrainingCenterDataCache.getInstance();
        cache.addListener(new IRecordListener() {

            @Override
            public void recordChanged(final Collection<TrainingCenterRecord> entry) {
                viewer.refresh();
            }
        });

    }

    private IConvert2Tcx getConverterImplementation(final IConfigurationElement[] configurationElementsFor) {
        for (final IConfigurationElement element : configurationElementsFor) {
            try {
                return (IConvert2Tcx) element.createExecutableExtension("class");
            } catch (final CoreException e) {
                System.err.println(e.getMessage());
            }
        }
        return null;
    }

    private final String getOverview(final TrainingCenterRecord run) {
        final ActivityT activityT = run.getTrainingCenterDatabase().getActivities().getActivity().get(0);
        final StringBuffer str = new StringBuffer();
        if (activityT.getLap() != null && activityT.getLap().size() > 1) {
            // intervall
            str.append("Training mit ");
            int activeIntervall = 0;
            for (final ActivityLapT lap : activityT.getLap()) {
                if (IntensityT.ACTIVE.equals(lap.getIntensity())) {
                    activeIntervall++;
                }
            }
            str.append(activeIntervall).append(" aktiven Intervallen");
        } else if (activityT.getLap() != null && activityT.getLap().size() == 1) {
            final ActivityLapT lap = activityT.getLap().get(0);
            str.append("Joggen mit ").append(DistanceHelper.roundDistanceFromMeterToKmMitEinheit(lap.getDistanceMeters()));
            str.append(" in ").append(TimeHelper.convertSecondsToHumanReadableZeit(lap.getTotalTimeSeconds()));
        }
        return str.toString();
    }

    /**
     * Passing the focus request to the viewer's control.
     */
    @Override
    public void setFocus() {
        viewer.getControl().setFocus();
    }
}