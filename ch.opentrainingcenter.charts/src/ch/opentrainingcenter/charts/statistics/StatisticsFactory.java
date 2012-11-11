package ch.opentrainingcenter.charts.statistics;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;

import ch.opentrainingcenter.charts.statistics.internal.GewichtChart;
import ch.opentrainingcenter.charts.statistics.internal.RuhePulsChart;
import ch.opentrainingcenter.core.cache.AbstractCache;
import ch.opentrainingcenter.model.navigation.ConcreteHealth;

public class StatisticsFactory {

    private final AbstractCache<Integer, ConcreteHealth> cache;
    private final IPreferenceStore store;

    public StatisticsFactory(final AbstractCache<Integer, ConcreteHealth> cache, final IPreferenceStore store) {
        this.cache = cache;
        this.store = store;
    }

    public void addRuhePulsChart(final TabFolder tabs) {
        final RuhePulsChart viewer = new RuhePulsChart(cache, store);
        final TabItem item = new TabItem(tabs, SWT.PUSH);
        item.setText(viewer.getTabName());

        viewer.createPartControl(tabs);

        item.setControl(viewer.getControl());
    }

    public void addRuheGewichtChart(final TabFolder tabs) {
        final GewichtChart viewer = new GewichtChart(cache, store);
        final TabItem item = new TabItem(tabs, SWT.PUSH);
        item.setText(viewer.getTabName());

        viewer.createPartControl(tabs);

        item.setControl(viewer.getControl());
    }

}
