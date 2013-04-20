package ch.opentrainingcenter.importer.fitnesslog.converter;

import ch.opentrainingcenter.importer.fitnesslog.model.FitnessWorkbook;
import ch.opentrainingcenter.transfer.ITraining;

public class ConvertWorkbook2Tcx {

    // private final ActivityLapConverter lapConverter = new
    // ActivityLapConverter();

    public ITraining convert(final FitnessWorkbook workbook) {

        // final List<AthleteLog> athleteLog = workbook.getAthleteLog();
        // for (final AthleteLog log : athleteLog) {
        // nur ein athlete
        // final List<Activity> activities =
        // workbook.getAthleteLog().get(0).getActivity();
        // for (final Activity activity : activities) {
        // final ActivityT aT = new ActivityT();
        //
        // aT.setSport(SportT.RUNNING);
        // final XMLGregorianCalendar startTime = activity.getStartTime();
        //
        // aT.setId(startTime);
        //
        // final Laps laps = activity.getLaps();
        // ActivityLapT converted = null;
        //
        // // da in fitnesslog kein mapping von lap auf tracks sind, werden
        // // einfach alle trackpoints der ersten runde zugewiesen
        // final List<Lap> singleLap = laps.getLap();
        // if (singleLap != null && !singleLap.isEmpty()) {
        // converted = lapConverter.convert(singleLap.get(0));
        // }
        //
        // // wenn ein lap da ist, werden die points einfach zu der ersten
        // // hinzugefügt
        // if (converted != null) {
        // final List<TrackT> tcxTracks = converted.getTrack();
        // final Track track = activity.getTrack();
        // if (track == null) {
        // continue;
        // }
        // final List<Pt> points = track.getPt();
        // final TrackT tcxTrack = new TrackT();
        // final TrackpointConverter trackPointConverter = new
        // TrackpointConverter(startTime);
        // Pt previousPoint = null;
        // for (final Pt pt : points) {
        // final TrackpointT tcxTrackPoint =
        // trackPointConverter.convert(previousPoint, pt);
        // tcxTrack.getTrackpoint().add(tcxTrackPoint);
        // previousPoint = pt;
        // previousPoint.setDist(BigDecimal.valueOf(tcxTrackPoint.getDistanceMeters()));
        // }
        // tcxTracks.add(tcxTrack);
        // }
        // aT.getLap().add(converted);
        // activityList.getActivity().add(aT);
        // }
        //
        // // }
        // database.setActivities(activityList);
        // return database;
        return null;
    }
}
