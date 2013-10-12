package ch.opentrainingcenter.model.strecke;

import java.util.Comparator;

public class StreckeModelComparator implements Comparator<StreckeModel> {

    @Override
    public int compare(final StreckeModel a, final StreckeModel b) {
        return Integer.valueOf(a.getId()).compareTo(Integer.valueOf(b.getId()));
    }
}
