package ch.opentrainingcenter.client.views.overview;

import java.util.Collection;
import java.util.List;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseTrackAdapter;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;

import ch.opentrainingcenter.client.ui.FormToolkitSupport;
import ch.opentrainingcenter.core.cache.IRecordListener;
import ch.opentrainingcenter.core.cache.RecordAdapter;
import ch.opentrainingcenter.core.cache.RouteCache;
import ch.opentrainingcenter.core.cache.SchuhCache;
import ch.opentrainingcenter.core.cache.TrainingCache;
import ch.opentrainingcenter.core.db.IDatabaseAccess;
import ch.opentrainingcenter.i18n.Messages;
import ch.opentrainingcenter.model.training.Wetter;
import ch.opentrainingcenter.transfer.IRoute;
import ch.opentrainingcenter.transfer.IShoe;
import ch.opentrainingcenter.transfer.ITraining;
import ch.opentrainingcenter.transfer.factory.CommonTransferFactory;

public class NoteSection {

    private final ITraining training;
    private final FormToolkit toolkit;
    private final IDatabaseAccess databaseAccess;

    public NoteSection(final ITraining training, final FormToolkit toolkit, final IDatabaseAccess databaseAccess) {
        this.training = training;
        this.toolkit = toolkit;
        this.databaseAccess = databaseAccess;
    }

