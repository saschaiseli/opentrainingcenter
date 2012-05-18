package ch.opentrainingcenter.client.model;

import ch.opentrainingcenter.client.Messages;

public enum Wetter {
    SUN(0, Messages.SUN), //
    LIGHT_CLOUDY(1, Messages.LIGHT_CLOUDY), //
    CLOUDY(2, Messages.CLOUDY), //
    HEAVY_CLOUDY(3, Messages.HEAVY_CLOUDY), //
    LIGHT_RAIN(4, Messages.LIGHT_RAIN), //
    RAIN(5, Messages.RAIN), //
    HEAVY_RAIN(6, Messages.HEAVY_RAIN), //
    LIGHT_SNOW(7, Messages.LIGHT_SNOW), //
    SNOW(8, Messages.SNOW), //
    UNBEKANNT(9, Messages.UNKNOWN);

    private final int index;
    private final String wetter;

    private Wetter(final int index, final String wetter) {
        this.index = index;
        this.wetter = wetter;
    }

    public static Wetter getRunType(final int index) {
        for (final Wetter type : Wetter.values()) {
            if (type.getIndex() == index) {
                return type;
            }
        }
        throw new IllegalArgumentException("Der Typ mit dem Index: " + index + " existiert nicht!"); //$NON-NLS-1$ //$NON-NLS-2$
    }

    public int getIndex() {
        return index;
    }

    public static String[] getItems() {
        final String items[] = new String[Wetter.values().length];
        for (int i = 0; i < Wetter.values().length; i++) {
            items[i] = Wetter.getRunType(i).getWetter();
        }
        return items;
    }

    public String getWetter() {
        return wetter;
    }
}
