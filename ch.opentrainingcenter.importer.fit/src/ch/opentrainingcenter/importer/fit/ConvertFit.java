package ch.opentrainingcenter.importer.fit;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import ch.opentrainingcenter.core.exceptions.ConvertException;
import ch.opentrainingcenter.core.importer.IConvert2Tcx;
import ch.opentrainingcenter.transfer.ITraining;

import com.garmin.fit.Decode;

public class ConvertFit implements IConvert2Tcx {

    @Override
    public ITraining convert(final File file) throws ConvertException {
        try {
            final InputStream is = new FileInputStream(file);
            final TrainingListener listener = new TrainingListener(file.getAbsolutePath());
            final Decode decode = new Decode();
            decode.read(is, listener);
            return listener.getTraining();
        } catch (final FileNotFoundException e) {
            throw new ConvertException(e);
        }
    }

    @Override
    public String getName() {
        return "Garmin FIT"; //$NON-NLS-1$
    }

    @Override
    public String getFilePrefix() {
        return "fit"; //$NON-NLS-1$
    }
}
