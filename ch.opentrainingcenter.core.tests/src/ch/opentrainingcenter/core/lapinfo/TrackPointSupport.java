package ch.opentrainingcenter.core.lapinfo;

import static org.junit.Assert.assertEquals;
import ch.opentrainingcenter.transfer.ILapInfo;
import ch.opentrainingcenter.transfer.ITrackPointProperty;
import ch.opentrainingcenter.transfer.factory.CommonTransferFactory;

public class TrackPointSupport {
    /**
     * Jaja, das erstellt halt nur einen trackpoint.
     */
    static ITrackPointProperty createTrackPoint(double distance, int heartbeat, long time) {
        return CommonTransferFactory.createTrackPointProperty(distance, heartbeat, 42, time, 1, null);
    }

    static void assertLapInfo(int distance, int heart, long time, String pace, ILapInfo lapInfo) {
        assertEquals("Distanz: ", distance, lapInfo.getDistance());
        assertEquals("Herz: ", heart, lapInfo.getHeartBeat());
        assertEquals("Pace: ", pace, lapInfo.getPace());
        assertEquals("Time: ", time, lapInfo.getTime());
    }
}
