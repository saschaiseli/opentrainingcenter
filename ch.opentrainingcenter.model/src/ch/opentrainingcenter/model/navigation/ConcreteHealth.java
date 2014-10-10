package ch.opentrainingcenter.model.navigation;

import java.util.Date;

import ch.opentrainingcenter.core.helper.TimeHelper;
import ch.opentrainingcenter.i18n.Messages;
import ch.opentrainingcenter.transfer.IHealth;

public class ConcreteHealth extends HealthDecorator implements INavigationItem {

    private final String image;

    public ConcreteHealth(final IHealth health, final String image) {
        super(health);
        this.image = image;
    }

    @Override
    public String getName() {
        return TimeHelper.convertDateToString(super.getDateofmeasure());
    }

    @Override
    public Date getDate() {
        return super.getDateofmeasure();
    }

    @Override
    public String getImage() {
        return image;
    }

    @Override
    public int compareTo(final INavigationItem other) {
        return other.getDate().compareTo(getDate());
    }

    @Override
    public String getTooltip() {
        final StringBuilder str = new StringBuilder();
        if (health.getCardio() != null) {
            str.append(Messages.HealthDialog2).append(": ").append(health.getCardio()).append(' ').append(Messages.Units0).append(' '); //$NON-NLS-1$
        }
        if (health.getWeight() != null) {
            str.append(Messages.HealthDialog3).append(": ").append(health.getWeight()).append(' ').append(Messages.Units6).append(' '); //$NON-NLS-1$
        }
        return str.toString();
    }

    @Override
    public double getLaengeInMeter() {
        return 0;
    }
}
