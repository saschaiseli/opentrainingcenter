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

package ch.opentrainingcenter.client.views.overview;

import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.forms.widgets.TableWrapData;
import org.eclipse.ui.forms.widgets.TableWrapLayout;

import ch.opentrainingcenter.charts.single.ChartFactory;
import ch.opentrainingcenter.charts.single.ChartType;
import ch.opentrainingcenter.client.ui.FormToolkitSupport;

public class ChartSectionSupport {

    private final FormToolkit toolkit;
    private final ChartFactory factory;

    public ChartSectionSupport(final FormToolkit toolkit, final ChartFactory factory) {
        this.toolkit = toolkit;
        this.factory = factory;
    }

    /**
     * Fuegt auf dem {@link Composite} ein Chart vom Typ {@link ChartType}
     * hinzu.
     * 
     */
    public ISelectionChangedListener createChartOnSection(final Composite body, final ChartType chartType, final boolean expanded) {
        final Section altitude = toolkit.createSection(body, FormToolkitSupport.SECTION_STYLE);
        altitude.setExpanded(expanded);

        final TableWrapData td = new TableWrapData(TableWrapData.FILL_GRAB);
        td.colspan = 2;
        td.grabHorizontal = true;
        td.grabVertical = true;
        altitude.setLayoutData(td);
        altitude.setText(chartType.getTitel());

        final Composite client = toolkit.createComposite(altitude);

        final TableWrapLayout layout = new TableWrapLayout();
        layout.numColumns = 2;
        layout.makeColumnsEqualWidth = false;
        client.setLayout(layout);

        final ISelectionChangedListener listener = factory.addChartToComposite(client, chartType);

        altitude.setClient(client);
        return listener;
    }

}
