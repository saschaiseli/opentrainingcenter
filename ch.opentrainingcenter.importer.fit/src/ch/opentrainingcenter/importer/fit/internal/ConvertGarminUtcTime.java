package ch.opentrainingcenter.importer.fit.internal;

import java.util.Date;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

public final class ConvertGarminUtcTime {
    private ConvertGarminUtcTime() {

    }

    public static long convertToLocalMillis(final Date date) {
        final DateTime timeInUtc = new DateTime(date, DateTimeZone.UTC);
        return timeInUtc.toLocalDateTime().toDate().getTime();
    }

    public static Date convertToLocalDate(final Date date) {
        final DateTime timeInUtc = new DateTime(date, DateTimeZone.UTC);
        return new Date(timeInUtc.toLocalDateTime().toDate().getTime());
    }
}
