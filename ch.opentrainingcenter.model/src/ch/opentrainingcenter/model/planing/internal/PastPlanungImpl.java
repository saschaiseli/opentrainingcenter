package ch.opentrainingcenter.model.planing.internal;

import java.util.List;

import ch.opentrainingcenter.model.planing.IPastPlanung;
import ch.opentrainingcenter.model.planing.PlanungStatus;
import ch.opentrainingcenter.transfer.IImported;
import ch.opentrainingcenter.transfer.IPlanungWoche;
import ch.opentrainingcenter.transfer.ITrainingType;

public class PastPlanungImpl implements IPastPlanung {

    private final IPlanungWoche planung;
    private final int kmEffective;
    private final int kmLangerLaufEffective;
    private final boolean interval;

    public PastPlanungImpl(final IPlanungWoche planung, final List<IImported> effective) {
        this.planung = planung;
        if (effective == null || effective.isEmpty()) {
            kmEffective = 0;
            kmLangerLaufEffective = 0;
            interval = false;
        } else {
            final int[] analyse = analyse(effective);
            kmEffective = analyse[0];
            kmLangerLaufEffective = analyse[1];
            interval = analyse[2] == 1 ? true : false;
        }

    }

    private int[] analyse(final List<IImported> effective) {
        final int[] result = new int[3];
        double km = 0;
        int longest = 0;
        boolean inter = false;
        for (final IImported record : effective) {
            final double kmTotal = record.getTraining().getLaengeInMeter() / 1000;
            if (longest < kmTotal) {
                longest = (int) kmTotal;
            }
            km += kmTotal;
            final ITrainingType type = record.getTrainingType();
            if (isInterval(type.getId())) {
                inter = true;
            }
        }
        result[0] = (int) km;
        result[1] = longest;
        result[2] = inter ? 1 : 0;
        return result;
    }

    // 1 --> Extensives 2 ist intensives intervall
    private boolean isInterval(final int id) {
        final boolean ext = 1 == id;// RunType.EXT_INTERVALL.equals(RunType.getRunType(id));
        final boolean in = 2 == id;// RunType.INT_INTERVALL.equals(RunType.getRunType(id));
        return ext || in;
    }

    @Override
    public IPlanungWoche getPlanung() {
        return planung;
    }

    @Override
    public int getKmEffective() {
        return kmEffective;
    }

    @Override
    public int getLangerLaufEffective() {
        return kmLangerLaufEffective;
    }

    @Override
    public boolean hasInterval() {
        return interval;
    }

    @Override
    public PlanungStatus isSuccess() {
        PlanungStatus status = PlanungStatus.NICHT_ERFOLGREICH;
        if (planung.getKmProWoche() <= 0) {
            status = PlanungStatus.UNBEKANNT;
        } else {
            final boolean isInter = interval || interval && planung.isInterval() || !planung.isInterval() && !interval;
            if (kmEffective >= planung.getKmProWoche() && kmLangerLaufEffective >= planung.getLangerLauf() && isInter) {
                status = PlanungStatus.ERFOLGREICH;
            }
        }
        return status;
    }

}
