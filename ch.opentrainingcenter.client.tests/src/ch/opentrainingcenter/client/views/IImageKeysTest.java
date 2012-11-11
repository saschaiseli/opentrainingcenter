package ch.opentrainingcenter.client.views;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

@SuppressWarnings("nls")
public class IImageKeysTest {
    @Test
    public void testStupidImageKeys() {
        assertEquals("icons/16_16/import_wiz.gif", IImageKeys.IMPORT_GPS_KLEIN);
        assertEquals("icons/16_16/import_wiz.gif", IImageKeys.IMPORT_GPS_GROSS);
        assertEquals("icons/package_games_sports_24_24.png", IImageKeys.RUNNING_MAN);
        assertEquals("icons/table_green_24_24.png", IImageKeys.GREEN_TABLE);
        assertEquals("icons/drive_backup_24_24.png", IImageKeys.BACKUP);
    }
}
