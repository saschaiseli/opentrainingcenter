package ch.opentrainingcenter.client.helper;

import java.util.Comparator;

import ch.opentrainingcenter.client.model.planing.impl.KwJahrKey;
import ch.opentrainingcenter.transfer.IPlanungWoche;

public class PlanungWocheComparator implements Comparator<IPlanungWoche> {

    private static KwJahrKeyComparator comparator = new KwJahrKeyComparator();

    @Override
    public int compare(final IPlanungWoche p1, final IPlanungWoche p2) {
        if (p1 != null && p2 != null) {
            final KwJahrKey k1 = new KwJahrKey(p1.getJahr(), p1.getKw());

            final KwJahrKey k2 = new KwJahrKey(p2.getJahr(), p2.getKw());
            return comparator.compare(k2, k1);
        }
        return 0;
    }

}
