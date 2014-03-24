package ch.opentrainingcenter.client.ui.datepicker;

//
//
// Source from http://sourceforge.net/projects/swt-datepicker/
//

import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.util.Date;

import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTError;
import org.eclipse.swt.accessibility.ACC;
import org.eclipse.swt.accessibility.AccessibleAdapter;
import org.eclipse.swt.accessibility.AccessibleControlAdapter;
import org.eclipse.swt.accessibility.AccessibleControlEvent;
import org.eclipse.swt.accessibility.AccessibleEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.TypedListener;

/**
 * The combo widget with drop-down date-picker panel.
 * 
 * changes by andyglow + added method setEnabled to properly manage state of
 * control changes by sebthom ~ declared package accessible methods as private ~
 * declared getEditable() as public + added useSingleMouseClickToCommit
 * behaviour + manually modifying the date in the text field is reflected in the
 * date picker + setDate(null) clears the date. + added getText & setText()
 * 
 * @author <a href="mailto:andy@tiff.ru">Andrey Onistchuk</a>
 * 
 */
@SuppressWarnings("nls")
public final class DatePickerCombo extends Composite {
    // ~ Instance fields
    // --------------------------------------------------------

    private Button arrow;
    private DatePicker dp;
    private boolean hasFocus;

    /**
     * @author sebthom
     */
    private boolean isClosePopupWithSingleMouseClick = false;
    private Shell popup;
    private Text text;

    /**
     * @author andyglow
     */
    private DateFormat format = null;

    // ~ Constructors
    // -----------------------------------------------------------

    /**
     * Creates a new DatePickerCombo object.
     * 
     * @param parent
     *            parent Composite
     * @param style
     *            SWT style
     */
    public DatePickerCombo(final Composite parent, int style) {
        super(parent, checkStyle(style));

        style = getStyle();

        int textStyle = SWT.SINGLE;

        if ((style & SWT.READ_ONLY) != 0) {
            textStyle |= SWT.READ_ONLY;
        }

        if ((style & SWT.FLAT) != 0) {
            textStyle |= SWT.FLAT;
        }

        text = new Text(this, textStyle);

        popup = new Shell(getShell(), SWT.NO_TRIM);

        int pickerStyle = SWT.SINGLE;

        if ((style & SWT.FLAT) != 0) {
            pickerStyle |= SWT.FLAT;
        }

        dp = new DatePicker(popup, pickerStyle);

        int arrowStyle = SWT.ARROW | SWT.DOWN;

        if ((style & SWT.FLAT) != 0) {
            arrowStyle |= SWT.FLAT;
        }

        arrow = new Button(this, arrowStyle);

        final Listener listener = new Listener() {
            @Override
            public void handleEvent(final Event event) {
                if (popup == event.widget) {
                    popupEvent(event);

                    return;
                }

                if (text == event.widget) {
                    textEvent(event);

                    return;
                }

                if (dp == event.widget) {
                    dpEvent(event);

                    return;
                }

                if (arrow == event.widget) {
                    arrowEvent(event);

                    return;
                }

                if (DatePickerCombo.this == event.widget) {
                    comboEvent(event);

                    return;
                }
            }
        };

        final int[] comboEvents = { SWT.Dispose, SWT.Move, SWT.Resize };

        for (int i = 0; i < comboEvents.length; i++) {
            this.addListener(comboEvents[i], listener);
        }

        final int[] popupEvents = { SWT.Close, SWT.Paint, SWT.Deactivate };

        for (int i = 0; i < popupEvents.length; i++) {
            popup.addListener(popupEvents[i], listener);
        }

        final int[] textEvents = { SWT.KeyDown, SWT.KeyUp, SWT.Modify, SWT.MouseDown, SWT.MouseUp, SWT.Traverse, SWT.FocusIn, SWT.FocusOut };

        for (int i = 0; i < textEvents.length; i++) {
            text.addListener(textEvents[i], listener);
        }

        final int[] dpEvents = { SWT.MouseUp, SWT.MouseDoubleClick, SWT.Selection, SWT.Traverse, SWT.KeyDown, SWT.KeyUp, SWT.FocusIn, SWT.FocusOut };

        for (int i = 0; i < dpEvents.length; i++) {
            dp.addListener(dpEvents[i], listener);
        }

        final int[] arrowEvents = { SWT.Selection, SWT.FocusIn, SWT.FocusOut };

        for (int i = 0; i < arrowEvents.length; i++) {
            arrow.addListener(arrowEvents[i], listener);
        }

        initAccessible();
    }

