package ch.opentrainingcenter.client.cache;

import javax.xml.datatype.DatatypeConfigurationException;

import ch.opentrainingcenter.transfer.CommonTransferFactory;
import ch.opentrainingcenter.transfer.ITraining;

public class ActivityTTestHelper {

    public static ITraining createActivity(final int year) throws DatatypeConfigurationException {
        return CommonTransferFactory.createTraining(year, 0d, 0d, 0, 0, 0d);
    }
}
