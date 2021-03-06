package ch.opentrainingcenter.model.navigation;

import java.util.Date;
import java.util.List;

import org.eclipse.osgi.util.NLS;

import ch.opentrainingcenter.core.helper.DistanceHelper;
import ch.opentrainingcenter.core.helper.TimeHelper;
import ch.opentrainingcenter.i18n.Messages;
import ch.opentrainingcenter.transfer.ILapInfo;
import ch.opentrainingcenter.transfer.IShoe;
import ch.opentrainingcenter.transfer.ITrackPointProperty;
import ch.opentrainingcenter.transfer.ITraining;
import ch.opentrainingcenter.transfer.IWeather;
import ch.opentrainingcenter.transfer.Sport;

public class ConcreteImported extends ImportedDecorator implements INavigationItem {

    public ConcreteImported(final ITraining training) {
        super(training);
    }

    public ITraining getTraining() {
        return training;
    }

    @Override
    public String getName() {
        return TimeHelper.convertDateToString(new Date(training.getDatum()));
    }

    @Override
    public String getTooltip() {
        final String tooltip;
        final String datum = TimeHelper.convertDateToString(new Date(training.getDatum()));
        final String distanz = DistanceHelper.roundDistanceFromMeterToKm(training.getLaengeInMeter());
        if (Sport.RUNNING.equals(training.getSport())) {
            if (training.getLapInfos().size() > 1) {
                tooltip = NLS.bind(Messages.NAVIGATION_TOOLTIP_RUNNING_RUNDEN, new Object[] { datum, distanz, Integer.valueOf(training.getLapInfos().size()) });
            } else {
                tooltip = NLS.bind(Messages.NAVIGATION_TOOLTIP_RUNNING, datum, distanz);
            }
        } else {
            tooltip = NLS.bind(Messages.NAVIGATION_TOOLTIP_OTHER, datum, distanz);
        }
        return tooltip;
    }

    @Override
    public Date getDate() {
        return new Date(training.getDatum());
    }

    @Override
    public String getImage() {
        if (Sport.BIKING.equals(training.getSport())) {
            return training.getSport().getImageIcon();
        } else {
            return training.getTrainingType().getImage();
        }
    }

    @Override
    public int compareTo(final INavigationItem o) {
        return o.getDate().compareTo(getDate());
    }

    @Override
    public String toString() {
        return "ConcreteImported [getName()=" + getName() + "]"; //$NON-NLS-1$ //$NON-NLS-2$
    }

    @Override
    public long getDatum() {
        return training.getDatum();
    }

    @Override
    public void setDatum(final long dateOfStart) {
        training.setDatum(dateOfStart);
    }

    @Override
    public Date getDateOfImport() {
        return training.getDateOfImport();
    }

    @Override
    public void setDateOfImport(final Date dateOfImport) {
        training.setDateOfImport(dateOfImport);
    }

    @Override
    public double getLaengeInMeter() {
        return training.getLaengeInMeter();
    }

    @Override
    public void setLaengeInMeter(final double laengeInMeter) {
        training.setLaengeInMeter(laengeInMeter);
    }

    @Override
    public int getAverageHeartBeat() {
        return training.getAverageHeartBeat();
    }

    @Override
    public void setAverageHeartBeat(final int avgHeartBeat) {
        training.setAverageHeartBeat(avgHeartBeat);
    }

    @Override
    public int getMaxHeartBeat() {
        return training.getMaxHeartBeat();
    }

    @Override
    public void setMaxHeartBeat(final int maxHeartBeat) {
        training.setMaxHeartBeat(maxHeartBeat);
    }

    @Override
    public double getMaxSpeed() {
        return training.getMaxSpeed();
    }

    @Override
    public void setMaxSpeed(final double maxSpeed) {
        training.setMaxSpeed(maxSpeed);
    }

    @Override
    public double getDauer() {
        return training.getDauer();
    }

    @Override
    public void setDauer(final double dauerInSekunden) {
        training.setDauer(dauerInSekunden);
    }

    @Override
    public String getNote() {
        return training.getNote();
    }

    @Override
    public void setNote(final String note) {
        training.setNote(note);
    }

    @Override
    public IWeather getWeather() {
        return training.getWeather();
    }

    @Override
    public void setWeather(final IWeather weather) {
        training.setWeather(weather);
    }

    @Override
    public List<ITrackPointProperty> getTrackPoints() {
        return training.getTrackPoints();
    }

    @Override
    public void setTrackPoints(final List<ITrackPointProperty> trackPoints) {
        training.setTrackPoints(trackPoints);
    }

    @Override
    public Integer getUpMeter() {
        return training.getUpMeter();
    }

    @Override
    public void setUpMeter(final Integer upMeter) {
        training.setUpMeter(upMeter);
    }

    @Override
    public Integer getDownMeter() {
        return training.getDownMeter();
    }

    @Override
    public void setDownMeter(final Integer downMeter) {
        training.setDownMeter(downMeter);
    }

    @Override
    public List<ILapInfo> getLapInfos() {
        return training.getLapInfos();
    }

    @Override
    public void setLapInfos(final List<ILapInfo> lapInfos) {
        training.setLapInfos(lapInfos);
    }

    @Override
    public Sport getSport() {
        return training.getSport();
    }

    @Override
    public void setSport(final Sport sport) {
        training.setSport(sport);
    }

    @Override
    public IShoe getShoe() {
        return training.getShoe();
    }

    @Override
    public void setShoe(final IShoe shoe) {
        training.setShoe(shoe);
    }

    @Override
    public Integer getGeoQuality() {
        return training.getGeoQuality();
    }

    @Override
    public void setGeoQuality(final Integer fehlerInProzent) {
        training.setGeoQuality(fehlerInProzent);

    }

    @Override
    public Integer getTrainingEffect() {
        return training.getTrainingEffect();
    }

    @Override
    public void setTrainingEffect(final Integer trainingEffect) {
        training.setTrainingEffect(trainingEffect);
    }
}