    // ~ Methods
    // ----------------------------------------------------------------

    /**
     * Style cheking
     * 
     * @param style
     *            STT style
     * 
     * @return style
     */
    public static int checkStyle(final int style) {
        final int mask = SWT.BORDER | SWT.READ_ONLY | SWT.FLAT;

        return style & mask;
    }

    /**
     * Adds the listener to receive events.
     * 
     * @param listener
     *            the listener
     * 
     * @exception SWTError
     *                (ERROR_THREAD_INVALID_ACCESS) when called from the wrong
     *                thread
     * @exception SWTError
     *                (ERROR_WIDGET_DISPOSED) when the widget has been disposed
     * @exception SWTError
     *                (ERROR_NULL_ARGUMENT) when listener is null
     */
    public void addModifyListener(final ModifyListener listener) {
        checkWidget();

        if (listener == null) {
            SWT.error(SWT.ERROR_NULL_ARGUMENT);
        }

        final TypedListener typedListener = new TypedListener(listener);
        addListener(SWT.Modify, typedListener);
    }

    /**
     * Adds the listener to receive events.
     * 
     * @param listener
     *            the listener
     * 
     * @exception SWTError
     *                (ERROR_THREAD_INVALID_ACCESS) when called from the wrong
     *                thread
     * @exception SWTError
     *                (ERROR_WIDGET_DISPOSED) when the widget has been disposed
     * @exception SWTError
     *                (ERROR_NULL_ARGUMENT) when listener is null
     */
    public void addSelectionListener(final SelectionListener listener) {
        checkWidget();

        if (listener == null) {
            SWT.error(SWT.ERROR_NULL_ARGUMENT);
        }

        final TypedListener typedListener = new TypedListener(listener);
        addListener(SWT.Selection, typedListener);
        addListener(SWT.DefaultSelection, typedListener);
    }

    private void arrowEvent(final Event event) {
        switch (event.type) {
        case SWT.FocusIn: {
            if (hasFocus) {
                return;
            }

            hasFocus = true;

            if (getEditable()) {
                text.selectAll();
            }

            final Event e = new Event();
            e.time = event.time;
            notifyListeners(SWT.FocusIn, e);

            break;
        }

        case SWT.FocusOut: {
            final Control focusControl = getDisplay().getFocusControl();

            if ((focusControl == dp) || (focusControl == text)) {
                return;
            }

            hasFocus = false;

            final Event e = new Event();
            e.time = event.time;
            notifyListeners(SWT.FocusOut, e);

            break;
        }

        case SWT.Selection: {
            dropDown(!isDropped());

            break;
        }
        }
    }

    /**
     * Clears the current selection.
     * 
     * @exception SWTError
     *                (ERROR_THREAD_INVALID_ACCESS) when called from the wrong
     *                thread
     * @exception SWTError
     *                (ERROR_WIDGET_DISPOSED) when the widget has been disposed
     */
    public void clearSelection() {
        checkWidget();
        text.clearSelection();
        dp.reset();
    }

    private void comboEvent(final Event event) {
        switch (event.type) {
        case SWT.Dispose:

            if ((popup != null) && !popup.isDisposed()) {
                popup.dispose();
            }

            popup = null;
            text = null;
            dp = null;
            arrow = null;

            break;

        case SWT.Move:
            dropDown(false);

            break;

        case SWT.Resize:
            internalLayout();

            break;
        }
    }

