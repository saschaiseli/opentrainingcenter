package ch.opentrainingcenter.client.views.navigation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.viewers.ColumnViewerToolTipSupport;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MenuAdapter;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchPartSite;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

import ch.opentrainingcenter.client.Activator;
import ch.opentrainingcenter.client.action.job.LoadJahresplanung;
import ch.opentrainingcenter.client.ui.open.OpenTrainingRunnable;
import ch.opentrainingcenter.client.views.ApplicationContext;
import ch.opentrainingcenter.client.views.dialoge.HealthDialog;
import ch.opentrainingcenter.client.views.navigation.tree.KalenderWocheTreeContentProvider;
import ch.opentrainingcenter.client.views.navigation.tree.KalenderWocheTreeLabelProvider;
import ch.opentrainingcenter.client.views.overview.SingleActivityViewPart;
import ch.opentrainingcenter.core.PreferenceConstants;
import ch.opentrainingcenter.core.cache.Cache;
import ch.opentrainingcenter.core.cache.Event;
import ch.opentrainingcenter.core.cache.HealthCache;
import ch.opentrainingcenter.core.cache.IRecordListener;
import ch.opentrainingcenter.core.cache.TrainingCache;
import ch.opentrainingcenter.core.db.IDatabaseAccess;
import ch.opentrainingcenter.core.service.IDatabaseService;
import ch.opentrainingcenter.i18n.Messages;
import ch.opentrainingcenter.model.ModelFactory;
import ch.opentrainingcenter.model.navigation.ConcreteImported;
import ch.opentrainingcenter.model.navigation.DecoratImported;
import ch.opentrainingcenter.model.navigation.IKalenderWocheNavigationModel;
import ch.opentrainingcenter.model.navigation.INavigationItem;
import ch.opentrainingcenter.model.navigation.NavigationElementComparer;
import ch.opentrainingcenter.transfer.IAthlete;
import ch.opentrainingcenter.transfer.IHealth;
import ch.opentrainingcenter.transfer.ITraining;

public class KalenderWocheNavigationView extends ViewPart {

    private static final Logger LOGGER = Logger.getLogger(KalenderWocheNavigationView.class);

    public static final String ID = "ch.opentrainingcenter.client.kalenderNavigationView"; //$NON-NLS-1$

    private final IPreferenceStore store = Activator.getDefault().getPreferenceStore();

    private final Cache cache = TrainingCache.getInstance();
    private final HealthCache healthCache = HealthCache.getInstance();
    private final IKalenderWocheNavigationModel treeModel = ModelFactory.createKwModel();

    private final ApplicationContext context = ApplicationContext.getApplicationContext();

    private TreeViewer viewer;
    private StatusLineWriter status;
    private IAthlete athlete;
    private final IDatabaseAccess databaseAccess;

    public KalenderWocheNavigationView() {
        LOGGER.debug("<< --- KalenderWocheNavigationView"); //$NON-NLS-1$
        final IDatabaseService service = (IDatabaseService) PlatformUI.getWorkbench().getService(IDatabaseService.class);
        databaseAccess = service.getDatabaseAccess();
        final String athleteId = store.getString(PreferenceConstants.ATHLETE_ID);
        if (athleteId != null && athleteId.length() > 0) {
            final int id = Integer.parseInt(athleteId);
            athlete = databaseAccess.getAthlete(id);
        } else {
            athlete = null;
        }
        updateModel();
        LOGGER.debug("KalenderWocheNavigationView -->>"); //$NON-NLS-1$
    }

    private void updateModel() {

        final List<String> list = new ArrayList<>();
        list.add("a");
        list.add("b");
        list.add("c");
        list.add("d");
        Collections.sort(list, (final String a, final String b) -> b.compareTo(a));
        for (final String string : list) {
            System.out.println(string);
        }
        Display.getDefault().asyncExec(new Runnable() {

            @Override
            public void run() {
                final List<ITraining> training = databaseAccess.getAllTrainings(athlete);
                treeModel.reset();
                treeModel.addItems(DecoratImported.decorateHealth(healthCache.getAll()));
                treeModel.addItems(DecoratImported.decorate(training));
                if (viewer != null && !viewer.getTree().isDisposed()) {
                    viewer.refresh();
                }
            }
        });
    }

    public TreeViewer getTreeViewer() {
        return viewer;
    }

    @Override
    public void createPartControl(final Composite parent) {
        status = new StatusLineWriter(getViewSite().getActionBars().getStatusLineManager());
        viewer = new TreeViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);
        viewer.setContentProvider(new KalenderWocheTreeContentProvider());
        viewer.setAutoExpandLevel(TreeViewer.ALL_LEVELS);
        viewer.setLabelProvider(new KalenderWocheTreeLabelProvider());
        viewer.setComparer(new NavigationElementComparer());
        viewer.setUseHashlookup(true);
        viewer.setInput(treeModel);
        ColumnViewerToolTipSupport.enableFor(viewer);

