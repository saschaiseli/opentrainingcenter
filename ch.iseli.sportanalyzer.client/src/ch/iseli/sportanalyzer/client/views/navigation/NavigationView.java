package ch.iseli.sportanalyzer.client.views.navigation;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.xml.datatype.XMLGregorianCalendar;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.iseli.sportanalyzer.client.Activator;
import ch.iseli.sportanalyzer.client.PreferenceConstants;
import ch.iseli.sportanalyzer.client.cache.IRecordListener;
import ch.iseli.sportanalyzer.client.cache.TrainingCenterDataCache;
import ch.iseli.sportanalyzer.client.cache.TrainingCenterDatabaseTChild;
import ch.iseli.sportanalyzer.client.cache.TrainingCenterDatabaseTParent;
import ch.iseli.sportanalyzer.client.helper.TimeHelper;
import ch.iseli.sportanalyzer.client.views.overview.SingleActivityViewPart;
import ch.iseli.sportanalyzer.db.IImportedDao;
import ch.iseli.sportanalyzer.importer.IConvert2Tcx;
import ch.iseli.sportanalyzer.tcx.ActivityLapT;
import ch.iseli.sportanalyzer.tcx.ActivityT;
import ch.iseli.sportanalyzer.tcx.IntensityT;
import ch.iseli.sportanalyzer.tcx.TrainingCenterDatabaseT;
import ch.opentrainingcenter.transfer.impl.Athlete;

public class NavigationView extends ViewPart {
    public static final String ID = "ch.iseli.sportanalyzer.client.navigationView";
    private TreeViewer viewer;
    private static final Logger log = LoggerFactory.getLogger(NavigationView.class.getName());

    private final TrainingCenterDataCache cache = TrainingCenterDataCache.getInstance();

    /**
     * This is a callback that will allow us to create the viewer and initialize it.
     */
    @Override
    public void createPartControl(Composite parent) {

        String athleteId = Activator.getDefault().getPreferenceStore().getString(PreferenceConstants.ATHLETE_NAME);

        IConfigurationElement[] daos = Platform.getExtensionRegistry().getConfigurationElementsFor("ch.opentrainingdatabase.db");
        IImportedDao dao = getDao(daos);
        int id = Integer.parseInt(athleteId);
        Athlete athlete = dao.getAthlete(id);

        viewer = new TreeViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);
        viewer.setContentProvider(new ViewContentProvider());
        viewer.setLabelProvider(new ViewLabelProvider(parent));
        viewer.addDoubleClickListener(new IDoubleClickListener() {

            @Override
            public void doubleClick(DoubleClickEvent event) {
                IStructuredSelection selection = (IStructuredSelection) event.getSelection();
                Object first = selection.getFirstElement();
                if (first instanceof TrainingCenterDatabaseTParent) {
                    openSingleRunView((TrainingCenterDatabaseTParent) first);
                } else {
                    // child
                    TrainingCenterDatabaseTChild child = (TrainingCenterDatabaseTChild) first;
                    openChildView(child);
                }

            }

            private void openSingleRunView(TrainingCenterDatabaseTParent first) {
                TrainingCenterDataCache.setSelectedRun(first.getTrainingCenterDatabase());

                try {
                    PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
                            .showView(SingleActivityViewPart.ID, TrainingCenterDataCache.getSelected().toString(), IWorkbenchPage.VIEW_ACTIVATE);
                } catch (PartInitException e) {
                    e.printStackTrace();
                }
            }

            private void openChildView(TrainingCenterDatabaseTChild child) {
                TrainingCenterDataCache.setSelectedRun(child.getParent().getTrainingCenterDatabase());
                try {
                    PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
                            .showView(child.getTyp().getViewId(), String.valueOf(child.hashCode()), IWorkbenchPage.VIEW_ACTIVATE);
                } catch (PartInitException e) {
                    e.printStackTrace();
                }

            }
        });

