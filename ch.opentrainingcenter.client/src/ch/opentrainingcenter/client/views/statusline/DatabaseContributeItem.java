package ch.opentrainingcenter.client.views.statusline;

import org.eclipse.jface.action.ContributionItem;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.PlatformUI;

import ch.opentrainingcenter.client.Activator;
import ch.opentrainingcenter.client.views.ApplicationContext;
import ch.opentrainingcenter.client.views.IImageKeys;
import ch.opentrainingcenter.core.db.DBSTATE;
import ch.opentrainingcenter.i18n.Messages;

/**
 * Contribute Item um in der Statuszeile die Datenbank und deren Zustand
 * anzuzeigen.
 * 
 */
public class DatabaseContributeItem extends ContributionItem {
    private final Image icon;
    private final String text;
    private final String dbUrl;

    public DatabaseContributeItem(final String text, final String dbUrl) {
        this(text, dbUrl, Activator.getImageDescriptor(IImageKeys.DATABASE).createImage());
    }

    public DatabaseContributeItem(final String text, final String dbUrl, final Image icon) {
        this.text = text;
        this.dbUrl = dbUrl;
        this.icon = icon;
    }

    @Override
    public void fill(final Composite parent) {
        final CLabel label = new CLabel(parent, SWT.NONE);
        label.setText(text);
        if (DBSTATE.OK.equals(ApplicationContext.getApplicationContext().getDbState())) {
            label.setToolTipText(NLS.bind(Messages.DbContributeItem_0, dbUrl));
        } else {
            label.setBackground(PlatformUI.getWorkbench().getDisplay().getSystemColor(SWT.COLOR_RED));
            label.setToolTipText(NLS.bind(Messages.ApplicationWorkbenchWindowAdvisor_0, text));
        }
        label.setImage(icon);
    }
}
