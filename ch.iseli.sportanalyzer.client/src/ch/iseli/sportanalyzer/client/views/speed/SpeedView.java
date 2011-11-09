package ch.iseli.sportanalyzer.client.views.speed;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.part.ViewPart;

import ch.iseli.sportanalyzer.client.Activator;

public class SpeedView extends ViewPart {

    public static final String ID = "ch.iseli.sportanalyzer.client.views.speed";

    public static final String IMAGE = "icons/speed_kmh.png";

    public SpeedView() {
        // TODO Auto-generated constructor stub
    }

    @Override
    public void createPartControl(Composite parent) {
        Text text = new Text(parent, SWT.BORDER);
        text.setText(ID);
    }

    @Override
    public void setFocus() {
    }

    @Override
    protected Image getDefaultImage() {
        return Activator.getImageDescriptor("icons/speed_kmh.png").createImage();
    }

}
