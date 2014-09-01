package ch.opentrainingcenter.importer.fit;

import java.io.File;

import ch.opentrainingcenter.core.exceptions.ConvertException;
import ch.opentrainingcenter.core.importer.IConvert2Tcx;
import ch.opentrainingcenter.transfer.HeartRate;
import ch.opentrainingcenter.transfer.ITraining;
import ch.opentrainingcenter.transfer.RunData;
import ch.opentrainingcenter.transfer.factory.CommonTransferFactory;

public class ConvertFit implements IConvert2Tcx {

    @Override
    public ITraining convert(final File file) throws ConvertException {
        final RunData runData = new RunData(1, 2, 3, 4);
        final HeartRate heart = new HeartRate(42, 42);
        return CommonTransferFactory.createTraining(runData, heart);
    }

    @Override
    public String getFilePrefix() {
        return "fit"; //$NON-NLS-1$
    }

    @Override
    public String getName() {
        return "Garmin FIT"; //$NON-NLS-1$
    }

}
