package ch.iseli.sportanalyzer.client.views;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.part.ViewPart;

import ch.iseli.sportanalyzer.client.cache.TrainingCenterDataCache;
import ch.iseli.sportanalyzer.tcx.AbstractStepT;
import ch.iseli.sportanalyzer.tcx.ActivityT;
import ch.iseli.sportanalyzer.tcx.StepT;
import ch.iseli.sportanalyzer.tcx.TrainingCenterDatabaseT;
import ch.iseli.sportanalyzer.tcx.WorkoutT;

public class CardioView extends ViewPart {

    public static final String            ID = "ch.iseli.sportanalyzer.client.views.cardio";
    private final TrainingCenterDatabaseT selected;
    private final ActivityT               activityT;
    private final WorkoutT                workoutT;

    public CardioView() {
        selected = TrainingCenterDataCache.getSelected();
        activityT = selected.getActivities().getActivity().get(0);
        workoutT = selected.getWorkouts().getWorkout().get(0);
    }

    @Override
    public void createPartControl(Composite parent) {
        Text text = new Text(parent, SWT.BORDER);
        text.setText(ID);
        List<AbstractStepT> steps = workoutT.getStep();
        List<StepT> sts = new ArrayList<StepT>();
        for (AbstractStepT step : steps) {
            if (step instanceof StepT) {
                sts.add((StepT) step);
            }
        }

    }

    @Override
    public void setFocus() {
        // TODO Auto-generated method stub

    }

}
