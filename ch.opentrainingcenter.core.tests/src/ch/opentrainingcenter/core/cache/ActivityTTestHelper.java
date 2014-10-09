package ch.opentrainingcenter.core.cache;

import javax.xml.datatype.DatatypeConfigurationException;

import ch.opentrainingcenter.transfer.HeartRate;
import ch.opentrainingcenter.transfer.ITraining;
import ch.opentrainingcenter.transfer.RunData;
import ch.opentrainingcenter.transfer.factory.CommonTransferFactory;

public final class ActivityTTestHelper {

    private ActivityTTestHelper() {

    }

    public static ITraining createActivity(final int year) throws DatatypeConfigurationException {
        return CommonTransferFactory.createTraining(new RunData(year, 0, 0, 0), new HeartRate(0, 0));
    }
}
