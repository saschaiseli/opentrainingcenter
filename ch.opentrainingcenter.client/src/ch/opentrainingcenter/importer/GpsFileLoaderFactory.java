package ch.opentrainingcenter.importer;

import ch.opentrainingcenter.importer.impl.GpsFileLoader;

public final class GpsFileLoaderFactory {

    private GpsFileLoaderFactory() {
    }

    public static IGpsFileLoader createGpsFileLoader() {
        return new GpsFileLoader();
    }

}
