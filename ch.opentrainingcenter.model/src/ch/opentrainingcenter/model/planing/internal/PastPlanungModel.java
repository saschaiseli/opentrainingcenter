package ch.opentrainingcenter.model.planing.internal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.joda.time.DateTime;

import ch.opentrainingcenter.core.helper.TimeHelper;
import ch.opentrainingcenter.model.planing.IPastPlanung;
import ch.opentrainingcenter.model.planing.IPastPlanungModel;
import ch.opentrainingcenter.model.planing.KwJahrKey;
import ch.opentrainingcenter.transfer.CommonTransferFactory;
import ch.opentrainingcenter.transfer.IAthlete;
import ch.opentrainingcenter.transfer.IPlanungWoche;
import ch.opentrainingcenter.transfer.ITraining;

public class PastPlanungModel implements IPastPlanungModel {

    private final List<IPastPlanung> pastPlanungen;
    private final KwJahrKey now;

    public PastPlanungModel(final List<IPlanungWoche> planungen, final List<ITraining> records, final KwJahrKey now) {
        this.now = now;
        final IAthlete athlete;
        if (hasElements(planungen)) {
            athlete = planungen.get(0).getAthlete();
        } else if (hasElements(records)) {
            athlete = records.get(0).getAthlete();
        } else {
            throw new IllegalArgumentException("Ohne Planung und Records kann das Model nicht erstellt werden"); //$NON-NLS-1$
        }
        pastPlanungen = new ArrayList<IPastPlanung>();
        final Map<KwJahrKey, List<ITraining>> recordMap = createRecordMap(records);
        final Map<KwJahrKey, IPlanungWoche> planungMap = createPlanungMap(planungen);
        mapRecordsToEmptyPlanung(athlete, recordMap, planungMap);
        mapPlanungToEmptyRecords(athlete, recordMap, planungMap);
        if (recordMap != null && planungMap != null) {
            mapPlanungToRecord(recordMap, planungMap);
        }
    }

    private Map<KwJahrKey, List<ITraining>> createRecordMap(final List<ITraining> records) {
        final Map<KwJahrKey, List<ITraining>> result = new HashMap<KwJahrKey, List<ITraining>>();
        if (records != null) {
            for (final ITraining record : records) {
                final DateTime dt = new DateTime(record.getDatum());

                final KwJahrKey key = new KwJahrKey(TimeHelper.getJahr(dt.toDate(), Locale.GERMAN), dt.getWeekOfWeekyear());
                if (!result.containsKey(key)) {
                    // noch kein ein wert vorhanden
                    result.put(key, new ArrayList<ITraining>());
                }
                result.get(key).add(record);
            }
        }
        return result;
    }

    private Map<KwJahrKey, IPlanungWoche> createPlanungMap(final List<IPlanungWoche> planungen) {
        final Map<KwJahrKey, IPlanungWoche> result = new HashMap<KwJahrKey, IPlanungWoche>();
        if (planungen != null) {
            for (final IPlanungWoche planung : planungen) {
                final KwJahrKey key = new KwJahrKey(planung.getJahr(), planung.getKw());
                result.put(key, planung);
            }
        }
        return result;
    }

    private void mapRecordsToEmptyPlanung(final IAthlete athlete, final Map<KwJahrKey, List<ITraining>> recordMap,
            final Map<KwJahrKey, IPlanungWoche> planungMap) {
        for (final Map.Entry<KwJahrKey, List<ITraining>> entry : recordMap.entrySet()) {
            final IPlanungWoche plan;
            final KwJahrKey key = entry.getKey();
            if (!planungMap.containsKey(key)) {
                plan = CommonTransferFactory.createIPlanungWocheEmpty(athlete, key.getJahr(), key.getKw());
                pastPlanungen.add(new PastPlanungImpl(plan, entry.getValue()));
            }
        }
    }

    private void mapPlanungToEmptyRecords(final IAthlete athlete, final Map<KwJahrKey, List<ITraining>> recordMap,
            final Map<KwJahrKey, IPlanungWoche> planungMap) {
        final KwJahrKeyComparator comparator = new KwJahrKeyComparator();
        for (final Map.Entry<KwJahrKey, IPlanungWoche> entry : planungMap.entrySet()) {
            final IPlanungWoche plan;
            final KwJahrKey key = entry.getKey();
            if (!recordMap.containsKey(key) && comparator.compare(key, now) <= 0) {
                plan = CommonTransferFactory.createIPlanungWocheEmpty(athlete, key.getJahr(), key.getKw());
                pastPlanungen.add(new PastPlanungImpl(plan, new ArrayList<ITraining>()));
            }
        }
    }

    private void mapPlanungToRecord(final Map<KwJahrKey, List<ITraining>> recordMap, final Map<KwJahrKey, IPlanungWoche> planungMap) {
        for (final Map.Entry<KwJahrKey, List<ITraining>> entry : recordMap.entrySet()) {
            final IPlanungWoche plan;
            final KwJahrKey key = entry.getKey();
            if (planungMap.containsKey(key)) {
                plan = planungMap.get(key);
                pastPlanungen.add(new PastPlanungImpl(plan, entry.getValue()));
            }
        }
    }

    private boolean hasElements(final List<?> list) {
        return list != null && !list.isEmpty();
    }

    @Override
    public List<IPastPlanung> getPastPlanungen() {
        Collections.sort(pastPlanungen, new PastPlanungComparator());
        return Collections.unmodifiableList(pastPlanungen);
    }
}
