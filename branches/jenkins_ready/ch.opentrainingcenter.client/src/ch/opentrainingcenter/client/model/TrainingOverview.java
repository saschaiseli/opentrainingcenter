package ch.opentrainingcenter.client.model;
// package ch.opentrainingcenter.client.model;
//
// import java.util.Date;
// import java.util.List;
//
// import javax.xml.datatype.XMLGregorianCalendar;
//
// import org.apache.log4j.Logger;
//
// import ch.opentrainingcenter.client.helper.DistanceHelper;
// import ch.opentrainingcenter.client.helper.TimeHelper;
// import ch.opentrainingcenter.tcx.ActivityLapT;
// import ch.opentrainingcenter.tcx.ActivityT;
// import ch.opentrainingcenter.tcx.IntensityT;
// import ch.opentrainingcenter.transfer.CommonTransferFactory;
// import ch.opentrainingcenter.transfer.ISimpleTraining;
// import ch.opentrainingcenter.transfer.ITrainingOverview;
//
// public class TrainingOverview implements ITrainingOverview {
//
// private static final Logger logger = Logger.getLogger(TrainingOverview.class);
//
// private final ActivityT activity;
//
// private String datum;
//
// private double distance;
//
// private String roundDistanceFromMeterToKm;
//
// private String avgHeartRate;
//
// private short maxHeartBeat;
//
// private double maximumSpeed;
//
// private String dauer;
//
// private double timeInSeconds;
//
// private String averageSpeed;
//
// private String maxPace;
//
// private Date dateOfStart;
//
// TrainingOverview(final ActivityT activity) {
// this.activity = activity;
// if (activity == null) {
//            throw new IllegalArgumentException("Trainingdatabase darf nicht null sein!"); //$NON-NLS-1$
// }
// // werte auslesen
// init();
// }
//
// private void init() {
// // datum
// final XMLGregorianCalendar date = activity.getId();
// datum = TimeHelper.convertGregorianDateToString(date, false);
// dateOfStart = date.toGregorianCalendar().getTime();
// // lauflänge
// final List<ActivityLapT> laps = activity.getLap();
// distance = 0.0;
// timeInSeconds = 0.0;
// short averageHeartRateBpm = 0;
// maxHeartBeat = 0;
// int lapWithCardio = 0;
// for (final ActivityLapT lap : laps) {
// if (IntensityT.ACTIVE.equals(lap.getIntensity())) {
// distance += lap.getDistanceMeters();
// if (lap.getMaximumSpeed() != null && maximumSpeed < lap.getMaximumSpeed()) {
// maximumSpeed = lap.getMaximumSpeed();
// }
// timeInSeconds += lap.getTotalTimeSeconds();
// if (!hasCardio(lap)) {
// continue;
// }
// lapWithCardio++;
// averageHeartRateBpm += lap.getAverageHeartRateBpm() != null ? lap.getAverageHeartRateBpm().getValue() : 0;
// if (maxHeartBeat < lap.getMaximumHeartRateBpm().getValue()) {
// maxHeartBeat = lap.getMaximumHeartRateBpm().getValue();
// }
// }
//            logger.debug("lap: " + lap.getIntensity() + " distance: " + distance); //$NON-NLS-1$//$NON-NLS-2$
// }
// // in kilometer
// roundDistanceFromMeterToKm = DistanceHelper.roundDistanceFromMeterToKm(distance);
// // durschnittliche herzfrequenz
// if (lapWithCardio > 0) {
// avgHeartRate = String.valueOf(averageHeartRateBpm / lapWithCardio);
// } else {
//            avgHeartRate = "-"; //$NON-NLS-1$
// }
// // dauer
// dauer = TimeHelper.convertSecondsToHumanReadableZeit(timeInSeconds);
// // durschnittliche geschwindigkeit
// averageSpeed = DistanceHelper.calculatePace(distance, timeInSeconds);
//
// maxPace = DistanceHelper.calculatePace(maximumSpeed);
// }
//
// private boolean hasCardio(final ActivityLapT lap) {
// return lap.getMaximumHeartRateBpm() != null;
// }
//
// @Override
// public String getDatum() {
// return datum;
// }
//
// @Override
// public String getLaengeInKilometer() {
// return roundDistanceFromMeterToKm;
// }
//
// @Override
// public double getLaengeInMeter() {
// return distance;
// }
//
// @Override
// public String getAverageHeartBeat() {
// return avgHeartRate;
// }
//
// @Override
// public String getMaxHeartBeat() {
// return String.valueOf(maxHeartBeat);
// }
//
// @Override
// public String getPace() {
// return averageSpeed;
// }
//
// @Override
// public String getMaxSpeed() {
// return maxPace;
// }
//
// @Override
// public String getDauer() {
// return dauer;
// }
//
// public Date getDate() {
// return dateOfStart;
// }
//
// @Override
// public double getDauerInSekunden() {
// return timeInSeconds;
// }
//
// @Override
// public void setDauerInSekunden(final double timeInSeconds) {
// this.timeInSeconds = timeInSeconds;
// }
//
// @Override
// public ISimpleTraining getTraining() {
// if (validAverageHeartRate()) {
// return CommonTransferFactory.createSimpleTraining(distance, timeInSeconds, dateOfStart, Integer.valueOf(avgHeartRate).intValue());
// } else {
// return CommonTransferFactory.createSimpleTraining(distance, timeInSeconds, dateOfStart, 0);
// }
// }
//
// private boolean validAverageHeartRate() {
//        return avgHeartRate != null && !avgHeartRate.contains("-"); //$NON-NLS-1$
// }
//
// @Override
// public void setId(final int id) {
// // TODO Auto-generated method stub
//
// }
//
// @Override
// public int getId() {
// // TODO Auto-generated method stub
// return 0;
// }
//
// @Override
// public void setDatum(final String datum) {
// // TODO Auto-generated method stub
//
// }
//
// @Override
// public void setDauer(final String dauer) {
// // TODO Auto-generated method stub
//
// }
//
// @Override
// public void setLaengeInKilometer(final String laenge) {
// // TODO Auto-generated method stub
//
// }
//
// @Override
// public void setLaengeInMeter(final double laengeInMeter) {
// // TODO Auto-generated method stub
//
// }
//
// @Override
// public void setAverageHeartBeat(final String avgHeartBeat) {
// // TODO Auto-generated method stub
//
// }
//
// @Override
// public void setMaxHeartBeat(final String maxHeartBeat) {
// // TODO Auto-generated method stub
//
// }
//
// @Override
// public void setPace(final String pace) {
// // TODO Auto-generated method stub
//
// }
//
// @Override
// public void setMaxSpeed(final String maxSpeed) {
// // TODO Auto-generated method stub
//
// }
//
// @Override
// public Date getDateOfStart() {
// // TODO Auto-generated method stub
// return null;
// }
//
// @Override
// public void setDateOfStart(final Date dateOfStart) {
// // TODO Auto-generated method stub
//
// }
// }
