package ch.opentrainingcenter.db.postgres;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class DbScriptReader {
    private DbScriptReader() {

    }

    /**
     * Liefert eine Liste von SQL Queries zurück. Wenn das File nicht gefunden
     * wird, wird eine {@link FileNotFoundException} geworfen.
     * @param dao
     * 
     * @throws FileNotFoundException
     */
    public static final List<String> readDbScript(final String sqlFileName) throws FileNotFoundException {
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
        String sql = null;
        try {
            while ((nextchar = reader.read()) != -1) {
                strBuilder.append((char) nextchar);
            }
            sql = strBuilder.toString();
        } catch (final IOException e) {
            e.printStackTrace();
        }
        return convertToList(sql);
    }

    private static List<String> convertToList(final String sql) {
        final String[] split = sql.split(";");
        final List<String> result = new ArrayList<>();
        for (final String item : Arrays.asList(split)) {
            result.add(item);
        }
        return result;
    }
}
