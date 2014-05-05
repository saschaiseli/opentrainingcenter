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
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.PlatformUI;
import org.joda.time.DateTime;

import ch.opentrainingcenter.client.views.ApplicationContext;
import ch.opentrainingcenter.core.db.IDatabaseAccess;
import ch.opentrainingcenter.core.helper.ColorFromPreferenceHelper;
import ch.opentrainingcenter.core.service.IDatabaseService;
import ch.opentrainingcenter.i18n.Messages;
import ch.opentrainingcenter.model.ModelFactory;
import ch.opentrainingcenter.model.planing.IPastPlanung;
import ch.opentrainingcenter.model.planing.IPastPlanungModel;
import ch.opentrainingcenter.model.planing.KwJahrKey;
import ch.opentrainingcenter.model.planing.PastPlanungSupport;
import ch.opentrainingcenter.model.planing.PlanungStatus;
import ch.opentrainingcenter.model.planing.PlanungWocheComparator;
import ch.opentrainingcenter.transfer.IAthlete;
import ch.opentrainingcenter.transfer.IPlanungWoche;
import ch.opentrainingcenter.transfer.ITraining;

public class PlanungPastViewer {

    private TableViewer viewer;
    private final IPreferenceStore store;
    private final IDatabaseAccess databaseAccess;

    public PlanungPastViewer(final IPreferenceStore store) {
        this.store = store;
        final IDatabaseService service = (IDatabaseService) PlatformUI.getWorkbench().getService(IDatabaseService.class);
        databaseAccess = service.getDatabaseAccess();
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

        // Layout the viewer
        final GridData gridData = new GridData();
        gridData.verticalAlignment = GridData.FILL;
        gridData.horizontalSpan = 2;
        gridData.grabExcessHorizontalSpace = true;
        gridData.grabExcessVerticalSpace = true;
        gridData.horizontalAlignment = GridData.FILL;
        // load data async
        loadData();
        viewer.getControl().setLayoutData(gridData);
    }

    private void loadData() {

        Display.getDefault().asyncExec(new Runnable() {

            @Override
            public void run() {
                // return
                final IAthlete athlete = ApplicationContext.getApplicationContext().getAthlete();
                final List<IPlanungWoche> planungsWoche = databaseAccess.getPlanungsWoche(athlete);

                Collections.sort(planungsWoche, new PlanungWocheComparator());

                final List<ITraining> allImported = databaseAccess.getAllTrainings(athlete);
                final DateTime dt = new DateTime();
                final KwJahrKey now = new KwJahrKey(dt.getYear(), dt.getWeekOfWeekyear());
                final IPastPlanungModel model = ModelFactory.createPastPlanungModel(planungsWoche, allImported, now);
                viewer.setInput(model.getPastPlanungen());
            }
        });
    }

    // This will create the columns for the table
    private void createColumns() {

        final String[] titles = { Messages.PlanungPastViewer_0, Messages.PlanungPastViewer_1, Messages.PlanungPastViewer_2, Messages.PlanungPastViewer_3,
                Messages.PlanungPastViewer_4, Messages.PlanungPastViewer_5, Messages.PlanungPastViewer_6, Messages.PlanungPastViewer_7,
                Messages.PlanungPastViewer_8, "" }; //$NON-NLS-1$
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
                return ColorFromPreferenceHelper.getSwtColor(store, woche.isSuccess().getColorPreference());
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
                return ColorFromPreferenceHelper.getSwtColor(store, woche.isSuccess().getColorPreference());
            }
        });

        // Km pro woche
        col = createTableViewerColumn(titles[2], bounds[2]);
        col.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public String getText(final Object element) {
                return PastPlanungSupport.getKmProWoche((IPastPlanung) element);
            }

        });

        // Langer Lauf
        col = createTableViewerColumn(titles[3], bounds[3]);
        col.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public String getText(final Object element) {
                return PastPlanungSupport.getLangerLauf((IPastPlanung) element);
            }
        });

        // Intervall
        col = createTableViewerColumn(titles[4], bounds[4]);
        col.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public String getText(final Object element) {
                return PastPlanungSupport.getIntervall((IPastPlanung) element);
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
                final String color;
                if (woche.getPlanung().getKmProWoche() <= 0) {
                    color = PlanungStatus.UNBEKANNT.getColorPreference();
                } else {
                    color = getStatus(woche.getPlanung().getKmProWoche(), woche.getKmEffective()).getColorPreference();
                }
                return ColorFromPreferenceHelper.getSwtColor(store, color);
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
                final String color;
                if (woche.getPlanung().getKmProWoche() <= 0) {
                    color = PlanungStatus.UNBEKANNT.getColorPreference();
                } else {
                    color = getStatus(woche.getPlanung().getLangerLauf(), woche.getLangerLaufEffective()).getColorPreference();
                }
                return ColorFromPreferenceHelper.getSwtColor(store, color);
            }
        });

        // Intervall effektiv
        col = createTableViewerColumn(titles[8], bounds[8]);
        col.setLabelProvider(new ColumnLabelProvider() {

            @Override
            public String getText(final Object element) {
                final IPastPlanung woche = (IPastPlanung) element;
                if (woche.hasInterval()) {
                    return Messages.Common_YES;
                } else {
                    return Messages.Common_NO;
                }
            }

            @Override
            public Color getBackground(final Object element) {
                final IPastPlanung woche = (IPastPlanung) element;
                final boolean isInter = woche.getPlanung().isInterval();
                final boolean hasInter = woche.hasInterval();
                final String color;
                if (woche.getPlanung().getKmProWoche() <= 0) {
                    color = PlanungStatus.UNBEKANNT.getColorPreference();
                } else {
                    if (hasInter || (isInter && hasInter) || (!isInter && !hasInter)) {
                        color = PlanungStatus.ERFOLGREICH.getColorPreference();
                    } else {
                        color = PlanungStatus.NICHT_ERFOLGREICH.getColorPreference();
                    }
                }
                return ColorFromPreferenceHelper.getSwtColor(store, color);
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

    /**
     * Nicht erfolgreich / erfolgreich (unbekannt muss anders detektiert werden)
     */
    private PlanungStatus getStatus(final int ziel, final int effective) {
        PlanungStatus status = PlanungStatus.NICHT_ERFOLGREICH;
        if (ziel <= effective) {
            status = PlanungStatus.ERFOLGREICH;
        }
        return status;
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
