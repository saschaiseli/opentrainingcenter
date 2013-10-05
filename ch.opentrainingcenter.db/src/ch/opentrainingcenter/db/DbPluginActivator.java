package ch.opentrainingcenter.db;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class DbPluginActivator implements BundleActivator {

    public static final String PLUGIN_ID = "ch.opentrainingcenter.db"; //$NON-NLS-1$

    private static BundleContext context;

    static BundleContext getContext() {
        return context;
    }

    @Override
    public void start(final BundleContext bundleContext) {
        DbPluginActivator.context = bundleContext;
    }

    @Override
    public void stop(final BundleContext bundleContext) {
        DbPluginActivator.context = null;
    }

}
