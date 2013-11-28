package ch.opentrainingcenter.importer.gpx;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.topografix.gpx.ExtensionsType;
import com.topografix.gpx.WptType;

public final class HeartRateHandler {

    private final List<Integer> hearts = new ArrayList<>();

    public int getAverage() {
        int sum = 0;
        for (final Integer heart : hearts) {
            sum += heart.intValue();
        }
        int avg = 0;
        if (!hearts.isEmpty()) {
            avg = sum / hearts.size();
        }
        return avg;
    }

    public List<Integer> getHearts() {
        return hearts;
    }

    public int getMax() {
        int result = 0;
        if (!hearts.isEmpty()) {
            result = Collections.max(hearts);
        }
        return result;
    }

    public int getHeartRate(final WptType wptType) {
        int result = 0;
        if (wptType.getExtensions() != null) {
            // mit extension
            final ExtensionsType extensions = wptType.getExtensions();
            final List<Object> any = extensions.getAny();
            for (final Object extension : any) {
                final Element el = (Element) extension;
                final Node firstChild = el.getFirstChild();
                final String nName = firstChild.getNodeName();
                if ("gpxtpx:hr".equals(nName)) { //$NON-NLS-1$
                    result = Integer.valueOf(firstChild.getTextContent());
                }
            }
        }
        return result;
    }

}
