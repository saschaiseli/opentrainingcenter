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

package ch.opentrainingcenter.client.views.dialoge;

import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import ch.opentrainingcenter.client.Activator;
import ch.opentrainingcenter.client.views.IImageKeys;

/**
 * Dialog um Tracks zu suchen.
 */
public class SearchDialog extends TitleAreaDialog {

    public SearchDialog(final Shell parentShell) {
        super(parentShell);
    }

    @Override
    protected Control createDialogArea(final Composite parent) {
        setTitle("Suche Track");
        setMessage("Suche mit den folgenden Einschränkungen die gewünschten Tracks");
        setTitleImage(Activator.getImageDescriptor(IImageKeys.SEARCH_72).createImage());

        final Composite main = new Composite(parent, SWT.NONE);
        final FillLayout fl = new FillLayout();
        fl.type = SWT.HORIZONTAL;
        main.setLayout(fl);

        final Composite c = new Composite(main, SWT.NONE);
        c.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_CYAN));

        // final FillLayout fl = new FillLayout(SWT.NONE);
        // c.setLayout(fl);
        // Fill

        // GridLayoutFactory.swtDefaults().numColumns(2).applyTo(c);
        // GridDataFactory.swtDefaults().grab(true, true).applyTo(c);

        // GridLayoutFactory.swtDefaults().numColumns(2).applyTo(c);
        // GridDataFactory.swtDefaults().grab(true, true).applyTo(c);
        //
        final Label beschreibung = new Label(c, SWT.NONE);
        beschreibung.setText("Beschreibung:");
        GridDataFactory.swtDefaults().grab(true, false).applyTo(beschreibung);
        final Text t = new Text(c, SWT.NONE);
        GridDataFactory.swtDefaults().grab(true, true).applyTo(t);

        return c;
    }
}
