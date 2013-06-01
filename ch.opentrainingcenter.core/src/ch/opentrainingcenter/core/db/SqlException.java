package ch.opentrainingcenter.core.db;

public class SqlException extends Exception {

    private static final long serialVersionUID = 1L;

    public SqlException() {
        super();
    }

    public SqlException(final String message) {
        super(message);
    }

    public SqlException(final Throwable throwable) {
        super(throwable);
    }

}
