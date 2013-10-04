package ch.opentrainingcenter.db.internal;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;

public final class DbScriptReader {

    private static final Logger LOGGER = Logger.getLogger(DbScriptReader.class);

    private DbScriptReader() {

    }

    /**
     * Liefert eine Liste von SQL Queries zurück. Wenn das File nicht gefunden
     * wird, wird eine {@link FileNotFoundException} geworfen.
     * 
     * @param dao
     * 
     * @throws FileNotFoundException
     */
    @SuppressWarnings("nls")
    public static final List<String> readDbScript(final String sqlFileName) throws FileNotFoundException {
        final List<String> result = new ArrayList<>();
        if (sqlFileName == null || sqlFileName.length() == 0) {
            throw new IllegalArgumentException("Ungültige Angabe des SQL Files. '" + sqlFileName + "'");
        }
        final InputStream in = DbScriptReader.class.getClassLoader().getResourceAsStream(sqlFileName); //$NON-NLS-1$
        if (in == null) {
            throw new FileNotFoundException("Das SQL Script File '" + sqlFileName + "' konnte nicht gefunden werden.");
        }
        final BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        final StringBuilder strBuilder = new StringBuilder();
        int nextchar;
        try {
            while ((nextchar = reader.read()) != -1) {
                strBuilder.append((char) nextchar);
            }
            result.addAll(convertToList(strBuilder.toString()));
        } catch (final IOException e) {
            LOGGER.warn("Kein DB Script gefunden: ", e);
        }
        return result;
    }

    private static List<String> convertToList(final String sql) {
        final String[] split = sql.split(";"); //$NON-NLS-1$
        final List<String> result = new ArrayList<>();
        for (final String item : Arrays.asList(split)) {
            result.add(item);
        }
        return result;
    }
}
