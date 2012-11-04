package ch.opentrainingcenter.client.model.planing.impl;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.log4j.Logger;

import ch.opentrainingcenter.client.model.planing.IPlanungWocheModel;
import ch.opentrainingcenter.transfer.CommonTransferFactory;
import ch.opentrainingcenter.transfer.IAthlete;
import ch.opentrainingcenter.transfer.IPlanungWoche;

public class PlanungWocheModel implements IPlanungWocheModel {
    private static final Logger LOG = Logger.getLogger(PlanungWocheModel.class);
    private final Map<KwJahrKey, IPlanungWoche> jahresplanung = new TreeMap<KwJahrKey, IPlanungWoche>();
    private final int kwStart;
    private final int anzahl;
    private final IAthlete athlete;
    private int jahr;

    public PlanungWocheModel(final List<IPlanungWoche> planungen, final IAthlete athlete, final int jahr, final int kwStart, final int anzahl) {
        this.athlete = athlete;
        this.jahr = jahr;
        this.kwStart = kwStart;
        this.anzahl = anzahl;
        if (planungen == null || planungen.isEmpty()) {
            populate();
        } else {
            populate(planungen);
        }
    }

    private void populate() {
        int kw = kwStart;
        for (int i = kwStart; i < kwStart + anzahl; i++) {
            if (i == 53) {
                jahr++;
                kw = 1;
            }
            final IPlanungWoche pl = CommonTransferFactory.createEmptyPlanungWoche(athlete, jahr, kw);
            LOG.info(pl);
            jahresplanung.put(new KwJahrKey(jahr, kw), pl);
            kw++;
        }
    }

    private void populate(final List<IPlanungWoche> planungen) {
        for (final IPlanungWoche woche : planungen) {
            final int kw = woche.getKw();
            final int j = woche.getJahr();
            jahresplanung.put(new KwJahrKey(j, kw), CommonTransferFactory.createPlanungWoche(athlete, j, kw, woche.getKmProWoche()));
        }
    }

    @Override
    public void addOrUpdate(final IPlanungWoche woche) {
        final KwJahrKey key = new KwJahrKey(woche.getJahr(), woche.getKw());
        jahresplanung.put(key, woche);
    }

    @Override
    public IPlanungWoche getPlanung(final int j, final int k) {
        final KwJahrKey key = new KwJahrKey(j, k);
        final IPlanungWoche result = jahresplanung.get(key);
        return result;
    }

    @Override
    public int size() {
        return jahresplanung.size();
    }

    @Override
    public Iterator<IPlanungWoche> iterator() {
        return jahresplanung.values().iterator();
    }
}
