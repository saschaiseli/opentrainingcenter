package ch.opentrainingcenter.model.navigation;

import java.util.Date;
import java.util.Locale;

import ch.opentrainingcenter.core.helper.TimeHelper;

public class KalenderWoche implements Comparable<KalenderWoche> {
    private final int kw;
    private final int jahr;

    public KalenderWoche(final Date date) {
        kw = TimeHelper.getKalenderWoche(date, Locale.GERMAN);
        jahr = TimeHelper.getJahr(date, Locale.GERMAN);
    }

    public int getKw() {
        return kw;
    }

    public int getJahr() {
        return jahr;
    }

    @Override
    public int compareTo(final KalenderWoche other) {
        if (jahr == other.getJahr()) {
            return Integer.valueOf(other.getKw()).compareTo(Integer.valueOf(kw));
        } else {
            return Integer.valueOf(other.getJahr()).compareTo(Integer.valueOf(jahr));
        }
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + jahr;
        result = prime * result + kw;
        return result;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final KalenderWoche other = (KalenderWoche) obj;
        if (jahr != other.jahr) {
            return false;
        }
        if (kw != other.kw) {
            return false;
        }
        return true;
    }

}
