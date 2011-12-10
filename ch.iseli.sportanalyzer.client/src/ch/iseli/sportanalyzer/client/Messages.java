package ch.iseli.sportanalyzer.client;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
    private static final String BUNDLE_NAME = "ch.iseli.sportanalyzer.client.messages"; //$NON-NLS-1$
    public static String Application_WindowTitle;
    public static String ApplicationActionBarAdvisor_File;
    public static String ApplicationActionBarAdvisor_Help;
    public static String ApplicationActionBarAdvisor_ImportGpsFiles;
    public static String ApplicationActionBarAdvisor_Restart;
    public static String ApplicationActionBarAdvisor_SwitchPerspective;
    public static String ApplicationActionBarAdvisor_Windows;
    public static String ApplicationWorkbenchAdvisor_AthleteFound;
    public static String ApplicationWorkbenchAdvisor_AthleteNotFound;
    public static String ApplicationWorkbenchAdvisor_AthleteNotInPreferences;
    static {
        // initialize resource bundle
        NLS.initializeMessages(BUNDLE_NAME, Messages.class);
    }

    private Messages() {
    }
}
