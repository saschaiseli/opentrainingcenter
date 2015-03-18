/**
 *    OpenTrainingCenter
 *
 *    Copyright (C) 2014 Sascha Iseli sascha.iseli(at)gmx.ch
 *
 *    This program is free software: you can redistribute it and/or modify
 *    it under the terms of the GNU General Public License as published by
 *    the Free Software Foundation, either version 3 of the License, or
 *    (at your option) any later version.
 *
 *    This program is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *    GNU General Public License for more details.
 *
 *    You should have received a copy of the GNU General Public License
 *    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package ch.opentrainingcenter.client.views.dialoge;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Scale;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;
import org.joda.time.DateTime;

import ch.opentrainingcenter.client.Activator;
import ch.opentrainingcenter.client.ui.open.OpenTrainingAction;
import ch.opentrainingcenter.client.views.ApplicationContext;
import ch.opentrainingcenter.client.views.IImageKeys;
import ch.opentrainingcenter.core.cache.RouteCache;
import ch.opentrainingcenter.core.db.CriteriaContainer;
import ch.opentrainingcenter.core.db.CriteriaFactory;
import ch.opentrainingcenter.core.helper.DistanceHelper;
import ch.opentrainingcenter.core.helper.TimeHelper;
import ch.opentrainingcenter.core.service.IDatabaseService;
import ch.opentrainingcenter.i18n.Messages;
import ch.opentrainingcenter.transfer.IRoute;
import ch.opentrainingcenter.transfer.ITraining;
import ch.opentrainingcenter.transfer.Sport;

/**
 * Dialog um Tracks zu suchen.
 */
public class SearchDialog extends TitleAreaDialog {

    private static final Logger LOGGER = Logger.getLogger(SearchDialog.class);
    // private final StreckeCache cacheStrecke = StreckeCache.getInstance();
    private final RouteCache cache = RouteCache.getInstance();
    private TableViewer viewer;

    private Scale scale;

    private Label scaleLabel;

    private Text beschreibungSearch;

    private final List<ITraining> trainings;

    private final IPreferenceStore store;

    private final boolean running, biking, other;

    private Button runButton;

    private Button bikeButton;

    private Button otherButton;

    private ComboViewer comboStrecke;

    private int referenzTrainingId;
    private final OpenTrainingAction action;
    private SearchDialogComparator comparator;

    public SearchDialog(final Shell parentShell) {
        super(parentShell);
        final IDatabaseService service = (IDatabaseService) PlatformUI.getWorkbench().getService(IDatabaseService.class);
        trainings = service.getDatabaseAccess().getAllTrainings(ApplicationContext.getApplicationContext().getAthlete());
        store = Activator.getDefault().getPreferenceStore();
        running = store.getBoolean(Sport.RUNNING.getMessage());
        biking = store.getBoolean(Sport.BIKING.getMessage());
        other = store.getBoolean(Sport.OTHER.getMessage());
        action = new OpenTrainingAction();
    }

