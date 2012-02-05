package ch.opentrainingcenter.client.views.overview;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import ch.opentrainingcenter.client.views.overview.MapConverter;

public class MapConverterTest {
    @Test
    public void testGetFirstPoint() {
        final String firstPointToPan = MapConverter.getFirstPointToPan("[[25.774252,-80.190262],[18.466465,-66.118292], [46.954, 7.448]]");//$NON-NLS-1$
        assertEquals("25.774252,-80.190262", firstPointToPan);//$NON-NLS-1$
    }
}
