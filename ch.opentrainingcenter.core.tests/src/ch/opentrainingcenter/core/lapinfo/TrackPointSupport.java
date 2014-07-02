package ch.opentrainingcenter.core.lapinfo;

import static org.junit.Assert.assertEquals;
import ch.opentrainingcenter.transfer.ILapInfo;
import ch.opentrainingcenter.transfer.ITrackPointProperty;
import ch.opentrainingcenter.transfer.factory.CommonTransferFactory;

@SuppressWarnings("nls")
public final class TrackPointSupport {

    private TrackPointSupport() {

    }

    /**
     * Jaja, das erstellt halt nur einen trackpoint.
     */
    static ITrackPointProperty createTrackPoint(final double distance, final int heartbeat, final long time) {
        return CommonTransferFactory.createTrackPointProperty(distance, heartbeat, 42, time, 1, null);
    }

    static void assertLapInfo(final int runde, final int start, final int end, final int heart, final long time, final String pace, final ILapInfo lapInfo) {
        assertEquals("Runde", runde, lapInfo.getLap());
        assertEquals("Start: ", start, lapInfo.getStart());
        assertEquals("End: ", end, lapInfo.getEnd());
        assertEquals("Herz: ", heart, lapInfo.getHeartBeat());
        assertEquals("Pace: ", pace, lapInfo.getPace());
        assertEquals("Time: ", time, lapInfo.getTime());
    }
}