    void addNoteSection(final Composite body) {
        final Section section = toolkit.createSection(body, FormToolkitSupport.SECTION_STYLE);
        section.setExpanded(true);
        section.setText(Messages.SingleActivityViewPart_0);
        section.setDescription(Messages.SingleActivityViewPart_1);

        final Composite container = toolkit.createComposite(section);
        GridLayoutFactory.fillDefaults().numColumns(2).applyTo(container);
        GridDataFactory.swtDefaults().applyTo(container);

        final Label label = toolkit.createLabel(container, ""); //$NON-NLS-1$
        label.setText(Messages.SingleActivityViewPart_3);

        final Text note = toolkit.createText(container, "", SWT.V_SCROLL | SWT.MULTI | SWT.WRAP | SWT.BORDER); //$NON-NLS-1$
        GridDataFactory.fillDefaults().grab(true, true).minSize(SWT.DEFAULT, 100).align(SWT.FILL, SWT.FILL).applyTo(note);

        final String notiz = training.getNote();
        if (notiz != null) {
            note.setText(notiz);
        }

        note.addMouseTrackListener(new MouseTrackAdapter() {

            @Override
            public void mouseExit(final MouseEvent e) {
                if (!training.getNote().equals(note.getText())) {
                    training.setNote(note.getText());
                    update(training);
                }
            }
        });

        note.addFocusListener(new FocusAdapter() {

            @Override
            public void focusLost(final FocusEvent e) {
                if (!training.getNote().equals(note.getText())) {
                    training.setNote(note.getText());
                    update(training);
                }
            }
        });

        final Label labelWetter = toolkit.createLabel(container, ""); //$NON-NLS-1$
        labelWetter.setText(Messages.SingleActivityViewPart_6);

        final Combo comboWetter = new Combo(container, SWT.READ_ONLY);
        comboWetter.setBounds(50, 50, 150, 65);

        comboWetter.setItems(Wetter.getItems());
        comboWetter.select(training.getWeather().getId());
        comboWetter.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(final SelectionEvent e) {
                if (training.getWeather().getId() != (comboWetter.getSelectionIndex())) {
                    training.setWeather(CommonTransferFactory.createWeather(comboWetter.getSelectionIndex()));
                    update(training);
                }
            }
        });
        GridDataFactory.fillDefaults().grab(true, true).align(SWT.FILL, SWT.FILL).applyTo(comboWetter);

        toolkit.createLabel(container, Messages.STRECKE);

        final ComboViewer comboStrecke = new ComboViewer(container);
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

        final List<IRoute> all = RouteCache.getInstance().getAll();
        comboStrecke.setInput(all);

        if (training.getRoute() != null) {
            final StructuredSelection selection = new StructuredSelection(RouteCache.getInstance().getAll().get(0));
            comboStrecke.setSelection(selection);
        }

        comboStrecke.addSelectionChangedListener(new ISelectionChangedListener() {

            @Override
            public void selectionChanged(final SelectionChangedEvent event) {
                if (!event.getSelection().isEmpty()) {
                    final StructuredSelection selection = (StructuredSelection) event.getSelection();
                    final IRoute route = (IRoute) selection.getFirstElement();
                    training.setRoute(route);
                    update(training);
                }
            }
        });
        final IRecordListener<IRoute> routeListener = new IRecordListener<IRoute>() {

            @Override
            public void recordChanged(final Collection<IRoute> entry) {
                final ISelection sel = comboStrecke.getSelection();
                if (sel.isEmpty()) {
                    return;
                }
                comboStrecke.setInput(RouteCache.getInstance().getAll());
                if (!entry.isEmpty() && entry.size() > 1) {
                    // update der liste
                    comboStrecke.setSelection(sel);
                    comboStrecke.refresh();
                } else if (!entry.isEmpty() && entry.size() == 1) {
                    // update von training
                    final IRoute next = entry.iterator().next();
                    final StructuredSelection selection = new StructuredSelection(next);
                    if (next.getReferenzTrack() != null && next.getReferenzTrack().getId() == training.getId()) {
                        comboStrecke.setSelection(selection);
                        comboStrecke.getCombo().setEnabled(false);
                    } else {
                        // select old selection
                        comboStrecke.setSelection(sel);
                        comboStrecke.refresh();
                    }
                } else {
                    // select old selection
                    comboStrecke.setSelection(sel);
                }
            }

            @Override
            public void deleteRecord(final Collection<IRoute> entry) {
                // updateCombo(entry);
                final StructuredSelection sel = (StructuredSelection) comboStrecke.getSelection();
                if (sel.isEmpty()) {
                    return;
                }
                final IRoute selectedRoute = (IRoute) sel.getFirstElement();
                if (entry.size() == 1 && entry.iterator().next().equals(selectedRoute)) {
                    // der eigene record wurde gelöscht
                    comboStrecke.setSelection(new StructuredSelection(training.getRoute()));
                    comboStrecke.getCombo().setEnabled(true);
                } else {
                    comboStrecke.setInput(RouteCache.getInstance().getAll());
                    comboStrecke.setSelection(sel);
                }
                comboStrecke.refresh();
            }
        };
        // ------------------------------- Schuhe
        final Label labelSchuhe = toolkit.createLabel(container, ""); //$NON-NLS-1$
        labelSchuhe.setText(Messages.SingleActivityViewPart_Schuhe);

        final ComboViewer comboSchuhe = new ComboViewer(container);
        comboSchuhe.setContentProvider(ArrayContentProvider.getInstance());
        comboSchuhe.setLabelProvider(new LabelProvider() {
            @Override
            public String getText(final Object element) {
                final IShoe shoe = (IShoe) element;
                return shoe.getSchuhname();
            }
        });
        GridDataFactory.fillDefaults().grab(true, true).align(SWT.FILL, SWT.FILL).applyTo(comboSchuhe.getControl());
        final List<IShoe> allShoes = SchuhCache.getInstance().getAll();
        comboSchuhe.setInput(allShoes);

        if (training.getShoe() != null) {
            final StructuredSelection selection = new StructuredSelection(training.getShoe());
            comboSchuhe.setSelection(selection);
        }

        comboSchuhe.addSelectionChangedListener(new ISelectionChangedListener() {

            @Override
            public void selectionChanged(final SelectionChangedEvent event) {
                if (!event.getSelection().isEmpty()) {
                    final StructuredSelection selection = (StructuredSelection) event.getSelection();
                    final IShoe shoe = (IShoe) selection.getFirstElement();
                    training.setShoe(shoe);
                    update(training);
                }
            }
        });
        // -------------------------------

        final RecordAdapter<ITraining> listener = new RecordAdapter<ITraining>() {

            @Override
            public void recordChanged(final Collection<ITraining> entry) {
                Display.getDefault().asyncExec(new Runnable() {

                    @Override
                    public void run() {
                        if (entry != null) {
                            final ITraining act = entry.iterator().next();
                            if (act.getDatum() == training.getDatum()) {
                                // nur wenn es dieser record ist!
                                if (training.getNote() != null && !note.isDisposed()) {
                                    note.setText(training.getNote());
                                }
                            }
                        }
                        if (!section.isDisposed()) {
                            section.update();
                        }
                    }
                });

            }
        };

        RouteCache.getInstance().addListener(routeListener);
        TrainingCache.getInstance().addListener(listener);

        if (training.getRoute() != null) {
            comboStrecke.setSelection(new StructuredSelection(training.getRoute()));
        } else {
            comboStrecke.setSelection(new StructuredSelection(all.get(0)));
        }
        handleStreckenCombo(training.getRoute(), comboStrecke);
        comboStrecke.refresh();
        section.setClient(container);
    }

    private void handleStreckenCombo(final IRoute route, final ComboViewer comboStrecke) {
        comboStrecke.getCombo().setEnabled(!isReferenzTrack(training, route));
    }

    private boolean isReferenzTrack(final ITraining training, final IRoute route) {
        return route != null && route.getReferenzTrack() != null && route.getReferenzTrack().getId() == training.getId()
                && !Messages.OTCKonstanten_0.equals(route.getName());
    }

    private void update(final ITraining record) {
        Display.getDefault().asyncExec(new Runnable() {

            @Override
            public void run() {
                databaseAccess.saveOrUpdate(record);
            }
        });

    }
}
