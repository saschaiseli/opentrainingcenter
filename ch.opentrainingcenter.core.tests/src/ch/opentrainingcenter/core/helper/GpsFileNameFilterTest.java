package ch.opentrainingcenter.core.helper;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import ch.opentrainingcenter.core.importer.IConvert2Tcx;
import ch.opentrainingcenter.tcx.ActivityT;
import ch.opentrainingcenter.tcx.TrainingCenterDatabaseT;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

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
            public List<ActivityT> convertActivity(final File file) throws Exception {
                return null;
            }

            @Override
            public TrainingCenterDatabaseT convert(final File file) throws Exception {
                return null;
            }
        });
        filter = new GpsFileNameFilter(converters);
    }

    @Test
    public void test() throws IOException {
        assertTrue(filter.accept(null, "test.gmn"));
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
