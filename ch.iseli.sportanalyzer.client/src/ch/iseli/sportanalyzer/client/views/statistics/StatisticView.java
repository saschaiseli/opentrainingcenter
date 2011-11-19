package ch.iseli.sportanalyzer.client.views.statistics;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.ui.part.ViewPart;

import ch.iseli.sportanalyzer.client.charts.ChartSerieType;
import ch.iseli.sportanalyzer.client.charts.OTCBarChartViewer;

public class StatisticView extends ViewPart {

    public static final String ID = "ch.iseli.sportanalyzer.client.views.statistics.StatisticView";

    @Override
    public void createPartControl(Composite parent) {
        Composite container = new Composite(parent, SWT.NONE);
        container.setLayout(new FillLayout(SWT.FILL));

        TabFolder tabs = new TabFolder(container, SWT.BORDER);

        TabItem itemA = new TabItem(tabs, SWT.PUSH);
        itemA.setText(ChartSerieType.DAY.getName());

        final OTCBarChartViewer d1 = new OTCBarChartViewer(tabs, ChartSerieType.DAY);
        itemA.setControl(d1.getControl());

        TabItem itemWoche = new TabItem(tabs, SWT.PUSH);
        itemWoche.setText(ChartSerieType.WEEK.getName());

        final OTCBarChartViewer woche = new OTCBarChartViewer(tabs, ChartSerieType.WEEK);
        itemWoche.setControl(woche.getControl());
    }

    @Override
    public void setFocus() {
    }
}
