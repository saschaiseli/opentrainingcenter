package ch.opentrainingcenter.client.views.overview;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.widgets.Composite;

import com.eclipsesource.widgets.gmaps.GMap;
import com.eclipsesource.widgets.gmaps.LatLng;

public class MapViewer extends Composite {
    public static final String ID = "ch.opentrainingcenter.client.views.overview.MapViewer"; //$NON-NLS-1$

    private static final Logger LOGGER = Logger.getLogger(MapViewer.class);
    private static final int INIT_ZOOM = 16;
    private static final int INIT_TYPE = GMap.TYPE_HYBRID;
    private final String initCenter;
    private final Composite parent;
    private final String path;

    private GMap gmap;

    public MapViewer(final Composite parent, final int style, final String path, final String initCenter) {
        super(parent, style);
        this.parent = parent;
        this.path = path;
        this.initCenter = initCenter;
    }

    public Composite getComposite() {
        LOGGER.debug("create map"); //$NON-NLS-1$
        final SashForm sash = new SashForm(parent, SWT.HORIZONTAL);
        createMap(sash);
        return sash;
    }

    private void createMap(final Composite o) {
        gmap = new GMap(o, SWT.BORDER, path);
        gmap.setCenter(stringToLatLng(initCenter));
        gmap.setZoom(INIT_ZOOM);
        gmap.setType(INIT_TYPE);
    }

    private LatLng stringToLatLng(final String input) {
        LatLng result = null;
        if (input != null) {
            final String[] temp = input.split(","); //$NON-NLS-1$
            if (temp.length == 2) {
                try {
                    final double lat = Double.parseDouble(temp[0]);
                    final double lon = Double.parseDouble(temp[1]);
                    result = new LatLng(lat, lon);
                } catch (final NumberFormatException ex) {
                    LOGGER.error("Koordinaten stringToLatLng", ex); //$NON-NLS-1$
                }
            }
        }
        return result;
    }

}
