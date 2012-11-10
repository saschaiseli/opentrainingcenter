package ch.opentrainingcenter.model.planing.internal;

import java.util.Comparator;

import ch.opentrainingcenter.model.planing.IPastPlanung;
import ch.opentrainingcenter.model.planing.KwJahrKey;
import ch.opentrainingcenter.transfer.IPlanungWoche;

public class PastPlanungComparator implements Comparator<IPastPlanung> {

    private static final KwJahrKeyComparator comparator = new KwJahrKeyComparator();

    @Override
    public int compare(final IPastPlanung o1, final IPastPlanung o2) {
        if (o1 != null && o2 != null) {
            final IPlanungWoche p1 = o1.getPlanung();
            final KwJahrKey k1 = new KwJahrKey(p1.getJahr(), p1.getKw());

            final IPlanungWoche p2 = o2.getPlanung();
            final KwJahrKey k2 = new KwJahrKey(p2.getJahr(), p2.getKw());
            return comparator.compare(k2, k1);
        }
        return 0;
    }

}
