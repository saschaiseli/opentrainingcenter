package ch.opentrainingcenter.model.planing.internal;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.log4j.Logger;

import ch.opentrainingcenter.model.ModelFactory;
import ch.opentrainingcenter.model.planing.IPlanungModel;
import ch.opentrainingcenter.model.planing.IPlanungWocheModel;
import ch.opentrainingcenter.model.planing.KwJahrKey;
import ch.opentrainingcenter.transfer.IAthlete;

public class PlanungWocheModel implements IPlanungWocheModel {
    private static final Logger LOG = Logger.getLogger(PlanungWocheModel.class);
    private final Map<KwJahrKey, IPlanungModel> jahresplanung = new TreeMap<KwJahrKey, IPlanungModel>();

    private final int anzahl;
    private final IAthlete athlete;
    private final int jahr;

    public PlanungWocheModel(final List<IPlanungModel> planungen, final IAthlete athlete, final int jahr, final int kwStart, final int anzahl) {
        this.athlete = athlete;
        this.anzahl = anzahl;
        if (planungen == null || planungen.isEmpty()) {
            this.jahr = jahr;
            populate(kwStart, anzahl);
        } else {
            final IPlanungModel model = planungen.get(0);
            this.jahr = model.getJahr();
            populate(planungen);
        }
    }

    private void populate(final int start, final int count) {
        int kw = start;
        int j = jahr;
        for (int i = start; i < start + count; i++) {
            if (i == 53) {
                j++;
                kw = 1;
            }
            final PlanungModel pl = ModelFactory.createEmptyPlanungModel(athlete, j, kw);
            LOG.info(pl);
            jahresplanung.put(new KwJahrKey(j, kw), pl);
            kw++;
        }
    }

    private void populate(final List<IPlanungModel> planungen) {
        KwJahrKey key = null;
        for (final IPlanungModel woche : planungen) {
            final int kw = woche.getKw();
            final int j = woche.getJahr();
            key = new KwJahrKey(j, kw);
            jahresplanung.put(key, ModelFactory.createPlanungModel(athlete, j, kw, woche.getKmProWoche(), woche.isInterval(), woche.getLangerLauf()));
        }
        if (jahresplanung.size() != anzahl && key != null) {
            // noch mit leeren auffüllen
            populate(key.getKw() + 1, anzahl - jahresplanung.size());
        }
    }

    @Override
    public void addOrUpdate(final IPlanungModel woche) {
        final KwJahrKey key = new KwJahrKey(woche.getJahr(), woche.getKw());
        jahresplanung.put(key, woche);
    }

    @Override
    public IPlanungModel getPlanung(final int j, final int k) {
        final KwJahrKey key = new KwJahrKey(j, k);
        final IPlanungModel result = jahresplanung.get(key);
        return result;
    }

    @Override
    public int size() {
        return jahresplanung.size();
    }

    @Override
    public Iterator<IPlanungModel> iterator() {
        return jahresplanung.values().iterator();
    }
}
