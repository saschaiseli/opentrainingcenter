package ch.opentrainingcenter.client.ui.tableviewer;

import java.io.File;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

import ch.opentrainingcenter.client.Activator;
import ch.opentrainingcenter.client.views.IImageKeys;
import ch.opentrainingcenter.core.data.Pair;
import ch.opentrainingcenter.core.helper.DistanceHelper;
import ch.opentrainingcenter.core.helper.TimeHelper;
import ch.opentrainingcenter.i18n.Messages;
import ch.opentrainingcenter.transfer.IShoe;

public class SchuhTableViewer extends TableViewer {

    private static final Logger LOG = Logger.getLogger(SchuhTableViewer.class);
    private Map<IShoe, Pair<Integer, Double>> anzahlUndKilometer;

    public SchuhTableViewer(final Composite parent, final int style) {
        super(parent, style);
    }

    public void createTableViewer(final List<IShoe> schuhe) {
        final Table table = getTable();
        table.setHeaderVisible(true);
        table.setLinesVisible(true);

        createColumns();

        setContentProvider(new ArrayContentProvider());

        setInput(schuhe);

        // Layout the viewer
        final GridData gridData = new GridData();
        gridData.verticalAlignment = GridData.FILL;
        gridData.horizontalSpan = 1;
        gridData.grabExcessHorizontalSpace = true;
        gridData.grabExcessVerticalSpace = true;
        gridData.horizontalAlignment = GridData.FILL;
        getControl().setLayoutData(gridData);
    }

    private void createColumns() {
        final String[] titles = { Messages.RoutenView_ID, Messages.RoutenView_Name_Marke, Messages.RoutenView_Icon, Messages.SchuhTableViewer_PreisTableHeader,
                Messages.SchuhTableViewer_KaufDatumTableHeader, Messages.SchuhTableViewer_AnzahlLaeufe, Messages.SchuhTableViewer_TotalDistanz };
        final int[] bounds = { 40, 300, 200, 50, 110, 60, 50 };

        TableViewerColumn col = createTableViewerColumn(titles[0], bounds[0], 0);
        col.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public String getText(final Object element) {
                final IShoe shoe = (IShoe) element;
                return String.valueOf(shoe.getId());
            }
        });

        col = createTableViewerColumn(titles[1], bounds[1], 1);
        col.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public String getText(final Object element) {
                final IShoe shoe = (IShoe) element;
                return shoe.getSchuhname();
            }
        });

        col = createTableViewerColumn(titles[2], bounds[2], 2);
        col.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public String getText(final Object element) {
                return null;
            }

            @Override
            public Image getImage(final Object element) {
                final IShoe shoe = (IShoe) element;
                final String imageicon = shoe.getImageicon();
                if (imageicon != null) {
                    final File image = new File(imageicon);
                    if (!image.exists()) {
                        LOG.error(String.format("Bild zu Schuh '%s' nicht gefunden", imageicon)); //$NON-NLS-1$
                        return null;
                    }
                }
                return imageicon != null ? new Image(Display.getDefault(), imageicon) : Activator.getImageDescriptor(IImageKeys.SHOE).createImage();
            }
        });
        col = createTableViewerColumn(titles[3], bounds[3], 3);
        col.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public String getText(final Object element) {
                final IShoe shoe = (IShoe) element;
                return String.valueOf(shoe.getPreis());
            }
        });

        col = createTableViewerColumn(titles[4], bounds[4], 4);
        col.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public String getText(final Object element) {
                final IShoe shoe = (IShoe) element;
                return TimeHelper.convertDateToString(shoe.getKaufdatum());
            }
        });

        col = createTableViewerColumn(titles[5], bounds[5], 5);
        col.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public String getText(final Object element) {
                final IShoe shoe = (IShoe) element;
                final Pair<Integer, Double> pair = anzahlUndKilometer.get(shoe);
                if (pair != null) {
                    return pair.getFirst().toString();
                } else {
                    return "-"; //$NON-NLS-1$
                }
            }
        });

        col = createTableViewerColumn(titles[6], bounds[6], 6);
        col.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public String getText(final Object element) {
                final IShoe shoe = (IShoe) element;
                final Pair<Integer, Double> pair = anzahlUndKilometer.get(shoe);
                if (pair != null) {
                    return DistanceHelper.roundDistanceFromMeterToKm(pair.getSecond());
                } else {
                    return "-"; //$NON-NLS-1$
                }
            }
        });
    }

    private TableViewerColumn createTableViewerColumn(final String title, final int bound, final int colNumber) {
        final TableViewerColumn viewerColumn = new TableViewerColumn(this, SWT.NONE);
        final TableColumn column = viewerColumn.getColumn();
        column.setText(title);
        column.setWidth(bound);
        column.setResizable(true);
        column.setMoveable(true);
        return viewerColumn;
    }

    public void setAnzahlUndKilometer(final Map<IShoe, Pair<Integer, Double>> anzahlUndKilometer) {
        this.anzahlUndKilometer = anzahlUndKilometer;
    }

}
