package ch.opentrainingcenter.client.views.einstellungen;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.ISelectionService;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.events.ExpansionAdapter;
import org.eclipse.ui.forms.events.ExpansionEvent;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.part.ViewPart;

import ch.opentrainingcenter.client.cache.SchuhCache;
import ch.opentrainingcenter.client.ui.FormToolkitSupport;
import ch.opentrainingcenter.client.ui.tableviewer.RoutenTableViewer;
import ch.opentrainingcenter.client.ui.tableviewer.SchuhTableViewer;
import ch.opentrainingcenter.client.ui.tableviewer.TrackTableViewer;
import ch.opentrainingcenter.client.views.ApplicationContext;
import ch.opentrainingcenter.client.views.dialoge.SchuhDialog;
import ch.opentrainingcenter.core.cache.IRecordListener;
import ch.opentrainingcenter.core.db.IDatabaseAccess;
import ch.opentrainingcenter.core.service.IDatabaseService;
import ch.opentrainingcenter.i18n.Messages;
import ch.opentrainingcenter.transfer.IAthlete;
import ch.opentrainingcenter.transfer.IRoute;
import ch.opentrainingcenter.transfer.IShoe;
import ch.opentrainingcenter.transfer.ITraining;

public class RoutenView extends ViewPart implements ISelectionListener {

    public static final String ID = "ch.opentrainingcenter.client.views.einstellungen.routen"; //$NON-NLS-1$

    private static final Logger LOGGER = Logger.getLogger(RoutenView.class);

    private static final int SWT_TABLE_PATTERN = SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION | SWT.BORDER;

    private final List<IRoute> routen = new ArrayList<>();

    private final List<IShoe> schuhe = new ArrayList<>();

    private final List<ITraining> tracks = new ArrayList<>();

    private FormToolkit toolkit;

    private ScrolledForm form;

    private final IAthlete athlete;

    private IAthlete selectedAthlete;

    private Section sectionRouten;

    private TrackTableViewer viewerTracks;

    private RoutenTableViewer viewerRouten;

    private SchuhTableViewer schuhTable;

    private final IDatabaseAccess databaseAccess;

    private ScrolledForm formSchuhe;

    private Section sectionSchuhe;

    private final SchuhCache shoeCache = SchuhCache.getInstance();

    public RoutenView() {
        athlete = ApplicationContext.getApplicationContext().getAthlete();
        final IDatabaseService service = (IDatabaseService) PlatformUI.getWorkbench().getService(IDatabaseService.class);
        databaseAccess = service.getDatabaseAccess();
    }

