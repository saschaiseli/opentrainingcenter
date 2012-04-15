package ch.opentrainingcenter.client.views.best;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.forms.widgets.TableWrapData;
import org.eclipse.ui.forms.widgets.TableWrapLayout;
import org.eclipse.ui.part.ViewPart;

import ch.opentrainingcenter.client.model.Units;

public class BestRunsView extends ViewPart {

    public static final String ID = "ch.opentrainingcenter.client.best.bestof"; //$NON-NLS-1$

    private static final Logger logger = Logger.getLogger(BestRunsView.class);

    private FormToolkit toolkit;

    private ScrolledForm form;

    private TableWrapData td;

    @Override
    public void createPartControl(final Composite parent) {

        logger.debug("create single activity view"); //$NON-NLS-1$
        toolkit = new FormToolkit(parent.getDisplay());
        form = toolkit.createScrolledForm(parent);
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
        form.setText("Bestzeiten:");

        addText(body);

        addPace(body);
    }

    private void addText(final Composite body) {
        final Section section = toolkit.createSection(body, Section.DESCRIPTION | Section.TITLE_BAR | Section.TWISTIE | Section.EXPANDED);

        td = new TableWrapData(TableWrapData.FILL_GRAB);
        td.colspan = 1;
        td.grabHorizontal = true;
        td.grabVertical = true;

        section.setLayoutData(td);
        section.setText("Rekorde");
        section.setDescription("Workouts, die sich zeigen lassen!");

        final Composite overViewComposite = toolkit.createComposite(section);
        final GridLayout layoutClient = new GridLayout(3, false);
        overViewComposite.setLayout(layoutClient);

        addLabelAndValue(overViewComposite, "Schnellste Pace", "1.5", Units.PACE);
        addLabelAndValue(overViewComposite, "Längster Lauf", "1.5", Units.KM);
        addLabelAndValue(overViewComposite, "Längster Lauf", "1.5", Units.HOUR_MINUTE_SEC);
        addLabelAndValue(overViewComposite, "Höchster Puls", "1.5", Units.BEATS_PER_MINUTE);
        addLabelAndValue(overViewComposite, "Höchster durchschnittlicher Puls", "1.5", Units.BEATS_PER_MINUTE);
        section.setClient(overViewComposite);
    }

    private void addPace(final Composite body) {
        final Section section = toolkit.createSection(body, Section.DESCRIPTION | Section.TITLE_BAR | Section.TWISTIE | Section.EXPANDED);

        td = new TableWrapData(TableWrapData.FILL_GRAB);
        td.colspan = 1;
        td.grabHorizontal = true;
        td.grabVertical = true;

        section.setLayoutData(td);
        section.setText("Pace Rekorde");
        section.setDescription("Die besten Pace, nach Distanz sortiert");

        final Composite overViewComposite = toolkit.createComposite(section);
        final GridLayout layoutClient = new GridLayout(3, false);
        overViewComposite.setLayout(layoutClient);

        addLabelAndValue(overViewComposite, "Schnellste Pace" + " (Distanz > 5km)", "1.5", Units.PACE);
        addLabelAndValue(overViewComposite, "Schnellste Pace" + " (Distanz > 10km<=15km)", "1.5", Units.PACE);
        addLabelAndValue(overViewComposite, "Schnellste Pace" + " (Distanz > 15km<=20km)", "1.5", Units.PACE);
        addLabelAndValue(overViewComposite, "Schnellste Pace" + " (Distanz > 20km<=25km)", "1.5", Units.PACE);
        addLabelAndValue(overViewComposite, "Schnellste Pace" + " (Distanz > 25km)", "1.5", Units.PACE);

        section.setClient(overViewComposite);
    }

    private void addLabelAndValue(final Composite parent, final String label, final String value, final Units unit) {
        // Label
        final Label dauerLabel = toolkit.createLabel(parent, label + ": "); //$NON-NLS-1$
        GridData gd = new GridData();
        gd.verticalIndent = 4;
        dauerLabel.setLayoutData(gd);

        // value
        final Label dauer = toolkit.createLabel(parent, value);
        gd = new GridData();
        gd.horizontalAlignment = SWT.RIGHT;
        gd.horizontalIndent = 10;
        gd.verticalIndent = 4;
        dauer.setLayoutData(gd);

        // einheit
        final Label einheit = toolkit.createLabel(parent, unit.getName());
        gd = new GridData();
        gd.horizontalAlignment = SWT.LEFT;
        gd.horizontalIndent = 10;
        gd.verticalIndent = 4;
        einheit.setLayoutData(gd);
    }

    @Override
    public void setFocus() {
        // TODO Auto-generated method stub

    }

}
