package ch.opentrainingcenter.client.views.shoes;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Table;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.events.ExpansionAdapter;
import org.eclipse.ui.forms.events.ExpansionEvent;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.part.ViewPart;

import ch.opentrainingcenter.client.ui.FormToolkitSupport;
import ch.opentrainingcenter.client.ui.tableviewer.SchuhTableViewer;
import ch.opentrainingcenter.client.views.ApplicationContext;
import ch.opentrainingcenter.client.views.dialoge.SchuhDialog;
import ch.opentrainingcenter.client.views.routen.RoutenView;
import ch.opentrainingcenter.core.cache.Event;
import ch.opentrainingcenter.core.cache.IRecordListener;
import ch.opentrainingcenter.core.cache.SchuhCache;
import ch.opentrainingcenter.core.cache.TrainingCache;
import ch.opentrainingcenter.core.data.Pair;
import ch.opentrainingcenter.core.db.IDatabaseAccess;
import ch.opentrainingcenter.core.service.IDatabaseService;
import ch.opentrainingcenter.i18n.Messages;
import ch.opentrainingcenter.transfer.IAthlete;
import ch.opentrainingcenter.transfer.IShoe;
import ch.opentrainingcenter.transfer.ITraining;

public class ShoeViewPart extends ViewPart {

    private static final Logger LOGGER = Logger.getLogger(RoutenView.class);

    private static final int SWT_TABLE_PATTERN = SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION | SWT.BORDER;
    public static final String ID = "ch.opentrainingcenter.client.views.shoes.ShoeViewPart"; //$NON-NLS-1$

    private FormToolkit toolkit;

    private final IAthlete athlete;
    private final IDatabaseAccess databaseAccess;

    private ScrolledForm form;
    private Section section;
    private SchuhTableViewer tableViewer;

    private final SchuhCache shoeCache = SchuhCache.getInstance();
    private Action deleteAction;

    public ShoeViewPart() {
        LOGGER.debug("<< --- ShoeViewPart"); //$NON-NLS-1$
        athlete = ApplicationContext.getApplicationContext().getAthlete();
        final IDatabaseService service = (IDatabaseService) PlatformUI.getWorkbench().getService(IDatabaseService.class);
        databaseAccess = service.getDatabaseAccess();
        LOGGER.debug("ShoeViewPart --- >>"); //$NON-NLS-1$
    }

