package ch.opentrainingcenter.client.views.navigation;

import java.util.Collection;
import java.util.List;

import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MenuEvent;
import org.eclipse.swt.events.MenuListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPartSite;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

import ch.opentrainingcenter.client.Activator;
import ch.opentrainingcenter.client.Messages;
import ch.opentrainingcenter.client.PreferenceConstants;
import ch.opentrainingcenter.client.action.job.LoadJahresplanung;
import ch.opentrainingcenter.client.cache.Cache;
import ch.opentrainingcenter.client.cache.IRecordListener;
import ch.opentrainingcenter.client.cache.impl.HealthCache;
import ch.opentrainingcenter.client.cache.impl.TrainingCenterDataCache;
import ch.opentrainingcenter.client.model.navigation.IKalenderWocheNavigationModel;
import ch.opentrainingcenter.client.model.navigation.impl.ConcreteHealth;
import ch.opentrainingcenter.client.model.navigation.impl.ConcreteImported;
import ch.opentrainingcenter.client.model.navigation.impl.DecoratImported;
import ch.opentrainingcenter.client.model.navigation.impl.KWTraining;
import ch.opentrainingcenter.client.model.navigation.impl.NavigationElementComparer;
import ch.opentrainingcenter.client.views.ApplicationContext;
import ch.opentrainingcenter.client.views.dialoge.HealthDialog;
import ch.opentrainingcenter.client.views.navigation.tree.KalenderWocheTreeContentProvider;
import ch.opentrainingcenter.client.views.navigation.tree.KalenderWocheTreeLabelProvider;
import ch.opentrainingcenter.client.views.overview.SingleActivityViewPart;
import ch.opentrainingcenter.db.DatabaseAccessFactory;
import ch.opentrainingcenter.db.IDatabaseAccess;
import ch.opentrainingcenter.importer.ConvertContainer;
import ch.opentrainingcenter.importer.ExtensionHelper;
import ch.opentrainingcenter.importer.IImportedConverter;
import ch.opentrainingcenter.importer.ImporterFactory;
import ch.opentrainingcenter.importer.LoadActivityJob;
import ch.opentrainingcenter.tcx.ActivityT;
import ch.opentrainingcenter.transfer.IAthlete;
import ch.opentrainingcenter.transfer.IHealth;
import ch.opentrainingcenter.transfer.IImported;

public class KalenderWocheNavigationView extends ViewPart {

    public static final String ID = "ch.opentrainingcenter.client.kalenderNavigationView"; //$NON-NLS-1$

    private final IDatabaseAccess db = DatabaseAccessFactory.getDatabaseAccess();

    private final IPreferenceStore store = Activator.getDefault().getPreferenceStore();

    private final Cache cache = TrainingCenterDataCache.getInstance();
    private final HealthCache healthCache = HealthCache.getInstance();
    private final IKalenderWocheNavigationModel treeModel = new KWTraining();

    private final ApplicationContext context = ApplicationContext.getApplicationContext();

    private final ConvertContainer cc = new ConvertContainer(ExtensionHelper.getConverters());

    private TreeViewer viewer;
    private StatusLineWriter status;
    private IAthlete athlete;

    public KalenderWocheNavigationView() {
        final String athleteId = store.getString(PreferenceConstants.ATHLETE_ID);
        if (athleteId != null && athleteId.length() > 0) {
            final int id = Integer.parseInt(athleteId);
            athlete = db.getAthlete(id);
        } else {
            athlete = null;
        }
        updateModel();
    }

    private void updateModel() {
        Display.getDefault().asyncExec(new Runnable() {

            @Override
            public void run() {
                try {
                    final List<IImported> imported = db.getAllImported(athlete);
                    treeModel.reset();
                    treeModel.addItems(healthCache.getAll());
                    treeModel.addItems(DecoratImported.decorate(imported));
                    if (viewer != null) {
                        viewer.refresh();
                    }
                } catch (final Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public TreeViewer getTreeViewer() {
        return viewer;
    }

    @Override
    public void createPartControl(final Composite parent) {
        parent.getShell().setMaximized(true);

        status = new StatusLineWriter(getViewSite().getActionBars().getStatusLineManager());
        viewer = new TreeViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);
        viewer.setContentProvider(new KalenderWocheTreeContentProvider());
        viewer.setLabelProvider(new KalenderWocheTreeLabelProvider());
        viewer.setComparer(new NavigationElementComparer());
        viewer.setUseHashlookup(true);
        viewer.setInput(treeModel);

        final MenuManager menuManager = new MenuManager("KontextMenu"); //$NON-NLS-1$
        final IWorkbenchPartSite site = getSite();
        site.registerContextMenu("ch.opentrainingcenter.client.somepopup", menuManager, viewer); //$NON-NLS-1$
        site.setSelectionProvider(viewer);

        menuManager.setRemoveAllWhenShown(true);

        final Tree tree = viewer.getTree();
        final Menu menu = menuManager.createContextMenu(tree);
        menu.addMenuListener(new MenuListener() {

            @Override
            public void menuShown(final MenuEvent e) {

                final Object selection = context.getSelection().get(0);
                if (selection instanceof ConcreteImported) {
                    // menu disablen wenn je nach runtype
                }
            }

            @Override
            public void menuHidden(final MenuEvent e) {
            }
        });
        tree.setMenu(menu);

        viewer.addSelectionChangedListener(new ISelectionChangedListener() {

            @Override
            public void selectionChanged(final SelectionChangedEvent event) {
                final IStructuredSelection selection = (IStructuredSelection) event.getSelection();
                final Object first = selection.getFirstElement();
                context.setSelection(selection.toArray());
                status.writeStatusLine(first);
            }
        });

        viewer.addDoubleClickListener(new IDoubleClickListener() {

            @Override
            public void doubleClick(final DoubleClickEvent event) {
                final IStructuredSelection selection = (IStructuredSelection) event.getSelection();
                final Object item = selection.getFirstElement();

                if (item instanceof ConcreteImported) {
                    openSingleRunView(((ConcreteImported) item).getImported());
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

        healthCache.addListener(new IRecordListener<ConcreteHealth>() {

            @Override
            public void recordChanged(final Collection<ConcreteHealth> entry) {
                updateModel();
            }

            @Override
            public void deleteRecord(final Collection<ConcreteHealth> entry) {
                updateModel();
            }
        });

        cache.addListener(new IRecordListener<ActivityT>() {

            @Override
            public void deleteRecord(final Collection<ActivityT> entry) {

                updateModel();
                final IWorkbenchPage wbp = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
                for (final ActivityT record : entry) {
                    final String secondaryViewId = getSecondaryId(record);
                    wbp.hideView(wbp.findViewReference(SingleActivityViewPart.ID, secondaryViewId));
                }
            }

            @Override
            public void recordChanged(final Collection<ActivityT> entry) {
                updateModel();
            }
        });
        final IImported newestRun = db.getNewestRun(athlete);
        if (newestRun != null) {
            // openSingleRunView(newestRun);
        }
    }

    private String getSecondaryId(final ActivityT record) {
        return String.valueOf(record.getId().hashCode());
    }

    /**
     * Öffnet einen Record asynchron
     * 
     * @param record
     */
    private void openSingleRunView(final IImported record) {
        context.setSelectedId(record.getActivityId());
        final IImportedConverter loader = ImporterFactory.createGpsFileLoader(store, cc);
        final LoadActivityJob job = new LoadActivityJob(Messages.NavigationView1, record, cache, loader);
        job.addJobChangeListener(new ImportActivityJobListener(record));
        job.schedule();
        viewer.setSelection(new StructuredSelection(record), true);
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
