package ch.opentrainingcenter.importer.fitnesslog.converter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;

import javax.xml.bind.JAXBException;

import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;

import ch.opentrainingcenter.importer.fitnesslog.model.Activity;
import ch.opentrainingcenter.importer.fitnesslog.model.AthleteLog;
import ch.opentrainingcenter.importer.fitnesslog.model.FitnessWorkbook;

public class ConvertFitnesslog2TcxTest {
    private ConvertFitnesslog2Tcx convertFitnesslog2Tcx;

    @Before
    public void setUp() {
        convertFitnesslog2Tcx = new ConvertFitnesslog2Tcx(true);
    }

    @Test
    public void testSuffix() {
        assertEquals("fitlog", convertFitnesslog2Tcx.getFilePrefix()); //$NON-NLS-1$
    }

    @Test
    public void testUnmarshall() throws JAXBException, SAXException {
        final File gpBern = new File("resources/gpbern.logfile"); //$NON-NLS-1$
        assertTrue(gpBern.exists());
        final FitnessWorkbook workbook = convertFitnesslog2Tcx.unmarshall(gpBern);
        assertNotNull(workbook);
        final AthleteLog athleteLog = workbook.getAthleteLog().get(0);
        final Activity activity = athleteLog.getActivity().get(0);
        assertNotNull(activity);
    }

    @Test
    public void testConvertTwoFitlogs() throws JAXBException, SAXException {
        final File gpBern = new File("resources/twoActivities.fitlog"); //$NON-NLS-1$
        assertTrue(gpBern.exists());
        final FitnessWorkbook workbook = convertFitnesslog2Tcx.unmarshall(gpBern);
        assertNotNull(workbook);
        final AthleteLog athleteLog = workbook.getAthleteLog().get(0);
        assertEquals(2, athleteLog.getActivity().size());
        // final Activity activity = athleteLog.getActivity().get(0);
        // assertNotNull(activity);
    }
}
