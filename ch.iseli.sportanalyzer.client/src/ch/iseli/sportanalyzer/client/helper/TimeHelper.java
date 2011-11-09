package ch.iseli.sportanalyzer.client.helper;

import java.util.Calendar;

public class TimeHelper {

    /**
     * Konvertiert Sekunden in H:MM:ss
     * 
     * @param sec
     *            sekunden
     * @return Zeit im Format HH:MM:ss
     */
    public static String convertSecondsToHumanReadableZeit(double sec) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis((long) sec * 1000);
        StringBuffer calStr = new StringBuffer();
        int hour = cal.get(Calendar.HOUR);
        int m = cal.get(Calendar.MINUTE);
        int s = cal.get(Calendar.SECOND);
        calStr.append(addZero(hour)).append(":").append(addZero(m)).append(":").append(addZero(s));
        return calStr.toString();
    }

    private static String addZero(int i) {
        return i < 10 ? "0" + i : "" + i;
    }
}
