package ch.opentrainingcenter.client.views.chart;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.TableWrapData;
import org.eclipse.ui.forms.widgets.TableWrapLayout;
import org.eclipse.ui.part.ViewPart;

import ch.opentrainingcenter.charts.ng.ChartViewer;
import ch.opentrainingcenter.client.views.ApplicationContext;
import ch.opentrainingcenter.i18n.Messages;
import ch.opentrainingcenter.model.navigation.NavigationSelection;
import ch.opentrainingcenter.transfer.ITraining;

/**
 * View Container um Charts darzustellen.
 */
public class ChartViewPart extends ViewPart {

    public static final String ID = "ch.opentrainingcenter.client.views.chart"; //$NON-NLS-1$
    private static final Logger LOGGER = Logger.getLogger(ChartViewPart.class);
    List<ITraining> selected = new ArrayList<>();
    private FormToolkit toolkit;
    private ScrolledForm form;
    private final NavigationSelection navigationSelection;

    public ChartViewPart() {
        navigationSelection = new NavigationSelection();
    }

    @Override
    public void createPartControl(final Composite parent) {
        LOGGER.debug("create chart view"); //$NON-NLS-1$
        toolkit = new FormToolkit(parent.getDisplay());
        form = toolkit.createScrolledForm(parent);

        toolkit.decorateFormHeading(form.getForm());

        form.setText(Messages.ChartViewPart_0);
        final Composite body = form.getBody();

        final TableWrapData td = new TableWrapData(TableWrapData.FILL_GRAB);
        td.colspan = 1;
        td.grabHorizontal = true;
        td.grabVertical = true;
        body.setLayoutData(td);

        final TableWrapLayout layout = new TableWrapLayout();
        layout.numColumns = 1;
        layout.makeColumnsEqualWidth = false;
        body.setLayout(layout);
        final List<?> selection = ApplicationContext.getApplicationContext().getSelection();
        new ChartViewer(body, navigationSelection.convertSelectionToTrainings(selection));
    }

    @Override
    public void setFocus() {
    }

}
