package ch.opentrainingcenter.core.charts;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import ch.opentrainingcenter.core.charts.IStatistikCreator;
import ch.opentrainingcenter.core.charts.internal.StatistikCreator;
import ch.opentrainingcenter.transfer.HeartRate;
import ch.opentrainingcenter.transfer.ITraining;
import ch.opentrainingcenter.transfer.RunData;
import ch.opentrainingcenter.transfer.factory.CommonTransferFactory;

@SuppressWarnings("nls")
public class IStatistikCreatorTest {

    private IStatistikCreator stats;
    private List<ITraining> trainings;

    @Before
    public void setUp() {
        stats = new StatistikCreator();
        trainings = new ArrayList<ITraining>();
        Locale.setDefault(Locale.GERMAN);
    }

    @After
    public void tearDown() {
        trainings.clear();
    }

    @Test
    public void testGetTrainingsProJahrNotNull() {
        final Map<Integer, List<ITraining>> trainingsProJahr = stats.getTrainingsProJahr(trainings);
        assertNotNull("Resultat darf nie null sein", trainingsProJahr); //$NON-NLS-1$
        assertTrue("Kein Training erfasst", trainingsProJahr.isEmpty()); //$NON-NLS-1$
    }

    @Test
    public void testGetTrainingsProJahrEinTraining() {
        trainings.add(createTraining(1974));
        final Map<Integer, List<ITraining>> trainingsProJahr = stats.getTrainingsProJahr(trainings);
        assertNotNull(trainingsProJahr);
        assertFalse("Ein Training erfasst", trainingsProJahr.isEmpty()); //$NON-NLS-1$
        final List<ITraining> list = trainingsProJahr.get(1974);
        assertNotNull("Resultat darf nie null sein", list); //$NON-NLS-1$
        assertEquals("Ein Training im Jahr 1974", 1, list.size()); //$NON-NLS-1$
    }

    @Test
    public void testGetTrainingsProJahrZweiTraining() {
        trainings.add(createTraining(1974));
        trainings.add(createTraining(1975));
        final Map<Integer, List<ITraining>> trainingsProJahr = stats.getTrainingsProJahr(trainings);
        assertNotNull("Resultat darf nie null sein", trainingsProJahr); //$NON-NLS-1$
        assertFalse("Ein Training erfasst", trainingsProJahr.isEmpty()); //$NON-NLS-1$
        assertNotNull("Resultat aus dem 1974 darf nicht null sein", trainingsProJahr.get(1974)); //$NON-NLS-1$
        assertEquals("Ein Training im Jahr 1974", 1, trainingsProJahr.get(1974).size()); //$NON-NLS-1$
        assertNotNull(trainingsProJahr.get(1975));
        assertEquals("Ein Training im Jahr 1975", 1, trainingsProJahr.get(1975).size()); //$NON-NLS-1$
    }

    @Test
    public void testGetTrainingsProJahrZweiTrainingImSelbenJahr() {
        trainings.add(createTraining(1974));
        trainings.add(createTraining(1974));
        final Map<Integer, List<ITraining>> trainingsProJahr = stats.getTrainingsProJahr(trainings);
        assertNotNull("Resultat darf nie null sein", trainingsProJahr); //$NON-NLS-1$
        assertFalse("Ein Training erfasst", trainingsProJahr.isEmpty()); //$NON-NLS-1$
        assertNotNull("Resultat aus dem 1974 darf nicht null sein", trainingsProJahr.get(1974)); //$NON-NLS-1$
        assertEquals("Ein Training im Jahr 1974", 2, trainingsProJahr.get(1974).size()); //$NON-NLS-1$
        assertNull("Im 1975 keine Trainings erfasst", trainingsProJahr.get(1975)); //$NON-NLS-1$
    }

    @Test
    public void testGetTrainingsProMonat() {
        trainings.add(createTraining(1974, 12));
        trainings.add(createTraining(1974, 12));
        final Map<Integer, Map<Integer, List<ITraining>>> trainingsProMonat = stats.getTrainingsProMonat(trainings);
        assertNotNull("Resultat darf nie null sein", trainingsProMonat); //$NON-NLS-1$
    }

    @Test
    public void testGetTrainingsProMonatZweiMonate() {
        trainings.add(createTraining(1974, 11));
        trainings.add(createTraining(1974, 12));
        final Map<Integer, Map<Integer, List<ITraining>>> trainingsProMonat = stats.getTrainingsProMonat(trainings);
        assertNotNull("Resultat darf nie null sein", trainingsProMonat); //$NON-NLS-1$
        final Map<Integer, List<ITraining>> jahr = trainingsProMonat.get(1974);
        assertNotNull("Resultat darf nie null sein", jahr.get(11)); //$NON-NLS-1$
        assertEquals("Im November ist ein Lauf", 1, jahr.get(11).size()); //$NON-NLS-1$
        assertEquals("Im Dezember ist ein Lauf", 1, jahr.get(12).size()); //$NON-NLS-1$
    }

