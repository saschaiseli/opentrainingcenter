package ch.iseli.sportanalyzer.client.views.cardio;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.part.ViewPart;

import ch.iseli.sportanalyzer.client.cache.TrainingCenterDataCache;
import ch.iseli.sportanalyzer.client.cache.TrainingCenterDatabaseTParent;

public class CardioView extends ViewPart {

    public static final String ID = "ch.iseli.sportanalyzer.client.views.cardio";

    public static final String IMAGE = "icons/cardiology.png";
    TrainingCenterDataCache cache = TrainingCenterDataCache.getInstance();
    private final TrainingCenterDatabaseTParent selected;

    // private final WorkoutT workoutT;

    public CardioView() {
        selected = cache.getSelected();
        selected.getTrainingCenterDatabase().getActivities().getActivity().get(0);
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
