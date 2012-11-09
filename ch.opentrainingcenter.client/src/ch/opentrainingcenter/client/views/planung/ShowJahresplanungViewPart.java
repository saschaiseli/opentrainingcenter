package ch.opentrainingcenter.client.views.planung;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.part.ViewPart;

public class ShowJahresplanungViewPart extends ViewPart {
    private static final Logger LOG = Logger.getLogger(ShowJahresplanungViewPart.class);
    public final static String ID = "ch.opentrainingcenter.client.views.planung.ShowJahresplanungViewPart"; //$NON-NLS-1$

    @Override
    public void createPartControl(final Composite parent) {
        final GridLayout layout = new GridLayout(2, false);
        parent.setLayout(layout);
        final Label futureLabel = new Label(parent, SWT.NONE);

        futureLabel.setText("Zukünftige Pläne:");
        futureLabel.setLayoutData(new GridData(GridData.GRAB_HORIZONTAL));
        final PlanungFutureViewer future = new PlanungFutureViewer();
        future.createViewer(parent);

        final Label pastLabel = new Label(parent, SWT.NONE);
        pastLabel.setText("Vergangene Pläne:");
        pastLabel.setLayoutData(new GridData(GridData.GRAB_HORIZONTAL | GridData.HORIZONTAL_ALIGN_FILL));
        final PlanungPastViewer past = new PlanungPastViewer();
        past.createViewer(parent);
    }

    @Override
    public void setFocus() {
    }
}
