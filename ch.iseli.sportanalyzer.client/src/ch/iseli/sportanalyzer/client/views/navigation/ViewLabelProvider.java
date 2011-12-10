package ch.iseli.sportanalyzer.client.views.navigation;

import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

import ch.iseli.sportanalyzer.client.Activator;
import ch.iseli.sportanalyzer.client.views.IImageKeys;

public class ViewLabelProvider extends LabelProvider {

    public ViewLabelProvider() {
    }

    @Override
    public String getText(final Object obj) {
        return obj.toString();
    }

    @Override
    public Image getImage(final Object obj) {
        return Activator.getImageDescriptor(IImageKeys.RUNNING_MAN).createImage();
    }
}
