package ch.opentrainingcenter.client.views;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import ch.opentrainingcenter.core.db.DBSTATE;
import ch.opentrainingcenter.transfer.IAthlete;

/**
 * App Context
 * 
 */
public final class ApplicationContext {

    private static final ApplicationContext INSTANCE = new ApplicationContext();

    private int selectedJahr;

    private Long selectedId;

    private IAthlete athlete;

    private final List<Object> selectedItems = new ArrayList<Object>();

    private DBSTATE dbState;

    private ApplicationContext() {
    }

    public static ApplicationContext getApplicationContext() {
        return INSTANCE;
    }

    public Long getSelectedId() {
        return selectedId;
    }

    public void setSelectedId(final Long selectedId) {
        this.selectedId = selectedId;
    }

    public IAthlete getAthlete() {
        return athlete;
    }

    public void setAthlete(final IAthlete athlete) {
        this.athlete = athlete;
    }

    public void setSelection(final Object[] items) {
        selectedItems.clear();
        if (items != null) {
            selectedItems.addAll(Arrays.asList(items));
        }
    }

    public List<?> getSelection() {
        return Collections.unmodifiableList(selectedItems);
    }

    public int getSelectedJahr() {
        return selectedJahr;
    }

    public void setSelectedJahr(final int selectedJahr) {
        this.selectedJahr = selectedJahr;
    }

    public DBSTATE getDbState() {
        return dbState;
    }

    public void setDbState(final DBSTATE dbState) {
        this.dbState = dbState;
    }
}
