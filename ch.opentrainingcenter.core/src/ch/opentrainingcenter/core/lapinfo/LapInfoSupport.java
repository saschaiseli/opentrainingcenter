package ch.opentrainingcenter.core.lapinfo;

import java.util.List;

import ch.opentrainingcenter.core.assertions.Assertions;
import ch.opentrainingcenter.core.helper.DistanceHelper;
import ch.opentrainingcenter.transfer.ILapInfo;
import ch.opentrainingcenter.transfer.ITrackPointProperty;
import ch.opentrainingcenter.transfer.Sport;
import ch.opentrainingcenter.transfer.factory.CommonTransferFactory;

public final class LapInfoSupport {

    private LapInfoSupport() {

    }

    /**
     * Erstellt aus einer Liste von {@link ITrackPointProperty}s eine
     * {@link ILapInfo}. Eine {@link ILapInfo} ist eine komprimierte Sicht auf
     * eine Runde.
     * 
     * Es muessen mindestens 2 Punkte vorhanden sein! Es braucht einen Start &
     * Endpunkt. <br>
     * <br>
     * <b> Wenn mehrere Runden gemacht werden, müsste der letzte punkt von Runde
     * n der erste Punkt von Runde n+1 sein. </b> <br>
     * <br>
     * 
     * @param points
     *            sortierte Liste von {@link ITrackPointProperty}. Darf nicht
     *            null sein. Muss mindestens 2 Punkte beinhalten.
     * @throws IllegalArgumentException
     *             wenn nicht mindestens zwei Trackpoints uebergeben werden.
     * 
     * @return eine {@link ILapInfo}
     */
    public static ILapInfo createLapInfo(final List<ITrackPointProperty> points, final Sport sport) {
        Assertions.notNull(points);
        Assertions.isValid(points.size() < 2, "Es braucht mindest 2 Punkte"); //$NON-NLS-1$

        int heart = 0;
        for (final ITrackPointProperty point : points) {
            heart += point.getHeartBeat();
        }
        final int heartBeat = heart / points.size();
        final ITrackPointProperty firstPoint = points.get(0);
        final ITrackPointProperty lastPoint = points.get(points.size() - 1);

        final int startDist = (int) firstPoint.getDistance();
        final int endDist = (int) lastPoint.getDistance();
        final int distance = endDist - startDist;

        final long startTime = firstPoint.getZeit();
        final long endTime = lastPoint.getZeit();
        final long time = endTime - startTime;

        final String pace = DistanceHelper.calculatePace(distance, time / 1000, sport);
        return CommonTransferFactory.createLapInfo(firstPoint.getLap(), startDist, endDist, time, heartBeat, pace);
    }

    /**
     * Erstellt aus einer Liste von {@link ITrackPointProperty}s eine
     * {@link ILapInfo}. Eine {@link ILapInfo} ist eine komprimierte Sicht auf
     * eine Runde.
     * 
     * Es muessen mindestens 1 Punkte vorhanden sein!. DIe initiale Position
     * muss einfach noch mitgegeben werden <br>
     * <br>
     * 
     * <PRE>
     * ES WIRD DIE RUNDE VON AUSSEN GESETZT UND NICHT VON DEN POINTS!!!!
     * </PRE>
     * 
     * @param runde
     *            die Runde der LapInfo.
     * @param points
     *            sortierte Liste von {@link ITrackPointProperty}. Darf nicht
     *            null sein. Muss mindestens 2 Punkte beinhalten.
     * 
     * @throws IllegalArgumentException
     *             wenn nicht mindestens zwei Trackpoints uebergeben werden.
     * 
     * @return eine {@link ILapInfo}
     */
    public static ILapInfo createLapInfo(final int runde, final List<ITrackPointProperty> points, final double initPosition, final long initTime,
            final Sport sport) {
        Assertions.notNull(points);
        Assertions.isValid(initPosition < 0, "initiale position muss groesser gleich 0 sein"); //$NON-NLS-1$
        Assertions.isValid(initTime < 0, "initiale zeit muss groesser gleich 0 sein"); //$NON-NLS-1$
        Assertions.isValid(points.isEmpty(), "Es braucht mindest einen Punkt, ha"); //$NON-NLS-1$
        int heart = 0;
        for (final ITrackPointProperty point : points) {
            heart += point.getHeartBeat();
        }
        final int heartBeat = heart / points.size();

        final ITrackPointProperty firstPoint = points.get(0);
        final ITrackPointProperty lastPoint = points.get(points.size() - 1);

        final int start;

        final int end = (int) lastPoint.getDistance();
        final int distance;

        final long endTime = lastPoint.getZeit();
        final long time;

        if (isFirstPoint(points, initTime)) {
            // erster punkt und mehr als ein punkt
            time = endTime - firstPoint.getZeit();
            start = (int) firstPoint.getDistance();
        } else {
            time = endTime - initTime;
            start = (int) initPosition;
        }
        distance = end - start;

        final String pace = DistanceHelper.calculatePace(distance, time / 1000, sport);
        return CommonTransferFactory.createLapInfo(runde, start, end, time, heartBeat, pace);
    }

    private static boolean isFirstPoint(final List<ITrackPointProperty> points, final long initTime) {
        return initTime == 0 && points.size() > 1;
    }
}
