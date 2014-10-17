package ch.opentrainingcenter.model.navigation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import org.eclipse.osgi.util.NLS;
import org.joda.time.DateTimeZone;
import org.junit.Before;
import org.junit.Test;

import ch.opentrainingcenter.i18n.Messages;
import ch.opentrainingcenter.transfer.ILapInfo;
import ch.opentrainingcenter.transfer.ITraining;
import ch.opentrainingcenter.transfer.Sport;
import ch.opentrainingcenter.transfer.TrainingType;

@SuppressWarnings("nls")
public class ConcreteImportedTest {

    @Before
    public void setUp() {
        Locale.setDefault(Locale.GERMAN);
        TimeZone.setDefault(TimeZone.getTimeZone("GMT+1"));

        final DateTimeZone zoneUTC = DateTimeZone.forID("Europe/Berlin");
        DateTimeZone.setDefault(zoneUTC);
    }

    @Test
    public void testName() {
        final Calendar cal = Calendar.getInstance(Locale.getDefault());
        cal.set(2012, 0, 4, 13, 22, 59);

        final ITraining imported = mock(ITraining.class);
        when(imported.getDatum()).thenReturn(cal.getTime().getTime());
        final ConcreteImported imp = new ConcreteImported(imported);

        final String result = imp.getName();

        assertEquals("04.01.2012", result);
    }

    @Test
    public void testTooltipRunning() {
        final Calendar cal = Calendar.getInstance(Locale.getDefault());
        cal.set(2012, 0, 4, 13, 22, 59);

        final ITraining imported = mock(ITraining.class);
        when(imported.getDatum()).thenReturn(cal.getTime().getTime());
        when(imported.getSport()).thenReturn(Sport.RUNNING);
        when(imported.getLaengeInMeter()).thenReturn(12345.6789);
        final List<ILapInfo> lapInfos = new ArrayList<>();
        final ILapInfo lapInfo = mock(ILapInfo.class);
        lapInfos.add(lapInfo);
        when(imported.getLapInfos()).thenReturn(lapInfos);
        final ConcreteImported imp = new ConcreteImported(imported);

        final String result = imp.getTooltip();

        assertEquals(NLS.bind(Messages.NAVIGATION_TOOLTIP_RUNNING, "04.01.2012", "12.346"), result);
    }

    @Test
    public void testTooltipRunningRunden() {
        final Calendar cal = Calendar.getInstance(Locale.getDefault());
        cal.set(2012, 0, 4, 13, 22, 59);

        final ITraining imported = mock(ITraining.class);
        when(imported.getDatum()).thenReturn(cal.getTime().getTime());
        when(imported.getSport()).thenReturn(Sport.RUNNING);
        when(imported.getLaengeInMeter()).thenReturn(12345.6789);
        final List<ILapInfo> lapInfos = new ArrayList<>();
        final ILapInfo lapInfo = mock(ILapInfo.class);
        lapInfos.add(lapInfo);
        lapInfos.add(lapInfo);
        when(imported.getLapInfos()).thenReturn(lapInfos);
        final ConcreteImported imp = new ConcreteImported(imported);

        final String result = imp.getTooltip();

        assertEquals(NLS.bind(Messages.NAVIGATION_TOOLTIP_RUNNING_RUNDEN, new Object[] { "04.01.2012", "12.346", "2" }), result);
    }

    @Test
    public void testTooltipOTHER() {
        final Calendar cal = Calendar.getInstance(Locale.getDefault());
        cal.set(2012, 0, 4, 13, 22, 59);

        final ITraining imported = mock(ITraining.class);
        when(imported.getDatum()).thenReturn(cal.getTime().getTime());
        when(imported.getSport()).thenReturn(Sport.BIKING);
        when(imported.getLaengeInMeter()).thenReturn(12345.6789);
        final List<ILapInfo> lapInfos = new ArrayList<>();
        final ILapInfo lapInfo = mock(ILapInfo.class);
        lapInfos.add(lapInfo);
        when(imported.getLapInfos()).thenReturn(lapInfos);
        final ConcreteImported imp = new ConcreteImported(imported);

        final String result = imp.getTooltip();

        assertEquals(NLS.bind(Messages.NAVIGATION_TOOLTIP_OTHER, "04.01.2012", "12.346"), result);
    }

