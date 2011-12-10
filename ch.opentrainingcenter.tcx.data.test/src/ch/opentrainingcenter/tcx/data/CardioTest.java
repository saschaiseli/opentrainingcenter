package ch.opentrainingcenter.tcx.data;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;

import org.junit.Before;
import org.junit.Test;

import ch.iseli.sport4ever.importer.Gmn2Tcx;
import ch.iseli.sportanalyzer.tcx.TrainingCenterDatabaseT;
import ch.opentrainingcenter.tcx.data.internal.CardioImpl;

public class CardioTest {
    private static final String GARMIN_FILE_NO_INTERVALL = "garmin/20110123T113055.gmn";//$NON-NLS-1$
    private static final String GARMIN_FILE_INTERVALL = "garmin/20110814T142321.gmn";//$NON-NLS-1$
    private Gmn2Tcx importer;
    private File file;
    private String path;
    private Cardio cardio;
    private TrainingCenterDatabaseT t_no_intervall;
    private TrainingCenterDatabaseT t_intervall;

    @Before
    public void setUp() throws Exception {
        file = new File(GARMIN_FILE_NO_INTERVALL);
        path = file.getAbsolutePath();
        path = path.replace(GARMIN_FILE_NO_INTERVALL, "resources");//$NON-NLS-1$
        final File f = new File(".txt");//$NON-NLS-1$
        String absolutePath = f.getAbsolutePath();
        final int indexOf = absolutePath.indexOf("ch.");//$NON-NLS-1$
        absolutePath = absolutePath.substring(0, indexOf) + "ch.iseli.sportanalyzer.importer/resources";//$NON-NLS-1$
        importer = new Gmn2Tcx(absolutePath);
        t_no_intervall = importer.convert(new File(GARMIN_FILE_NO_INTERVALL));
        t_intervall = importer.convert(new File(GARMIN_FILE_INTERVALL));
    }

    @Test
    public void testNoIntervall() {
        cardio = new CardioImpl(t_no_intervall);
        assertNotNull(cardio);
        assertTrue("durchschnitt muss grösser null sein", cardio.getAverageCardio() > 0);//$NON-NLS-1$
    }

    @Test
    public void testWithIntervall() {
        cardio = new CardioImpl(t_intervall);
        assertNotNull(cardio);
        assertTrue("durchschnitt muss grösser null sein", cardio.getAverageCardio() > 0);//$NON-NLS-1$
    }

}