    @Override
    protected Control createDialogArea(final Composite parent) {
        setTitle(Messages.SearchDialog_SEARCH);

        setMessage(Messages.SearchDialog_SEARCH_DESCRIPTION);
        setTitleImage(Activator.getImageDescriptor(IImageKeys.SEARCH_WIZ).createImage());
        final Label separator = new Label(parent, SWT.SEPARATOR | SWT.HORIZONTAL);
        GridDataFactory.fillDefaults().align(SWT.FILL, SWT.TOP).grab(true, false).applyTo(separator);

        final Composite search = new Composite(parent, SWT.NONE);

        GridLayoutFactory.swtDefaults().numColumns(3).applyTo(search);
        GridDataFactory.swtDefaults().align(SWT.FILL, SWT.FILL).grab(true, false).applyTo(search);

        final Label beschreibung = new Label(search, SWT.NONE);
        beschreibung.setText(Messages.SearchDialog_SEARCH_DESC_LABEL);
        GridDataFactory.swtDefaults().grab(false, false).applyTo(beschreibung);

        beschreibungSearch = new Text(search, SWT.NONE);
        GridDataFactory.swtDefaults().align(SWT.FILL, SWT.FILL).grab(true, false).span(2, 1).applyTo(beschreibungSearch);
        beschreibungSearch.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(final KeyEvent e) {
                update();
            }
        });

        final Label minDistanz = new Label(search, SWT.NONE);
        minDistanz.setText(Messages.SearchDialog_SEARCH_DIST_LABEL);
        GridDataFactory.swtDefaults().grab(false, false).applyTo(minDistanz);

        scale = new Scale(search, SWT.BORDER);
        GridDataFactory.swtDefaults().grab(true, false).align(SWT.FILL, SWT.FILL).applyTo(scale);

        scale.setMaximum(80);
        scale.setPageIncrement(5);
        scale.setSelection(0);
        scale.setIncrement(5);
        scale.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(final SelectionEvent e) {
                super.widgetSelected(e);
                update();

            }
        });

        scaleLabel = new Label(search, SWT.NONE);
        scaleLabel.setText(scale.getSelection() + Messages.SearchDialog_COMMON_KM);
        GridDataFactory.swtDefaults().grab(false, false).hint(50, 20).applyTo(scaleLabel);

        // sportart
        final Composite sport = new Composite(search, SWT.NONE);
        GridLayoutFactory.swtDefaults().numColumns(3).applyTo(sport);
        GridDataFactory.swtDefaults().grab(true, true).span(3, 1).align(SWT.FILL, SWT.FILL).applyTo(sport);
        runButton = new Button(sport, SWT.CHECK);
        runButton.setText(Sport.RUNNING.getTranslated());
        runButton.setVisible(running);
        runButton.setSelection(running);
        runButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(final SelectionEvent e) {
                super.widgetSelected(e);
                update();

            }
        });

        bikeButton = new Button(sport, SWT.CHECK);
        bikeButton.setText(Sport.BIKING.getTranslated());
        bikeButton.setVisible(biking);
        bikeButton.setSelection(biking);
        bikeButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(final SelectionEvent e) {
                super.widgetSelected(e);
                update();

            }
        });

        otherButton = new Button(sport, SWT.CHECK);
        otherButton.setText(Sport.OTHER.getTranslated());
        otherButton.setVisible(other);
        otherButton.setSelection(biking);
        otherButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(final SelectionEvent e) {
                super.widgetSelected(e);
                update();

            }
        });

        // Strecke
        final Composite strecke = new Composite(search, SWT.NONE);
        GridLayoutFactory.swtDefaults().numColumns(3).applyTo(strecke);
        final Label l = new Label(strecke, SWT.NONE);
        l.setText(Messages.STRECKE);
        GridDataFactory.swtDefaults().grab(true, true).span(3, 1).align(SWT.FILL, SWT.FILL).applyTo(strecke);

        comboStrecke = new ComboViewer(strecke);
        comboStrecke.setContentProvider(ArrayContentProvider.getInstance());
        comboStrecke.setLabelProvider(new LabelProvider() {
            @Override
            public String getText(final Object element) {
                String label = ""; //$NON-NLS-1$
                if (element instanceof IRoute) {
                    final IRoute route = (IRoute) element;
                    label = route.getName();
                }
                return label;
            }
        });
        GridDataFactory.fillDefaults().grab(true, true).align(SWT.FILL, SWT.FILL).applyTo(comboStrecke.getControl());

        final List<IRoute> all = cache.getAll();
        comboStrecke.setInput(all);

        comboStrecke.addSelectionChangedListener(new ISelectionChangedListener() {

            @Override
            public void selectionChanged(final SelectionChangedEvent event) {
                final StructuredSelection selection = (StructuredSelection) event.getSelection();
                final IRoute route = (IRoute) selection.getFirstElement();
                referenzTrainingId = route.getId();
                update();
            }
        });

        // result ---------------------------
        final Composite result = new Composite(parent, SWT.NONE);
        GridLayoutFactory.swtDefaults().applyTo(result);
        GridDataFactory.swtDefaults().align(SWT.FILL, SWT.FILL).grab(true, true).applyTo(result);
        viewer = new TableViewer(result);
        comparator = new SearchDialogComparator();
        viewer.setComparator(comparator);

        final Table table = viewer.getTable();
        table.setHeaderVisible(true);
        table.setLinesVisible(true);

        viewer.setContentProvider(new ArrayContentProvider());
        // viewer.setLabelProvider(new TrainingLabelProvider());
        createColumns(result, viewer);

        viewer.addSelectionChangedListener(new ISelectionChangedListener() {

            @Override
            public void selectionChanged(final SelectionChangedEvent selection) {
                updateButton();
            }

        });

        GridDataFactory.swtDefaults().align(SWT.FILL, SWT.FILL).grab(true, true).applyTo(viewer.getControl());

        update();
        return search;
    }

    private void createColumns(final Composite parent, final TableViewer viewer) {
        final String[] titles = { Messages.Common_DATUM, Messages.Common_DISTANZ, Messages.Common_DAUER, Messages.PACE };
        final int[] bounds = { 100, 100, 100, 100 };

        TableViewerColumn col = createTableViewerColumn(titles[0], bounds[0], 0);
        col.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public String getText(final Object element) {
                final ITraining training = (ITraining) element;
                return TimeHelper.convertDateToString(training.getDatum());
            }
        });

        col = createTableViewerColumn(titles[1], bounds[1], 1);
        col.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public String getText(final Object element) {
                final ITraining training = (ITraining) element;
                return DistanceHelper.roundDistanceFromMeterToKmMitEinheit(training.getLaengeInMeter());
            }
        });

        col = createTableViewerColumn(titles[2], bounds[2], 2);
        col.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public String getText(final Object element) {
                final ITraining training = (ITraining) element;
                return TimeHelper.convertTimeToString(1000L * (long) training.getDauer());
            }
        });

        col = createTableViewerColumn(titles[3], bounds[3], 3);
        col.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public String getText(final Object element) {
                final ITraining training = (ITraining) element;
                return DistanceHelper.calculatePace(training.getLaengeInMeter(), training.getDauer(), training.getSport());
            }
        });
    }

    private TableViewerColumn createTableViewerColumn(final String title, final int bound, final int colNumber) {
        final TableViewerColumn viewerColumn = new TableViewerColumn(viewer, SWT.NONE);
        final TableColumn column = viewerColumn.getColumn();
        column.setText(title);
        column.setWidth(bound);
        column.setResizable(true);
        column.setMoveable(true);
        column.addSelectionListener(getSelectionAdapter(column, colNumber));
        return viewerColumn;
    }

    private SelectionAdapter getSelectionAdapter(final TableColumn column, final int index) {
        final SelectionAdapter selectionAdapter = new SelectionAdapter() {
            @Override
            public void widgetSelected(final SelectionEvent e) {
                comparator.setColumn(index);
                final int dir = comparator.getDirection();
                viewer.getTable().setSortDirection(dir);
                viewer.getTable().setSortColumn(column);
                viewer.refresh();
            }
        };
        return selectionAdapter;
    }

    @Override
    protected Point getInitialSize() {
        final Point initialSize = super.getInitialSize();
        return new Point(initialSize.x, 550);
    }

    private void update() {
        Display.getDefault().asyncExec(new Runnable() {

            @Override
            public void run() {
                final CriteriaContainer container = CriteriaFactory.createCriteriaContainer();
                // damit es meter werden * 1000
                container.addCriteria(CriteriaFactory.createDistanceCriteria(scale.getSelection() * 1000));
                container.addCriteria(CriteriaFactory.createNoteCriteria(beschreibungSearch.getText()));
                final Set<Sport> sports = new HashSet<>();
                if (runButton.getSelection()) {
                    sports.add(Sport.RUNNING);
                }
                if (bikeButton.getSelection()) {
                    sports.add(Sport.BIKING);
                }
                if (otherButton.getSelection()) {
                    sports.add(Sport.OTHER);
                }
                container.addCriteria(CriteriaFactory.createSportCriteria(sports));
                container.addCriteria(CriteriaFactory.createStreckeCriteria(referenzTrainingId));
                final long start = DateTime.now().getMillis();

                final List<ITraining> result = new ArrayList<>();
                for (final ITraining training : trainings) {
                    if (container.matches(training)) {
                        result.add(training);
                    }
                }
                LOGGER.info(String.format("Filter trainings dauerte %s [ms]", DateTime.now().getMillis() - start)); //$NON-NLS-1$
                if (viewer != null) {
                    viewer.setInput(result);
                }
                updateButton();
            }

        });
        scaleLabel.setText(String.format("%s %s", scale.getSelection(), Messages.SearchDialog_COMMON_KM)); //$NON-NLS-1$
    }

    private void updateButton() {
        final StructuredSelection selection = (StructuredSelection) viewer.getSelection();
        getButton(Dialog.OK).setEnabled(!selection.isEmpty());
    }

    @Override
    protected void okPressed() {
        action.open(viewer);
        super.okPressed();
    }
}
