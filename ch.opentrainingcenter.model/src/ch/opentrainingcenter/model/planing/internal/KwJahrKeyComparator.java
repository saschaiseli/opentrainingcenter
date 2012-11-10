package ch.opentrainingcenter.model.planing.internal;

import java.util.Comparator;

import ch.opentrainingcenter.model.planing.KwJahrKey;

public class KwJahrKeyComparator implements Comparator<KwJahrKey> {

    @Override
    public int compare(final KwJahrKey o1, final KwJahrKey o2) {
        final int jahr1 = o1.getJahr();
        final int jahr2 = o2.getJahr();
        if (jahr1 == jahr2) {
            // kw vergleichen
            return o1.getKw() - o2.getKw();
        } else {
            return jahr1 - jahr2;
        }
    }

}
