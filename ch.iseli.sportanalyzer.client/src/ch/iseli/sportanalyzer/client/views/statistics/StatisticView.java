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
    public void createPartControl(final Composite parent) {
        final Composite container = new Composite(parent, SWT.NONE);
        container.setLayout(new FillLayout(SWT.FILL));

        final TabFolder tabs = new TabFolder(container, SWT.BORDER);

        addItem(tabs, ChartSerieType.DAY);
        addItem(tabs, ChartSerieType.WEEK);
        addItem(tabs, ChartSerieType.MONTH);
        addItem(tabs, ChartSerieType.YEAR);
    }

    private void addItem(final TabFolder tabs, final ChartSerieType type) {
        final TabItem item = new TabItem(tabs, SWT.PUSH);
        item.setText(type.getName());

        final OTCBarChartViewer viewer = new OTCBarChartViewer(tabs, type);
        item.setControl(viewer.getControl());
    }

    @Override
    public void setFocus() {
    }
}
