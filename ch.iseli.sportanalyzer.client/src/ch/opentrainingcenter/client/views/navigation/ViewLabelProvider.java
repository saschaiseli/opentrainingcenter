package ch.opentrainingcenter.client.views.navigation;

import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

import ch.opentrainingcenter.client.Activator;
import ch.opentrainingcenter.client.helper.TimeHelper;
import ch.opentrainingcenter.client.views.IImageKeys;
import ch.opentrainingcenter.transfer.IImported;

public class ViewLabelProvider extends LabelProvider {

    public ViewLabelProvider() {
    }

    @Override
    public String getText(final Object object) {
        // wird immer eine IImported sein
        return TimeHelper.convertDateToString(((IImported) object).getActivityId(), false);
    }

    @Override
    public Image getImage(final Object obj) {
        return Activator.getImageDescriptor(IImageKeys.RUNNING_MAN).createImage();
    }
}
