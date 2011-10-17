package ch.iseli.sportanalyzer.client.views;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.part.ViewPart;

public class CardioView extends ViewPart {

    public static final String ID = "ch.iseli.sportanalyzer.client.views.cardio";

    public CardioView() {
	// TODO Auto-generated constructor stub
    }

    @Override
    public void createPartControl(Composite parent) {
	Text text = new Text(parent, SWT.BORDER);
	text.setText(ID);

    }

    @Override
    public void setFocus() {
	// TODO Auto-generated method stub

    }

}