    @Override
    public Point computeSize(final int wHint, final int hHint, final boolean changed) {
        checkWidget();

        int width = 0;
        int height = 0;
        final Point textSize = text.computeSize(wHint, SWT.DEFAULT, changed);
        final Point arrowSize = arrow.computeSize(SWT.DEFAULT, SWT.DEFAULT, changed);
        final Point listSize = dp.computeSize(wHint, SWT.DEFAULT, changed);
        final int borderWidth = getBorderWidth();

        height = Math.max(hHint, Math.max(textSize.y, arrowSize.y) + (2 * borderWidth));
        width = Math.max(wHint, Math.max(textSize.x + arrowSize.x + (2 * borderWidth), listSize.x + 2));

        return new Point(width, height);
    }

    private void dpEvent(final Event event) {
        switch (event.type) {
        case SWT.FocusIn: {
            if (hasFocus) {
                return;
            }

            hasFocus = true;

            if (getEditable()) {
                text.selectAll();
            }

            final Event e = new Event();
            e.time = event.time;
            notifyListeners(SWT.FocusIn, e);

            break;
        }

        case SWT.FocusOut: {
            final Control focusControl = getDisplay().getFocusControl();

            if ((focusControl == text) || (focusControl == arrow)) {
                return;
            }

            hasFocus = false;

            final Event e = new Event();
            e.time = event.time;
            notifyListeners(SWT.FocusOut, e);

            break;
        }

        case SWT.MouseDown: {
            if (event.button != 1) {
                return;
            }

            dropDown(false);

            final Event e = new Event();
            e.time = event.time;
            notifyListeners(SWT.DefaultSelection, e);

            break;
        }

        case SWT.Selection: {
            // sebthom
            if (!isClosePopupWithSingleMouseClick) {
                final Date date = dp.getDate();
                text.setText(getFormat().format(date));
                text.selectAll();

                final Event e = new Event();
                e.time = event.time;
                e.stateMask = event.stateMask;
                e.doit = event.doit;
                notifyListeners(SWT.Selection, e);
                event.doit = e.doit;

                break;
            }

            // otherwise perform the code of SWT.MouseDoubleClick
        }

        case SWT.MouseDoubleClick: {
            dropDown(false);

            final Date date = dp.getDate();

            // sebthom
            if (date == null) {
                text.setText("");
            } else {
                text.setText(getFormat().format(date));
                text.selectAll();
            }

            final Event e = new Event();
            e.time = event.time;
            e.stateMask = event.stateMask;
            e.doit = event.doit;
            notifyListeners(SWT.Selection, e);
            event.doit = e.doit;

            break;
        }

        case SWT.Traverse: {
            switch (event.detail) {
            case SWT.TRAVERSE_TAB_NEXT:
            case SWT.TRAVERSE_RETURN:
            case SWT.TRAVERSE_ESCAPE:
            case SWT.TRAVERSE_ARROW_PREVIOUS:
            case SWT.TRAVERSE_ARROW_NEXT:
                event.doit = false;

                break;
            }

            final Event e = new Event();
            e.time = event.time;
            e.detail = event.detail;
            e.doit = event.doit;
            e.keyCode = event.keyCode;
            notifyListeners(SWT.Traverse, e);
            event.doit = e.doit;

            break;
        }

        case SWT.KeyUp: {
            final Event e = new Event();
            e.time = event.time;
            e.character = event.character;
            e.keyCode = event.keyCode;
            e.stateMask = event.stateMask;
            notifyListeners(SWT.KeyUp, e);

            break;
        }

        case SWT.KeyDown: {
            if (event.character == SWT.ESC) {
                // escape key cancels popup dp
                dropDown(false);
            }

            if ((event.character == SWT.CR) || (event.character == '\t')) {
                // Enter and Tab cause default selection
                dropDown(false);

                final Event e = new Event();
                e.time = event.time;
                e.stateMask = event.stateMask;
                notifyListeners(SWT.DefaultSelection, e);
            }

            // At this point the widget may have been disposed.
            // If so, do not continue.
            if (isDisposed()) {
                break;
            }

            final Event e = new Event();
            e.time = event.time;
            e.character = event.character;
            e.keyCode = event.keyCode;
            e.stateMask = event.stateMask;
            notifyListeners(SWT.KeyDown, e);

            break;
        }
        }
    }

