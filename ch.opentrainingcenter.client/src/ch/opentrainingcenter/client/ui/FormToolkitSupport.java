package ch.opentrainingcenter.client.ui;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;

import ch.opentrainingcenter.client.model.Units;

public final class FormToolkitSupport {

    public static final int SECTION_STYLE = Section.DESCRIPTION | ExpandableComposite.TITLE_BAR | ExpandableComposite.TWISTIE | ExpandableComposite.EXPANDED;

    private final FormToolkit toolkit;

    public FormToolkitSupport(final FormToolkit toolkit) {
        this.toolkit = toolkit;
    }

    /**
     * Erstellt auf dem {@link FormToolkit} ein Label mit Wert und Einheit.
     * 
     * @return das Label mit dem Wert, so kann dieser auch noch aktualisiert
     *         werden.
     */
    public Label addLabelAndValue(final Composite parent, final String label, final String value, final Units unit) {
        // Label
        final Label dauerLabel = toolkit.createLabel(parent, label + ": "); //$NON-NLS-1$
        GridDataFactory.swtDefaults().applyTo(dauerLabel);

        // value
        final Label valueLabel = toolkit.createLabel(parent, value);
        GridDataFactory.swtDefaults().indent(10, 4).align(SWT.RIGHT, SWT.CENTER).grab(true, true).applyTo(valueLabel);

        // einheit
        final Label einheit = toolkit.createLabel(parent, unit.getName());
        GridDataFactory.swtDefaults().indent(10, 4).align(SWT.LEFT, SWT.CENTER).applyTo(einheit);

        return valueLabel;
    }
}
