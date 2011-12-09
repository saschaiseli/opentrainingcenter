package ch.iseli.sportanalyzer.client.splashHandlers;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.Platform;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.splash.AbstractSplashHandler;

/**
 * @since 3.3
 * 
 */
public class ExtensibleSplashHandler extends AbstractSplashHandler {

    private final static String F_SPLASH_EXTENSION_ID = "ch.iseli.sportanalyzer.client.splashExtension"; // NON-NLS-1

    private final static String F_ELEMENT_ICON = "icon"; // NON-NLS-1

    private final static String F_ELEMENT_TOOLTIP = "tooltip"; // NON-NLS-1

    private final static String F_DEFAULT_TOOLTIP = "Image"; // NON-NLS-1

    private final static int F_IMAGE_WIDTH = 50;

    private final static int F_IMAGE_HEIGHT = 50;

    private final static int F_SPLASH_SCREEN_BEVEL = 5;

    private final Composite fIconPanel;

    /**
	 * 
	 */
    public ExtensibleSplashHandler() {
        fIconPanel = null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.splash.AbstractSplashHandler#init(org.eclipse.swt.widgets.Shell)
     */
    @Override
    public void init(final Shell splash) {
        // Store the shell
        super.init(splash);
        // Configure the shell layout
        configureUISplash();
        // Load all splash extensions
        loadSplashExtensions();
        // Enter event loop and prevent the RCP application from
        // loading until all work is done
        doEventLoop();
    }

    /**
	 * 
	 */
    private void loadSplashExtensions() {
        // Get all splash handler extensions
        final IExtension[] extensions = Platform.getExtensionRegistry().getExtensionPoint(F_SPLASH_EXTENSION_ID).getExtensions();
        // Process all splash handler extensions
        for (int i = 0; i < extensions.length; i++) {
            processSplashExtension(extensions[i]);
        }
    }

    /**
     * @param extension
     */
    private void processSplashExtension(final IExtension extension) {
        // Get all splash handler configuration elements
        final IConfigurationElement[] elements = extension.getConfigurationElements();
        // Process all splash handler configuration elements
        for (int j = 0; j < elements.length; j++) {
            processSplashElements(elements[j]);
        }
    }

    /**
     * @param configurationElement
     */
    private void processSplashElements(final IConfigurationElement configurationElement) {
        // Attribute: icon
        // Attribute: tooltip
    }

    /**
	 * 
	 */
    private void configureUISplash() {
        // Configure layout
        final GridLayout layout = new GridLayout(1, true);
        getSplash().setLayout(layout);
        // Force shell to inherit the splash background
        getSplash().setBackgroundMode(SWT.INHERIT_DEFAULT);
    }

    /**
	 * 
	 */
    private void doEventLoop() {
        final Shell splash = getSplash();
        if (splash.getDisplay().readAndDispatch() == false) {
            splash.getDisplay().sleep();
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.splash.AbstractSplashHandler#dispose()
     */
    @Override
    public void dispose() {
        super.dispose();
        // Check to see if any images were defined
    }
}
