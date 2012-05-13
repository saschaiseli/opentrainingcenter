package ch.opentrainingcenter.client.charts;

import org.jfree.data.xy.XYDataset;

public interface DataSetCreator {

    XYDataset createDatasetHeart();

    XYDataset createDatasetSpeed();

    XYDataset createDatasetAltitude();
}
