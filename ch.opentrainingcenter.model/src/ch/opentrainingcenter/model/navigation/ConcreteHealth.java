package ch.opentrainingcenter.model.navigation;

import java.util.Date;

import ch.opentrainingcenter.core.helper.TimeHelper;
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
        return null;
    }

    @Override
    public double getLaengeInMeter() {
        return 0;
    }
}
