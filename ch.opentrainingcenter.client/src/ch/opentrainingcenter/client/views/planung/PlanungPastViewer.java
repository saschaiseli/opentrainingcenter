package ch.opentrainingcenter.client.views.planung;

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

import ch.opentrainingcenter.client.helper.PlanungWocheComparator;
import ch.opentrainingcenter.client.model.planing.IPastPlanung;
import ch.opentrainingcenter.client.model.planing.IPastPlanungModel;
import ch.opentrainingcenter.client.model.planing.impl.KwJahrKey;
import ch.opentrainingcenter.client.model.planing.impl.PastPlanungModel;
import ch.opentrainingcenter.client.views.ApplicationContext;
import ch.opentrainingcenter.db.DatabaseAccessFactory;
import ch.opentrainingcenter.db.IDatabaseAccess;
import ch.opentrainingcenter.transfer.IAthlete;
import ch.opentrainingcenter.transfer.IImported;
import ch.opentrainingcenter.transfer.IPlanungWoche;

public class PlanungPastViewer {

    private TableViewer viewer;

    void createViewer(final Composite parent) {
        viewer = new TableViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION | SWT.BORDER);
        createColumns();
        final Table table = viewer.getTable();
        table.setHeaderVisible(true);
        table.setLinesVisible(true);

        viewer.setContentProvider(new ArrayContentProvider());

        final MenuManager menuManager = new MenuManager("KontextMenu"); //$NON-NLS-1$
        table.setMenu(menuManager.createContextMenu(table));

        final IDatabaseAccess db = DatabaseAccessFactory.getDatabaseAccess();
        final IAthlete athlete = ApplicationContext.getApplicationContext().getAthlete();
        final List<IPlanungWoche> planungsWoche = db.getPlanungsWoche(athlete);
        Collections.sort(planungsWoche, new PlanungWocheComparator());

        final List<IImported> allImported = db.getAllImported(athlete);
        final DateTime dt = new DateTime();
        final KwJahrKey now = new KwJahrKey(dt.getYear(), dt.getWeekOfWeekyear());
        final IPastPlanungModel model = new PastPlanungModel(planungsWoche, allImported, now);

        viewer.setInput(model.getPastPlanungen());

        // Layout the viewer
        final GridData gridData = new GridData();
        gridData.verticalAlignment = GridData.FILL;
        gridData.horizontalSpan = 2;
        gridData.grabExcessHorizontalSpace = true;
        gridData.grabExcessVerticalSpace = true;
        gridData.horizontalAlignment = GridData.FILL;
        viewer.getControl().setLayoutData(gridData);

    }

    // This will create the columns for the table
    private void createColumns() {
        final String[] titles = { "Jahr", "KW", "Km/Woche geplant", "Km/Woche effektiv", "Langer Lauf", "Langer Lauf effektiv", "Intervall",
                "Intervall effektiv" };
        final int[] bounds = { 50, 50, 50, 50, 50, 50, 50, 50 };

        // Jahr
        TableViewerColumn col = createTableViewerColumn(titles[0], bounds[0]);
        col.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public String getText(final Object element) {
                final IPastPlanung woche = (IPastPlanung) element;
                return String.valueOf(woche.getPlanung().getJahr());
            }
        });

        // Kw
        col = createTableViewerColumn(titles[1], bounds[1]);
        col.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public String getText(final Object element) {
                final IPastPlanung woche = (IPastPlanung) element;
                return String.valueOf(woche.getPlanung().getKw());
            }
        });

        // Km pro woche
        col = createTableViewerColumn(titles[2], bounds[2]);
        col.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public String getText(final Object element) {
                final IPastPlanung woche = (IPastPlanung) element;
                return String.valueOf(woche.getPlanung().getKmProWoche());
            }
        });

        // Km pro woche effektiv
        col = createTableViewerColumn(titles[3], bounds[3]);
        col.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public String getText(final Object element) {
                final IPastPlanung woche = (IPastPlanung) element;
                return String.valueOf(woche.getKmEffective());
            }
        });

        // Langer Lauf
        col = createTableViewerColumn(titles[4], bounds[4]);
        col.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public String getText(final Object element) {
                final IPastPlanung woche = (IPastPlanung) element;
                return String.valueOf(woche.getPlanung().getLangerLauf());
            }
        });

        // Langer Lauf effektiv
        col = createTableViewerColumn(titles[5], bounds[5]);
        col.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public String getText(final Object element) {
                final IPastPlanung woche = (IPastPlanung) element;
                return String.valueOf(woche.getLangerLaufEffective());
            }
        });
        // Intervall
        col = createTableViewerColumn(titles[6], bounds[6]);
        col.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public String getText(final Object element) {
                final IPastPlanung woche = (IPastPlanung) element;
                return String.valueOf(woche.getPlanung().isInterval());
            }
        });

        // Intervall effektiv
        col = createTableViewerColumn(titles[7], bounds[7]);
        col.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public String getText(final Object element) {
                final IPastPlanung woche = (IPastPlanung) element;
                return String.valueOf(woche.hasInterval());
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
