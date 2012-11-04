package ch.opentrainingcenter.client.model.planing.impl;

public class KwJahrKey implements Comparable<KwJahrKey> {
    private final int jahr;
    private final int kw;

    public KwJahrKey(final int jahr, final int kw) {
        this.jahr = jahr;
        this.kw = kw;
    }

    public int getJahr() {
        return jahr;
    }

    public int getKw() {
        return kw;
    }

    @Override
    public int compareTo(final KwJahrKey other) {
        if (jahr == other.getJahr()) {
            return Integer.valueOf(kw).compareTo(Integer.valueOf(other.getKw()));
        } else {
            return Integer.valueOf(jahr).compareTo(Integer.valueOf(other.getJahr()));
        }
    }
}
