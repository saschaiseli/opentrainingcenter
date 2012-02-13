package ch.opentrainingcenter.client.views.navigation;

import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

import ch.opentrainingcenter.client.Activator;
import ch.opentrainingcenter.client.helper.TimeHelper;
import ch.opentrainingcenter.transfer.IImported;

public class ViewLabelProvider extends LabelProvider {

    public ViewLabelProvider() {
    }

    @Override
    public String getText(final Object element) {
        final IImported record = (IImported) element;
        return TimeHelper.convertDateToString(record.getActivityId(), false);
    }

    @Override
    public Image getImage(final Object element) {
        final IImported record = (IImported) element;
        return Activator.getImageDescriptor(record.getTrainingType().getImageicon()).createImage();
    }
}
