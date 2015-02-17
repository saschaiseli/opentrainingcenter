package ch.opentrainingcenter.model.training.filter;

public interface Filter<T> {
    /**
     * @return true wenn das Element dem Filter entspricht.
     */
    boolean matches(T t);
}
