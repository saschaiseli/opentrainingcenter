package ch.opentrainingcenter.transfer;

import ch.opentrainingcenter.i18n.Messages;

public enum Sport {
    RUNNING(0, "RUNNING", Messages.Sport_JOGGEN), //$NON-NLS-1$

    BIKE(1, "BIKE", Messages.Sport_BIKE), //$NON-NLS-1$

    OTHER(2, "OTHER", Messages.Sport_UNBEKANNT); //$NON-NLS-1$

    private final int index;
    private final String message;
    private final String translated;

    private Sport(final int index, final String message, final String translated) {
        this.index = index;
        this.message = message;
        this.translated = translated;
    }

    public int getIndex() {
        return index;
    }

    public String getMessage() {
        return message;
    }

    public String getTranslated() {
        return translated;
    }
}
