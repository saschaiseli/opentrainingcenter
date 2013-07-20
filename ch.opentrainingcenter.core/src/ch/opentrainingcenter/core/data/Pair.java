package ch.opentrainingcenter.core.data;

public class Pair<K extends Number, V> {
    private K first;
    private V second;

    public Pair() {

    }

    public Pair(final K first, final V second) {
        this.first = first;
        this.second = second;
    }

    public V getSecond() {
        return second;
    }

    public void setsetSecond(final V second) {
        this.second = second;
    }

    public K getFirst() {
        return first;
    }

    public void setFirst(final K first) {
        this.first = first;
    }

    @Override
    public String toString() {
        return "Pair [first=" + first + ", second=" + second + "]"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
    }
}
