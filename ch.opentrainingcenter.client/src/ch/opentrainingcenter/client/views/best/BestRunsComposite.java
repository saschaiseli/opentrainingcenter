package ch.opentrainingcenter.client.views.best;

import java.util.Collection;

import org.apache.log4j.Logger;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.swt.SWT;
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

import ch.opentrainingcenter.client.action.GoldMedalAction;
import ch.opentrainingcenter.client.model.Units;
import ch.opentrainingcenter.client.ui.FormToolkitSupport;
import ch.opentrainingcenter.client.views.ApplicationContext;
import ch.opentrainingcenter.client.views.overview.SingleActivityViewPart;
import ch.opentrainingcenter.core.assertions.Assertions;
import ch.opentrainingcenter.core.cache.Event;
import ch.opentrainingcenter.core.cache.IRecordListener;
import ch.opentrainingcenter.core.cache.TrainingCache;
import ch.opentrainingcenter.core.data.Pair;
import ch.opentrainingcenter.core.db.IDatabaseAccess;
import ch.opentrainingcenter.i18n.Messages;
import ch.opentrainingcenter.model.ModelFactory;
import ch.opentrainingcenter.model.training.GoldMedalTyp;
import ch.opentrainingcenter.model.training.IGoldMedalModel;
import ch.opentrainingcenter.model.training.Intervall;
import ch.opentrainingcenter.transfer.IAthlete;
import ch.opentrainingcenter.transfer.ITraining;
import ch.opentrainingcenter.transfer.Sport;

public class BestRunsComposite {
    private static final Logger LOGGER = Logger.getLogger(BestRunsComposite.class);
    private Hyperlink bestPace;
    private Hyperlink longestRun;
    private Hyperlink highestPulse;
    private Hyperlink highAvPulse;
    private Hyperlink lowestAvPulse;
    private Hyperlink longestDistance;

    private Hyperlink bestKl10;
    private Hyperlink best10;
    private Hyperlink best15;
    private Hyperlink best20;
    private Hyperlink best25;

    private Section sectionPace;
    private Section sectionOverall;
    private TableWrapData td;
    private GoldMedalAction action;
    private IGoldMedalModel model;

    private final FormToolkit toolkit;
    private final Sport sport;
    private final IDatabaseAccess databaseAccess;
    private final IAthlete athlete;
    private final Units unit;

    public BestRunsComposite(final FormToolkit toolkit, final Sport sport, final IDatabaseAccess databaseAccess, final IAthlete athlete) {
        this.toolkit = toolkit;
        this.sport = sport;
        this.databaseAccess = databaseAccess;
        this.athlete = athlete;
        if (Sport.BIKING.equals(sport)) {
            unit = Units.GESCHWINDIGKEIT;
        } else {
            unit = Units.PACE;
        }
        init();

    }

    private void init() {
        TrainingCache.getInstance().addListener(new IRecordListener<ITraining>() {

            @Override
            public void onEvent(final Collection<ITraining> entry, final Event event) {
                if (!Event.CHANGED.equals(event)) {
                    update(entry);
                } else {
                    LOGGER.info("Training hat sich geaendert, interessiert nicht, da distanz und/oder Zeit nicht geaendert werden kann"); //$NON-NLS-1$
                }
            }
        });

    }

    public void createPartControl(final Composite parent) {
        LOGGER.info("BestRunsComposite createPartControl"); //$NON-NLS-1$
        final ScrolledForm form = toolkit.createScrolledForm(parent);

        toolkit.decorateFormHeading(form.getForm());

        final TableWrapLayout layout = new TableWrapLayout();
        layout.numColumns = 1;
        layout.makeColumnsEqualWidth = false;

        final Composite body = form.getBody();
        body.setLayout(layout);

        final Composite bodyBiking = form.getBody();
        bodyBiking.setLayout(layout);

        td = new TableWrapData(TableWrapData.FILL_GRAB);
        td.colspan = 1;
        td.grabHorizontal = true;
        td.grabVertical = true;
        body.setLayoutData(td);

        form.setText(Messages.BestRunsView0 + " " + sport.getTranslated()); //$NON-NLS-1$

        action = new GoldMedalAction(sport);
        model = ModelFactory.createGoldMedalModel();

        update(null);

        addText(body);
        addPace(body);
        LOGGER.info("BestRunsComposite createPartControl finished"); //$NON-NLS-1$
    }

