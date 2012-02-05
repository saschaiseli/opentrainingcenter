package ch.opentrainingcenter.importer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import ch.opentrainingcenter.importer.ConvertHandler;
import ch.opentrainingcenter.importer.IConvert2Tcx;
import ch.opentrainingcenter.tcx.ActivityT;
import ch.opentrainingcenter.tcx.TrainingCenterDatabaseT;

public class ConvertHandlerTest {
    private ConvertHandler handler;

    @Before
    public void setUp() {
        handler = new ConvertHandler();
    }

    @Test
    public void testMatchingConverter() throws IOException {
        final IConvert2Tcx converterGarmin = new MockConverter("gmn"); //$NON-NLS-1$
        handler.addConverter(converterGarmin);
        final IConvert2Tcx converterFitnesslog = new MockConverter("fitnesslog"); //$NON-NLS-1$
        handler.addConverter(converterFitnesslog);

        final IConvert2Tcx matchingConverter = handler.getMatchingConverter(File.createTempFile("abc", ".gmn")); //$NON-NLS-1$ //$NON-NLS-2$
        assertNotNull(matchingConverter);
        assertEquals("gmn", matchingConverter.getFilePrefix()); //$NON-NLS-1$
    }

    @Test
    public void testSupportedFiles() {
        final IConvert2Tcx converterGarmin = new MockConverter("gmn"); //$NON-NLS-1$
        handler.addConverter(converterGarmin);
        final IConvert2Tcx converterFitnesslog = new MockConverter("fitnesslog"); //$NON-NLS-1$
        handler.addConverter(converterFitnesslog);

        final List<String> supportedFileSuffixes = handler.getSupportedFileSuffixes();
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

    }
}