    private void dropDown(final boolean drop) {
        if (drop == isDropped()) {
            return;
        }

        if (!drop) {
            popup.setVisible(false);
            text.setFocus();

            return;
        }

        final Rectangle listRect = dp.getBounds();
        final Point point = getParent().toDisplay(getLocation());
        final Point comboSize = getSize();
        final int width = Math.max(comboSize.x, listRect.width + 2);
        popup.setBounds(point.x, point.y + comboSize.y, width, listRect.height + 2);
        popup.setVisible(true);
        dp.setFocus();
    }

    @Override
    public Control[] getChildren() {
        checkWidget();
        return new Control[0];
    }

    public Date getDate() {
        checkWidget();
        return dp.getDate();
    }

    public boolean getEditable() {
        return text.getEditable();
    }

    /**
     * @author sebthom
     */
    public String getText() {
        return text.getText();
    }

    /**
     * Set uu the date in text mode
     * 
     * @param txt
     *            text representation of Date
     * @deprecated use setDate(date) instead
     */
    @Deprecated
    public void setText(final String txt) {
        text.setText(txt);
    }

    /**
     * Returns the height of Text widget
     * 
     * @return height
     */
    public int getTextHeight() {
        checkWidget();
        return text.getLineHeight();
    }

    private void initAccessible() {
        getAccessible().addAccessibleListener(new AccessibleAdapter() {
            @Override
            public void getHelp(final AccessibleEvent e) {
                e.result = getToolTipText();
            }
        });

        getAccessible().addAccessibleControlListener(new AccessibleControlAdapter() {
            @Override
            public void getChildAtPoint(final AccessibleControlEvent e) {
                final Point testPoint = toControl(new Point(e.x, e.y));

                if (getBounds().contains(testPoint)) {
                    e.childID = ACC.CHILDID_SELF;
                }
            }

            @Override
            public void getChildCount(final AccessibleControlEvent e) {
                e.detail = 0;
            }

            @Override
            public void getLocation(final AccessibleControlEvent e) {
                final Rectangle location = getBounds();
                final Point pt = toDisplay(new Point(location.x, location.y));
                e.x = pt.x;
                e.y = pt.y;
                e.width = location.width;
                e.height = location.height;
            }

            @Override
            public void getRole(final AccessibleControlEvent e) {
                e.detail = ACC.ROLE_COMBOBOX;
            }

            @Override
            public void getState(final AccessibleControlEvent e) {
                e.detail = ACC.STATE_NORMAL;
            }

            @Override
            public void getValue(final AccessibleControlEvent e) {
                e.result = text.getText();
            }
        });
    }

    private void internalLayout() {
        if (isDropped()) {
            dropDown(false);
        }

        final Rectangle rect = getClientArea();
        final int width = rect.width;
        final int height = rect.height;
        final Point arrowSize = arrow.computeSize(SWT.DEFAULT, height);
        text.setBounds(0, 0, width - arrowSize.x, height);
        arrow.setBounds(width - arrowSize.x, 0, arrowSize.x, arrowSize.y);

        final Point size = getSize();
        final int itemHeight = dp.getBounds().height;
        final Point listSize = dp.computeSize(SWT.DEFAULT, itemHeight);
        dp.setBounds(1, 1, Math.max(size.x - 2, listSize.x), listSize.y);
    }

