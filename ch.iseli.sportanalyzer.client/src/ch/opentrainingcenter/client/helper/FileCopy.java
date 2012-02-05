package ch.opentrainingcenter.client.helper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

public class FileCopy {
    /**
     * Kopiert ein file in ein anderes das destination file wird nie überschrieben werden.
     */
    public static void copyFile(final File sourceFile, final File destFile) throws IOException {
        if (sourceFile.getAbsolutePath().equals(destFile.getAbsolutePath())) {
            return;
        }
        if (!destFile.exists()) {
            destFile.createNewFile();
        }

        FileChannel source = null;
        FileChannel destination = null;
        try {
            source = new FileInputStream(sourceFile).getChannel();
            destination = new FileOutputStream(destFile).getChannel();

            long count = 0;
            final long size = source.size();
            while ((count += destination.transferFrom(source, 0, size - count)) < size)
                ;
        } finally {
            if (source != null) {
                source.close();
            }
            if (destination != null) {
                destination.close();
            }
        }
    }
}
