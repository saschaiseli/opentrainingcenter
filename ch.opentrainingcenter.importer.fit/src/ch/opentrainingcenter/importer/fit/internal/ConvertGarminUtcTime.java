package ch.opentrainingcenter.importer.fit.internal;

import java.util.Date;

import org.joda.time.DateTimeZone;

public final class ConvertGarminUtcTime {
    private ConvertGarminUtcTime() {

    }

    public static long convertToLocalMillis(final Date date) {
        return DateTimeZone.getDefault().convertUTCToLocal(date.getTime());
    }

    public static Date convertToLocalDate(final Date date) {
        return new Date(DateTimeZone.getDefault().convertUTCToLocal(date.getTime()));
    }
}
