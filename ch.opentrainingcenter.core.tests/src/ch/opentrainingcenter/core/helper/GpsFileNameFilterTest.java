package ch.opentrainingcenter.core.helper;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import ch.opentrainingcenter.core.exceptions.ConvertException;
import ch.opentrainingcenter.core.importer.IConvert2Tcx;
import ch.opentrainingcenter.transfer.ITraining;

@SuppressWarnings("nls")
public class GpsFileNameFilterTest {
    private final Map<String, IConvert2Tcx> converters = new HashMap<String, IConvert2Tcx>();
    private GpsFileNameFilter filter;

    @Before
    public void setUp() {
        converters.put("gmn", new IConvert2Tcx() {

            @Override
            public String getName() {
                return null;
            }

            @Override
            public String getFilePrefix() {
                return "gmn";
            }

            @Override
            public ITraining convert(final File file) throws ConvertException {
                return null;
            }
        });
        converters.put("FIT", new IConvert2Tcx() {

            @Override
            public String getName() {
                return null;
            }

            @Override
            public String getFilePrefix() {
                return "fit";
            }

            @Override
            public ITraining convert(final File file) throws ConvertException {
                return null;
            }
        });
        filter = new GpsFileNameFilter(converters);
    }

    @Test
    public void testLower() throws IOException {
        assertTrue(filter.accept(null, "test.gmn"));
        assertTrue(filter.accept(null, "test.fit"));
    }

    @Test
    public void testUpper() throws IOException {
        assertTrue(filter.accept(null, "test.GMN"));
        assertTrue(filter.accept(null, "test.FIT"));
    }

    @Test
    public void testFalse() throws IOException {
        assertFalse(filter.accept(null, "test.tcx"));
    }

    @Test
    public void testNameNull() throws IOException {
        assertFalse(filter.accept(null, null));
    }
}
