package ch.iseli.sportanalyzer.client.views;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.part.ViewPart;

import ch.iseli.sportanalyzer.client.cache.TrainingCenterDataCache;
import ch.iseli.sportanalyzer.tcx.TrainingCenterDatabaseT;

public class SingleActivityViewPart extends ViewPart {

    public static final String ID = "ch.iseli.sportanalyzer.client.views.singlerun";

    public SingleActivityViewPart() {
    }

    @Override
    public void createPartControl(Composite parent) {
	TrainingCenterDatabaseT t = TrainingCenterDataCache.getSelected();
	setPartName("kuckuck: " + t.getActivities().getActivity().get(0).getId().toString());

	Text text = new Text(parent, SWT.BORDER);
	text.setText(t.getActivities().getActivity().get(0).getId().toString());

	// Composite top = new Composite(parent, SWT.NONE);
	// GridLayout layout = new GridLayout();
    }

    @Override
    public void setFocus() {
    }

}
