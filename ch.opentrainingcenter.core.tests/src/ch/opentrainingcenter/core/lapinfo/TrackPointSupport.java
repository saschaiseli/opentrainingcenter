package ch.opentrainingcenter.core.lapinfo;

import ch.opentrainingcenter.transfer.ILapInfo;
import ch.opentrainingcenter.transfer.ITrackPointProperty;
import ch.opentrainingcenter.transfer.factory.CommonTransferFactory;
import static org.junit.Assert.assertEquals;

@SuppressWarnings("nls")
public class TrackPointSupport {
    /**
     * Jaja, das erstellt halt nur einen trackpoint.
     */
    static ITrackPointProperty createTrackPoint(final double distance, final int heartbeat, final long time) {
        return CommonTransferFactory.createTrackPointProperty(distance, heartbeat, 42, time, 1, null);
    }

    static void assertLapInfo(final int distance, final int heart, final long time, final String pace, final ILapInfo lapInfo) {
        assertEquals("Distanz: ", distance, lapInfo.getDistance());
        assertEquals("Herz: ", heart, lapInfo.getHeartBeat());
        assertEquals("Pace: ", pace, lapInfo.getPace());
        assertEquals("Time: ", time, lapInfo.getTime());
    }
}