    /**
     * determines if you need to double click a date in the expanded calender
     * control to hide it default is false meaning you have to double click a
     * date
     * 
     * @author sebthom
     * 
     * @param useSingleMouseClickToCommit
     */
    public boolean isClosePopupWithSingleMouseClick() {
        return isClosePopupWithSingleMouseClick;
    }

    private boolean isDropped() {
        return popup.getVisible();
    }

    /**
     * Retirns true if control is in focus
     * 
     * @return focus state
     */
    @Override
    public boolean isFocusControl() {
        checkWidget();

        if (text.isFocusControl() || arrow.isFocusControl() || dp.isFocusControl() || popup.isFocusControl()) {
            return true;
        } else {
            return super.isFocusControl();
        }
    }

    private void popupEvent(final Event event) {
        switch (event.type) {
        case SWT.Paint:

            // draw black rectangle around dp
            final Rectangle listRect = dp.getBounds();
            final Color black = getDisplay().getSystemColor(SWT.COLOR_BLACK);
            event.gc.setForeground(black);
            event.gc.drawRectangle(0, 0, listRect.width + 1, listRect.height + 1);

            break;

        case SWT.Close:
            event.doit = false;
            dropDown(false);

            break;

        case SWT.Deactivate:
            dropDown(false);

            break;
        }
    }

    /**
     * Drawing stuff
     * 
     * @param x
     *            x
     * @param y
     *            y
     * @param width
     *            width
     * @param height
     *            height
     * @param all
     *            must we redraw all of controls?
     */
    @Override
    public void redraw(final int x, final int y, final int width, final int height, final boolean all) {
        checkWidget();

        if (!all) {
            return;
        }

        Point location = text.getLocation();
        text.redraw(x - location.x, y - location.y, width, height, all);
        location = dp.getLocation();
        dp.redraw(x - location.x, y - location.y, width, height, all);

        if (arrow != null) {
            location = arrow.getLocation();
            arrow.redraw(x - location.x, y - location.y, width, height, all);
        }
    }

    /**
     * Removes the listener.
     * 
     * @param listener
     *            the listener
     * 
     * @exception SWTError
     *                (ERROR_THREAD_INVALID_ACCESS) when called from the wrong
     *                thread
     * @exception SWTError
     *                (ERROR_WIDGET_DISPOSED) when the widget has been disposed
     * @exception SWTError
     *                (ERROR_NULL_ARGUMENT) when listener is null
     */
    public void removeModifyListener(final ModifyListener listener) {
        checkWidget();

        if (listener == null) {
            SWT.error(SWT.ERROR_NULL_ARGUMENT);
        }

        removeListener(SWT.Modify, listener);
    }

    /**
     * Removes the listener.
     * 
     * @param listener
     *            the listener
     * 
     * @exception SWTError
     *                (ERROR_THREAD_INVALID_ACCESS) when called from the wrong
     *                thread
     * @exception SWTError
     *                (ERROR_WIDGET_DISPOSED) when the widget has been disposed
     * @exception SWTError
     *                (ERROR_NULL_ARGUMENT) when listener is null
     */
    public void removeSelectionListener(final SelectionListener listener) {
        checkWidget();

        if (listener == null) {
            SWT.error(SWT.ERROR_NULL_ARGUMENT);
        }

        removeListener(SWT.Selection, listener);
        removeListener(SWT.DefaultSelection, listener);
    }

    /**
     * Set up background of control
     * 
     * @param color
     *            background color
     */
    @Override
    public void setBackground(final Color color) {
        super.setBackground(color);

        if (text != null) {
            text.setBackground(color);
        }

        if (dp != null) {
            dp.setBackground(color);
        }

        if (arrow != null) {
            arrow.setBackground(color);
        }
    }

