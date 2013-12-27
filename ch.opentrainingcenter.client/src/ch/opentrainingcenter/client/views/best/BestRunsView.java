package ch.opentrainingcenter.client.views.best;

import java.util.Collection;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.events.HyperlinkEvent;
import org.eclipse.ui.forms.events.IHyperlinkListener;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Hyperlink;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.forms.widgets.TableWrapData;
import org.eclipse.ui.forms.widgets.TableWrapLayout;
import org.eclipse.ui.part.ViewPart;

import ch.opentrainingcenter.client.Activator;
import ch.opentrainingcenter.client.action.GoldMedalAction;
import ch.opentrainingcenter.client.model.Units;
import ch.opentrainingcenter.client.ui.FormToolkitSupport;
import ch.opentrainingcenter.client.views.ApplicationContext;
import ch.opentrainingcenter.client.views.overview.SingleActivityViewPart;
import ch.opentrainingcenter.core.PreferenceConstants;
import ch.opentrainingcenter.core.assertions.Assertions;
import ch.opentrainingcenter.core.cache.IRecordListener;
import ch.opentrainingcenter.core.cache.TrainingCache;
import ch.opentrainingcenter.core.data.Pair;
import ch.opentrainingcenter.core.db.IDatabaseAccess;
import ch.opentrainingcenter.core.service.IDatabaseService;
import ch.opentrainingcenter.i18n.Messages;
import ch.opentrainingcenter.model.training.IGoldMedalModel;
import ch.opentrainingcenter.model.training.Intervall;
import ch.opentrainingcenter.transfer.IAthlete;
import ch.opentrainingcenter.transfer.ITraining;

public class BestRunsView extends ViewPart {

    public static final String ID = "ch.opentrainingcenter.client.best.bestof"; //$NON-NLS-1$

    private static final Logger LOGGER = Logger.getLogger(BestRunsView.class);

    private FormToolkit toolkit;

    private TableWrapData td;

    private Hyperlink bestPace;

    private Hyperlink longestRun;

    private Hyperlink highestPulse;

    private Hyperlink highAvPulse;

    private Hyperlink lowestAvPulse;

    private GoldMedalAction action;

    private IAthlete athlete;

    private Hyperlink bestKl10;

    private Hyperlink best10;

    private Hyperlink best15;

    private Hyperlink best20;

    private Hyperlink best25;

    private Section sectionPace;

    private Section sectionOverall;

    private Hyperlink longestDistance;

    private final IDatabaseAccess databaseAccess;

    public BestRunsView() {
        TrainingCache.getInstance().addListener(new IRecordListener<ITraining>() {

            @Override
            public void recordChanged(final Collection<ITraining> entry) {
                update();
            }

            @Override
            public void deleteRecord(final Collection<ITraining> entry) {
                update();
            }
        });
        final IDatabaseService service = (IDatabaseService) PlatformUI.getWorkbench().getService(IDatabaseService.class);
        databaseAccess = service.getDatabaseAccess();
    }

    @Override
    public void createPartControl(final Composite parent) {

        LOGGER.debug("create BestRunsView view"); //$NON-NLS-1$
        toolkit = new FormToolkit(parent.getDisplay());
        final ScrolledForm form = toolkit.createScrolledForm(parent);

        toolkit.decorateFormHeading(form.getForm());

        final TableWrapLayout layout = new TableWrapLayout();
        layout.numColumns = 1;
        layout.makeColumnsEqualWidth = false;

        final Composite body = form.getBody();
        body.setLayout(layout);

        td = new TableWrapData(TableWrapData.FILL_GRAB);
        td.colspan = 1;
        td.grabHorizontal = true;
        td.grabVertical = true;
        body.setLayoutData(td);
        form.setText(Messages.BestRunsView0);
        final String athleteId = Activator.getDefault().getPreferenceStore().getString(PreferenceConstants.ATHLETE_ID);
        if (athleteId != null && athleteId.length() > 0) {
            athlete = databaseAccess.getAthlete(Integer.parseInt(athleteId));
        } else {
            athlete = null;
        }
        action = new GoldMedalAction();
        final IGoldMedalModel model = action.getModel(databaseAccess.getAllTrainings(athlete));

        addText(body, model);

        addPace(body, model);
    }

    private void addPace(final Composite body, final IGoldMedalModel model) {
        sectionPace = toolkit.createSection(body, FormToolkitSupport.SECTION_STYLE);

        td = new TableWrapData(TableWrapData.FILL_GRAB);
        td.colspan = 1;
        td.grabHorizontal = true;
        td.grabVertical = true;

        sectionPace.setLayoutData(td);
        sectionPace.setText(Messages.BestRunsView8);
        sectionPace.setDescription(Messages.BestRunsView9);

        final Composite overViewComposite = toolkit.createComposite(sectionPace);
        final GridLayout layoutClient = new GridLayout(3, false);
        overViewComposite.setLayout(layoutClient);

        bestKl10 = createLink(overViewComposite, Messages.BestRunsView10 + Messages.BestRunsView11, model.getSchnellstePace(Intervall.KLEINER_10), Units.PACE);
        best10 = createLink(overViewComposite, Messages.BestRunsView12 + Messages.BestRunsView13, model.getSchnellstePace(Intervall.VON10_BIS_15), Units.PACE);
        best15 = createLink(overViewComposite, Messages.BestRunsView14 + Messages.BestRunsView15, model.getSchnellstePace(Intervall.VON15_BIS_20), Units.PACE);
        best20 = createLink(overViewComposite, Messages.BestRunsView16 + Messages.BestRunsView17, model.getSchnellstePace(Intervall.VON20_BIS_25), Units.PACE);
        best25 = createLink(overViewComposite, Messages.BestRunsView18 + Messages.BestRunsView19, model.getSchnellstePace(Intervall.PLUS25), Units.PACE);
        sectionPace.setClient(overViewComposite);

    }