    @Test
    public void testGetTrainingsProMonatZweiJahre() {
        trainings.add(createTraining(1974, 12));
        trainings.add(createTraining(1975, 1));
        final Map<Integer, Map<Integer, List<ITraining>>> trainingsProMonat = stats.getTrainingsProMonat(trainings);
        assertNotNull("Resultat darf nie null sein", trainingsProMonat); //$NON-NLS-1$
        final Map<Integer, List<ITraining>> jahr = trainingsProMonat.get(1974);
        assertNotNull("Resultat darf nie null sein", jahr.get(12)); //$NON-NLS-1$
        assertEquals("Im Dezember ist ein Lauf", 1, jahr.get(12).size()); //$NON-NLS-1$

        final Map<Integer, List<ITraining>> jahr2 = trainingsProMonat.get(1975);
        assertNotNull("Resultat darf nie null sein", jahr2.get(1)); //$NON-NLS-1$
        assertEquals("Im Januar ist ein Lauf", 1, jahr2.get(1).size()); //$NON-NLS-1$
    }

    @Test
    public void testGetTrainingsProWoche_1() {
        trainings.add(createTraining(2012, 2, 19));
        final Map<Integer, Map<Integer, List<ITraining>>> trainingsProMonat = stats.getTrainingsProWoche(trainings);
        for (final Map.Entry<Integer, Map<Integer, List<ITraining>>> entry : trainingsProMonat.entrySet()) {
            final Map<Integer, List<ITraining>> value = entry.getValue();
            for (final Map.Entry<Integer, List<ITraining>> em : value.entrySet()) {
                final List<ITraining> simpletra = em.getValue();
                for (final ITraining t : simpletra) {
                    System.out.println("ITraining Training: " + t);
                }
            }
        }

        assertNotNull("Resultat darf nie null sein", trainingsProMonat); //$NON-NLS-1$
        final Map<Integer, List<ITraining>> jahr = trainingsProMonat.get(2012);
        assertNotNull("Resultat darf nie null sein", jahr.get(7)); //$NON-NLS-1$
        assertEquals("Im Februar ist ein Lauf", 1, jahr.get(7).size()); //$NON-NLS-1$
    }

    @Test
    public void testGetTrainingsProWoche_2() {
        trainings.add(createTraining(2012, 2, 19)); // KW7
        trainings.add(createTraining(2012, 2, 20)); // KW8
        final Map<Integer, Map<Integer, List<ITraining>>> trainingsProMonat = stats.getTrainingsProWoche(trainings);
        assertNotNull("Resultat darf nie null sein", trainingsProMonat); //$NON-NLS-1$
        final Map<Integer, List<ITraining>> jahr = trainingsProMonat.get(2012);
        assertNotNull("Resultat darf nie null sein", jahr.get(7)); //$NON-NLS-1$
        assertEquals("In KW 6 ist ein Lauf", 1, jahr.get(7).size()); //$NON-NLS-1$

        assertNotNull("Resultat darf nie null sein", jahr.get(8)); //$NON-NLS-1$
        assertEquals("In KW 7 ist ein Lauf", 1, jahr.get(8).size()); //$NON-NLS-1$
    }

    @Test
    public void testGetTrainingsProWoche_3_Problem_Mit_ersten_Tag_Im_Jahr() {
        trainings.add(createTraining(2012, 1, 1)); // KW52
        trainings.add(createTraining(2012, 1, 2)); // KW1
        final Map<Integer, Map<Integer, List<ITraining>>> trainingsProMonat = stats.getTrainingsProWoche(trainings);
        assertNotNull("Resultat darf nie null sein", trainingsProMonat); //$NON-NLS-1$
        final Map<Integer, List<ITraining>> jahr2011 = trainingsProMonat.get(2011);
        final Map<Integer, List<ITraining>> jahr2012 = trainingsProMonat.get(2012);
        assertNull("Resultat darf nie null sein", jahr2012.get(52)); //$NON-NLS-1$
        assertEquals("Lauf zaehlt wochentechnisch noch zum 2011. ", 1, jahr2011.size()); //$NON-NLS-1$
        assertEquals("Es darf im 2012 nur ein Lauf haben. ", 1, jahr2012.size()); //$NON-NLS-1$
        assertNotNull("Resultat darf nie null sein", jahr2012.get(1)); //$NON-NLS-1$
        assertEquals("In KW 1 ist ein Lauf", 1, jahr2012.get(1).size()); //$NON-NLS-1$
    }

    private ITraining createTraining(final int year, final int monat, final int day) {
        final Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, day);
        cal.set(Calendar.MONTH, monat - 1);
        cal.set(Calendar.YEAR, year);
        return createSimpleTraining(cal);
    }

    private ITraining createTraining(final int year, final int monat) {
        final Calendar cal = Calendar.getInstance();
        cal.set(Calendar.MONTH, monat - 1);
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.DAY_OF_MONTH, 2);
        return createSimpleTraining(cal);
    }

    private ITraining createTraining(final int year) {
        final Calendar cal = Calendar.getInstance();
        cal.set(Calendar.MONTH, 2);
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.DAY_OF_MONTH, 2);
        return createSimpleTraining(cal);
    }

    private ITraining createSimpleTraining(final Calendar cal) {
        final double distanzInMeter = 0;
        final double dauerInSekunden = 0;
        final int avgHeartRate = 0;
        final int maxHeartRate = 1;
        final double speed = 0;
        final RunData runData = new RunData(cal.getTimeInMillis(), dauerInSekunden, distanzInMeter, speed);
        final HeartRate heartRate = new HeartRate(avgHeartRate, maxHeartRate);
        return CommonTransferFactory.createTraining(runData, heartRate);
    }
}
