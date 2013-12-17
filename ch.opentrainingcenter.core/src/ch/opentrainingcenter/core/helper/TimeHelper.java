package ch.opentrainingcenter.core.helper;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import org.joda.time.DateTime;
import org.joda.time.Interval;

import ch.opentrainingcenter.i18n.Messages;

public final class TimeHelper {

    private static final String DATE_FILE_FORMAT_PATTERN = "yyyyMMddHHmmssSSS"; //$NON-NLS-1$
    private static final String DATE_TIME_FORMAT_PATTERN = "dd.MM.yyyy HH:mm:ss"; //$NON-NLS-1$
    private static final String TIME_FORMAT_PATTERN = "HH:mm:ss"; //$NON-NLS-1$
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
    public static String convertSecondsToHumanReadableZeit(final double sec) {
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
     * konvertiere ein {@link Date} in ein lesbareres format.
     * 
     * wenn der parameter withDay true ist wird der wochentag noch mit
     * ausgegeben
     * 
     * @param datum
     *            {@link Date}
     * @param withDay
     *            flag ob der wochenag auch mitgegeben werden soll.
     * @return das datum '13.01.2013 00:00:00' und wenn das flag with Day ist
     *         wird noch der ausgeschreibene wochentag vorangestellt.
     */
    public static String convertDateToString(final Date datum, final boolean withDay) {
        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(datum);
        final SimpleDateFormat format = new SimpleDateFormat(DATE_TIME_FORMAT_PATTERN);
        if (withDay) {
            return calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault()) + " " + format.format(datum); //$NON-NLS-1$
        } else {
            return format.format(datum);
        }
    }

    /**
     * konvertiere ein {@link Date} in ein lesbareres format.
     * 
     * wenn der parameter withDay true ist wird der wochentag noch mit
     * ausgegeben
     * 
     * @param datum
     *            {@link Date}
     * @return das datum '201011231423'.
     */
    public static String convertDateToFileName(final Date datum) {
        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(datum);
        final SimpleDateFormat format = new SimpleDateFormat(DATE_FILE_FORMAT_PATTERN);
        return format.format(datum);
    }

    /**
     * @return datum als String in der Form dd.mm.yyyy
     */
    public static String convertDateToString(final Date datum) {
        final Calendar calendar = Calendar.getInstance(Locale.getDefault());
        calendar.setTime(datum);
        final SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT_PATTERN);
        return format.format(datum);
    }

    /**
     * @return datum als String in der Form dd.mm.yyyy
     */
    public static String convertDateToString(final Long millis) {
        return convertDateToString(new Date(millis));
    }

    /**
     * @return zeit als String in der Form HH:mm:ss
     */
    public static String convertTimeToString(final long timeInMillis) {
        final Calendar calendar = Calendar.getInstance(Locale.GERMAN);
        calendar.setTimeInMillis(timeInMillis);
        final SimpleDateFormat format = new SimpleDateFormat(TIME_FORMAT_PATTERN);
        format.setTimeZone(TimeZone.getTimeZone("GMT+0")); //$NON-NLS-1$
        return format.format(calendar.getTime());
    }

    public static int getKalenderWoche(final Date date, final Locale locale) {
        final Calendar calendar = Calendar.getInstance(locale);
        calendar.setTime(date);
        return calendar.get(Calendar.WEEK_OF_YEAR);
    }

    /**
     * Gibt das Jahr zurück. Allerdings gibt zum Beispiel der 1. Januar 2012
     * nicht 2012 zurück sondern 2011. Das Jahr bezieht sich immer auf die Woche
     * in der der Tag ist.
     * 
     */
    public static int getJahr(final Date date, final Locale locale) {
        final Calendar calendar = Calendar.getInstance(locale);
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
        if (i < 10) {
            return "0" + i; //$NON-NLS-1$
        } else {
            return "" + i; //$NON-NLS-1$
        }
    }

    /**
     * Berechnet den Montag, sowie den Sonntag der angegebenen Kalenderwoche in
     * dem entsprechenden Jahr. </br> Für KW 44 im 2012 wäre dies 29. Oktober
     * <--> 4. November
     * 
     * @param jahr
     *            Das Jahr
     * @param kalenderwoche
     *            Die Kalenderwoche
     * @return Start und Enddatum --> Montag und Sonntag der Kalenderwoche
     */
    public static Interval getInterval(final int jahr, final int kalenderwoche) {
        Interval result = null;
        final DateTime dt = new DateTime(jahr, 1, 1, 0, 0);
        DateTime day = null;
        // etwas gewagt, aber das scheiss ding will einfach nicht anders
        for (int i = kalenderwoche - 1; i <= kalenderwoche + 1; i++) {
            day = dt.weekOfWeekyear().addToCopy(i);
            if (day.getWeekOfWeekyear() == kalenderwoche && day.getYear() == jahr) {
                break;
            }
        }
        if (day != null) {
            final int dayOfWeek = day.getDayOfWeek();
            final DateTime start = day.minusDays(dayOfWeek - 1);
            final DateTime end = day.plusDays(7 - dayOfWeek);
            result = new Interval(start.getMillis(), end.getMillis());
        }
        return result;
    }

    /**
     * Gibt den Montag 00:00:00 Uhr zurück von der Woche mit dem angegebenen
     * Datum.
     * 
     * Zum Beispiel: Sonntag 15. Dezember 2013 09:11:00 --> gibt Montag 9.
     * Dezember 2013 00:00:00 zurück.
     */
    public static DateTime getFirstDayOfWeek(final DateTime now) {
        DateTime result = now.minusDays(now.getDayOfWeek() - 1);
        result = result.minusHours(now.getHourOfDay());
        result = result.minusMinutes(now.getMinuteOfHour());
        result = result.minusSeconds(now.getSecondOfMinute());
        result = result.minusMillis(now.getMillisOfSecond());
        return result;
    }

    /**
     * Gibt den ersten Tag mit 00:00:00 Uhr vom Monat zurück.
     * 
     * Zum Beispiel: Sonntag 15. Dezember 2013 09:11:00 --> gibt Sonntag 1.
     * Dezember 2013 00:00:00 zurück.
     */
    public static DateTime getFirstDayOfMonth(final DateTime now) {
        DateTime result = now.minusDays(now.getDayOfMonth() - 1);
        result = result.minusHours(now.getHourOfDay());
        result = result.minusMinutes(now.getMinuteOfHour());
        result = result.minusSeconds(now.getSecondOfMinute());
        result = result.minusMillis(now.getMillisOfSecond());
        return result;
    }

    public static String getTranslatedMonat(final DateTime now) {
        final int month = now.getMonthOfYear();
        final String result;
        switch (month) {
        case 1:
            result = Messages.TimeHelper_0;
            break;
        case 2:
            result = Messages.TimeHelper_1;
            break;
        case 3:
            result = Messages.TimeHelper_2;
            break;
        case 4:
            result = Messages.TimeHelper_3;
            break;
        case 5:
            result = Messages.TimeHelper_4;
            break;
        case 6:
            result = Messages.TimeHelper_5;
            break;
        case 7:
            result = Messages.TimeHelper_6;
            break;
        case 8:
            result = Messages.TimeHelper_7;
            break;
        case 9:
            result = Messages.TimeHelper_8;
            break;
        case 10:
            result = Messages.TimeHelper_9;
            break;
        case 11:
            result = Messages.TimeHelper_10;
            break;
        case 12:
            result = Messages.TimeHelper_11;
            break;
        default:
            result = Messages.TimeHelper_12;
            //            throw new RuntimeException("Unmöglicher Monat: " + month); //$NON-NLS-1$
        }
        return result;
    }
}
