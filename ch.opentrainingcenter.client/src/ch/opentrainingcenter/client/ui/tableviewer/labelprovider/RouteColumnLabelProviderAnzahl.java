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

package ch.opentrainingcenter.client.ui.tableviewer.labelprovider;

import org.eclipse.jface.viewers.ColumnLabelProvider;

import ch.opentrainingcenter.client.ui.tableviewer.RoutenTableModel;

public final class RouteColumnLabelProviderAnzahl extends ColumnLabelProvider {

    @Override
    public String getText(final Object element) {
        final RoutenTableModel route = (RoutenTableModel) element;
        return String.valueOf(route.getAnzahl());
    }
}