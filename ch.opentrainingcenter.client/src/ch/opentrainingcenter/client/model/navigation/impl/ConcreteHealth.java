package ch.opentrainingcenter.client.model.navigation.impl;

import java.util.Date;

import ch.opentrainingcenter.client.helper.TimeHelper;
import ch.opentrainingcenter.client.model.navigation.INavigationItem;
import ch.opentrainingcenter.client.views.IImageKeys;
import ch.opentrainingcenter.transfer.IHealth;

public class ConcreteHealth extends HealthDecorator implements INavigationItem {

    public ConcreteHealth(final IHealth health) {
        super(health);
    }

    @Override
    public String getName() {
        return TimeHelper.convertDateToString(super.getDateofmeasure(), false);
    }

    @Override
    public Date getDate() {
        return super.getDateofmeasure();
    }

    @Override
    public String getImage() {
        return IImageKeys.CARDIO3232;
    }

    @Override
    public int compareTo(final INavigationItem other) {
        return other.getDate().compareTo(getDate());
    }
}
