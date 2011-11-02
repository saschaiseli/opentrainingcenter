package ch.opentrainingcenter.db.h2;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class Activator implements BundleActivator {

    // The shared instance
    private static Activator plugin;
    private BundleContext context;

    public BundleContext getContext() {
        return context;
    }

    /**
     * The constructor
     */
    public Activator() {
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext )
     */
    @Override
    public void start(BundleContext context) throws Exception {
        this.context = context;
        plugin = this;
    }

    @Override
    public void stop(BundleContext context) throws Exception {

    }

}
