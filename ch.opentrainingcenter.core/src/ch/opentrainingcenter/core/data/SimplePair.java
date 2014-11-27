package ch.opentrainingcenter.core.data;

public class SimplePair<T> {

    private final T first;
    private final T second;

    public SimplePair(final T first, final T second) {
        this.first = first;
        this.second = second;
    }

    public T getFirst() {
        return first;
    }

    public T getSecond() {
        return second;
    }

    @Override
    public String toString() {
        return "SimplePair [first=" + first + ", second=" + second + "]"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
    }
}
