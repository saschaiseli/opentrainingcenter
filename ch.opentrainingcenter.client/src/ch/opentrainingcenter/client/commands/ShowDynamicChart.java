/**
 *    OpenTrainingCenter
 *
 *    Copyright (C) 2013 Sascha Iseli sascha.iseli(at)gmx.ch
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

package ch.opentrainingcenter.client.commands;

import org.apache.log4j.Logger;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

import ch.opentrainingcenter.client.Activator;
import ch.opentrainingcenter.client.views.ngchart.DynamicChartViewPart;

/**
 * zeigt die dynamischen Charts wo wochen, tage oder jahresübersichten
 * ausgewertet werden können.
 */
public class ShowDynamicChart extends OtcAbstractHandler {

    private static final Logger LOGGER = Logger.getLogger(ShowDynamicChart.class);

    public ShowDynamicChart() {
        super(Activator.getDefault().getPreferenceStore());
    }

    @Override
    public Object execute(final ExecutionEvent event) throws ExecutionException {
        Display.getDefault().asyncExec(new Runnable() {

            @Override
            public void run() {
                try {
                    final IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
                    page.showView(DynamicChartViewPart.ID);
                } catch (final PartInitException e) {
                    LOGGER.error(e);
                }
            }
        });
        return null;
    }

}
