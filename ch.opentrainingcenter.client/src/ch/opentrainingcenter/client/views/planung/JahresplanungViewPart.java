package ch.opentrainingcenter.client.views.planung;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.part.ViewPart;

public class JahresplanungViewPart extends ViewPart {

    public static final String ID = "ch.opentrainingcenter.client.views.planung.JahresplanungViewPart"; //$NON-NLS-1$

    public JahresplanungViewPart() {
    }

    @Override
    public void createPartControl(final Composite parent) {
        final Composite c = new Composite(parent, SWT.NONE);
        final Text t = new Text(c, SWT.NONE);
        t.setText("Jeppa, Jahresplanung...");
    }

    @Override
    public void setFocus() {

    }

}
