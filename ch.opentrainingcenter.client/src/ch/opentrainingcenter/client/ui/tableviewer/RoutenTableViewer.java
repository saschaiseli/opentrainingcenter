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

import ch.opentrainingcenter.client.ui.tableviewer.labelprovider.RouteColumnLabelProviderAnzahl;
import ch.opentrainingcenter.client.ui.tableviewer.labelprovider.RouteColumnLabelProviderBeschreibung;
import ch.opentrainingcenter.client.ui.tableviewer.labelprovider.RouteColumnLabelProviderLaenge;
import ch.opentrainingcenter.client.ui.tableviewer.labelprovider.RouteColumnLabelProviderName;
import ch.opentrainingcenter.i18n.Messages;

public class RoutenTableViewer extends TableViewer {

    public RoutenTableViewer(final Composite parent, final int style) {
        super(parent, style);
    }

    public void createTableViewer(final List<RoutenTableModel> models) {
        final Table table = getTable();
        table.setHeaderVisible(true);
        table.setLinesVisible(true);

        createRouteColumns();

        setContentProvider(new ArrayContentProvider());
        setInput(models);

        // Layout the viewer
        final GridData gridData = new GridData();
        gridData.verticalAlignment = GridData.FILL;
        gridData.horizontalSpan = 1;
        gridData.grabExcessHorizontalSpace = true;
        gridData.grabExcessVerticalSpace = true;
        gridData.horizontalAlignment = GridData.FILL;
        gridData.minimumHeight = 300;
        getControl().setLayoutData(gridData);
    }

    private void createRouteColumns() {
        final String[] titles = { Messages.RoutenView_10, Messages.RoutenView_11, Messages.RoutenView_6, Messages.RoutenView_13 };
        final int[] bounds = { 80, 200, 100, 40 };

        TableViewerColumn col = createRouteColumn(titles[0], bounds[0]);
        col.setLabelProvider(new RouteColumnLabelProviderName());

        col = createRouteColumn(titles[1], bounds[1]);
        col.setLabelProvider(new RouteColumnLabelProviderBeschreibung());

        col = createRouteColumn(titles[2], bounds[2]);
        col.setLabelProvider(new RouteColumnLabelProviderLaenge());

        col = createRouteColumn(titles[3], bounds[3]);
        col.setLabelProvider(new RouteColumnLabelProviderAnzahl());
    }

    private TableViewerColumn createRouteColumn(final String title, final int bound) {
        final TableViewerColumn viewerColumn = new TableViewerColumn(this, SWT.NONE);
        final TableColumn column = viewerColumn.getColumn();
        column.setText(title);
        column.setWidth(bound);
        column.setResizable(true);
        column.setMoveable(true);
        return viewerColumn;
    }
}
