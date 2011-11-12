package ch.iseli.sportanalyzer.client.helper;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import javax.xml.datatype.XMLGregorianCalendar;

public class TimeHelper {

    /**
     * Konvertiert Sekunden in H:MM:ss
     * 
     * @param sec
     *            sekunden
     * @return Zeit im Format HH:MM:ss
     */
    public static final String convertSecondsToHumanReadableZeit(final double sec) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis((long) sec * 1000);
        StringBuffer calStr = new StringBuffer();
        int hour = cal.get(Calendar.HOUR);
        int m = cal.get(Calendar.MINUTE);
        int s = cal.get(Calendar.SECOND);
        calStr.append(addZero(hour)).append(":").append(addZero(m)).append(":").append(addZero(s));
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
     * wenn der parameter withDay true ist wird der wochentag noch mit ausgegeben
     * 
     * @param datum
     *            {@link XMLGregorianCalendar}
     * @param withDay
     *            flag ob der wochenag auch mitgegeben werden soll.
     * @return das datum '2010.11.23 14:23' und wenn das flag with Day ist wird noch der ausgeschreibene wochentag vorangestellt.
     */
    public static final String convertGregorianDateToString(final XMLGregorianCalendar datum, boolean withDay) {
        Date time = datum.toGregorianCalendar().getTime();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(time);
        calendar.get(Calendar.DAY_OF_WEEK);
        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
        if (withDay) {
            return calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault()) + " " + format.format(time);
        } else {
            return format.format(time);
        }
    }

    private static String addZero(int i) {
        return i < 10 ? "0" + i : "" + i;
    }
}