    @Test
    public void testImage() {
        final Calendar cal = Calendar.getInstance(Locale.getDefault());
        cal.set(2012, 0, 4, 13, 22, 59);

        final ITraining training = mock(ITraining.class);
        final TrainingType trainingType = TrainingType.NONE;

        when(training.getSport()).thenReturn(Sport.RUNNING);
        when(training.getTrainingType()).thenReturn(trainingType);

        final ConcreteImported imp = new ConcreteImported(training);

        assertEquals(TrainingType.NONE.getImage(), imp.getImage());
    }

    @Test
    public void testImageBike() {
        final Calendar cal = Calendar.getInstance(Locale.getDefault());
        cal.set(2012, 0, 4, 13, 22, 59);

        final ITraining training = mock(ITraining.class);
        final TrainingType trainingType = TrainingType.NONE;

        when(training.getTrainingType()).thenReturn(trainingType);
        when(training.getSport()).thenReturn(Sport.BIKING);

        final ConcreteImported imp = new ConcreteImported(training);

        assertEquals(Sport.BIKING.getImageIcon(), imp.getImage());
    }

    @Test
    public void testImported() {
        final ITraining training = mock(ITraining.class);

        final ConcreteImported imp = new ConcreteImported(training);

        assertEquals(training, imp.getTraining());
    }

    @Test
    public void testToString() {
        final ITraining training = mock(ITraining.class);

        final ConcreteImported imp = new ConcreteImported(training);

        assertTrue(imp.toString().startsWith("ConcreteImported ["));
    }

    @Test
    public void testDatum() {
        final ITraining training = mock(ITraining.class);
        when(training.getDatum()).thenReturn(42L);

        final ConcreteImported imp = new ConcreteImported(training);

        assertEquals(42, imp.getDatum());
    }

    @Test
    public void testSetDatum() {
        final ITraining training = mock(ITraining.class);

        final ConcreteImported imp = new ConcreteImported(training);
        imp.setDatum(42);

        verify(training).setDatum(42);
    }

    @Test
    public void testDateOfImport() {
        final ITraining training = mock(ITraining.class);
        final Date date = new Date();
        when(training.getDateOfImport()).thenReturn(date);

        final ConcreteImported imp = new ConcreteImported(training);

        assertEquals(date, imp.getDateOfImport());
    }

    @Test
    public void testSetDateOfImport() {
        final ITraining training = mock(ITraining.class);
        final Date date = new Date();
        final ConcreteImported imp = new ConcreteImported(training);
        imp.setDateOfImport(date);

        verify(training).setDateOfImport(date);
    }

    @Test
    public void testLaenge() {
        final ITraining training = mock(ITraining.class);
        final double laenge = 142.01;
        when(training.getLaengeInMeter()).thenReturn(laenge);

        final ConcreteImported imp = new ConcreteImported(training);

        assertEquals(laenge, imp.getLaengeInMeter(), 0.001);
    }

    @Test
    public void testSetLaenge() {
        final ITraining training = mock(ITraining.class);
        final double laenge = 142.01;
        final ConcreteImported imp = new ConcreteImported(training);
        imp.setLaengeInMeter(laenge);

        verify(training).setLaengeInMeter(laenge);
    }

    @Test
    public void testAverageHeartBeat() {
        final ITraining training = mock(ITraining.class);
        final int herz = 142;
        when(training.getAverageHeartBeat()).thenReturn(herz);

        final ConcreteImported imp = new ConcreteImported(training);

        assertEquals(herz, imp.getAverageHeartBeat());
    }

    @Test
    public void testSetAverageHeartBeat() {
        final ITraining training = mock(ITraining.class);
        final int herz = 142;
        final ConcreteImported imp = new ConcreteImported(training);
        imp.setAverageHeartBeat(herz);

        verify(training).setAverageHeartBeat(herz);
    }

    @Test
    public void testMaxHeartBeat() {
        final ITraining training = mock(ITraining.class);
        final int herz = 142;
        when(training.getMaxHeartBeat()).thenReturn(herz);

        final ConcreteImported imp = new ConcreteImported(training);

        assertEquals(herz, imp.getMaxHeartBeat());
    }