        viewer.addSelectionChangedListener(new ISelectionChangedListener() {

            @Override
            public void selectionChanged(SelectionChangedEvent event) {
                IStructuredSelection selection = (IStructuredSelection) event.getSelection();
                Object first = selection.getFirstElement();
                if (first instanceof TrainingCenterDatabaseTParent) {
                    writeToStatusLine(((TrainingCenterDatabaseTParent) first).getTrainingCenterDatabase());
                } else if (first instanceof TrainingCenterDatabaseTChild) {
                    // ist ein child
                    writeToStatusLine(((TrainingCenterDatabaseTChild) first).getParent().getTrainingCenterDatabase());
                } else {
                    writeToStatusLine("");
                }
            }

            private void writeToStatusLine(String message) {
                getViewSite().getActionBars().getStatusLineManager().setMessage(message);
            }

            private void writeToStatusLine(TrainingCenterDatabaseT selectedRun) {
                writeToStatusLine("Lauf vom " + convertGregorianDateToString(selectedRun.getActivities().getActivity().get(0).getId()) + " "
                        + getOverview(selectedRun));
            }
        });

        // load garmin data
        IConfigurationElement[] configurationElementsFor = Platform.getExtensionRegistry().getConfigurationElementsFor("ch.iseli.sportanalyzer.myimporter");
        final IConvert2Tcx tcx = getConverterImplementation(configurationElementsFor);
        final List<TrainingCenterDatabaseT> allRuns = new ArrayList<TrainingCenterDatabaseT>();
        final List<File> allFiles = new ArrayList<File>();
        if (tcx != null) {

            List<String> importedRecords = dao.getImportedRecords(athlete);
            // Start the Job
            List<File> loadAllGPSFiles = tcx.loadAllGPSFilesFromAthlete(importedRecords);
            if (loadAllGPSFiles == null || loadAllGPSFiles.isEmpty()) {
                getViewSite().getActionBars().getStatusLineManager().setMessage("Keine GPS Files gefunden, vielleicht ist auch kein Pfad definiert...");
            }
            allFiles.addAll(loadAllGPSFiles);
            final Job job = new Job("Lade GPS Daten") {
                @Override
                protected IStatus run(IProgressMonitor monitor) {
                    // Set total number of work units
                    monitor.beginTask("Lade GPS Daten", allFiles.size());
                    try {
                        for (File file : allFiles) {
                            allRuns.add(tcx.convert(file));
                            monitor.worked(1);
                        }
                        Display.getDefault().asyncExec(new Runnable() {

                            @Override
                            public void run() {
                                try {
                                    TrainingCenterDataCache.getInstance().addAll(allRuns);
                                    viewer.setInput(cache.getAllRuns());
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                    return Status.OK_STATUS;
                }
            };
            job.schedule();
        }
        TrainingCenterDataCache cache = TrainingCenterDataCache.getInstance();
        cache.addListener(new IRecordListener() {

            @Override
            public void recordChanged(List<TrainingCenterDatabaseT> newRecords) {
                viewer.refresh();
            }
        });

    }

    private IImportedDao getDao(IConfigurationElement[] configurationElementsFor) {
        for (final IConfigurationElement element : configurationElementsFor) {
            try {
                return (IImportedDao) element.createExecutableExtension("classImportedDao");
            } catch (CoreException e) {
                log.info(e.getMessage());
            }
        }
        return null;
    }

    private IConvert2Tcx getConverterImplementation(IConfigurationElement[] configurationElementsFor) {
        for (final IConfigurationElement element : configurationElementsFor) {
            try {
                return (IConvert2Tcx) element.createExecutableExtension("class");
            } catch (CoreException e) {
                System.err.println(e.getMessage());
            }
        }
        return null;
    }

    private final String convertGregorianDateToString(XMLGregorianCalendar datum) {
        Date time = datum.toGregorianCalendar().getTime();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(time);
        calendar.get(Calendar.DAY_OF_WEEK);
        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
        return calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault()) + " " + format.format(time);
    }

    private final String getOverview(TrainingCenterDatabaseT run) {
        ActivityT activityT = run.getActivities().getActivity().get(0);
        StringBuffer str = new StringBuffer();
        if (activityT.getLap() != null && activityT.getLap().size() > 1) {
            // intervall
            str.append("Training mit ");
            int activeIntervall = 0;
            for (ActivityLapT lap : activityT.getLap()) {
                if (IntensityT.ACTIVE.equals(lap.getIntensity())) {
                    activeIntervall++;
                }
            }
            str.append(activeIntervall).append(" aktiven Intervallen");
        } else if (activityT.getLap() != null && activityT.getLap().size() == 1) {
            ActivityLapT lap = activityT.getLap().get(0);
            str.append("Joggen mit ").append(lap.getDistanceMeters() / 1000);
            str.append("km in ").append(TimeHelper.convertSecondsToHumanReadableZeit(lap.getTotalTimeSeconds()));
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