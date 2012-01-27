package ch.iseli.sportanalyzer.client.views.navigation;

import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

import ch.iseli.sportanalyzer.client.Activator;
import ch.iseli.sportanalyzer.client.helper.TimeHelper;
import ch.iseli.sportanalyzer.client.views.IImageKeys;
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
