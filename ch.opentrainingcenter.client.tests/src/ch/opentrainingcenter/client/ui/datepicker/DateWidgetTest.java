package ch.opentrainingcenter.client.ui.datepicker;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.widgets.DateTime;
import org.junit.Before;
import org.junit.Test;

public class DateWidgetTest {

    private DateTime dateTime;
    private DateWidget widget;

    @Before
    public void setUp() {
        dateTime = mock(DateTime.class);
        widget = new DateWidget(dateTime);
    }

    @Test
    public void testSetDate() {
        // 1. Januar 2010
        final org.joda.time.DateTime date = new org.joda.time.DateTime(2010, 1, 1, 0, 0);
        widget.setDate(date);

        verify(dateTime).setDate(2010, 0, 1);
    }

    @Test
    public void testGetDate() {
        when(dateTime.getYear()).thenReturn(2012);
        // 12. Februar 2012
        when(dateTime.getMonth()).thenReturn(1);
        when(dateTime.getDay()).thenReturn(12);

        final org.joda.time.DateTime date = widget.getDate();

        assertEquals(new org.joda.time.DateTime(2012, 2, 12, 0, 0), date);
    }

    @Test
    public void testAddSelectionListener() {
        final SelectionAdapter listener = mock(SelectionAdapter.class);

        widget.addSelectionListener(listener);

        verify(dateTime).addSelectionListener(listener);
    }

    @Test
    public void testDispose() {
        widget.dispose();

        verify(dateTime).dispose();
    }
}
