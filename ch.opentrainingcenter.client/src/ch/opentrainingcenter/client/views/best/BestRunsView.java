package ch.opentrainingcenter.client.views.best;

import java.util.Collection;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.forms.widgets.TableWrapData;
import org.eclipse.ui.forms.widgets.TableWrapLayout;
import org.eclipse.ui.part.ViewPart;

import ch.opentrainingcenter.client.Activator;
import ch.opentrainingcenter.client.Messages;
import ch.opentrainingcenter.client.PreferenceConstants;
import ch.opentrainingcenter.client.action.GoldMedalAction;
import ch.opentrainingcenter.client.cache.impl.TrainingCenterDataCache;
import ch.opentrainingcenter.client.model.Units;
import ch.opentrainingcenter.core.cache.IRecordListener;
import ch.opentrainingcenter.core.db.DatabaseAccessFactory;
import ch.opentrainingcenter.model.training.IGoldMedalModel;
import ch.opentrainingcenter.model.training.Intervall;
import ch.opentrainingcenter.tcx.ActivityT;
import ch.opentrainingcenter.transfer.IAthlete;

public class BestRunsView extends ViewPart {

    public static final String ID = "ch.opentrainingcenter.client.best.bestof"; //$NON-NLS-1$

    private static final Logger LOGGER = Logger.getLogger(BestRunsView.class);

    private FormToolkit toolkit;

    private TableWrapData td;

    private Label bestPace;

    private Label longestDistance;

    private Label longestRun;

    private Label highestPulse;

    private Label highAvPulse;

    private Label lowestAvPulse;

    private GoldMedalAction action;

    private IAthlete athlete;

    private Label bestKl10;

    private Label best10;

    private Label best15;

    private Label best20;

    private Label best25;

    private Section sectionPace;

    private Section sectionOverall;

    public BestRunsView() {
        TrainingCenterDataCache.getInstance().addListener(new IRecordListener<ActivityT>() {

            @Override
            public void deleteRecord(final Collection<ActivityT> entry) {
                update();
            }

            @Override
            public void recordChanged(final Collection<ActivityT> entry) {
                update();
            }
        });
    }

    private void addPace(final Composite body, final IGoldMedalModel model) {
        sectionPace = toolkit.createSection(body, Section.DESCRIPTION | ExpandableComposite.TITLE_BAR | ExpandableComposite.TWISTIE
                | ExpandableComposite.EXPANDED);

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

        bestKl10 = createLabel(overViewComposite, Messages.BestRunsView10 + Messages.BestRunsView11, model.getSchnellstePace(Intervall.KLEINER_10), Units.PACE);
        best10 = createLabel(overViewComposite, Messages.BestRunsView12 + Messages.BestRunsView13, model.getSchnellstePace(Intervall.VON10_BIS_15), Units.PACE);
        best15 = createLabel(overViewComposite, Messages.BestRunsView14 + Messages.BestRunsView15, model.getSchnellstePace(Intervall.VON15_BIS_20), Units.PACE);
        best20 = createLabel(overViewComposite, Messages.BestRunsView16 + Messages.BestRunsView17, model.getSchnellstePace(Intervall.VON20_BIS_25), Units.PACE);
        best25 = createLabel(overViewComposite, Messages.BestRunsView18 + Messages.BestRunsView19, model.getSchnellstePace(Intervall.PLUS25), Units.PACE);
        sectionPace.setClient(overViewComposite);

    }

    private void addText(final Composite body, final IGoldMedalModel model) {
        sectionOverall = toolkit.createSection(body, Section.DESCRIPTION | ExpandableComposite.TITLE_BAR | ExpandableComposite.TWISTIE
                | ExpandableComposite.EXPANDED);

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

        bestPace = createLabel(overViewComposite, Messages.BestRunsView3, model.getSchnellstePace(), Units.PACE);
        longestDistance = createLabel(overViewComposite, Messages.BestRunsView4, model.getLongestDistance(), Units.KM);
        longestRun = createLabel(overViewComposite, Messages.BestRunsView5, model.getLongestRun(), Units.HOUR_MINUTE_SEC);
        highestPulse = createLabel(overViewComposite, Messages.BestRunsView6, model.getHighestPulse(), Units.BEATS_PER_MINUTE);
        highAvPulse = createLabel(overViewComposite, Messages.BestRunsView7, model.getHighestAveragePulse(), Units.BEATS_PER_MINUTE);
        lowestAvPulse = createLabel(overViewComposite, Messages.BestRunsView20, model.getLowestAveragePulse(), Units.BEATS_PER_MINUTE);

        sectionOverall.setClient(overViewComposite);
    }

    private Label createLabel(final Composite parent, final String label, final String value, final Units unit) {
        // Label
        final Label dauerLabel = toolkit.createLabel(parent, label + ": "); //$NON-NLS-1$
        GridData gd = new GridData();
        gd.verticalIndent = 4;
        dauerLabel.setLayoutData(gd);

        // value
        final Label valueLabel = toolkit.createLabel(parent, value);
        gd = new GridData();
        gd.horizontalAlignment = SWT.RIGHT;
        gd.horizontalIndent = 10;
        gd.verticalIndent = 4;
        valueLabel.setLayoutData(gd);

        // einheit
        final Label einheit = toolkit.createLabel(parent, unit.getName());
        gd = new GridData();
        gd.horizontalAlignment = SWT.LEFT;
        gd.horizontalIndent = 10;
        gd.verticalIndent = 4;
        einheit.setLayoutData(gd);
        return valueLabel;
    }

    @Override
    public void createPartControl(final Composite parent) {

        LOGGER.debug("create single activity view"); //$NON-NLS-1$
        toolkit = new FormToolkit(parent.getDisplay());
        final ScrolledForm form = toolkit.createScrolledForm(parent);
        // form.setSize(1000, 2000);
        // gridlayout definieren

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
            athlete = DatabaseAccessFactory.getDatabaseAccess().getAthlete(Integer.parseInt(athleteId));
        } else {
            athlete = null;
        }
        action = new GoldMedalAction();
        final IGoldMedalModel model = action.getModel(DatabaseAccessFactory.getDatabaseAccess().getAllImported(athlete));

        addText(body, model);

        addPace(body, model);
    }

    @Override
    public void setFocus() {
        // TODO Auto-generated method stub

    }

    private void update() {
        final IGoldMedalModel model = action.getModel(DatabaseAccessFactory.getDatabaseAccess().getAllImported(athlete));
        bestPace.setText(model.getSchnellstePace());
        longestDistance.setText(model.getLongestDistance());
        longestRun.setText(model.getLongestRun());
        highestPulse.setText(model.getHighestPulse());
        highAvPulse.setText(model.getHighestAveragePulse());
        lowestAvPulse.setText(model.getLowestAveragePulse());

        bestKl10.setText(model.getSchnellstePace(Intervall.KLEINER_10));
        best10.setText(model.getSchnellstePace(Intervall.VON10_BIS_15));
        best15.setText(model.getSchnellstePace(Intervall.VON15_BIS_20));
        best20.setText(model.getSchnellstePace(Intervall.VON20_BIS_25));
        best25.setText(model.getSchnellstePace(Intervall.PLUS25));

        sectionOverall.redraw();
        sectionPace.redraw();
    }

}
