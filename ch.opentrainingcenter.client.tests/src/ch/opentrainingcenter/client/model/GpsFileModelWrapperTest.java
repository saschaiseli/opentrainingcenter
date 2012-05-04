package ch.opentrainingcenter.client.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import ch.opentrainingcenter.client.model.impl.GpsFileModel;
import ch.opentrainingcenter.client.model.impl.GpsFileModelWrapper;

public class GpsFileModelWrapperTest {

    @Test
    public void testNull() {
        final GpsFileModelWrapper wrapper = new GpsFileModelWrapper(null);
        assertNotNull(wrapper);
        assertEquals("Leerer wrapper", 0, wrapper.size());
        assertNotNull("Leerer wrapper", wrapper.getGpsFileModels());
    }

    @Test
    public void testEmpty() {
        final List<IGpsFileModel> fileModels = new ArrayList<IGpsFileModel>();
        final GpsFileModelWrapper wrapper = new GpsFileModelWrapper(fileModels);
        assertNotNull(wrapper);
        assertEquals("Leerer wrapper", 0, wrapper.size());
        assertNotNull("Leerer wrapper", wrapper.getGpsFileModels());
    }

    @Test
    public void testWithModel() {
        final List<IGpsFileModel> fileModels = new ArrayList<IGpsFileModel>();
        final GpsFileModel model = new GpsFileModel("Junit");
        fileModels.add(model);
        final GpsFileModelWrapper wrapper = new GpsFileModelWrapper(fileModels);
        assertNotNull(wrapper);
        assertEquals("Ein Element im Wrapper", 1, wrapper.size());
        final List<IGpsFileModel> gpsFileModels = wrapper.getGpsFileModels();
        assertNotNull("Wrapper nicht mehr leer", gpsFileModels);
        assertEquals(model, gpsFileModels.get(0));
    }

    @Test
    public void testWithModelNichtImportier() {
        final List<IGpsFileModel> fileModels = new ArrayList<IGpsFileModel>();
        final GpsFileModel model = new GpsFileModel("Junit");
        model.setImportFile(false);

        fileModels.add(model);
        final GpsFileModelWrapper wrapper = new GpsFileModelWrapper(fileModels);
        assertNotNull(wrapper);
        assertEquals("Kein Element im Wrapper", 0, wrapper.size());
        final List<IGpsFileModel> gpsFileModels = wrapper.getGpsFileModels();
        assertEquals("Wrapper nicht mehr leer", 0, gpsFileModels.size());
    }
}
