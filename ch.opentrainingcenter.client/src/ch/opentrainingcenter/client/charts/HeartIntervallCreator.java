package ch.opentrainingcenter.client.charts;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.jface.preference.IPreferenceStore;
import org.jfree.chart.plot.IntervalMarker;
import org.jfree.ui.RectangleAnchor;
import org.jfree.ui.TextAnchor;

import ch.opentrainingcenter.client.Messages;
import ch.opentrainingcenter.client.helper.ZoneHelper;
import ch.opentrainingcenter.transfer.IAthlete;

public final class HeartIntervallCreator {
    private final ZoneHelper zoneHelper;

    public HeartIntervallCreator(final IPreferenceStore store) {
        zoneHelper = new ZoneHelper(store);
    }

    public Map<ZoneHelper.Zone, IntervalMarker> createMarker(final IAthlete athlete) {
        final double start = 0d;
        final double rec = zoneHelper.getZonenWert(athlete, ZoneHelper.Zone.RECOM);
        final double ga1 = zoneHelper.getZonenWert(athlete, ZoneHelper.Zone.GA1);
        final double ga12 = zoneHelper.getZonenWert(athlete, ZoneHelper.Zone.GA12);
        final double ga2 = zoneHelper.getZonenWert(athlete, ZoneHelper.Zone.GA2);
        final double wsa = zoneHelper.getZonenWert(athlete, ZoneHelper.Zone.WSA);

        final Map<ZoneHelper.Zone, IntervalMarker> result = new HashMap<ZoneHelper.Zone, IntervalMarker>();

        final IntervalMarker recom = new IntervalMarker(start, rec);
        recom.setLabel(Messages.HeartIntervallCreator_0);
        recom.setLabelAnchor(RectangleAnchor.LEFT);
        recom.setLabelTextAnchor(TextAnchor.CENTER_LEFT);
        recom.setPaint(zoneHelper.getZonenFarbe(ZoneHelper.Zone.RECOM));
        result.put(ZoneHelper.Zone.RECOM, recom);

        final IntervalMarker grundlage1 = new IntervalMarker(rec, ga1);
        grundlage1.setLabel(Messages.HeartIntervallCreator_1);
        grundlage1.setLabelAnchor(RectangleAnchor.LEFT);
        grundlage1.setLabelTextAnchor(TextAnchor.CENTER_LEFT);
        grundlage1.setPaint(zoneHelper.getZonenFarbe(ZoneHelper.Zone.GA1));
        result.put(ZoneHelper.Zone.GA1, grundlage1);

        final IntervalMarker grundlage12 = new IntervalMarker(ga1, ga12);
        grundlage12.setLabel(Messages.HeartIntervallCreator_2);
        grundlage12.setLabelAnchor(RectangleAnchor.LEFT);
        grundlage12.setLabelTextAnchor(TextAnchor.CENTER_LEFT);
        grundlage12.setPaint(zoneHelper.getZonenFarbe(ZoneHelper.Zone.GA12));
        result.put(ZoneHelper.Zone.GA12, grundlage12);

        final IntervalMarker grundlage2 = new IntervalMarker(ga12, ga2);
        grundlage2.setLabel(Messages.HeartIntervallCreator_3);
        grundlage2.setLabelAnchor(RectangleAnchor.LEFT);
        grundlage2.setLabelTextAnchor(TextAnchor.CENTER_LEFT);
        grundlage2.setPaint(zoneHelper.getZonenFarbe(ZoneHelper.Zone.GA2));
        result.put(ZoneHelper.Zone.GA2, grundlage2);

        final IntervalMarker wettkampf = new IntervalMarker(ga2, wsa * 1.1);
        wettkampf.setLabel(Messages.HeartIntervallCreator_4);
        wettkampf.setLabelAnchor(RectangleAnchor.LEFT);
        wettkampf.setLabelTextAnchor(TextAnchor.CENTER_LEFT);
        wettkampf.setPaint(zoneHelper.getZonenFarbe(ZoneHelper.Zone.WSA));
        result.put(ZoneHelper.Zone.WSA, wettkampf);

        return result;
    }

}
