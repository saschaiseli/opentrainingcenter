package ch.opentrainingcenter.client.ui.datepicker;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DateTime;

public class DateWidget {

    private final DateTime dateTime;

    public DateWidget(final DateTime dateTime) {
        this.dateTime = dateTime;
    }

    public DateWidget(final Composite parent, final int style) {
        dateTime = new DateTime(parent, style);
    }

    public void setDate(final org.joda.time.DateTime date) {
        dateTime.setDate(date.getYear(), date.getMonthOfYear() - 1, date.getDayOfMonth());
    }

    public org.joda.time.DateTime getDate() {
        final int year = dateTime.getYear();
        final int month = dateTime.getMonth() + 1;
        final int day = dateTime.getDay();
        return new org.joda.time.DateTime(year, month, day, 0, 0);
    }

    public void addSelectionListener(final SelectionAdapter listener) {
        dateTime.addSelectionListener(listener);
    }

    public void setEnabled(final boolean enabled) {
        dateTime.setEnabled(enabled);
    }

    public void setFocus() {
        dateTime.setFocus();
    }

    public void dispose() {
        dateTime.dispose();
    }
}
