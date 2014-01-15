package ch.opentrainingcenter.charts.bar;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;

import ch.opentrainingcenter.charts.bar.internal.OTCBarChartViewer;
import ch.opentrainingcenter.charts.single.ChartSerieType;
import ch.opentrainingcenter.transfer.IAthlete;

public class BarChartFactory {

    private final IPreferenceStore store;
    private final IAthlete athlete;

    public BarChartFactory(final IPreferenceStore store, final IAthlete athlete) {
        this.store = store;
        this.athlete = athlete;
    }

    public void addItem(final TabFolder tabs, final ChartSerieType type) {
        final TabItem item = new TabItem(tabs, SWT.PUSH);
        item.setText(type.getName());
        final OTCBarChartViewer viewer = new OTCBarChartViewer(tabs, type, store, athlete);
        item.setControl(viewer.getControl());
    }

}
