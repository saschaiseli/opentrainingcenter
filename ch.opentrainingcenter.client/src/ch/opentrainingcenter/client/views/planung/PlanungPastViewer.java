package ch.opentrainingcenter.client.views.planung;

import java.util.Collections;
import java.util.List;

import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.joda.time.DateTime;

import ch.opentrainingcenter.client.views.ApplicationContext;
import ch.opentrainingcenter.core.PreferenceConstants;
import ch.opentrainingcenter.core.db.DatabaseAccessFactory;
import ch.opentrainingcenter.core.db.IDatabaseAccess;
import ch.opentrainingcenter.core.helper.ColorFromPreferenceHelper;
import ch.opentrainingcenter.model.ModelFactory;
import ch.opentrainingcenter.model.planing.IPastPlanung;
import ch.opentrainingcenter.model.planing.IPastPlanungModel;
import ch.opentrainingcenter.model.planing.KwJahrKey;
import ch.opentrainingcenter.model.planing.PlanungWocheComparator;
import ch.opentrainingcenter.transfer.IAthlete;
import ch.opentrainingcenter.transfer.IImported;
import ch.opentrainingcenter.transfer.IPlanungWoche;

public class PlanungPastViewer {

    private TableViewer viewer;
    private final Color erfuellt;
    private final Color nichtErfuellt;

    public PlanungPastViewer(final IPreferenceStore store) {
        erfuellt = ColorFromPreferenceHelper.getSwtColor(store, PreferenceConstants.ZIEL_ERFUELLT_COLOR);
        nichtErfuellt = ColorFromPreferenceHelper.getSwtColor(store, PreferenceConstants.ZIEL_NICHT_ERFUELLT_COLOR);
    }

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
        final IPastPlanungModel model = ModelFactory.createPastPlanungModel(planungsWoche, allImported, now);

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

        final String[] titles = { "Jahr", "KW", "Km/Woche", "Langer Lauf", "Intervall", "", "Km/Woche effektiv", "Langer Lauf effektiv", "Intervall effektiv",
                "" }; //$NON-NLS-1$
        final int[] bounds = { 50, 40, 100, 100, 65, 100, 150, 150, 150, 100 };

        // Jahr
        TableViewerColumn col = createTableViewerColumn(titles[0], bounds[0]);
        col.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public String getText(final Object element) {
                final IPastPlanung woche = (IPastPlanung) element;
                return String.valueOf(woche.getPlanung().getJahr());
            }

            @Override
            public Color getBackground(final Object element) {
                final IPastPlanung woche = (IPastPlanung) element;
                if (woche.isSuccess()) {
                    return erfuellt;
                } else {
                    return nichtErfuellt;
                }
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

            @Override
            public Color getBackground(final Object element) {
                final IPastPlanung woche = (IPastPlanung) element;
                if (woche.isSuccess()) {
                    return erfuellt;
                } else {
                    return nichtErfuellt;
                }
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

        // Langer Lauf
        col = createTableViewerColumn(titles[3], bounds[3]);
        col.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public String getText(final Object element) {
                final IPastPlanung woche = (IPastPlanung) element;
                return String.valueOf(woche.getPlanung().getLangerLauf());
            }
        });

        // Intervall
        col = createTableViewerColumn(titles[4], bounds[4]);
        col.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public String getText(final Object element) {
                final IPastPlanung woche = (IPastPlanung) element;
                return String.valueOf(woche.getPlanung().isInterval());
            }
        });

        // LEEEEEEEEEEEEEEEEEEEER
        col = createTableViewerColumn(titles[5], bounds[5]);
        col.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public String getText(final Object element) {
                return String.valueOf(""); //$NON-NLS-1$
            }
        });

        // Km pro woche effektiv
        col = createTableViewerColumn(titles[6], bounds[6]);
        col.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public String getText(final Object element) {
                final IPastPlanung woche = (IPastPlanung) element;
                return String.valueOf(woche.getKmEffective());
            }

            @Override
            public Color getBackground(final Object element) {
                final IPastPlanung woche = (IPastPlanung) element;
                if (woche.getPlanung().getKmProWoche() > woche.getKmEffective()) {
                    return nichtErfuellt;
                } else {
                    return erfuellt;
                }
            }
        });

        // Langer Lauf effektiv
        col = createTableViewerColumn(titles[7], bounds[7]);
        col.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public String getText(final Object element) {
                final IPastPlanung woche = (IPastPlanung) element;
                return String.valueOf(woche.getLangerLaufEffective());
            }

            @Override
            public Color getBackground(final Object element) {
                final IPastPlanung woche = (IPastPlanung) element;
                if (woche.getPlanung().getLangerLauf() > woche.getLangerLaufEffective()) {
                    return nichtErfuellt;
                } else {
                    return erfuellt;
                }
            }
        });

        // Intervall effektiv
        col = createTableViewerColumn(titles[8], bounds[8]);
        col.setLabelProvider(new ColumnLabelProvider() {

            @Override
            public String getText(final Object element) {
                final IPastPlanung woche = (IPastPlanung) element;
                return String.valueOf(woche.hasInterval());
            }

            @Override
            public Color getBackground(final Object element) {
                final IPastPlanung woche = (IPastPlanung) element;
                final boolean isInter = woche.getPlanung().isInterval();
                final boolean hasInter = woche.hasInterval();
                if (hasInter || (isInter && hasInter) || (!isInter && !hasInter)) {
                    return erfuellt;
                } else {
                    return nichtErfuellt;
                }
            }
        });
        // Intervall effektiv
        col = createTableViewerColumn(titles[9], bounds[9]);
        col.setLabelProvider(new ColumnLabelProvider() {

            @Override
            public String getText(final Object element) {
                return String.valueOf("      "); //$NON-NLS-1$
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
