package ch.iseli.sportanalyzer.client;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

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
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

import ch.iseli.sportanalyzer.client.cache.TrainingCenterDataCache;
import ch.iseli.sportanalyzer.client.cache.TrainingCenterDatabaseTChild;
import ch.iseli.sportanalyzer.client.cache.TrainingCenterDatabaseTParent;
import ch.iseli.sportanalyzer.client.views.SingleActivityViewPart;
import ch.iseli.sportanalyzer.importer.IConvert2Tcx;
import ch.iseli.sportanalyzer.tcx.TrainingCenterDatabaseT;

public class NavigationView extends ViewPart {
    public static final String ID = "ch.iseli.sportanalyzer.client.navigationView";
    private TreeViewer viewer;

    class ViewContentProvider implements IStructuredContentProvider, ITreeContentProvider {

	@Override
	public void inputChanged(Viewer v, Object oldInput, Object newInput) {
	}

	@Override
	public void dispose() {
	}

	@Override
	public Object[] getElements(Object parent) {
	    List l = (List) parent;
	    return l.toArray();
	}

	@Override
	public Object[] getChildren(Object parentElement) {
	    TrainingCenterDatabaseTParent parent = (TrainingCenterDatabaseTParent) parentElement;
	    return parent.getChilds().toArray();
	}

	@Override
	public Object getParent(Object element) {
	    TrainingCenterDatabaseTChild child = (TrainingCenterDatabaseTChild) element;
	    return child.getParent();
	}

	@Override
	public boolean hasChildren(Object element) {
	    return element instanceof TrainingCenterDatabaseTParent;
	}

    }

    class ViewLabelProvider extends LabelProvider {

	private final Composite parent;

	ViewLabelProvider(Composite parent) {
	    this.parent = parent;

	}

	@Override
	public String getText(Object obj) {
	    return obj.toString();
	}

	@Override
	public Image getImage(Object obj) {
	    String imageKey = ISharedImages.IMG_OBJ_ELEMENT;
	    if (obj instanceof TrainingCenterDatabaseTParent) {
		imageKey = ISharedImages.IMG_OBJ_FOLDER;
	    }
	    return PlatformUI.getWorkbench().getSharedImages().getImage(imageKey);
	}

    }

    /**
     * This is a callback that will allow us to create the viewer and initialize
     * it.
     */
    @Override
    public void createPartControl(Composite parent) {
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

	    private void openChildView(TrainingCenterDatabaseTChild child) {
		TrainingCenterDataCache.setSelectedRun(child.getParent().getTrainingCenterDatabase());
		try {
		    PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView(child.getTyp().getViewId(), String.valueOf(child.hashCode()), 1);
		} catch (PartInitException e) {
		    e.printStackTrace();
		}

	    }

	    private void openSingleRunView(TrainingCenterDatabaseTParent first) {
		TrainingCenterDataCache.setSelectedRun(first.getTrainingCenterDatabase());

		try {
		    PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView(SingleActivityViewPart.ID, TrainingCenterDataCache.getSelected().toString(), 1);
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
		writeToStatusLine("Run vom " + selectedRun.getActivities().getActivity().get(0).getId().toString());
	    }
	});

	// load garmin data
	IConfigurationElement[] configurationElementsFor = Platform.getExtensionRegistry().getConfigurationElementsFor("ch.iseli.sportanalyzer.myimporter");
	final IConvert2Tcx tcx = getConverterImplementation(configurationElementsFor);
	final List<TrainingCenterDatabaseT> allRuns = new ArrayList<TrainingCenterDatabaseT>();
	final List<File> allFiles = new ArrayList<File>();
	if (tcx != null) {

	    // Start the Job
	    allFiles.addAll(tcx.loadAllGPSFiles());
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
				    TrainingCenterDataCache cache = TrainingCenterDataCache.initCache(allRuns);
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

    /**
     * Passing the focus request to the viewer's control.
     */
    @Override
    public void setFocus() {
	viewer.getControl().setFocus();
    }
}