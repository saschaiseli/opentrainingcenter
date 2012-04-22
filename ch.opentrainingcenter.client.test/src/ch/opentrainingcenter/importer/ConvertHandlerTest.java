package ch.opentrainingcenter.importer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import ch.opentrainingcenter.tcx.ActivityT;
import ch.opentrainingcenter.tcx.TrainingCenterDatabaseT;

public class ConvertHandlerTest {
    private Map<String, IConvert2Tcx> converters;
    ConvertContainer cc;

    @Before
    public void setUp() {
        converters = new HashMap<String, IConvert2Tcx>();
    }

    @Test
    public void testMatchingConverter() throws IOException {
        final IConvert2Tcx converterGarmin = new MockConverter("gmn"); //$NON-NLS-1$
        converters.put(converterGarmin.getFilePrefix(), converterGarmin);
        final IConvert2Tcx converterFitnesslog = new MockConverter("fitnesslog"); //$NON-NLS-1$
        converters.put(converterFitnesslog.getFilePrefix(), converterFitnesslog);
        cc = new ConvertContainer(converters);

        final IConvert2Tcx matchingConverter = cc.getMatchingConverter(File.createTempFile("abc", ".gmn")); //$NON-NLS-1$ //$NON-NLS-2$
        assertNotNull(matchingConverter);
        assertEquals("gmn", matchingConverter.getFilePrefix()); //$NON-NLS-1$
    }

    @Test
    public void testSupportedFiles() {
        final IConvert2Tcx converterGarmin = new MockConverter("gmn"); //$NON-NLS-1$
        converters.put(converterGarmin.getFilePrefix(), converterGarmin);
        final IConvert2Tcx converterFitnesslog = new MockConverter("fitnesslog"); //$NON-NLS-1$
        converters.put(converterFitnesslog.getFilePrefix(), converterFitnesslog);
        cc = new ConvertContainer(converters);

        final List<String> supportedFileSuffixes = cc.getSupportedFileSuffixes();
        assertTrue(supportedFileSuffixes.contains("*.gmn")); //$NON-NLS-1$
        assertTrue(supportedFileSuffixes.contains("*.fitnesslog")); //$NON-NLS-1$
    }

    class MockConverter implements IConvert2Tcx {

        private final String prefix;

        MockConverter(final String prefix) {
            this.prefix = prefix;

        }

        @Override
        public TrainingCenterDatabaseT convert(final File file) throws Exception {
            return null;
        }

        @Override
        public String getFilePrefix() {
            return prefix;
        }

        @Override
        public List<ActivityT> convertActivity(final File file) throws Exception {
            return null;
        }

        @Override
        public String getName() {
            return "name"; //$NON-NLS-1$
        }

    }
}
