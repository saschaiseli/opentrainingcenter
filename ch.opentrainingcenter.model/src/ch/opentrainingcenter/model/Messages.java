package ch.opentrainingcenter.model;

import org.eclipse.osgi.util.NLS;

public final class Messages extends NLS {

    private static final String BUNDLENAME = "ch.opentrainingcenter.model.messages"; //$NON-NLS-1$
    public static String SUN;
    public static String LIGHTCLOUDY;
    public static String CLOUDY;
    public static String HEAVYCLOUDY;
    public static String LIGHTRAIN;
    public static String RAIN;
    public static String HEAVYRAIN;
    public static String LIGHTSNOW;
    public static String SNOW;
    public static String UNKNOWN;
    public static String RunType0;
    public static String RunType1;
    public static String RunType2;
    public static String RunType3;
    public static String RunType4;
    public static String RunType5;

    static {
        // initialize resource bundle
        NLS.initializeMessages(BUNDLENAME, Messages.class);
    }

    private Messages() {
    }
}