    private void addText(final Composite body) {
        LOGGER.info("addText Rekorde"); //$NON-NLS-1$
        sectionOverall = toolkit.createSection(body, FormToolkitSupport.SECTION_STYLE);
        sectionOverall.setExpanded(false);

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

        bestPace = createLink(overViewComposite, Messages.BestRunsView3, model.getRecord(GoldMedalTyp.SCHNELLSTE_PACE), unit);
        longestDistance = createLink(overViewComposite, Messages.BestRunsView4, model.getRecord(GoldMedalTyp.LAENGSTE_DISTANZ), Units.KM);
        longestRun = createLink(overViewComposite, Messages.BestRunsView5, model.getRecord(GoldMedalTyp.LAENGSTER_LAUF), Units.HOUR_MINUTE_SEC);
        highestPulse = createLink(overViewComposite, Messages.BestRunsView6, model.getRecord(GoldMedalTyp.HOECHSTER_PULS), Units.BEATS_PER_MINUTE);
        highAvPulse = createLink(overViewComposite, Messages.BestRunsView7, model.getRecord(GoldMedalTyp.HOECHSTER_AVERAGE_PULS), Units.BEATS_PER_MINUTE);
        lowestAvPulse = createLink(overViewComposite, Messages.BestRunsView20, model.getRecord(GoldMedalTyp.TIEFSTER_AVERAGE_PULS), Units.BEATS_PER_MINUTE);

        sectionOverall.setClient(overViewComposite);
        LOGGER.info("addText Rekorde finished"); //$NON-NLS-1$
    }

    private void addPace(final Composite body) {
        LOGGER.info("addText Beste Paces"); //$NON-NLS-1$
        sectionPace = toolkit.createSection(body, FormToolkitSupport.SECTION_STYLE);
        sectionPace.setExpanded(false);

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

        bestKl10 = createLink(overViewComposite, Messages.BestRunsView10 + Messages.BestRunsView11, model.getSchnellstePace(Intervall.KLEINER_10), unit);
        best10 = createLink(overViewComposite, Messages.BestRunsView12 + Messages.BestRunsView13, model.getSchnellstePace(Intervall.VON10_BIS_15), unit);
        best15 = createLink(overViewComposite, Messages.BestRunsView14 + Messages.BestRunsView15, model.getSchnellstePace(Intervall.VON15_BIS_20), unit);
        best20 = createLink(overViewComposite, Messages.BestRunsView16 + Messages.BestRunsView17, model.getSchnellstePace(Intervall.VON20_BIS_25), unit);
        best25 = createLink(overViewComposite, Messages.BestRunsView18 + Messages.BestRunsView19, model.getSchnellstePace(Intervall.PLUS25), unit);
        sectionPace.setClient(overViewComposite);
        LOGGER.info("addText Beste Paces finished"); //$NON-NLS-1$
    }