    @Test
    public void testSetMaxHeartBeat() {
        final ITraining training = mock(ITraining.class);
        final int herz = 142;
        final ConcreteImported imp = new ConcreteImported(training);
        imp.setMaxHeartBeat(herz);

        verify(training).setMaxHeartBeat(herz);
    }

    @Test
    public void testSpeed() {
        final ITraining training = mock(ITraining.class);
        final double speed = 142;
        when(training.getMaxSpeed()).thenReturn(speed);

        final ConcreteImported imp = new ConcreteImported(training);

        assertEquals(speed, imp.getMaxSpeed(), 0.001);
    }

    @Test
    public void testSetSpeed() {
        final ITraining training = mock(ITraining.class);
        final double speed = 142;
        final ConcreteImported imp = new ConcreteImported(training);
        imp.setMaxSpeed(speed);

        verify(training).setMaxSpeed(speed);
    }

    @Test
    public void testDauer() {
        final ITraining training = mock(ITraining.class);
        final double dauer = 142;
        when(training.getDauer()).thenReturn(dauer);

        final ConcreteImported imp = new ConcreteImported(training);

        assertEquals(dauer, imp.getDauer(), 0.001);
    }

    @Test
    public void testSetDauer() {
        final ITraining training = mock(ITraining.class);
        final double dauer = 142;
        final ConcreteImported imp = new ConcreteImported(training);
        imp.setDauer(dauer);

        verify(training).setDauer(dauer);
    }

    @Test
    public void testNote() {
        final ITraining training = mock(ITraining.class);
        final String note = "senselesstest";
        when(training.getNote()).thenReturn(note);

        final ConcreteImported imp = new ConcreteImported(training);

        assertEquals(note, imp.getNote());
    }

    @Test
    public void testSetNote() {
        final ITraining training = mock(ITraining.class);
        final String note = "senselesstest";
        final ConcreteImported imp = new ConcreteImported(training);
        imp.setNote(note);

        verify(training).setNote(note);
    }

    @Test
    public void testUp() {
        final ITraining training = mock(ITraining.class);
        final Integer mum = Integer.valueOf(42);
        when(training.getUpMeter()).thenReturn(mum);

        final ConcreteImported imp = new ConcreteImported(training);

        assertEquals(mum, imp.getUpMeter());
    }

    @Test
    public void testSetUp() {
        final ITraining training = mock(ITraining.class);
        final Integer mum = Integer.valueOf(42);
        final ConcreteImported imp = new ConcreteImported(training);
        imp.setUpMeter(mum);

        verify(training).setUpMeter(mum);
    }

    @Test
    public void testDown() {
        final ITraining training = mock(ITraining.class);
        final Integer mum = Integer.valueOf(42);
        when(training.getDownMeter()).thenReturn(mum);

        final ConcreteImported imp = new ConcreteImported(training);

        assertEquals(mum, imp.getDownMeter());
    }

    @Test
    public void testSetDown() {
        final ITraining training = mock(ITraining.class);
        final Integer mum = Integer.valueOf(42);
        final ConcreteImported imp = new ConcreteImported(training);
        imp.setDownMeter(mum);

        verify(training).setDownMeter(mum);
    }

    @Test
    public void testOrder() {
        final Calendar cal = Calendar.getInstance(Locale.getDefault());
        cal.set(2012, 0, 4, 13, 22, 59);

        final ITraining imported1 = mock(ITraining.class);
        when(imported1.getDatum()).thenReturn(cal.getTime().getTime());
        final ConcreteImported imp1 = new ConcreteImported(imported1);

        final Calendar cal2 = Calendar.getInstance(Locale.getDefault());
        cal2.set(2012, 0, 5, 13, 22, 59);

        final ITraining imported2 = mock(ITraining.class);
        when(imported2.getDatum()).thenReturn(cal2.getTime().getTime());
        final ConcreteImported imp2 = new ConcreteImported(imported2);

        assertEquals("Der erste Record ist älter, daher auch grösser", 1, imp1.compareTo(imp2));
    }
}
