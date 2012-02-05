package ch.opentrainingcenter.client.views.navigation;

import java.util.Collection;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

import ch.opentrainingcenter.client.Activator;
import ch.opentrainingcenter.client.Messages;
import ch.opentrainingcenter.client.PreferenceConstants;
import ch.opentrainingcenter.client.action.DeleteImportedRecord;
import ch.opentrainingcenter.client.cache.IRecordListener;
import ch.opentrainingcenter.client.cache.TrainingCenterDataCache;
import ch.opentrainingcenter.client.helper.DistanceHelper;
import ch.opentrainingcenter.client.helper.TimeHelper;
import ch.opentrainingcenter.client.views.overview.SingleActivityViewPart;
import ch.opentrainingcenter.db.DatabaseAccessFactory;
import ch.opentrainingcenter.importer.GpsFileLoader;
import ch.opentrainingcenter.importer.ImportJob;
import ch.opentrainingcenter.tcx.ActivityT;
import ch.opentrainingcenter.transfer.IAthlete;
import ch.opentrainingcenter.transfer.IImported;
import ch.opentrainingcenter.transfer.ITraining;

public class NavigationView extends ViewPart {

    public static final String ID = "ch.opentrainingcenter.client.navigationView"; //$NON-NLS-1$

    private TreeViewer viewer;

    public static final Logger logger = Logger.getLogger(NavigationView.class);

    private final TrainingCenterDataCache cache = TrainingCenterDataCache.getInstance();

    private final GpsFileLoader loadGpsFile = new GpsFileLoader();

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
                final Object selectedRecord = selection.getFirstElement();

                if (selectedRecord instanceof IImported) {
                    openSingleRunView((IImported) selectedRecord);
                }
            }

            private void openSingleRunView(final IImported record) {
                final ActivityT selected = getActivity(record);
                final String hash = getSecondaryId(selected);
                try {
                    PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
                            .showView(SingleActivityViewPart.ID, hash, IWorkbenchPage.VIEW_ACTIVATE);
                } catch (final PartInitException e) {
                    e.printStackTrace();
                }
            }

            private ActivityT getActivity(final IImported selectedRecord) {
                ActivityT selected = null;
                if (!cache.contains(selectedRecord.getActivityId())) {
                    try {
                        selected = loadGpsFile.convertActivity(selectedRecord);
                        cache.setSelectedRun(selectedRecord);
                        cache.add(selected);
                    } catch (final Exception e) {
                        logger.error("Konnte File nicht einlesen"); //$NON-NLS-1$
                    }

                } else {
                    // read from cache
                    selected = cache.get(selectedRecord.getActivityId());
                }
                return selected;
            }
        });

        viewer.addSelectionChangedListener(new ISelectionChangedListener() {

            @Override
            public void selectionChanged(final SelectionChangedEvent event) {
                final IStructuredSelection selection = (IStructuredSelection) event.getSelection();
                final Object first = selection.getFirstElement();
                if (first instanceof IImported) {
                    final IImported record = (IImported) first;
                    cache.setSelection(selection.toArray());
                    writeToStatusLine(record);
                } else {
                    writeToStatusLine(""); //$NON-NLS-1$
                    cache.setSelectedRun(null);
                }
            }

            private void writeToStatusLine(final String message) {
                getViewSite().getActionBars().getStatusLineManager().setMessage(message);
            }

            private void writeToStatusLine(final IImported record) {
                writeToStatusLine(Messages.NavigationView_0 + TimeHelper.convertDateToString(record.getActivityId(), false) + " " + getOverview(record)); //$NON-NLS-1$
            }
        });

        final TrainingCenterDataCache cache = TrainingCenterDataCache.getInstance();
        if (!cache.isCacheLoaded()) {
            final Job job = new ImportJob(Messages.NavigationView_1, athlete);
            job.schedule();
            job.addJobChangeListener(new ImportJobChangeListener(viewer));
        } else {
            final Collection<IImported> allImported = cache.getAllActivities();
            viewer.setInput(allImported);
            writeStatus(Messages.NavigationView_2 + allImported.size() + Messages.NavigationView_3);
        }

        cache.addListener(new IRecordListener() {

            @Override
            public void recordChanged(final Collection<ActivityT> entry) {
                final Collection<IImported> allImported = cache.getAllActivities();
                Display.getDefault().asyncExec(new Runnable() {

                    @Override
                    public void run() {
                        try {
                            if (allImported == null || allImported.isEmpty()) {
                                writeStatus(Messages.NavigationView_4);
                            }
                            viewer.setInput(allImported);
                            viewer.refresh();
                        } catch (final Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }

            @Override
            public void deleteRecord(final Collection<ActivityT> entry) {
                viewer.setInput(cache.getAllActivities());
                viewer.refresh();
                final IWorkbenchPage wbp = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
                for (final ActivityT record : entry) {
                    final String secondaryViewId = getSecondaryId(record);
                    wbp.hideView(wbp.findViewReference(SingleActivityViewPart.ID, secondaryViewId));
                }
            }
        });
        if (cache.getSelected() != null) {
            viewer.setSelection(new StructuredSelection(cache.getSelected()), true);
        }
    }

    private void writeStatus(final String message) {
        getViewSite().getActionBars().getStatusLineManager().setMessage(message);
    }

    private final String getOverview(final IImported record) {
        final StringBuffer str = new StringBuffer();
        final ITraining training = record.getTraining();
        str.append(Messages.NavigationView_7).append(DistanceHelper.roundDistanceFromMeterToKmMitEinheit(training.getLaengeInMeter()));
        str.append(Messages.NavigationView_8).append(TimeHelper.convertSecondsToHumanReadableZeit(training.getDauerInSekunden()));
        return str.toString();
    }

    /**
     * Passing the focus request to the viewer's control.
     */
    @Override
    public void setFocus() {
        viewer.getControl().setFocus();
    }

    private String getSecondaryId(final ActivityT record) {
        return String.valueOf(record.getId().hashCode());
    }
}