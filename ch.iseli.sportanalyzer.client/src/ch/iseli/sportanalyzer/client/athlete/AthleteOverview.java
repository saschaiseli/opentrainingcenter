package ch.iseli.sportanalyzer.client.athlete;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.part.ViewPart;

public class AthleteOverview extends ViewPart {

    public final static String ID = "ch.iseli.sportanalyzer.client.athlete.athleteOverview";

    public AthleteOverview() {
    }

    @Override
    public void createPartControl(Composite parent) {
        Text text = new Text(parent, SWT.BORDER);
        text.setText(ID);
    }

    @Override
    public void setFocus() {

    }

}
