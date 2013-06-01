package ch.opentrainingcenter.db;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.FileNotFoundException;
import java.util.List;

import org.junit.Test;

public class DbScriptReaderTest {

    @Test(expected = IllegalArgumentException.class)
    public void readNull() throws FileNotFoundException {
        DbScriptReader.readDbScript(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void readEmpty() throws FileNotFoundException {
        DbScriptReader.readDbScript("");
    }

    @Test(expected = FileNotFoundException.class)
    public void readNotFound() throws FileNotFoundException {
        DbScriptReader.readDbScript("notFound.sql");
    }

    @Test
    public void readFound() throws FileNotFoundException {
        final List<String> sql = DbScriptReader.readDbScript("otc.sql");
        assertNotNull(sql);
        assertTrue("Es müssen sich mehrere Queries im File befinden.", sql.size() > 10);
    }
}
