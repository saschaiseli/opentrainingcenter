package ch.opentrainingcenter.client;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import ch.opentrainingcenter.client.splash.OtcSplashHandler;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends AbstractUIPlugin {

    // The plug-in ID
    public static final String PLUGIN_ID = "ch.opentrainingcenter.client"; //$NON-NLS-1$

    // The shared instance
    private static Activator plugin;

    private static OtcSplashHandler otcSplashHandler;

    /**
     * The constructor
     */
    public Activator() {
    }

    @Override
    public void start(final BundleContext context) throws Exception {
        super.start(context);
        plugin = this;
    }

    @Override
    public void stop(final BundleContext context) throws Exception {
        plugin = null;
        super.stop(context);
    }

    /**
     * Returns the shared instance
     * 
     * @return the shared instance
     */
    public static Activator getDefault() {
        return plugin;
    }

    /**
     * Returns an image descriptor for the image file at the given plug-in
     * relative path
     * 
     * @param path
     *            the path
     * @return the image descriptor
     */
    public static ImageDescriptor getImageDescriptor(final String path) {
        return imageDescriptorFromPlugin(PLUGIN_ID, path);
    }

    public static void setSplashHandler(final OtcSplashHandler otcSplashHandler) {
        Activator.otcSplashHandler = otcSplashHandler;

    }

    public static OtcSplashHandler getSplashHandler() {
        return Activator.otcSplashHandler;
    }
}