    @Override
    public void createPartControl(final Composite parent) {
        LOGGER.info("create Route view"); //$NON-NLS-1$
        toolkit = new FormToolkit(parent.getDisplay());
        final Composite main = toolkit.createComposite(parent);

        GridLayoutFactory.fillDefaults().applyTo(main);
        GridDataFactory.fillDefaults().align(SWT.FILL, SWT.FILL).grab(true, true).applyTo(main);

        form = toolkit.createScrolledForm(main);
        GridLayoutFactory.swtDefaults().applyTo(form);
        GridDataFactory.swtDefaults().align(SWT.FILL, SWT.FILL).grab(true, true).applyTo(form);

        toolkit.decorateFormHeading(form.getForm());

        form.setText(Messages.RoutenView_Schuhe);
        GridLayoutFactory.swtDefaults().applyTo(form);
        GridDataFactory.swtDefaults().align(SWT.FILL, SWT.FILL).grab(true, true).applyTo(form);

        final Composite bodySchuhe = form.getBody();
        GridLayoutFactory.swtDefaults().applyTo(bodySchuhe);
        GridDataFactory.swtDefaults().align(SWT.FILL, SWT.FILL).grab(true, true).applyTo(bodySchuhe);

        section = toolkit.createSection(bodySchuhe, FormToolkitSupport.SECTION_STYLE);
        section.addExpansionListener(new ExpansionAdapter() {
            @Override
            public void expansionStateChanged(final ExpansionEvent e) {
                form.reflow(true);
            }
        });
        section.setExpanded(true);

        section.setText(Messages.RoutenView_12);
        section.setDescription(Messages.RoutenView_14);
        GridLayoutFactory.swtDefaults().applyTo(section);
        GridDataFactory.swtDefaults().align(SWT.FILL, SWT.FILL).grab(true, true).applyTo(section);

        final Composite compositeSchuhe = toolkit.createComposite(section);
        GridLayoutFactory.swtDefaults().applyTo(compositeSchuhe);
        GridDataFactory.swtDefaults().align(SWT.FILL, SWT.FILL).grab(true, true).applyTo(compositeSchuhe);

        tableViewer = new SchuhTableViewer(compositeSchuhe, SWT_TABLE_PATTERN);
        final List<IShoe> all = shoeCache.getAll();
        tableViewer.setAnzahlUndKilometer(getAnzahlUndKilometer(all));
        tableViewer.createTableViewer(all);
        final MenuManager menuManager = new MenuManager();
        final Table table = tableViewer.getTable();
        final Menu contextMenu = menuManager.createContextMenu(table);
        table.setMenu(contextMenu);
        getSite().registerContextMenu(menuManager, tableViewer);

        menuManager.add(new Action(Messages.RoutenView_15, null) {
            @Override
            public void run() {
                final StructuredSelection ss = (StructuredSelection) tableViewer.getSelection();
                final IShoe shoe = (IShoe) ss.getFirstElement();
                final SchuhDialog dialog = new SchuhDialog(main.getShell(), databaseAccess, shoe, Messages.RoutenView_16);
                dialog.open();
            }
        });

        deleteAction = new Action(Messages.RoutenView_17, null) {
            @Override
            public void run() {
                final StructuredSelection ss = (StructuredSelection) tableViewer.getSelection();
                final IShoe shoe = (IShoe) ss.getFirstElement();
                databaseAccess.deleteShoe(shoe.getId());
                shoeCache.remove(String.valueOf(shoe.getId()));
            }
        };
        menuManager.add(deleteAction);
        menuManager.add(new Action(Messages.RoutenView_18, null) {
            @Override
            public void run() {
                final SchuhDialog dialog = new SchuhDialog(main.getShell(), databaseAccess, athlete, Messages.SchuhDialog_Add_Schuh);
                dialog.open();
            }
        });
        tableViewer.addSelectionChangedListener(new ISelectionChangedListener() {

            @Override
            public void selectionChanged(final SelectionChangedEvent event) {
                final StructuredSelection ss = (StructuredSelection) event.getSelection();
                final IShoe shoe = (IShoe) ss.getFirstElement();
                if (shoe != null) {
                    deleteAction.setEnabled(!hasTraining(shoe));
                }
            }
        });

        tableViewer.addDoubleClickListener(new IDoubleClickListener() {

            @Override
            public void doubleClick(final DoubleClickEvent event) {
                final StructuredSelection ss = (StructuredSelection) event.getSelection();
                final IShoe shoe = (IShoe) ss.getFirstElement();
                final SchuhDialog dialog = new SchuhDialog(main.getShell(), databaseAccess, shoe, Messages.RoutenView_19);
                dialog.open();
            }
        });
        final Button addSchuh = new Button(compositeSchuhe, SWT.PUSH);
        addSchuh.setText(" + "); //$NON-NLS-1$

        addSchuh.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(final SelectionEvent e) {
                final SchuhDialog dialog = new SchuhDialog(main.getShell(), databaseAccess, athlete, Messages.SchuhDialog_Add_Schuh);
                dialog.open();
            }
        });

        TrainingCache.getInstance().addListener(new IRecordListener<ITraining>() {

            @Override
            public void onEvent(final Collection<ITraining> entry, final Event event) {
                update();
            }
        });

        shoeCache.addListener(new IRecordListener<IShoe>() {

            @Override
            public void onEvent(final Collection<IShoe> entry, final Event event) {
                update();
            }

        });
        section.setClient(compositeSchuhe);
        update();
    }

    @Override
    public void setFocus() {
        section.setFocus();
    }

    private void update() {
        Display.getDefault().asyncExec(new Runnable() {

            @Override
            public void run() {
                final List<IShoe> shoes = databaseAccess.getShoes(athlete);
                if (!tableViewer.getTable().isDisposed()) {
                    tableViewer.setAnzahlUndKilometer(getAnzahlUndKilometer(shoes));
                    tableViewer.setInput(shoes);
                    tableViewer.refresh();
                }
            }
        });
    }

    private Map<IShoe, Pair<Integer, Double>> getAnzahlUndKilometer(final List<IShoe> shoes) {
        final Map<IShoe, Pair<Integer, Double>> result = new HashMap<>();
        for (final IShoe shoe : shoes) {
            result.put(shoe, getAnzahlUndKilometer(shoe));
        }
        return result;
    }

    private Pair<Integer, Double> getAnzahlUndKilometer(final IShoe shoe) {
        final List<ITraining> all = TrainingCache.getInstance().getAll(shoe.getAthlete());
        int count = 0;
        double km = 0;
        for (final ITraining training : all) {
            if (shoe.equals(training.getShoe())) {
                count++;
                km += training.getLaengeInMeter();
            }
        }
        return new Pair<Integer, Double>(count, km);
    }

    private boolean hasTraining(final IShoe shoe) {
        final List<ITraining> all = TrainingCache.getInstance().getAll(shoe.getAthlete());
        for (final ITraining training : all) {
            if (shoe.equals(training.getShoe())) {
                return true;
            }
        }
        return false;
    }

}
