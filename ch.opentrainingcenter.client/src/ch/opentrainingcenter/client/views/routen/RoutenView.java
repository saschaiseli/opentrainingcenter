package ch.opentrainingcenter.client.views.routen;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.events.ExpansionAdapter;
import org.eclipse.ui.forms.events.ExpansionEvent;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.part.ViewPart;

import ch.opentrainingcenter.client.ui.FormToolkitSupport;
import ch.opentrainingcenter.client.ui.tableviewer.RoutenTableViewer;
import ch.opentrainingcenter.client.ui.tableviewer.TrackTableViewer;
import ch.opentrainingcenter.client.views.ApplicationContext;
import ch.opentrainingcenter.core.cache.IRecordListener;
import ch.opentrainingcenter.core.cache.TrainingCache;
import ch.opentrainingcenter.core.db.IDatabaseAccess;
import ch.opentrainingcenter.core.service.IDatabaseService;
import ch.opentrainingcenter.i18n.Messages;
import ch.opentrainingcenter.transfer.IAthlete;
import ch.opentrainingcenter.transfer.IRoute;
import ch.opentrainingcenter.transfer.ITraining;

@SuppressWarnings("rawtypes")
public class RoutenView extends ViewPart implements IRecordListener {

    public static final String ID = "ch.opentrainingcenter.client.views.einstellungen.routen"; //$NON-NLS-1$

    private static final Logger LOGGER = Logger.getLogger(RoutenView.class);

    private static final int SWT_TABLE_PATTERN = SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION | SWT.BORDER;

    private final List<IRoute> routen = new ArrayList<>();

    private final List<ITraining> tracks = new ArrayList<>();

    private FormToolkit toolkit;

    private ScrolledForm form;

    private final IAthlete athlete;

    private Section sectionRouten;

    private TrackTableViewer viewerTracks;

    private RoutenTableViewer viewerRouten;

    private final IDatabaseAccess databaseAccess;

    public RoutenView() {
        athlete = ApplicationContext.getApplicationContext().getAthlete();
        final IDatabaseService service = (IDatabaseService) PlatformUI.getWorkbench().getService(IDatabaseService.class);
        databaseAccess = service.getDatabaseAccess();
    }

    @SuppressWarnings("unchecked")
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
        // StreckeCache.getInstance().addListener(this);
        TrainingCache.getInstance().addListener(this);
        update();
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
    public void recordChanged(final Collection entry) {
        update();
    }

    @Override
    public void deleteRecord(final Collection entry) {
        update();
    }

    private void update() {
        routen.clear();
        tracks.clear();
        Display.getDefault().asyncExec(new Runnable() {

            @Override
            public void run() {
                routen.addAll(databaseAccess.getRoute(athlete));
                tracks.addAll(databaseAccess.getAllTrainings(athlete));
                viewerRouten.refresh();
                viewerTracks.refresh();
            }
        });
    }
}