        final MenuManager menuManager = new MenuManager("KontextMenu"); //$NON-NLS-1$
        final IWorkbenchPartSite site = getSite();

        // selection weitergeben
        site.setSelectionProvider(viewer);
        site.registerContextMenu("ch.opentrainingcenter.client.somepopup", menuManager, viewer); //$NON-NLS-1$

        menuManager.setRemoveAllWhenShown(true);

        final Tree tree = viewer.getTree();
        final Menu menu = menuManager.createContextMenu(tree);

        menu.addMenuListener(new MenuAdapter() {
        });
        tree.setMenu(menu);

        viewer.addSelectionChangedListener(new ISelectionChangedListener() {

            @Override
            public void selectionChanged(final SelectionChangedEvent event) {
                final TreeSelection selection = (TreeSelection) event.getSelection();
                final Object[] array = selection.toArray();
                status.writeStatusLine(array);
                context.setSelection(selection.toArray());
            }
        });

        viewer.addDoubleClickListener(new IDoubleClickListener() {

            @Override
            public void doubleClick(final DoubleClickEvent event) {
                final IStructuredSelection selection = (IStructuredSelection) event.getSelection();
                final Object item = selection.getFirstElement();

                if (item instanceof ConcreteImported) {
                    final ITraining selectedTraining = ((ConcreteImported) item).getTraining();
                    openSingleRunView(selectedTraining);
                }

                if (item instanceof IHealth) {
                    final HealthDialog dialog = new HealthDialog(parent.getShell(), (IHealth) item);
                    dialog.open();
                }

                if (item instanceof Integer) {
                    // Jahr
                    openJahresplanung((Integer) item);
                }
            }

        });

        healthCache.addListener(new IRecordListener<IHealth>() {

            @Override
            public void onEvent(final Collection<IHealth> entry, final Event event) {
                if (!Event.CHANGED.equals(event)) {
                    updateModel();
                } else {
                    LOGGER.info("Health changed: Do nothing in Tree"); //$NON-NLS-1$
                }
            }
        });

        cache.addListener(new IRecordListener<ITraining>() {

            @Override
            public void onEvent(final Collection<ITraining> entry, final Event event) {
                switch (event) {
                case ADDED:
                    recordAdded(entry);
                    break;
                case CHANGED:
                    updateModel();
                    break;
                case DELETED:
                    deleteRecord(entry);
                    break;
                }
            }

            private void recordAdded(final Collection<ITraining> entry) {
                updateModel();
                if (entry.size() < 8) {
                    for (final ITraining training : entry) {
                        Display.getDefault().asyncExec(new OpenTrainingRunnable(training));
                    }
                }
            }

            private void deleteRecord(final Collection<ITraining> entry) {

                updateModel();
                final IWorkbenchPage wbp = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
                for (final ITraining record : entry) {
                    final String secondaryViewId = getSecondaryId(record);
                    wbp.hideView(wbp.findViewReference(SingleActivityViewPart.ID, secondaryViewId));
                }
            }
        });
        final ITraining newestRun = databaseAccess.getNewestTraining(athlete);
        if (newestRun != null) {
            openSingleRunView(newestRun);
        }
        getSite().getWorkbenchWindow().getSelectionService().addSelectionListener(new ISelectionListener() {

            @Override
            public void selectionChanged(final IWorkbenchPart part, final ISelection selection) {
                if (selection instanceof StructuredSelection && !selection.isEmpty()) {
                    final Object firstElement = ((StructuredSelection) selection).getFirstElement();
                    if (firstElement instanceof ITraining) {
                        final ITraining training = (ITraining) firstElement;
                        final INavigationItem model = treeModel.getImportedItem(new Date(training.getDatum()));
                        if (model != null && !(part instanceof KalenderWocheNavigationView)) {
                            final StructuredSelection element = new StructuredSelection(model);
                            viewer.setSelection(element, false);
                        }
                    }
                }
            }
        });
    }

    private String getSecondaryId(final ITraining record) {
        return String.valueOf(record.getDatum());
    }

    /**
     * Öffnet einen Record asynchron
     * 
     * @param training
     */
    private void openSingleRunView(final ITraining training) {
        Display.getDefault().asyncExec(new OpenTrainingRunnable(training));
        context.setSelectedId(training.getDatum());
        viewer.setSelection(new StructuredSelection(new ConcreteImported(training)), true);
    }

    /**
     * Öffnet die Jahresplanung für das angegebene Jahr.
     */
    private void openJahresplanung(final Integer jahr) {
        final LoadJahresplanung job = new LoadJahresplanung(Messages.KalenderWocheNavigationView_0, jahr);
        job.schedule();
    }

    @Override
    public void setFocus() {
        viewer.getControl().setFocus();
    }
}
