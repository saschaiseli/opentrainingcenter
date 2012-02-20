package ch.opentrainingcenter.client.charts;

import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;

import ch.opentrainingcenter.client.model.RunType;

public class RadioSelectionListener implements SelectionListener {

    private RunType filter;
    private final RunType button;

    public RadioSelectionListener(final RunType filter, final RunType button) {
        this.filter = filter;
        this.button = button;
    }

    @Override
    public void widgetSelected(final SelectionEvent e) {
        this.filter = button;
    }

    @Override
    public void widgetDefaultSelected(final SelectionEvent e) {

    }

}
