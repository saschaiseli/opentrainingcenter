package ch.opentrainingcenter.client.views.statistics;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.ui.part.ViewPart;

import ch.opentrainingcenter.charts.bar.BarChartFactory;
import ch.opentrainingcenter.charts.statistics.StatisticsFactory;
import ch.opentrainingcenter.client.Activator;
import ch.opentrainingcenter.client.cache.HealthCache;
import ch.opentrainingcenter.client.views.ApplicationContext;
import ch.opentrainingcenter.core.helper.ChartSerieType;

public class StatisticView extends ViewPart {

    public static final String ID = "ch.opentrainingcenter.client.views.statistics.StatisticView"; //$NON-NLS-1$

    @Override
    public void createPartControl(final Composite parent) {
        final Composite container = new Composite(parent, SWT.NONE);
        container.setLayout(new FillLayout(SWT.FILL));

        final TabFolder tabs = new TabFolder(container, SWT.BORDER);

        final IPreferenceStore store = Activator.getDefault().getPreferenceStore();
        final BarChartFactory barChartFactory = new BarChartFactory(store, ApplicationContext.getApplicationContext().getAthlete());

        barChartFactory.addItem(tabs, ChartSerieType.DAY);
        barChartFactory.addItem(tabs, ChartSerieType.WEEK);
        barChartFactory.addItem(tabs, ChartSerieType.MONTH);
        barChartFactory.addItem(tabs, ChartSerieType.YEAR);

        final StatisticsFactory factory = new StatisticsFactory(HealthCache.getInstance(), store);

        factory.addRuhePulsChart(tabs);
        factory.addRuheGewichtChart(tabs);

        tabs.setSelection(1);
    }

    @Override
    public void setFocus() {
    }
}
