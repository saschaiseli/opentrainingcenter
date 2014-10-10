package ch.opentrainingcenter.client.views.navigation;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.eclipse.jface.action.IStatusLineManager;
import org.junit.Before;
import org.junit.Test;

import ch.opentrainingcenter.i18n.Messages;
import ch.opentrainingcenter.model.navigation.ConcreteHealth;
import ch.opentrainingcenter.model.navigation.ConcreteImported;
import ch.opentrainingcenter.model.navigation.INavigationItem;
import ch.opentrainingcenter.model.navigation.INavigationParent;

@SuppressWarnings({ "nls", "rawtypes", "unchecked" })
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

        @Override
        public String getTooltip() {
            return null;
        }

        @Override
        public double getLaengeInMeter() {
            return 0;
        }
    }

    @Before
    public void before() {
        manager = mock(IStatusLineManager.class);

        final Calendar cal = Calendar.getInstance(Locale.getDefault());
        cal.set(2012, 07, 11, 21, 23, 42);
        date = cal.getTime();
        writer = new StatusLineWriter(manager);
    }

    @Test
    public void testParentNull() {
        writer.writeStatusLine(null);
        verify(manager).setMessage("");
    }

    @Test
    public void testParent1Training() {
        final ConcreteImported trainingA = mock(ConcreteImported.class);
        when(trainingA.getLaengeInMeter()).thenReturn(100D);
        when(trainingA.getDauer()).thenReturn(200D);
        writer.writeStatusLine(new Object[] { trainingA });
        verify(manager).setMessage("Anzahl Läufe: 1 Distanz total: 0.100km Dauer total: 00:03:20");
    }

    @Test
    public void testParent2Training() {
        final ConcreteImported trainingA = mock(ConcreteImported.class);
        when(trainingA.getLaengeInMeter()).thenReturn(100D);
        when(trainingA.getDauer()).thenReturn(200D);
        final ConcreteImported trainingB = mock(ConcreteImported.class);
        when(trainingB.getLaengeInMeter()).thenReturn(1100D);
        when(trainingB.getDauer()).thenReturn(1200D);
        writer.writeStatusLine(new Object[] { trainingA, trainingB });
        verify(manager).setMessage("Anzahl Läufe: 2 Distanz total: 1.200km Dauer total: 00:23:20");
    }

    @Test
    public void testParent2Training_selberParent() {
        final ConcreteImported trainingA = mock(ConcreteImported.class);
        when(trainingA.getLaengeInMeter()).thenReturn(100D);
        when(trainingA.getDauer()).thenReturn(200D);
        final ConcreteImported trainingB = mock(ConcreteImported.class);
        when(trainingB.getLaengeInMeter()).thenReturn(1100D);
        when(trainingB.getDauer()).thenReturn(1200D);
        final INavigationParent parent = mock(INavigationParent.class);
        final List list = Arrays.asList(trainingA, trainingB);
        when(parent.getChilds()).thenReturn(list);
        writer.writeStatusLine(new Object[] { parent });
        verify(manager).setMessage("Anzahl Läufe: 2 Distanz total: 1.200km Dauer total: 00:23:20");
    }

    @Test
    public void testParent1Health() {
        final ConcreteHealth healthA = mock(ConcreteHealth.class);
        when(healthA.getDate()).thenReturn(new Date(900_000_000));
        when(healthA.getTooltip()).thenReturn("Blabla");
        writer.writeStatusLine(new Object[] { healthA });
        verify(manager).setMessage("11.01.1970 Blabla");
    }

    @Test
    public void testParent2Health() {
        final ConcreteHealth healthA = mock(ConcreteHealth.class);
        when(healthA.getDate()).thenReturn(new Date(900_000_000));
        when(healthA.getTooltip()).thenReturn("Blabla");
        final ConcreteHealth healthB = mock(ConcreteHealth.class);
        when(healthA.getDate()).thenReturn(new Date(900_000_000));
        when(healthA.getTooltip()).thenReturn("Hello");
        writer.writeStatusLine(new Object[] { healthA, healthB });
        verify(manager).setMessage(Messages.StatusLineWriter_LetzteVitaldaten + "11.01.1970 Hello");
    }

    @Test
    public void testParent2Training_2Healths_selberParent() {
        final ConcreteImported trainingA = mock(ConcreteImported.class);
        when(trainingA.getLaengeInMeter()).thenReturn(100D);
        when(trainingA.getDauer()).thenReturn(200D);
        final ConcreteImported trainingB = mock(ConcreteImported.class);
        when(trainingB.getLaengeInMeter()).thenReturn(1100D);
        when(trainingB.getDauer()).thenReturn(1200D);
        final INavigationParent parent = mock(INavigationParent.class);
        final List list = Arrays.asList(trainingA, trainingB);
        when(parent.getChilds()).thenReturn(list);
        final ConcreteHealth healthA = mock(ConcreteHealth.class);
        when(healthA.getDate()).thenReturn(new Date(900_000_000));
        when(healthA.getTooltip()).thenReturn("Blabla");
        final ConcreteHealth healthB = mock(ConcreteHealth.class);
        when(healthA.getDate()).thenReturn(new Date(900_000_000));
        when(healthA.getTooltip()).thenReturn("Hello");
        writer.writeStatusLine(new Object[] { parent, healthA, healthB });
        verify(manager).setMessage("Anzahl Läufe: 2 Distanz total: 1.200km Dauer total: 00:23:20");
    }
}
