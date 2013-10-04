package ch.opentrainingcenter.client.views.planung;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.part.ViewPart;

import ch.opentrainingcenter.client.Activator;
import ch.opentrainingcenter.i18n.Messages;

public class ShowJahresplanungViewPart extends ViewPart {
    public static final String ID = "ch.opentrainingcenter.client.views.planung.ShowJahresplanungViewPart"; //$NON-NLS-1$
    private final IPreferenceStore store = Activator.getDefault().getPreferenceStore();

    @Override
    public void createPartControl(final Composite parent) {
        final GridLayout layout = new GridLayout(2, false);
        parent.setLayout(layout);
        final Label futureLabel = new Label(parent, SWT.NONE);

        futureLabel.setText(Messages.ShowJahresplanungViewPart_0);
        final PlanungFutureViewer future = new PlanungFutureViewer();
        future.createViewer(parent);

        final Label pastLabel = new Label(parent, SWT.NONE);
        pastLabel.setText(Messages.ShowJahresplanungViewPart_1);
        final PlanungPastViewer past = new PlanungPastViewer(store);
        past.createViewer(parent);
    }

    @Override
    public void setFocus() {
    }
}
