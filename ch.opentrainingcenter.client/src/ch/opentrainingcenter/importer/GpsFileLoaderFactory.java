package ch.opentrainingcenter.importer;

import ch.opentrainingcenter.importer.impl.GpsFileLoader;

public class GpsFileLoaderFactory {

    public static IGpsFileLoader createGpsFileLoader() {
	return new GpsFileLoader();
    }

    private GpsFileLoaderFactory() {

    }
}
