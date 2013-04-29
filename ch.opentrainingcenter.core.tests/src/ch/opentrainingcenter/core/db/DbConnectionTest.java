package ch.opentrainingcenter.core.db;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class DbConnectionTest {
    @Test
    public void testDbNameGut() {
        final DbConnection connection = new DbConnection("driver", "jdbc:h2:file:~/.otc_dev/otc", null, null);
        assertEquals("otc", connection.getDatabaseName());
    }

    @Test
    public void testDbNameFalsch() {
        final DbConnection connection = new DbConnection("driver", "jdbc:h2:file:~.otc_devotc", null, null);
        assertEquals("jdbc:h2:file:~.otc_devotc", connection.getDatabaseName());
    }
}
