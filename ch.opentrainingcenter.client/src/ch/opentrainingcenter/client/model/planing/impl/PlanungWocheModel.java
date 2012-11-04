package ch.opentrainingcenter.client.model.planing.impl;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.log4j.Logger;

import ch.opentrainingcenter.client.model.ModelFactory;
import ch.opentrainingcenter.client.model.planing.IPlanungWocheModel;
import ch.opentrainingcenter.transfer.IAthlete;

public class PlanungWocheModel implements IPlanungWocheModel {
    private static final Logger LOG = Logger.getLogger(PlanungWocheModel.class);
    private final Map<KwJahrKey, PlanungModel> jahresplanung = new TreeMap<KwJahrKey, PlanungModel>();
    private final int kwStart;
    private final int anzahl;
    private final IAthlete athlete;
    private int jahr;

    public PlanungWocheModel(final List<PlanungModel> planungen, final IAthlete athlete, final int jahr, final int kwStart, final int anzahl) {
        this.athlete = athlete;
        this.jahr = jahr;
        this.kwStart = kwStart;
        this.anzahl = anzahl;
        if (planungen == null || planungen.isEmpty()) {
            populate(kwStart, anzahl);
        } else {
            populate(planungen);
        }
    }

    private void populate(final int start, final int count) {
        int kw = start;
        for (int i = start; i < start + count; i++) {
            if (i == 53) {
                jahr++;
                kw = 1;
            }
            final PlanungModel pl = ModelFactory.createEmptyPlanungModel(athlete, jahr, kw);
            LOG.info(pl);
            jahresplanung.put(new KwJahrKey(jahr, kw), pl);
            kw++;
        }
    }

    private void populate(final List<PlanungModel> planungen) {
        KwJahrKey key = null;
        for (final PlanungModel woche : planungen) {
            final int kw = woche.getKw();
            final int j = woche.getJahr();
            key = new KwJahrKey(j, kw);
            jahresplanung.put(key, ModelFactory.createPlanungModel(athlete, j, kw, woche.getKmProWoche(), woche.isInterval()));
        }
        if (jahresplanung.size() != anzahl && key != null) {
            // noch mit leeren auffüllen
            populate(key.getKw() + 1, anzahl - jahresplanung.size());
        }
    }

    @Override
    public void addOrUpdate(final PlanungModel woche) {
        final KwJahrKey key = new KwJahrKey(woche.getJahr(), woche.getKw());
        jahresplanung.put(key, woche);
    }

    @Override
    public PlanungModel getPlanung(final int j, final int k) {
        final KwJahrKey key = new KwJahrKey(j, k);
        final PlanungModel result = jahresplanung.get(key);
        return result;
    }

    @Override
    public int size() {
        return jahresplanung.size();
    }

    @Override
    public Iterator<PlanungModel> iterator() {
        return jahresplanung.values().iterator();
    }
}