    private Hyperlink createLink(final Composite parent, final String label, final Pair<Long, String> pair, final Units unit) {
        // Label
        Assertions.notNull(pair, "datentupel darf nicht null sein"); //$NON-NLS-1$
        Assertions.notNull(pair.getSecond(), "Label darf nicht null sein"); //$NON-NLS-1$
        final Label dauerLabel = toolkit.createLabel(parent, label + ": "); //$NON-NLS-1$
        GridDataFactory.swtDefaults().applyTo(dauerLabel);

        final Hyperlink link = toolkit.createHyperlink(parent, pair.getSecond(), SWT.UNDERLINE_LINK);
        GridDataFactory.swtDefaults().indent(10, 4).align(SWT.RIGHT, SWT.CENTER).grab(true, true).applyTo(link);

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
            public void linkActivated(final HyperlinkEvent linktEvent) {
                @SuppressWarnings("unchecked")
                final Long id = ((Pair<Long, String>) link.getData()).getFirst();
                if (id != null) {
                    try {
                        ApplicationContext.getApplicationContext().setSelectedId(id);
                        final IWorkbenchPage activePage = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
                        activePage.showView(SingleActivityViewPart.ID, String.valueOf(id), IWorkbenchPage.VIEW_ACTIVATE);
                    } catch (final PartInitException pie) {
                        LOGGER.error(pie);
                    }
                }
            }
        });

        // einheit
        final Label einheit = toolkit.createLabel(parent, unit.getName());
        GridDataFactory.swtDefaults().indent(10, 4).align(SWT.LEFT, SWT.CENTER).applyTo(einheit);
        return link;
    }

    private void update(final Collection<ITraining> entry) {
        if (model != null) {
            Display.getDefault().asyncExec(new Runnable() {

                @Override
                public void run() {
                    LOGGER.info(String.format("Update BestRun Composite mit Sport %s", sport)); //$NON-NLS-1$
                    model = action.getModel(databaseAccess.getAllTrainings(athlete));
                    bestPace.setData(model.getRecord(GoldMedalTyp.SCHNELLSTE_PACE));
                    bestPace.setText(model.getRecord(GoldMedalTyp.SCHNELLSTE_PACE).getSecond());

                    longestDistance.setData(model.getRecord(GoldMedalTyp.LAENGSTE_DISTANZ));
                    longestDistance.setText(model.getRecord(GoldMedalTyp.LAENGSTE_DISTANZ).getSecond());

                    longestRun.setData(model.getRecord(GoldMedalTyp.LAENGSTER_LAUF));
                    longestRun.setText(model.getRecord(GoldMedalTyp.LAENGSTER_LAUF).getSecond());

                    highestPulse.setData(model.getRecord(GoldMedalTyp.HOECHSTER_PULS));
                    highestPulse.setText(model.getRecord(GoldMedalTyp.HOECHSTER_PULS).getSecond());

                    highAvPulse.setData(model.getRecord(GoldMedalTyp.HOECHSTER_AVERAGE_PULS));
                    highAvPulse.setText(model.getRecord(GoldMedalTyp.HOECHSTER_AVERAGE_PULS).getSecond());

                    lowestAvPulse.setData(model.getRecord(GoldMedalTyp.TIEFSTER_AVERAGE_PULS));
                    lowestAvPulse.setText(model.getRecord(GoldMedalTyp.TIEFSTER_AVERAGE_PULS).getSecond());

                    bestKl10.setData(model.getSchnellstePace(Intervall.KLEINER_10));
                    bestKl10.setText(model.getSchnellstePace(Intervall.KLEINER_10).getSecond());

                    best10.setData(model.getSchnellstePace(Intervall.VON10_BIS_15));
                    best10.setText(model.getSchnellstePace(Intervall.VON10_BIS_15).getSecond());

                    best15.setData(model.getSchnellstePace(Intervall.VON15_BIS_20));
                    best15.setText(model.getSchnellstePace(Intervall.VON15_BIS_20).getSecond());

                    best20.setData(model.getSchnellstePace(Intervall.VON20_BIS_25));
                    best20.setText(model.getSchnellstePace(Intervall.VON20_BIS_25).getSecond());

                    best25.setData(model.getSchnellstePace(Intervall.PLUS25));
                    best25.setText(model.getSchnellstePace(Intervall.PLUS25).getSecond());

                    sectionOverall.setExpanded(true);
                    sectionPace.setExpanded(true);

                    sectionOverall.redraw();
                    sectionPace.redraw();
                    LOGGER.info(String.format("Update BestRun finished...")); //$NON-NLS-1$
                }
            });
        }
    }
}
