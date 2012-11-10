package ch.opentrainingcenter.importer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.channels.FileChannel;

import org.apache.log4j.Logger;

import ch.opentrainingcenter.core.importer.IFileCopy;

public class FileCopy implements IFileCopy {
    private static final Logger LOG = Logger.getLogger(FileCopy.class);

    @Override
    public void copyFile(final File sourceFile, final File destFile) throws IOException {
        if (sourceFile.getAbsolutePath().equals(destFile.getAbsolutePath())) {
            return;
        }
        if (!destFile.exists()) {
            LOG.debug("Destination File existiert nicht");
            final File dir = new File(getFolder(destFile.getAbsolutePath()));
            if (dir.mkdir()) {
                LOG.debug("Folder erstellt");
                destFile.createNewFile();
            } else {
                LOG.debug("Folder nicht erstellt");
            }
        }

        FileChannel source = null;
        FileChannel destination = null;
        try {
            source = new FileInputStream(sourceFile).getChannel();
            destination = new FileOutputStream(destFile).getChannel();

            long count = 0;
            final long size = source.size();
            while ((count += destination.transferFrom(source, 0, size - count)) < size) {
                LOG.info("do copy work...");
            }
        } finally {
            if (source != null) {
                source.close();
            }
            if (destination != null) {
                destination.close();
            }
        }
    }

    public void copyFiles(final String sourceFolder, final String destinationFolder, final FilenameFilter filter) throws IOException {
        final File file = new File(sourceFolder);
        final String[] filteredFileNames = file.list(filter);
        for (final String fileName : filteredFileNames) {
            copyFile(new File(sourceFolder, fileName), new File(destinationFolder, fileName));
        }
    }

    private String getFolder(final String absolutePath) {
        return absolutePath.substring(0, absolutePath.lastIndexOf(File.separator));
    }
}
