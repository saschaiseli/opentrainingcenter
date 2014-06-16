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

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.IWorkbenchPartSite;
import org.eclipse.ui.forms.widgets.TableWrapData;

import ch.opentrainingcenter.client.ui.tableviewer.labelprovider.LapInfoColumnLabelProvider;
import ch.opentrainingcenter.core.helper.DistanceHelper;
import ch.opentrainingcenter.core.helper.TimeHelper;
import ch.opentrainingcenter.i18n.Messages;
import ch.opentrainingcenter.transfer.ILapInfo;

public class LapInfoTableViewer extends TableViewer {

    public LapInfoTableViewer(final Composite parent, final int style) {
        super(parent, style);
    }

    public void createTableViewer(final List<ILapInfo> input, final IWorkbenchPartSite site) {
        final Table table = getTable();
        table.setHeaderVisible(true);
        table.setLinesVisible(true);

        createLapColumns();

        setContentProvider(new ArrayContentProvider());
        Collections.sort(input, new Comparator<ILapInfo>() {

            @Override
            public int compare(final ILapInfo o1, final ILapInfo o2) {
                return Integer.compare(o1.getLap(), o2.getLap());
            }
        });
        setInput(input);
        refresh();

        final TableWrapData clientLayoutData = new TableWrapData(TableWrapData.FILL_GRAB);
        clientLayoutData.maxHeight = 250;
        clientLayoutData.grabHorizontal = true;
        clientLayoutData.grabVertical = true;
        getControl().setLayoutData(clientLayoutData);
    }

    private void createLapColumns() {
        final String[] titles = { Messages.RUNDE, Messages.ZEIT, Messages.DISTANZ, Messages.PACE, Messages.HERZFREQUENZ };
        final int[] bounds = { 60, 100, 100, 100, 100 };

        // Runde
        TableViewerColumn col = createTableViewerColumn(titles[0], bounds[0], this);
        col.setLabelProvider(new LapInfoColumnLabelProvider() {

            @Override
            public String getLapInfoText(final ILapInfo lapInfo) {
                return String.valueOf(lapInfo.getLap());
            }
        });

        // Zeit
        col = createTableViewerColumn(titles[1], bounds[1], this);
        col.setLabelProvider(new LapInfoColumnLabelProvider() {

            @Override
            public String getLapInfoText(final ILapInfo lapInfo) {
                return TimeHelper.convertSecondsToHumanReadableZeit(lapInfo.getTime() / 1000);
            }
        });

        // distanz
        col = createTableViewerColumn(titles[2], bounds[2], this);
        col.setLabelProvider(new LapInfoColumnLabelProvider() {

            @Override
            public String getLapInfoText(final ILapInfo lapInfo) {
                return DistanceHelper.roundDistanceFromMeterToKm(lapInfo.getEnd() - lapInfo.getStart());
            }
        });

        // pace
        col = createTableViewerColumn(titles[3], bounds[3], this);
        col.setLabelProvider(new LapInfoColumnLabelProvider() {

            @Override
            public String getLapInfoText(final ILapInfo lapInfo) {
                return lapInfo.getPace();
            }
        });

        // Herzfrequenz
        col = createTableViewerColumn(titles[4], bounds[4], this);
        col.setLabelProvider(new LapInfoColumnLabelProvider() {

            @Override
            public String getLapInfoText(final ILapInfo lapInfo) {
                return String.valueOf(lapInfo.getHeartBeat());
            }
        });
    }

    private TableViewerColumn createTableViewerColumn(final String title, final int bound, final TableViewer viewer) {
        final TableViewerColumn viewerColumn = new TableViewerColumn(viewer, SWT.NONE);
        final TableColumn column = viewerColumn.getColumn();
        column.setText(title);
        column.setWidth(bound);
        column.setResizable(true);
        column.setMoveable(true);
        return viewerColumn;
    }

}
