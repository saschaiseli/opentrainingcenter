package ch.opentrainingcenter.client.views.summary;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.part.ViewPart;

import ch.opentrainingcenter.i18n.Messages;
import ch.opentrainingcenter.model.summary.SummaryModel;
import ch.opentrainingcenter.transfer.Sport;

public class SummaryView extends ViewPart {

    public static final String ID = "ch.opentrainingcenter.client.views.summary.SummaryView"; //$NON-NLS-1$

    private static final Logger LOGGER = Logger.getLogger(SummaryView.class);

    private FormToolkit toolkit;

    private ScrolledForm form;

    private Composite body;

    Map<Sport, SummarySection> sections = new HashMap<>();

    @Override
    public void createPartControl(final Composite parent) {
        LOGGER.info("create Summary view"); //$NON-NLS-1$
        toolkit = new FormToolkit(parent.getDisplay());

        form = toolkit.createScrolledForm(parent);
        form.setText(Messages.SummaryView_1);
        toolkit.decorateFormHeading(form.getForm());

        body = form.getBody();
        body.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_CYAN));
        GridLayoutFactory.swtDefaults().numColumns(1).applyTo(body);
        GridDataFactory.swtDefaults().align(SWT.FILL, SWT.FILL).applyTo(body);
    }

    @Override
    public void setFocus() {

    }

    public void setModels(final Map<Sport, SummaryModel> models) {
        for (final Map.Entry<Sport, SummaryModel> entry : models.entrySet()) {
            final SummarySection summarySection = new SummarySection(toolkit);
            summarySection.addSection(body, entry.getKey());
            summarySection.setModel(entry.getValue(), entry.getKey());
        }
    }
}