    private void addText(final Composite body, final IGoldMedalModel model) {
        sectionOverall = toolkit.createSection(body, FormToolkitSupport.SECTION_STYLE);

        td = new TableWrapData(TableWrapData.FILL_GRAB);
        td.colspan = 1;
        td.grabHorizontal = true;
        td.grabVertical = true;

        sectionOverall.setLayoutData(td);
        sectionOverall.setText(Messages.BestRunsView1);
        sectionOverall.setDescription(Messages.BestRunsView2);

        final Composite overViewComposite = toolkit.createComposite(sectionOverall);
        final GridLayout layoutClient = new GridLayout(3, false);
        overViewComposite.setLayout(layoutClient);

        bestPace = createLink(overViewComposite, Messages.BestRunsView3, model.getSchnellstePace(), Units.PACE);
        longestDistance = createLink(overViewComposite, Messages.BestRunsView4, model.getLongestDistance(), Units.KM);
        longestRun = createLink(overViewComposite, Messages.BestRunsView5, model.getLongestRun(), Units.HOUR_MINUTE_SEC);
        highestPulse = createLink(overViewComposite, Messages.BestRunsView6, model.getHighestPulse(), Units.BEATS_PER_MINUTE);
        highAvPulse = createLink(overViewComposite, Messages.BestRunsView7, model.getHighestAveragePulse(), Units.BEATS_PER_MINUTE);
        lowestAvPulse = createLink(overViewComposite, Messages.BestRunsView20, model.getLowestAveragePulse(), Units.BEATS_PER_MINUTE);

        sectionOverall.setClient(overViewComposite);
    }

    private Hyperlink createLink(final Composite parent, final String label, final Pair<Long, String> pair, final Units unit) {
        // Label
        Assertions.notNull(pair, "datentupel darf nicht null sein"); //$NON-NLS-1$
        Assertions.notNull(pair.getSecond(), "Label darf nicht null sein"); //$NON-NLS-1$
        final Label dauerLabel = toolkit.createLabel(parent, label + ": "); //$NON-NLS-1$
        GridData gd = new GridData();
        gd.verticalIndent = 4;
        dauerLabel.setLayoutData(gd);

        final Hyperlink link = toolkit.createHyperlink(parent, pair.getSecond(), SWT.UNDERLINE_LINK);
        gd = new GridData();
        gd.horizontalAlignment = SWT.RIGHT;
        gd.horizontalIndent = 10;
        gd.verticalIndent = 4;
        link.setLayoutData(gd);
        link.setData(pair);
        link.setToolTipText(Messages.BestRunsView_0);
        link.addHyperlinkListener(new IHyperlinkListener() {

            @Override
            public void linkExited(final HyperlinkEvent e) {
                // do nothing
            }

            @Override
            public void linkEntered(final HyperlinkEvent e) {
                // do nothing
            }

            @Override
            public void linkActivated(final HyperlinkEvent e) {
                final String hash = String.valueOf(pair.getFirst());
                try {
                    ApplicationContext.getApplicationContext().setSelectedId(pair.getFirst());
                    PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
                            .showView(SingleActivityViewPart.ID, hash, IWorkbenchPage.VIEW_ACTIVATE);
                } catch (final PartInitException e1) {
                    e1.printStackTrace();
                }
            }
        });

        // einheit
        final Label einheit = toolkit.createLabel(parent, unit.getName());
        gd = new GridData();
        gd.horizontalAlignment = SWT.LEFT;
        gd.horizontalIndent = 10;
        gd.verticalIndent = 4;
        einheit.setLayoutData(gd);
        return link;
    }

    @Override
    public void setFocus() {
        // do nothing
    }

    private void update() {
        Display.getDefault().asyncExec(new Runnable() {

            @Override
            public void run() {
                final IGoldMedalModel model = action.getModel(databaseAccess.getAllTrainings(athlete));
                bestPace.setText(model.getSchnellstePace().getSecond());
                longestDistance.setText(model.getLongestDistance().getSecond());
                longestDistance.setText(model.getLongestDistance().getSecond());
                longestRun.setText(model.getLongestRun().getSecond());
                highestPulse.setText(model.getHighestPulse().getSecond());
                highAvPulse.setText(model.getHighestAveragePulse().getSecond());
                lowestAvPulse.setText(model.getLowestAveragePulse().getSecond());

                bestKl10.setText(model.getSchnellstePace(Intervall.KLEINER_10).getSecond());
                best10.setText(model.getSchnellstePace(Intervall.VON10_BIS_15).getSecond());
                best15.setText(model.getSchnellstePace(Intervall.VON15_BIS_20).getSecond());
                best20.setText(model.getSchnellstePace(Intervall.VON20_BIS_25).getSecond());
                best25.setText(model.getSchnellstePace(Intervall.PLUS25).getSecond());

                sectionOverall.redraw();
                sectionPace.redraw();
            }
        });
    }
}
