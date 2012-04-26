package ch.opentrainingcenter.client.helper;

public final class FileNameToDateConverter {

    private FileNameToDateConverter() {

    }

    public static String getHumanReadableDate(final String fileName) {
        if (fileName == null) {
            throw new IllegalArgumentException("file darf nicht null sein"); //$NON-NLS-1$
        }
        if (fileName.length() <= 16) {
            return fileName;
        }
        final StringBuffer str = new StringBuffer();
        try {
            Integer.parseInt(fileName.substring(0, 4));
            Integer.parseInt(fileName.substring(4, 6));
            Integer.parseInt(fileName.substring(6, 8));
            Integer.parseInt(fileName.substring(9, 11));
            Integer.parseInt(fileName.substring(11, 13));
            str.append(fileName.substring(0, 4)).append('.').append(fileName.substring(4, 6)).append('.').append(fileName.substring(6, 8)).append(' ')
                    .append(fileName.substring(9, 11)).append(':').append(fileName.substring(11, 13));
            return str.toString();
        } catch (final NumberFormatException nfe) {
            return fileName;
        }
    }
}
