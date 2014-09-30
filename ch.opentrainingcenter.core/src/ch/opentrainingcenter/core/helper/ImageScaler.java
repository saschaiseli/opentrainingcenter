package ch.opentrainingcenter.core.helper;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;

public final class ImageScaler {
    private ImageScaler() {

    }

    public static Image scale(final Image image, final int maxHeight) {
        final Rectangle bounds = image.getBounds();
        int height;
        int width;
        float ratio = 1;
        if (bounds.height > bounds.width && bounds.height > maxHeight) {
            ratio = bounds.height / maxHeight;
        } else if (bounds.width > bounds.height && bounds.width > maxHeight) {
            ratio = bounds.height / maxHeight;
        }
        height = (int) (bounds.height / ratio);
        width = (int) (bounds.width / ratio);
        final Image scaled = new Image(Display.getDefault(), width, height);
        final GC gc = new GC(scaled);
        gc.setAntialias(SWT.ON);
        gc.setInterpolation(SWT.HIGH);
        gc.drawImage(image, 0, 0, bounds.width, bounds.height, 0, 0, width, height);
        gc.dispose();
        // image.dispose();
        return scaled;
    }
}
