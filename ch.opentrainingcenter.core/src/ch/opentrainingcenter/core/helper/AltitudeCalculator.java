package ch.opentrainingcenter.core.helper;

import java.util.List;

import ch.opentrainingcenter.core.assertions.Assertions;
import ch.opentrainingcenter.transfer.ITrackPointProperty;

/**
 * Hilfsklasse um die erreichten höhenmeter zu berechnen
 * 
 */
public final class AltitudeCalculator {

    public static class Ascending {

        private final int up;
        private final int down;

        public Ascending(final int up, final int down) {
            this.up = up;
            this.down = down;
        }

        public int getUp() {
            return up;
        }

        public int getDown() {
            return down;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + down;
            result = prime * result + up;
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
            final Ascending other = (Ascending) obj;
            if (down != other.down) {
                return false;
            }
            if (up != other.up) {
                return false;
            }
            return true;
        }

        @SuppressWarnings("nls")
        @Override
        public String toString() {
            return "Ascending [up=" + up + ", down=" + down + "]";
        }
    }

    private AltitudeCalculator() {

    }

    /**
     * Berechnet die Höhenmeter nach oben und nach unten. 1m hoch und 1m runter
     * ergibt je 1m nicht null. Die Meter nach oben werden gerechnet und die
     * nach unten.
     * 
     * @param trackpoints
     *            alle trackpoints eines laufes. Sortiert nach abfolge.
     * @return {@link Ascending}
     */
    public static Ascending calculateAscending(final List<ITrackPointProperty> trackpoints) {
        Assertions.notNull(trackpoints, "Trackpoints dürfen nicht null sein!!"); //$NON-NLS-1$
        int up = 0;
        int down = 0;
        int previous = 0;
        boolean first = true;
        for (final ITrackPointProperty point : trackpoints) {
            final int current = point.getAltitude();
            if (first) {
                previous = current;
                first = false;
            } else {
                final int diff = previous - current;
                if (diff > 0) {
                    // geht nach unten
                    down += diff;
                } else {
                    // geht nach oben
                    up += Math.abs(diff);
                }
                previous = current;
            }
        }
        return new Ascending(up, down);
    }
}