    /**
     * set if you need to double click a date in the expanded calender control
     * to hide it default is false meaning you have to double click a date
     * 
     * @author sebthom
     * @param useSingleMouseClickToCommit
     */
    public void setClosePopupWithSingleMouseClick(final boolean isClosePopupWithSingleMouseClick) {
        this.isClosePopupWithSingleMouseClick = isClosePopupWithSingleMouseClick;
    }

    /**
     * Set DateFormat object, which is parser/formatter of control's content
     * 
     * @author andyglow
     * @param format
     */
    public void setFormat(final DateFormat format) {
        this.format = format;
    }

    public void setDateSymbols(final DateFormatSymbols symbols) {
        dp.setDateSymbols(symbols);
    }

    /**
     * Return current DateFormat used to format/parse control content
     * 
     * @author andyglow
     * @return current DateFormat
     */
    protected DateFormat getFormat() {
        if (format == null) {
            format = DateFormat.getDateInstance(DateFormat.SHORT);
        }
        return format;
    }

    /**
     * Set up the date of control
     * 
     * @param date
     *            Date
     */
    public void setDate(final Date date) {
        checkWidget();

        // sebthom
        if (date != null) {
            text.setText(getFormat().format(date));
            text.selectAll();
        } else {
            text.setText("");
        }

        dp.setDate(date);
    }

    /**
     * Set the focus to control
     * 
     * @return result of action
     */
    @Override
    public boolean setFocus() {
        checkWidget();

        return text.setFocus();
    }

    /**
     * DOCUMENT ME!
     * 
     * @param font
     *            DOCUMENT ME!
     */
    @Override
    public void setFont(final Font font) {
        super.setFont(font);
        text.setFont(font);
        dp.setFont(font);
        internalLayout();
    }

    /**
     * Set up the control foreground color
     * 
     * @param color
     *            Foreground color
     */
    @Override
    public void setForeground(final Color color) {
        super.setForeground(color);

        if (text != null) {
            text.setForeground(color);
        }

        if (dp != null) {
            dp.setForeground(color);
        }

        if (arrow != null) {
            arrow.setForeground(color);
        }
    }

    /**
     * Sets the new selection.
     * 
     * @param selection
     *            point representing the start and the end of the new selection
     * 
     * @exception SWTError
     *                (ERROR_THREAD_INVALID_ACCESS) when called from the wrong
     *                thread
     * @exception SWTError
     *                (ERROR_WIDGET_DISPOSED) when the widget has been disposed
     * @exception SWTError
     *                (ERROR_NULL_ARGUMENT) when selection is null
     */
    public void setSelection(final Point selection) {
        checkWidget();

        if (selection == null) {
            SWT.error(SWT.ERROR_NULL_ARGUMENT);
        }

        text.setSelection(selection.x, selection.y);
    }

    /**
     * Sets the text limit
     * 
     * @param limit
     *            new text limit
     * 
     * @exception SWTError
     *                (ERROR_THREAD_INVALID_ACCESS) when called from the wrong
     *                thread
     * @exception SWTError
     *                (ERROR_WIDGET_DISPOSED) when the widget has been disposed
     * @exception SWTError
     *                (ERROR_CANNOT_BE_ZERO) when limit is 0
     */
    public void setTextLimit(final int limit) {
        checkWidget();
        text.setTextLimit(limit);
    }

    /**
     * Set up control tool-tip
     * 
     * @param string
     *            Text of tool-tip
     */
    @Override
    public void setToolTipText(final String string) {
        checkWidget();
        super.setToolTipText(string);
        arrow.setToolTipText(string);
        text.setToolTipText(string);
    }

    /**
     * Set up visibility of control
     * 
     * @param visible
     *            visibility state
     */
    @Override
    public void setVisible(final boolean visible) {
        super.setVisible(visible);

        if (!visible) {
            popup.setVisible(false);
        }
    }

