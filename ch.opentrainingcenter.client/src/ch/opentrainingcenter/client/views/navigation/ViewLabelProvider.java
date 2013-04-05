package ch.opentrainingcenter.client.views.navigation;

import java.util.Date;

import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

import ch.opentrainingcenter.client.Activator;
import ch.opentrainingcenter.client.views.IImageKeys;
import ch.opentrainingcenter.core.helper.TimeHelper;
import ch.opentrainingcenter.i18n.Messages;
import ch.opentrainingcenter.transfer.IHealth;
import ch.opentrainingcenter.transfer.ITraining;

public class ViewLabelProvider extends LabelProvider {

    public ViewLabelProvider() {
    }

    @Override
    public String getText(final Object element) {
        if (element instanceof ITraining) {
            final ITraining record = (ITraining) element;
            return TimeHelper.convertDateToString(new Date(record.getDatum()), false);
        } else {
            final IHealth health = (IHealth) element;
            return TimeHelper.convertDateToString(health.getDateofmeasure(), false) + Messages.ViewLabelProvider_0 + health.getCardio();
        }
    }

    @Override
    public Image getImage(final Object element) {
        if (element instanceof ITraining) {
            final ITraining record = (ITraining) element;
            return Activator.getImageDescriptor(record.getTrainingType().getImageicon()).createImage();
        } else {
            return Activator.getImageDescriptor(IImageKeys.CARDIO3232).createImage();
        }

    }
}
