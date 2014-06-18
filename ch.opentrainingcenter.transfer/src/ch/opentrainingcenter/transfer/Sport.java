package ch.opentrainingcenter.transfer;

import ch.opentrainingcenter.i18n.Messages;

public enum Sport {
    RUNNING(0, "RUNNING", Messages.Sport_JOGGEN, "icons/man_u_32_32.png"), //$NON-NLS-1$ //$NON-NLS-2$

    BIKING(1, "BIKE", Messages.Sport_BIKE, "icons/32_32/bike_32_32.png"), //$NON-NLS-1$ //$NON-NLS-2$

    OTHER(2, "OTHER", Messages.Sport_UNBEKANNT, "icons/man_u_32_32.png"); //$NON-NLS-1$ //$NON-NLS-2$

    private final int index;
    private final String message;
    private final String translated;
    private final String imageIcon;

    private Sport(final int index, final String message, final String translated, final String imageIcon) {
        this.index = index;
        this.message = message;
        this.translated = translated;
        this.imageIcon = imageIcon;
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

    public String getImageIcon() {
        return imageIcon;
    }
}
