package ch.opentrainingcenter.client.views.navigation;

import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

import ch.opentrainingcenter.client.Activator;
import ch.opentrainingcenter.client.Messages;
import ch.opentrainingcenter.client.helper.TimeHelper;
import ch.opentrainingcenter.client.views.IImageKeys;
import ch.opentrainingcenter.transfer.IHealth;
import ch.opentrainingcenter.transfer.IImported;

public class ViewLabelProvider extends LabelProvider {

    public ViewLabelProvider() {
    }

    @Override
    public String getText(final Object element) {
        if (element instanceof IImported) {
            final IImported record = (IImported) element;
            return TimeHelper.convertDateToString(record.getActivityId(), false);
        } else {
            final IHealth health = (IHealth) element;
            return TimeHelper.convertDateToString(health.getDateofmeasure(), false) + Messages.ViewLabelProvider_0 + health.getCardio();
        }
    }

    @Override
    public Image getImage(final Object element) {
        if (element instanceof IImported) {
            final IImported record = (IImported) element;
            return Activator.getImageDescriptor(record.getTrainingType().getImageicon()).createImage();
        } else {
            return Activator.getImageDescriptor(IImageKeys.CARDIO3232).createImage();
        }

    }
}
