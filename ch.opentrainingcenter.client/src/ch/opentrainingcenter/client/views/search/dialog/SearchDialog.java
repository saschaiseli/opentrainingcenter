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

package ch.opentrainingcenter.client.views.search.dialog;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Scale;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.joda.time.DateTime;

import ch.opentrainingcenter.client.Activator;
import ch.opentrainingcenter.client.views.ApplicationContext;
import ch.opentrainingcenter.client.views.IImageKeys;
import ch.opentrainingcenter.client.views.overview.SingleActivityViewPart;
import ch.opentrainingcenter.core.db.CriteriaContainer;
import ch.opentrainingcenter.core.db.CriteriaFactory;
import ch.opentrainingcenter.core.service.IDatabaseService;
import ch.opentrainingcenter.i18n.Messages;
import ch.opentrainingcenter.transfer.ITraining;

/**
 * Dialog um Tracks zu suchen.
 */
public class SearchDialog extends TitleAreaDialog {

    private static final Logger LOGGER = Logger.getLogger(SearchDialog.class);

    private TableViewer viewer;

    private Scale scale;

    private Label scaleLabel;

    private Text beschreibungSearch;

    private final List<ITraining> trainings;

    public SearchDialog(final Shell parentShell) {
        super(parentShell);
        final IDatabaseService service = (IDatabaseService) PlatformUI.getWorkbench().getService(IDatabaseService.class);
        trainings = service.getDatabaseAccess().getAllTrainings(ApplicationContext.getApplicationContext().getAthlete());
    }

    @Override
    protected Control createDialogArea(final Composite parent) {
        setTitle(Messages.SearchDialog_SEARCH);

        setMessage(Messages.SearchDialog_SEARCH_DESCRIPTION);
        setTitleImage(Activator.getImageDescriptor(IImageKeys.SEARCH_72).createImage());
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

        // result ---------------------------
        final Composite result = new Composite(parent, SWT.NONE);
        GridLayoutFactory.swtDefaults().applyTo(result);
        GridDataFactory.swtDefaults().align(SWT.FILL, SWT.FILL).grab(true, true).applyTo(result);

        viewer = new TableViewer(result);
        viewer.setContentProvider(new ArrayContentProvider());
        viewer.setLabelProvider(new TrainingLabelProvider());
        viewer.addSelectionChangedListener(new ISelectionChangedListener() {

            @Override
            public void selectionChanged(final SelectionChangedEvent selection) {
                getButton(Dialog.OK).setEnabled(!selection.getSelection().isEmpty());
            }
        });
        GridDataFactory.swtDefaults().align(SWT.FILL, SWT.FILL).grab(true, true).applyTo(viewer.getControl());

        update();
        return search;
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
            }

        });
        scaleLabel.setText(String.format("%s %s", scale.getSelection(), Messages.SearchDialog_COMMON_KM)); //$NON-NLS-1$
    }

    @Override
    protected void okPressed() {
        final StructuredSelection selection = (StructuredSelection) viewer.getSelection();
        final Object[] array = selection.toArray();
        final List<Object> records = Arrays.asList(array);
        Display.getDefault().asyncExec(new Runnable() {

            @Override
            public void run() {
                Display.getDefault().asyncExec(new Runnable() {

                    @Override
                    public void run() {
                        for (final Object record : records) {
                            final ITraining training = (ITraining) record;
                            final String hash = String.valueOf(training.getDatum());
                            try {
                                final IWorkbenchPage activePage = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
                                ApplicationContext.getApplicationContext().setSelectedId(training.getDatum());
                                activePage.showView(SingleActivityViewPart.ID, hash, IWorkbenchPage.VIEW_ACTIVATE);
                            } catch (final PartInitException e) {
                                LOGGER.error(e);
                            }
                        }
                    }
                });
            }
        });
        super.okPressed();
    }
}
