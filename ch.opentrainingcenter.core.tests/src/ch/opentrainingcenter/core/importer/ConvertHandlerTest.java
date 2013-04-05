package ch.opentrainingcenter.core.importer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import ch.opentrainingcenter.transfer.ITraining;

@SuppressWarnings("nls")
public class ConvertHandlerTest {
    private Map<String, IConvert2Tcx> converters;
    ConvertContainer cc;
    private File source;

    @Before
    public void setUp() {
        converters = new HashMap<String, IConvert2Tcx>();
    }

    @After
    public void after() {
        if (source != null) {
            source.deleteOnExit();
        }
    }

    @Test
    public void testMatchingConverter() throws IOException {
        final IConvert2Tcx converterGarmin = new MockConverter("gmn");
        converters.put(converterGarmin.getFilePrefix(), converterGarmin);
        final IConvert2Tcx converterFitnesslog = new MockConverter("fitnesslog");
        converters.put(converterFitnesslog.getFilePrefix(), converterFitnesslog);
        cc = new ConvertContainer(converters);

        source = File.createTempFile("abc", ".gmn");
        final IConvert2Tcx matchingConverter = cc.getMatchingConverter(source); //$NON-NLS-2$
        assertNotNull(matchingConverter);
        assertEquals("gmn", matchingConverter.getFilePrefix());
    }

    @Test
    public void testSupportedFiles() {
        final IConvert2Tcx converterGarmin = new MockConverter("gmn");
        converters.put(converterGarmin.getFilePrefix(), converterGarmin);
        final IConvert2Tcx converterFitnesslog = new MockConverter("fitnesslog");
        converters.put(converterFitnesslog.getFilePrefix(), converterFitnesslog);
        cc = new ConvertContainer(converters);

        final List<String> supportedFileSuffixes = cc.getSupportedFileSuffixes();
        assertTrue(supportedFileSuffixes.contains("*.gmn"));
        assertTrue(supportedFileSuffixes.contains("*.fitnesslog"));
    }

    class MockConverter implements IConvert2Tcx {

        private final String prefix;

        MockConverter(final String prefix) {
            this.prefix = prefix;

        }

        @Override
        public String getFilePrefix() {
            return prefix;
        }

        @Override
        public List<ITraining> convertActivity(final File file) throws Exception {
            return null;
        }

        @Override
        public String getName() {
            return "name";
        }

    }
}