    private void textEvent(final Event event) {
        switch (event.type) {
        case SWT.FocusIn: {
            if (hasFocus) {
                return;
            }

            hasFocus = true;

            if (getEditable()) {
                text.selectAll();
            }

            final Event e = new Event();
            e.time = event.time;
            notifyListeners(SWT.FocusIn, e);

            break;
        }

        case SWT.FocusOut: {
            final Control focusControl = getDisplay().getFocusControl();

            if ((focusControl == dp) || (focusControl == arrow)) {
                return;
            }

            hasFocus = false;

            final Event e = new Event();
            e.time = event.time;
            notifyListeners(SWT.FocusOut, e);

            break;
        }

        case SWT.KeyDown: {
            if (event.character == SWT.ESC) {
                // escape key cancels popup dp
                dropDown(false);
            }

            if (event.character == SWT.CR) {
                dropDown(false);

                final Event e = new Event();
                e.time = event.time;
                e.stateMask = event.stateMask;
                notifyListeners(SWT.DefaultSelection, e);
            }

            // At this point the widget may have been disposed.
            // If so, do not continue.
            if (isDisposed()) {
                break;
            }

            if ((event.keyCode == SWT.ARROW_UP) || (event.keyCode == SWT.ARROW_DOWN)) {
                // Date oldDate = getDate();
                // At this point the widget may have been disposed.
                // If so, do not continue.
                if (isDisposed()) {
                    break;
                }
            }

            // Further work : Need to add support for incremental
            // search in pop up dp as characters typed in text widget
            final Event e = new Event();
            e.time = event.time;
            e.character = event.character;
            e.keyCode = event.keyCode;
            e.stateMask = event.stateMask;
            notifyListeners(SWT.KeyDown, e);

            break;
        }

        case SWT.KeyUp: {
            final Event e = new Event();
            e.time = event.time;
            e.character = event.character;
            e.keyCode = event.keyCode;
            e.stateMask = event.stateMask;
            notifyListeners(SWT.KeyUp, e);

            break;
        }

        case SWT.Modify: {
            // sebthom
            if (!popup.isVisible()) {
                if (text.getText().length() == 0) {
                    dp.setDate(null);
                } else {
                    try {
                        dp.setDate(getFormat().parse(text.getText()));
                    } catch (final ParseException pe) {
                        dp.setDate(null);
                    }
                }
            }

            // dp.deselectAll ();
            final Event e = new Event();
            e.time = event.time;
            notifyListeners(SWT.Modify, e);

            break;
        }

        case SWT.MouseDown: {
            if ((event.button != 1) || text.getEditable()) {
                return;
            }

            final boolean dropped = isDropped();
            text.selectAll();

            if (!dropped) {
                setFocus();
            }

            dropDown(!dropped);

            break;
        }

        case SWT.MouseUp: {
            if ((event.button != 1) || text.getEditable()) {
                return;
            }

            text.selectAll();

            break;
        }

        case SWT.Traverse: {
            switch (event.detail) {
            case SWT.TRAVERSE_RETURN:
            case SWT.TRAVERSE_ARROW_PREVIOUS:
            case SWT.TRAVERSE_ARROW_NEXT:

                // The enter causes default selection and
                // the arrow keys are used to manipulate the dp
                // contents so do not use them for traversal.
                event.doit = false;

                break;
            }

            final Event e = new Event();
            e.time = event.time;
            e.detail = event.detail;
            e.doit = event.doit;
            e.keyCode = event.keyCode;
            notifyListeners(SWT.Traverse, e);
            event.doit = e.doit;

            break;
        }
        }
    }

    /**
     * Enable/disable widget
     * 
     * @author andyglow
     * @param enabled
     */
    @Override
    public void setEnabled(final boolean enabled) {
        super.setEnabled(enabled);
        text.setEnabled(enabled);
        dp.setEnabled(enabled);
        arrow.setEnabled(enabled);
    }
}
