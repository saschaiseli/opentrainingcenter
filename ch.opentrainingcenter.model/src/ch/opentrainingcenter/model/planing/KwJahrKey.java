package ch.opentrainingcenter.model.planing;

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
        final KwJahrKey other = (KwJahrKey) obj;
        if (jahr != other.jahr) {
            return false;
        }
        if (kw != other.kw) {
            return false;
        }
        return true;
    }

    @Override
    public int compareTo(final KwJahrKey other) {
        if (jahr == other.getJahr()) {
            return Integer.valueOf(kw).compareTo(Integer.valueOf(other.getKw()));
        } else {
            return Integer.valueOf(jahr).compareTo(Integer.valueOf(other.getJahr()));
        }
    }

    @Override
    public String toString() {
        return "KwJahrKey [jahr=" + jahr + ", kw=" + kw + "]"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
    }

}
