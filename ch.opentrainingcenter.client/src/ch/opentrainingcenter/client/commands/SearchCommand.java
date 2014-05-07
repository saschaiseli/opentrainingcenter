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

package ch.opentrainingcenter.client.commands;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.ui.PlatformUI;

import ch.opentrainingcenter.client.Activator;
import ch.opentrainingcenter.client.views.search.dialog.SearchDialog;

public class SearchCommand extends OtcAbstractHandler {

    public static final String ID = "ch.opentrainingcenter.client.commands.search"; //$NON-NLS-1$

    /**
     * Constructor um mit OSGI zu starten
     */
    public SearchCommand() {
        this(Activator.getDefault().getPreferenceStore());

    }

    public SearchCommand(final IPreferenceStore store) {
        super(store);
    }

    @Override
    public Object execute(final ExecutionEvent arg0) throws ExecutionException {
        final SearchDialog dialog = new SearchDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell());
        dialog.open();
        return null;
    }
}
