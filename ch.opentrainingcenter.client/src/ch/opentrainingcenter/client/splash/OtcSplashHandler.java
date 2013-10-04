package ch.opentrainingcenter.client.splash;

import org.eclipse.core.runtime.IProduct;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.resource.StringConverter;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.branding.IProductConstants;
import org.eclipse.ui.splash.BasicSplashHandler;
import org.osgi.framework.Version;

import ch.opentrainingcenter.client.Activator;

public class OtcSplashHandler extends BasicSplashHandler {

    private static final int WHITE = 0xD2D7FF;
    private static final int FOREGROUND = 16;
    public static final String ID = "ch.opentrainingcenter.client.splash"; //$NON-NLS-1$

    public OtcSplashHandler() {
        super();
    }

    @Override
    public void init(final Shell splash) {
        super.init(splash);
        Activator.setSplashHandler(this);
        String progressRectString = null;
        String messageRectString = null;
        String foregroundColorString = null;
        final IProduct product = Platform.getProduct();
        if (product != null) {
            progressRectString = product.getProperty(IProductConstants.STARTUP_PROGRESS_RECT);
            messageRectString = product.getProperty(IProductConstants.STARTUP_MESSAGE_RECT);
            foregroundColorString = product.getProperty(IProductConstants.STARTUP_FOREGROUND_COLOR);
        }
        final Rectangle progressRect = StringConverter.asRectangle(progressRectString, new Rectangle(10, 10, 300, 15));
        setProgressRect(progressRect);

        final Rectangle messageRect = StringConverter.asRectangle(messageRectString, new Rectangle(10, 35, 300, 15));
        setMessageRect(messageRect);

        int foregroundColorInteger;
        try {
            foregroundColorInteger = Integer.parseInt(foregroundColorString, FOREGROUND);
        } catch (final Exception ex) {
            foregroundColorInteger = WHITE; // off white
        }

        setForeground(new RGB((foregroundColorInteger & 0xFF0000) >> 16, (foregroundColorInteger & 0xFF00) >> 8, foregroundColorInteger & 0xFF));
        final Version version = Activator.getDefault().getBundle().getVersion();
        final String buildId;
        if (version.getQualifier().equalsIgnoreCase("qualifier")) { //$NON-NLS-1$
            buildId = "DEV"; //$NON-NLS-1$
        } else {
            buildId = version.getMajor() + "." + version.getMinor() + " / " + version.getQualifier(); //$NON-NLS-1$ //$NON-NLS-2$
        }
        if (product != null) {
            final String buildIdLocString = product.getProperty("buildIdLocation"); //$NON-NLS-1$
            final Point buildIdPoint = StringConverter.asPoint(buildIdLocString, new Point(5, 10));
            getContent().addPaintListener(new PaintListener() {

                @Override
                public void paintControl(final PaintEvent e) {
                    e.gc.setForeground(getForeground());
                    e.gc.drawText(buildId, buildIdPoint.x, buildIdPoint.y, true);
                }
            });
        }

        getContent();
    }
}
