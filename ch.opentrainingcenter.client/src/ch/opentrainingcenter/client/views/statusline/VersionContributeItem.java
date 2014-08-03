package ch.opentrainingcenter.client.views.statusline;

import org.eclipse.jface.action.ContributionItem;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.osgi.framework.Version;

import ch.opentrainingcenter.client.Activator;
import ch.opentrainingcenter.client.views.IImageKeys;

public class VersionContributeItem extends ContributionItem {

    @Override
    public void fill(final Composite parent) {
        final Version version = Activator.getDefault().getBundle().getVersion();
        final StringBuilder buildId = new StringBuilder();
        final CLabel label = new CLabel(parent, SWT.NONE);
        if ("qualifier".equalsIgnoreCase(version.getQualifier())) { //$NON-NLS-1$
            buildId.append("DEV"); //$NON-NLS-1$
        } else {
            buildId.append(version.getMajor()).append(".").append(version.getMinor()).append(" / ").append(version.getQualifier()); //$NON-NLS-1$ //$NON-NLS-2$
        }
        label.setToolTipText(buildId.toString());
        final Image icon = Activator.getImageDescriptor(IImageKeys.VERSION).createImage();
        label.setImage(icon);
    }
}
