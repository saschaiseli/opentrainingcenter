package ch.iseli.sportanalyzer.client.views.navigation;

import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

import ch.iseli.sportanalyzer.client.Activator;
import ch.iseli.sportanalyzer.client.helper.TimeHelper;
import ch.iseli.sportanalyzer.client.views.IImageKeys;
import ch.iseli.sportanalyzer.tcx.ActivityT;

public class ViewLabelProvider extends LabelProvider {

    public ViewLabelProvider() {
    }

    @Override
    public String getText(final Object object) {
        // wird immer eine ActivityT sein
        return TimeHelper.convertGregorianDateToString(((ActivityT) object).getId());
    }

    @Override
    public Image getImage(final Object obj) {
        return Activator.getImageDescriptor(IImageKeys.RUNNING_MAN).createImage();
    }
}
