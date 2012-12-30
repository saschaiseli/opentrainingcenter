package ch.opentrainingcenter.client.views.planung;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.joda.time.DateTime;

import ch.opentrainingcenter.client.views.ApplicationContext;
import ch.opentrainingcenter.core.db.DatabaseAccessFactory;
import ch.opentrainingcenter.model.planing.KwJahrKey;
import ch.opentrainingcenter.model.planing.PlanungWocheComparator;
import ch.opentrainingcenter.transfer.IPlanungWoche;

public class PlanungFutureViewer {
    private TableViewer viewer;

    void createViewer(final Composite parent) {
        viewer = new TableViewer(parent, SWT.SINGLE | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION | SWT.BORDER);
        createColumns();
        final Table table = viewer.getTable();
        table.setHeaderVisible(true);
        table.setLinesVisible(true);

        viewer.setContentProvider(new ArrayContentProvider());

        final MenuManager menuManager = new MenuManager("KontextMenu"); //$NON-NLS-1$
        table.setMenu(menuManager.createContextMenu(table));

        final List<IPlanungWoche> all = DatabaseAccessFactory.getDatabaseAccess().getPlanungsWoche(ApplicationContext.getApplicationContext().getAthlete());
        final DateTime dt = new DateTime();
        final KwJahrKey now = new KwJahrKey(dt.getYear(), dt.getWeekOfWeekyear());
        final List<IPlanungWoche> planungsWoche = new ArrayList<IPlanungWoche>();
        for (final IPlanungWoche woche : all) {
            final KwJahrKey kwJahrKey = new KwJahrKey(woche.getJahr(), woche.getKw());
            if (now.compareTo(kwJahrKey) < 0) {
                planungsWoche.add(woche);
            }
        }
        Collections.sort(planungsWoche, new PlanungWocheComparator(false));
        viewer.setInput(planungsWoche);

        final GridData gridData = new GridData();
        // gridData.verticalAlignment = GridData.FILL;
        gridData.horizontalSpan = 2;
        gridData.grabExcessHorizontalSpace = false;
        gridData.grabExcessVerticalSpace = false;
        gridData.horizontalAlignment = GridData.FILL;
        viewer.getControl().setLayoutData(gridData);

    }

    // This will create the columns for the table
    private void createColumns() {
        final String[] titles = { "Jahr", "KW", "Km/Woche", "Langer Lauf", "Intervall" };
        final int[] bounds = { 50, 40, 100, 100, 50 };

        // Jahr
        TableViewerColumn col = createTableViewerColumn(titles[0], bounds[0]);
        col.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public String getText(final Object element) {
                final IPlanungWoche woche = (IPlanungWoche) element;
                return String.valueOf(woche.getJahr());
            }
        });

        // Kw
        col = createTableViewerColumn(titles[1], bounds[1]);
        col.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public String getText(final Object element) {
                final IPlanungWoche woche = (IPlanungWoche) element;
                return String.valueOf(woche.getKw());
            }
        });

        // Km pro woche
        col = createTableViewerColumn(titles[2], bounds[2]);
        col.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public String getText(final Object element) {
                final IPlanungWoche woche = (IPlanungWoche) element;
                return String.valueOf(woche.getKmProWoche());
            }
        });

        // Langer Lauf
        col = createTableViewerColumn(titles[3], bounds[3]);
        col.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public String getText(final Object element) {
                final IPlanungWoche woche = (IPlanungWoche) element;
                return String.valueOf(woche.getLangerLauf());
            }
        });

        // Intervall
        col = createTableViewerColumn(titles[4], bounds[4]);
        col.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public String getText(final Object element) {
                final IPlanungWoche woche = (IPlanungWoche) element;
                return String.valueOf(woche.isInterval());
            }
        });

    }

    private TableViewerColumn createTableViewerColumn(final String title, final int bound) {
        final TableViewerColumn viewerColumn = new TableViewerColumn(viewer, SWT.NONE);
        final TableColumn column = viewerColumn.getColumn();
        column.setText(title);
        column.setWidth(bound);
        column.setResizable(true);
        column.setMoveable(true);
        return viewerColumn;
    }
}
