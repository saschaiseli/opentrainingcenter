package ch.opentrainingcenter.charts.single.creators.internal;

import org.jfree.data.xy.XYDataset;

public interface DataSetCreator {

    XYDataset createDatasetHeart();

    XYDataset createDatasetSpeed();

    XYDataset createDatasetAltitude();
}
