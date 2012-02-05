package ch.opentrainingcenter.importer.xml;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;

import javax.xml.bind.JAXBException;

import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;

import ch.opentrainingcenter.importer.internal.xml.ConvertXml;
import ch.opentrainingcenter.tcx.TrainingCenterDatabaseT;

public class ConvertXmlTest {

    private ConvertXml convertXml;

    @Before
    public void setUp() {
        convertXml = new ConvertXml("resources");//$NON-NLS-1$
    }

    @Test
    public void simpleTest() {
        try {
            final TrainingCenterDatabaseT value = convertXml.unmarshall(new File("resources/example.xml"));//$NON-NLS-1$
            assertNotNull(value);
            assertTrue(value.getActivities().getActivity().size() == 1);
        } catch (final JAXBException e) {
            fail("should not throw an exception: " + e.getMessage());//$NON-NLS-1$
        } catch (final SAXException e) {
            fail("should not throw an exception: " + e.getMessage());//$NON-NLS-1$
        }
    }
}
