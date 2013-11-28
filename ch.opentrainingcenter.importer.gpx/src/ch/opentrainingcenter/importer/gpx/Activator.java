package ch.opentrainingcenter.importer.gpx;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class Activator implements BundleActivator {

    public static final String BUNDLE_ID = "ch.opentrainingcenter.importer.gpx"; //$NON-NLS-1$
    private static BundleContext context;

    static BundleContext getContext() {
        return context;
    }

    @Override
    public void start(final BundleContext bundleContext) throws Exception {
        Activator.context = bundleContext;
    }

    @Override
    public void stop(final BundleContext bundleContext) throws Exception {
        Activator.context = null;
    }

}
