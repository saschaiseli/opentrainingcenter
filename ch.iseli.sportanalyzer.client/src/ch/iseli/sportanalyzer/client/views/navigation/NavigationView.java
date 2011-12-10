package ch.iseli.sportanalyzer.client.views.navigation;

import java.util.Collection;

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
import ch.iseli.sportanalyzer.client.Messages;
import ch.iseli.sportanalyzer.client.PreferenceConstants;
import ch.iseli.sportanalyzer.client.action.DeleteImportedRecord;
import ch.iseli.sportanalyzer.client.cache.IRecordListener;
import ch.iseli.sportanalyzer.client.cache.TrainingCenterDataCache;
import ch.iseli.sportanalyzer.client.cache.TrainingCenterRecord;
import ch.iseli.sportanalyzer.client.helper.DistanceHelper;
import ch.iseli.sportanalyzer.client.helper.TimeHelper;
import ch.iseli.sportanalyzer.client.views.overview.SingleActivityViewPart;
import ch.iseli.sportanalyzer.db.DatabaseAccessFactory;
import ch.iseli.sportanalyzer.importer.ImportJob;
import ch.iseli.sportanalyzer.tcx.ActivityLapT;
import ch.iseli.sportanalyzer.tcx.ActivityT;
import ch.iseli.sportanalyzer.tcx.IntensityT;
import ch.opentrainingcenter.transfer.IAthlete;

public class NavigationView extends ViewPart {

    public static final String ID = "ch.iseli.sportanalyzer.client.navigationView"; //$NON-NLS-1$

    private TreeViewer viewer;

    private final TrainingCenterDataCache cache = TrainingCenterDataCache.getInstance();

    /**
     * This is a callback that will allow us to create the viewer and initialize it.
     */
    @Override
    public void createPartControl(final Composite parent) {

        final String athleteId = Activator.getDefault().getPreferenceStore().getString(PreferenceConstants.ATHLETE_ID);
        final int id = Integer.parseInt(athleteId);
        final IAthlete athlete = DatabaseAccessFactory.getDatabaseAccess().getAthlete(id);

        viewer = new TreeViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);
        viewer.setContentProvider(new ViewContentProvider());
        viewer.setLabelProvider(new ViewLabelProvider());

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
                    writeToStatusLine(""); //$NON-NLS-1$
                    cache.setSelectedRun(null);
                }
            }

            private void writeToStatusLine(final String message) {
                getViewSite().getActionBars().getStatusLineManager().setMessage(message);
            }

            private void writeToStatusLine(final TrainingCenterRecord selectedRun) {
                writeToStatusLine(Messages.NavigationView_0
                        + TimeHelper.convertGregorianDateToString(selectedRun.getTrainingCenterDatabase().getActivities().getActivity().get(0).getId(), false)
                        + " " + getOverview(selectedRun)); //$NON-NLS-1$
            }
        });

        final TrainingCenterDataCache cache = TrainingCenterDataCache.getInstance();
        if (!cache.isCacheLoaded()) {
            final Job job = new ImportJob(Messages.NavigationView_1, athlete);
            job.schedule();
            job.addJobChangeListener(new ImportJobChangeListener(viewer));
        } else {
            final Collection<TrainingCenterRecord> allRuns = cache.getAllRuns();
            viewer.setInput(allRuns);
            writeStatus(Messages.NavigationView_2 + allRuns.size() + Messages.NavigationView_3);
        }

        cache.addListener(new IRecordListener() {

            @Override
            public void recordChanged(final Collection<TrainingCenterRecord> entry) {
                final Collection<TrainingCenterRecord> allRuns = cache.getAllRuns();
                Display.getDefault().asyncExec(new Runnable() {

                    @Override
                    public void run() {
                        try {
                            if (allRuns == null || allRuns.isEmpty()) {
                                writeStatus(Messages.NavigationView_4);
                            }
                            viewer.setInput(allRuns);
                            viewer.refresh();
                        } catch (final Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }

            @Override
            public void deleteRecord(final Collection<TrainingCenterRecord> entry) {
                viewer.setInput(cache.getAllRuns());
                viewer.refresh();
                final IWorkbenchPage wbp = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
                for (final TrainingCenterRecord record : entry) {
                    final String secondaryViewId = String.valueOf(record.toString().hashCode());
                    wbp.hideView(wbp.findViewReference(SingleActivityViewPart.ID, secondaryViewId));
                }
            }
        });
    }

    private void writeStatus(final String message) {
        getViewSite().getActionBars().getStatusLineManager().setMessage(message);
    }

    private final String getOverview(final TrainingCenterRecord run) {
        final ActivityT activityT = run.getTrainingCenterDatabase().getActivities().getActivity().get(0);
        final StringBuffer str = new StringBuffer();
        if (activityT.getLap() != null && activityT.getLap().size() > 1) {
            // intervall
            str.append(Messages.NavigationView_5);
            int activeIntervall = 0;
            for (final ActivityLapT lap : activityT.getLap()) {
                if (IntensityT.ACTIVE.equals(lap.getIntensity())) {
                    activeIntervall++;
                }
            }
            str.append(activeIntervall).append(Messages.NavigationView_6);
        } else if (activityT.getLap() != null && activityT.getLap().size() == 1) {
            final ActivityLapT lap = activityT.getLap().get(0);
            str.append(Messages.NavigationView_7).append(DistanceHelper.roundDistanceFromMeterToKmMitEinheit(lap.getDistanceMeters()));
            str.append(Messages.NavigationView_8).append(TimeHelper.convertSecondsToHumanReadableZeit(lap.getTotalTimeSeconds()));
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