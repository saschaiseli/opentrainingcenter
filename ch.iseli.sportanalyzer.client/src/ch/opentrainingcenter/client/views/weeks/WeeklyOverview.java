package ch.opentrainingcenter.client.views.weeks;

import org.apache.log4j.Logger;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.TableWrapData;
import org.eclipse.ui.forms.widgets.TableWrapLayout;
import org.eclipse.ui.part.ViewPart;

public class WeeklyOverview extends ViewPart {
    public static final String ID = "ch.opentrainingcenter.client.weeks.weeklyOverview"; //$NON-NLS-1$

    private static final Logger logger = Logger.getLogger(WeeklyOverview.class);

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
        body.setLayoutData(td);
        form.setText("Wochenübersicht:");
    }

    /**
     * Passing the focus request to the viewer's control.
     */
    @Override
    public void setFocus() {
        // this.getControl().setFocus();
    }
}