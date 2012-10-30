package ch.opentrainingcenter.client.helper;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import javax.xml.datatype.XMLGregorianCalendar;

import ch.opentrainingcenter.client.Messages;

public final class TimeHelper {

    private static final String DATE_TIME_FORMAT_PATTERN = "dd.MM.yyyy HH:mm:ss"; //$NON-NLS-1$
    private static final String DATE_FORMAT_PATTERN = "dd.MM.yyyy"; //$NON-NLS-1$
    private static final int SEKUNDE_IN_MS = 1000;
    private static final String UNKNOWN_DATE = "--:--:--"; //$NON-NLS-1$

    private TimeHelper() {

    }

    /**
     * Konvertiert Sekunden in H:MM:ss
     * 
     * @param sec
     *            sekunden
     * @return Zeit im Format HH:MM:ss. Wenn Zeit negativ ist, wird --:--:--
     *         zurückgegeben.
     */
    public static final String convertSecondsToHumanReadableZeit(final double sec) {
        if (sec < 0) {
            return UNKNOWN_DATE;
        }
        final Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis((long) sec * SEKUNDE_IN_MS);
        final StringBuffer calStr = new StringBuffer();
        final int hour = cal.get(Calendar.HOUR) - 1;
        final int m = cal.get(Calendar.MINUTE);
        final int s = cal.get(Calendar.SECOND);
        calStr.append(hour).append(":").append(addZero(m)).append(":").append(addZero(s)); //$NON-NLS-1$ //$NON-NLS-2$
        return calStr.toString();
    }

    /**
     * 
     */
    public static final String convertGregorianDateToString(final XMLGregorianCalendar datum) {
        return convertGregorianDateToString(datum, false);
    }

    /**
     * konvertiere ein {@link XMLGregorianCalendar} in ein lesbareres format.
     * 
     * wenn der parameter withDay true ist wird der wochentag noch mit
     * ausgegeben
     * 
     * @param datum
     *            {@link XMLGregorianCalendar}
     * @param withDay
     *            flag ob der wochenag auch mitgegeben werden soll.
     * @return das datum '2010.11.23 14:23' und wenn das flag with Day ist wird
     *         noch der ausgeschreibene wochentag vorangestellt.
     */
    public static final String convertGregorianDateToString(final XMLGregorianCalendar datum, final boolean withDay) {
        final Date time = datum.toGregorianCalendar().getTime();
        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(time);
        calendar.get(Calendar.DAY_OF_WEEK);
        final SimpleDateFormat format = new SimpleDateFormat(DATE_TIME_FORMAT_PATTERN);
        if (withDay) {
            return calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault()) + Messages.TimeHelper1 + format.format(time);
        } else {
            return format.format(time);
        }
    }

    /**
     * konvertiere ein {@link Date} in ein lesbareres format.
     * 
     * wenn der parameter withDay true ist wird der wochentag noch mit
     * ausgegeben
     * 
     * @param datum
     *            {@link XMLGregorianCalendar}
     * @param withDay
     *            flag ob der wochenag auch mitgegeben werden soll.
     * @return das datum '2010.11.23 14:23' und wenn das flag with Day ist wird
     *         noch der ausgeschreibene wochentag vorangestellt.
     */
    public static final String convertDateToString(final Date datum, final boolean withDay) {
        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(datum);
        final SimpleDateFormat format = new SimpleDateFormat(DATE_TIME_FORMAT_PATTERN);
        if (withDay) {
            return calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault()) + Messages.TimeHelper1 + format.format(datum);
        } else {
            return format.format(datum);
        }
    }

    /**
     * @return datum als String in der Form dd.mm.yyyy
     */
    public static final String convertDateToString(final Date datum) {
        final Calendar calendar = Calendar.getInstance(Locale.getDefault());
        calendar.setTime(datum);
        final SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT_PATTERN);
        return format.format(datum);
    }

    public static int getKalenderWoche(final Date date) {
        final Calendar calendar = Calendar.getInstance(Locale.getDefault());
        calendar.setTime(date);
        return calendar.get(Calendar.WEEK_OF_YEAR);
    }

    /**
     * Gibt das Jahr zurück. Allerdings gibt zum Beispiel der 1. Januar 2012
     * nicht 2012 zurück sondern 2011. Das Jahr bezieht sich immer auf die Woche
     * in der der Tag ist.
     */
    public static int getJahr(final Date date) {
        final Calendar calendar = Calendar.getInstance(Locale.getDefault());
        calendar.setTime(date);
        int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int week = calendar.get(Calendar.WEEK_OF_YEAR);
        if (week == 52 && month == 0) {
            // so zum beispiel am 1. januar 2012 --> KW52
            year--;
        }
        return year;
    }

    private static String addZero(final int i) {
        return i < 10 ? Messages.TimeHelper2 + i : Messages.TimeHelper3 + i;
    }
}
