package ch.opentrainingcenter.client.model.navigation.impl;

import java.util.Date;

import ch.opentrainingcenter.client.helper.TimeHelper;
import ch.opentrainingcenter.client.model.navigation.INavigationItem;
import ch.opentrainingcenter.transfer.IImported;

public class ConcreteImported extends ImportedDecorator implements INavigationItem, IImported {

    public ConcreteImported(final IImported imported) {
        super(imported);
    }

    @Override
    public String getName() {
        return TimeHelper.convertDateToString(super.getActivityId(), false);
    }

    @Override
    public Date getDate() {
        return super.getActivityId();
    }

    @Override
    public String getImage() {
        return super.getTrainingType().getImageicon();
    }

    @Override
    public int compareTo(final INavigationItem o) {
        return o.getDate().compareTo(getDate());
    }
}
