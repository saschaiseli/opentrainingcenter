package ch.opentrainingcenter.importer;

import java.io.IOException;

public class LoadImportedException extends IOException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public LoadImportedException(final String message) {
        super(message);
    }

    public LoadImportedException(final String message, final Throwable throwable) {
        super(message, throwable);
    }
}
