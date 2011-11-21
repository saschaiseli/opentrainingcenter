package ch.iseli.sportanalyzer.client.views.navigation;

import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

import ch.iseli.sportanalyzer.tcx.TrainingCenterDatabaseT;

public class ViewLabelProvider extends LabelProvider {

    public ViewLabelProvider(Composite parent) {

    }

    @Override
    public String getText(Object obj) {
        return obj.toString();
    }

    @Override
    public Image getImage(Object obj) {
        String imageKey = ISharedImages.IMG_OBJ_ELEMENT;
        if (obj instanceof TrainingCenterDatabaseT) {
            imageKey = ISharedImages.IMG_OBJ_FOLDER;
        }
        // else if (obj instanceof TrainingCenterDatabaseTChild) {
        // TrainingCenterDatabaseTChild child = (TrainingCenterDatabaseTChild) obj;
        // switch (child.getTyp()) {
        // case SPEED:
        // return ChildTyp.SPEED.getImage();
        // case CARDIO:
        // return ChildTyp.CARDIO.getImage();
        // default:
        // // no icon defined
        // }
        // }
        return PlatformUI.getWorkbench().getSharedImages().getImage(imageKey);
    }
}
