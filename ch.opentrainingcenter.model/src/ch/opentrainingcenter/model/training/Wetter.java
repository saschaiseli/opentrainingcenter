package ch.opentrainingcenter.model.training;

import ch.opentrainingcenter.i18n.Messages;

public enum Wetter {
    SUN(0, Messages.SUN), //
    LIGHT_CLOUDY(1, Messages.LIGHTCLOUDY), //
    CLOUDY(2, Messages.CLOUDY), //
    HEAVY_CLOUDY(3, Messages.HEAVYCLOUDY), //
    LIGHT_RAIN(4, Messages.LIGHTRAIN), //
    RAIN(5, Messages.RAIN), //
    HEAVY_RAIN(6, Messages.HEAVYRAIN), //
    LIGHT_SNOW(7, Messages.LIGHTSNOW), //
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
