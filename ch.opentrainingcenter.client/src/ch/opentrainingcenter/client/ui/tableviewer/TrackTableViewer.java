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

package ch.opentrainingcenter.client.ui.tableviewer;

import java.util.List;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

import ch.opentrainingcenter.client.ui.tableviewer.labelprovider.TrackColumnLabelProviderDistanz;
import ch.opentrainingcenter.client.ui.tableviewer.labelprovider.TrackColumnLabelProviderRouten;
import ch.opentrainingcenter.client.ui.tableviewer.labelprovider.TrackColumnLabelProviderZeit;
import ch.opentrainingcenter.i18n.Messages;
import ch.opentrainingcenter.transfer.ITraining;

public class TrackTableViewer extends TableViewer {

    public TrackTableViewer(final Composite parent, final int style) {
        super(parent, style);

    }

    public void createTableViewer(final List<ITraining> input) {
        final Table table = getTable();
        table.setHeaderVisible(true);
        table.setLinesVisible(true);

        createTrackColumns();

        setContentProvider(new ArrayContentProvider());
        setInput(input);

        final GridData gridData = new GridData();
        gridData.verticalAlignment = GridData.FILL;
        gridData.horizontalSpan = 1;
        gridData.grabExcessHorizontalSpace = true;
        gridData.grabExcessVerticalSpace = true;
        gridData.horizontalAlignment = GridData.FILL;
        gridData.minimumHeight = 300;
        getControl().setLayoutData(gridData);
    }

    private void createTrackColumns() {
        final String[] titles = { Messages.RoutenView_5, Messages.RoutenView_6, Messages.RoutenView_7, Messages.RoutenView_8 };
        final int[] bounds = { 100, 120, 100 };

        TableViewerColumn col = createTrackColumn(titles[0], bounds[0]);
        col.setLabelProvider(new TrackColumnLabelProviderZeit());

        col = createTrackColumn(titles[1], bounds[1]);
        col.setLabelProvider(new TrackColumnLabelProviderDistanz());

        col = createTrackColumn(titles[2], bounds[2]);
        col.setLabelProvider(new TrackColumnLabelProviderRouten());
    }

    private TableViewerColumn createTrackColumn(final String title, final int bound) {
        final TableViewerColumn viewerColumn = new TableViewerColumn(this, SWT.NONE);
        final TableColumn column = viewerColumn.getColumn();
        column.setText(title);
        column.setWidth(bound);
        column.setResizable(true);
        column.setMoveable(true);
        return viewerColumn;
    }

}