    @Override
    public void createPartControl(final Composite parent) {
        LOGGER.info("create Route view"); //$NON-NLS-1$
        toolkit = new FormToolkit(parent.getDisplay());
        final Composite main = toolkit.createComposite(parent);
        //
        GridLayoutFactory.fillDefaults().applyTo(main);
        GridDataFactory.fillDefaults().align(SWT.FILL, SWT.FILL).grab(true, true).applyTo(main);

        form = toolkit.createScrolledForm(main);
        GridLayoutFactory.swtDefaults().applyTo(form);
        GridDataFactory.swtDefaults().align(SWT.FILL, SWT.FILL).grab(true, true).applyTo(form);

        toolkit.decorateFormHeading(form.getForm());
        form.setText(Messages.RoutenView_0);

        final ISelectionService selectionService = getSite().getWorkbenchWindow().getSelectionService();
        selectionService.addSelectionListener(this);

        final Composite body = form.getBody();
        GridLayoutFactory.swtDefaults().applyTo(body);
        GridDataFactory.swtDefaults().align(SWT.FILL, SWT.FILL).grab(true, true).applyTo(body);

        sectionRouten = toolkit.createSection(body, FormToolkitSupport.SECTION_STYLE);
        sectionRouten.addExpansionListener(new ExpansionAdapter() {
            @Override
            public void expansionStateChanged(final ExpansionEvent e) {
                form.reflow(true);
            }
        });
        sectionRouten.setExpanded(true);

        sectionRouten.setText(Messages.RoutenView_1);
        sectionRouten.setDescription(Messages.RoutenView_2);
        GridLayoutFactory.swtDefaults().applyTo(sectionRouten);
        GridDataFactory.swtDefaults().align(SWT.FILL, SWT.FILL).grab(true, true).applyTo(sectionRouten);

        final Composite composite = toolkit.createComposite(sectionRouten);
        GridLayoutFactory.swtDefaults().numColumns(2).equalWidth(true).applyTo(composite);
        GridDataFactory.swtDefaults().align(SWT.FILL, SWT.FILL).grab(true, true).applyTo(composite);

        final Composite compositeLeft = new Composite(composite, SWT.NONE);
        GridLayoutFactory.swtDefaults().applyTo(compositeLeft);
        GridDataFactory.swtDefaults().align(SWT.FILL, SWT.FILL).grab(true, true).applyTo(compositeLeft);

        createLeftComposite(compositeLeft);

        final Composite compositeRight = new Composite(composite, SWT.NONE);
        GridLayoutFactory.swtDefaults().applyTo(compositeRight);
        GridDataFactory.swtDefaults().align(SWT.FILL, SWT.FILL).grab(true, true).applyTo(compositeRight);

        createRightComposite(compositeRight);
        sectionRouten.setClient(composite);

        // -------------------------------
        formSchuhe = toolkit.createScrolledForm(main);
        toolkit.decorateFormHeading(formSchuhe.getForm());
        formSchuhe.setText(Messages.RoutenView_Schuhe);
        GridLayoutFactory.swtDefaults().applyTo(formSchuhe);
        GridDataFactory.swtDefaults().align(SWT.FILL, SWT.FILL).grab(true, true).applyTo(formSchuhe);

        final Composite bodySchuhe = formSchuhe.getBody();
        GridLayoutFactory.swtDefaults().applyTo(bodySchuhe);
        GridDataFactory.swtDefaults().align(SWT.FILL, SWT.FILL).grab(true, true).applyTo(bodySchuhe);

        sectionSchuhe = toolkit.createSection(bodySchuhe, FormToolkitSupport.SECTION_STYLE);
        sectionSchuhe.addExpansionListener(new ExpansionAdapter() {
            @Override
            public void expansionStateChanged(final ExpansionEvent e) {
                formSchuhe.reflow(true);
            }
        });
        sectionSchuhe.setExpanded(true);

        sectionSchuhe.setText(Messages.RoutenView_1);
        sectionSchuhe.setDescription(Messages.RoutenView_2);
        GridLayoutFactory.swtDefaults().applyTo(sectionRouten);
        GridDataFactory.swtDefaults().align(SWT.FILL, SWT.FILL).grab(true, true).applyTo(sectionSchuhe);

        final Composite compositeSchuhe = toolkit.createComposite(sectionSchuhe);
        GridLayoutFactory.swtDefaults().applyTo(compositeSchuhe);
        GridDataFactory.swtDefaults().align(SWT.FILL, SWT.FILL).grab(true, true).applyTo(compositeSchuhe);

        schuhTable = new SchuhTableViewer(compositeSchuhe, SWT_TABLE_PATTERN);
        schuhTable.createTableViewer(shoeCache.getAll());
        final Button addSchuh = new Button(compositeSchuhe, SWT.PUSH);
        addSchuh.setText("+"); //$NON-NLS-1$

        addSchuh.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(final SelectionEvent e) {
                final SchuhDialog dialog = new SchuhDialog(main.getShell(), databaseAccess, athlete);
                dialog.open();
            }
        });
        shoeCache.addListener(new IRecordListener<IShoe>() {

            @Override
            public void recordChanged(final Collection<IShoe> entry) {
                schuhTable.setInput(entry);
                schuhTable.refresh();
            }

            @Override
            public void deleteRecord(final Collection<IShoe> entry) {

            }
        });
        sectionSchuhe.setClient(compositeSchuhe);
    }

    private void createLeftComposite(final Composite composite) {
        final Label headLeft = new Label(composite, SWT.BOLD);
        headLeft.setText(Messages.RoutenView_3);

        viewerRouten = new RoutenTableViewer(composite, SWT_TABLE_PATTERN);
        viewerRouten.createTableViewer(routen, tracks);
    }

    private void createRightComposite(final Composite compositeRight) {
        final Label headRight = new Label(compositeRight, SWT.BOLD);
        headRight.setText(Messages.RoutenView_4);

        viewerTracks = new TrackTableViewer(compositeRight, SWT_TABLE_PATTERN);
        viewerTracks.createTableViewer(tracks);
    }

    @Override
    public void setFocus() {
        sectionRouten.setFocus();
    }

    @Override
    public void dispose() {
        final ISelectionService s = getSite().getWorkbenchWindow().getSelectionService();
        s.removeSelectionListener(this);
        super.dispose();
    }

    @Override
    public void selectionChanged(final IWorkbenchPart part, final ISelection selection) {
        if (part instanceof UserView && selection instanceof IStructuredSelection) {
            final IStructuredSelection sel = (IStructuredSelection) selection;
            final Object firstElement = sel.getFirstElement();
            routen.clear();
            tracks.clear();
            if (firstElement instanceof IAthlete) {
                selectedAthlete = (IAthlete) firstElement;
                routen.addAll(databaseAccess.getRoute(selectedAthlete));
                tracks.addAll(databaseAccess.getAllTrainings(selectedAthlete));
                LOGGER.info(String.format("Neuer Athlete selektiert: %s mit %s Routen und %s Tracks", firstElement, routen.size(), tracks.size())); //$NON-NLS-1$      
            }
            final boolean enabled = athlete != null && athlete.equals(selectedAthlete);
            viewerRouten.getTable().setEnabled(enabled);
            viewerTracks.getTable().setEnabled(enabled);
            viewerRouten.refresh();
            viewerTracks.refresh();
        } else {
            LOGGER.info(String.format("Part: %s Selection: %s", part, selection)); //$NON-NLS-1$
        }
    }
}
