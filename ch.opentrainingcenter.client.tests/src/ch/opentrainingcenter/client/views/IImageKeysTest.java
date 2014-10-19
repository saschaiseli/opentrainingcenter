package ch.opentrainingcenter.client.views;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

@SuppressWarnings("nls")
public class IImageKeysTest {
    @Test
    public void testStupidImageKeys() {
        assertEquals("icons/wizban/import_wiz.png", IImageKeys.IMPORT_WIZ);
        assertEquals("icons/package_games_sports_24_24.png", IImageKeys.RUNNING_MAN);
        assertEquals("icons/table_green_24_24.png", IImageKeys.GREEN_TABLE);
        assertEquals("icons/drive_backup_24_24.png", IImageKeys.BACKUP);
    }
}
