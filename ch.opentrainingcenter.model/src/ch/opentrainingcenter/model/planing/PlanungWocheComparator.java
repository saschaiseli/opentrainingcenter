package ch.opentrainingcenter.model.planing;

import java.util.Comparator;

import ch.opentrainingcenter.model.planing.internal.KwJahrKeyComparator;
import ch.opentrainingcenter.transfer.IPlanungWoche;

public class PlanungWocheComparator implements Comparator<IPlanungWoche> {

    private static KwJahrKeyComparator comparator = new KwJahrKeyComparator();
    private final boolean newestFirst;

    /**
     * Neuster Eintrag ist zuerst.
     */
    public PlanungWocheComparator() {
        this(true);
    }

    /**
     * @param newestFirst
     */
    public PlanungWocheComparator(final boolean newestFirst) {
        this.newestFirst = newestFirst;
    }

    @Override
    public int compare(final IPlanungWoche p1, final IPlanungWoche p2) {
        final int result;
        if (p1 != null && p2 != null) {
            final KwJahrKey k1 = new KwJahrKey(p1.getJahr(), p1.getKw());
            final KwJahrKey k2 = new KwJahrKey(p2.getJahr(), p2.getKw());
            if (newestFirst) {
                result = comparator.compare(k2, k1);
            } else {
                result = comparator.compare(k1, k2);
            }
        } else {
            result = 0;
        }
        return result;
    }

}
