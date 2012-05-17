package ch.opentrainingcenter.client.views;

import java.util.Date;

import ch.opentrainingcenter.transfer.IAthlete;

/**
 * App Context
 * 
 */
public class ApplicationContext {

    private static final ApplicationContext INSTANCE = new ApplicationContext();

    private Date selectedId;

    private IAthlete athlete;

    private ApplicationContext() {
    }

    public static ApplicationContext getApplicationContext() {
        return INSTANCE;
    }

    public Date getSelectedId() {
        return selectedId;
    }

    public void setSelectedId(final Date selectedId) {
        this.selectedId = selectedId;
    }

    public IAthlete getAthlete() {
        return athlete;
    }

    public void setAthlete(final IAthlete athlete) {
        this.athlete = athlete;
    }
}
