package ch.opentrainingcenter.client.views.navigation;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.eclipse.jface.action.IStatusLineManager;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import ch.opentrainingcenter.client.helper.DistanceHelper;
import ch.opentrainingcenter.client.helper.TimeHelper;
import ch.opentrainingcenter.client.model.navigation.INavigationItem;
import ch.opentrainingcenter.client.model.navigation.impl.ConcreteHealth;
import ch.opentrainingcenter.client.model.navigation.impl.ConcreteImported;
import ch.opentrainingcenter.client.model.navigation.impl.NavigationParent;
import ch.opentrainingcenter.transfer.IHealth;
import ch.opentrainingcenter.transfer.IImported;
import ch.opentrainingcenter.transfer.ITraining;
import ch.opentrainingcenter.transfer.ITrainingType;

@SuppressWarnings("nls")
public class StatusLineWriterTest {
    private IStatusLineManager manager;
    private Date date;
    private StatusLineWriter writer;

    class DummyItem implements INavigationItem {

        @Override
        public int compareTo(final INavigationItem o) {
            return 0;
        }

        @Override
        public String getName() {
            return "";
        }

        @Override
        public Date getDate() {
            return date;
        }

        @Override
        public String getImage() {
            return null;
        }
    }

    @Before
    public void before() {
        manager = Mockito.mock(IStatusLineManager.class);

        final Calendar cal = Calendar.getInstance(Locale.getDefault());
        cal.set(2012, 07, 11, 21, 23, 42);
        date = cal.getTime();
        writer = new StatusLineWriter(manager);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParentOhneChild() {
        final NavigationParent selection = new NavigationParent();
        writer.writeStatusLine(selection);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParentOhneDefiniertesChild() {
        final NavigationParent selection = new NavigationParent();
        selection.add(new DummyItem());
        writer.writeStatusLine(selection);
    }

    @Test
    public void testParentNull() {
        writer.writeStatusLine(null);
        Mockito.verify(manager).setMessage(""); //$NON-NLS-1$
    }

    @Test
    public void testChildMitHealthNurGewicht() {

        final IHealth health = Mockito.mock(IHealth.class);
        Mockito.when(health.getDateofmeasure()).thenReturn(date);
        Mockito.when(health.getWeight()).thenReturn(67.5d);
        Mockito.when(health.getCardio()).thenReturn(null);

        final INavigationItem item = new ConcreteHealth(health);

        // execute
        writer.writeStatusLine(item);

        Mockito.verify(manager).setMessage("Gewicht vom " + TimeHelper.convertDateToString(date, false) + " --> 67.5kg");
    }

    @Test
    public void testChildMitHealthNurCardio() {

        final IHealth health = Mockito.mock(IHealth.class);
        Mockito.when(health.getDateofmeasure()).thenReturn(date);
        Mockito.when(health.getWeight()).thenReturn(null);
        Mockito.when(health.getCardio()).thenReturn(59);

        final INavigationItem item = new ConcreteHealth(health);

        // execute
        writer.writeStatusLine(item);

        Mockito.verify(manager).setMessage("Ruhepuls vom " + TimeHelper.convertDateToString(date, false) + " --> 59bpm");
    }

    @Test
    public void testChildMitHealth() {

        final IHealth health = Mockito.mock(IHealth.class);
        Mockito.when(health.getDateofmeasure()).thenReturn(date);
        Mockito.when(health.getWeight()).thenReturn(67.5d);
        Mockito.when(health.getCardio()).thenReturn(59);

        final INavigationItem item = new ConcreteHealth(health);

        // execute
        writer.writeStatusLine(item);

        Mockito.verify(manager).setMessage("Ruhepuls/Gewicht vom " + TimeHelper.convertDateToString(date, false) + " --> 59bpm / 67.5kg");
    }

    @Test
    public void testChildMitImported() {

        final IImported imported = Mockito.mock(IImported.class);
        Mockito.when(imported.getActivityId()).thenReturn(date);
        final ITrainingType type = Mockito.mock(ITrainingType.class);
        Mockito.when(type.getId()).thenReturn(42);
        Mockito.when(imported.getTrainingType()).thenReturn(type);
        final ITraining training = Mockito.mock(ITraining.class);
        Mockito.when(training.getLaengeInMeter()).thenReturn(1042.4);
        Mockito.when(training.getDauerInSekunden()).thenReturn(54321.6);

        Mockito.when(imported.getTraining()).thenReturn(training);
        final INavigationItem item = new ConcreteImported(imported);

        // execute
        writer.writeStatusLine(item);

        Mockito.verify(manager).setMessage(
                "Lauf vom "
                        + TimeHelper.convertDateToString(date, false)//
                        + " --> Distanz: " + DistanceHelper.roundDistanceFromMeterToKmMitEinheit(1042.4) + " Dauer: "
                        + TimeHelper.convertSecondsToHumanReadableZeit(54321.6));
    }

    @Test
    public void testParentMitImported() {
        final NavigationParent selection = new NavigationParent();
        final IImported imported = Mockito.mock(IImported.class);
        Mockito.when(imported.getActivityId()).thenReturn(date);
        final INavigationItem item = new ConcreteImported(imported);

        selection.add(item);

        // execute
        writer.writeStatusLine(selection);

        Mockito.verify(manager).setMessage("Kalenderwoche 32 Anzahl Läufe: " + 1);
    }

    @Test
    public void testParentMitHealth() {
        final NavigationParent selection = new NavigationParent();
        final IHealth health = Mockito.mock(IHealth.class);
        Mockito.when(health.getDateofmeasure()).thenReturn(date);
        Mockito.when(health.getWeight()).thenReturn(67.5d);
        Mockito.when(health.getCardio()).thenReturn(59);

        final INavigationItem item = new ConcreteHealth(health);

        selection.add(item);

        // execute
        writer.writeStatusLine(selection);

        Mockito.verify(manager).setMessage("Kalenderwoche 32 Anzahl Messdaten: " + 1);
    }

    @Test
    public void testParentMitZweiChild() {
        final NavigationParent selection = new NavigationParent();
        final ConcreteImported imported = Mockito.mock(ConcreteImported.class);
        final ConcreteHealth health = Mockito.mock(ConcreteHealth.class);

        Mockito.when(imported.getActivityId()).thenReturn(date);

        final ConcreteImported item = new ConcreteImported(imported);

        Mockito.when(health.getDate()).thenReturn(date);
        Mockito.when(item.getDate()).thenReturn(date);

        selection.add(health);
        selection.add(item);
        // execute
        writer.writeStatusLine(selection);

        Mockito.verify(manager).setMessage("Kalenderwoche 32 Anzahl Läufe: " + 1 + " Anzahl Messdaten: " + 1);
    }
}
