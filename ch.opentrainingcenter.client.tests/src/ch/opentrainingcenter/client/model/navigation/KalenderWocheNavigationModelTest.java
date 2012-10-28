package ch.opentrainingcenter.client.model.navigation;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import ch.opentrainingcenter.client.model.KalenderWoche;
import ch.opentrainingcenter.client.model.navigation.impl.KWTraining;
import static org.junit.Assert.assertEquals;

@SuppressWarnings("nls")
public class KalenderWocheNavigationModelTest {

    private IKalenderWocheNavigationModel kw;
    private Calendar cal;

    @Before
    public void before() {
        kw = new KWTraining();
        cal = Calendar.getInstance(Locale.getDefault());
    }

    @Test
    public void testAnzahl1() {
        final INavigationItem item = Mockito.mock(INavigationItem.class);
        cal.set(2012, 07, 29);
        final Date date = cal.getTime();
        Mockito.when(item.getDate()).thenReturn(date);
        kw.addItem(item);
        final Collection<INavigationParent> parents = kw.getParents();
        assertEquals("Ein Child an einem Parent", 1, parents.size());
        final INavigationParent parent = parents.iterator().next();
        assertEquals("Ein Child vorhanden", 1, parent.getChilds().size());
    }

    @Test
    public void testAnzahl1Remove() {
        final INavigationItem item = Mockito.mock(INavigationItem.class);
        cal.set(2012, 07, 29);
        final Date date = cal.getTime();
        Mockito.when(item.getDate()).thenReturn(date);
        kw.addItem(item);
        kw.removeItem(item);
        assertEquals("Keine Parents", 0, kw.getParents().size());
    }

    @Test
    public void testAnzahl2() {
        kw.addItem(createItem(2012, 7, 28)); // kw35
        kw.addItem(createItem(2012, 7, 29)); // kw35
        kw.addItem(createItem(2012, 8, 3)); // kw36
        final Collection<INavigationParent> parents = kw.getParents();
        assertEquals("2 KWs abgefüllt", 2, parents.size());
        final Iterator<INavigationParent> iterator = parents.iterator();
        final INavigationParent parent = iterator.next();
        final List<INavigationItem> childs = parent.getChilds();
        assertEquals("1 Child vorhanden", 1, childs.size());
        assertEquals("KW36", "KW36", parent.getName());
        assertEquals("2012", 2012, parent.getKalenderWoche().getJahr());

        final INavigationParent parent2 = iterator.next();
        assertEquals("2 Child vorhanden", 2, parent2.getChilds().size());
    }

    @Test
    public void testAnzahl2Remove() {
        kw.addItem(createItem(2012, 7, 28)); // KW35
        final INavigationItem itemToRemove = createItem(2012, 7, 29); // KW35
        kw.addItem(itemToRemove);
        kw.addItem(createItem(2012, 8, 3)); // KW36
        kw.removeItem(itemToRemove);

        final Collection<INavigationParent> parents = kw.getParents();
        assertEquals("2 KWs abgefüllt", 2, parents.size());

        final Iterator<INavigationParent> iterator = parents.iterator();
        // der erste wird kw36 sein
        final INavigationParent parent = iterator.next();
        final List<INavigationItem> childs = parent.getChilds();
        assertEquals("1 Child vorhanden", 1, childs.size());
        assertEquals("KW36", "KW36", parent.getName());
        assertEquals("2012", 2012, parent.getKalenderWoche().getJahr());

        final INavigationParent parent2 = iterator.next();
        assertEquals("1 Child vorhanden", 1, parent2.getChilds().size());
    }

    @Test
    public void testAnzahl3() {
        kw.addItem(createItem(2012, 11, 24));
        kw.addItem(createItem(2013, 0, 1));
        final Collection<INavigationParent> parents = kw.getParents();
        assertEquals("2 KWs abgefüllt", 2, parents.size());
        final Iterator<INavigationParent> iterator = parents.iterator();

        final INavigationParent parent2 = iterator.next();
        final List<INavigationItem> childs2 = parent2.getChilds();
        assertEquals("1 Child vorhanden", 1, childs2.size());
        assertEquals("KW1", "KW1 - 2013", parent2.getName());
        assertEquals("2013", 2013, parent2.getKalenderWoche().getJahr());

        final INavigationParent parent = iterator.next();
        final List<INavigationItem> childs = parent.getChilds();
        assertEquals("1 Child vorhanden", 1, childs.size());
        assertEquals("KW52", "KW52 - 2012", parent.getName());
        assertEquals("2012", 2012, parent.getKalenderWoche().getJahr());
    }

    @Test
    public void testKW() {
        final INavigationItem item = Mockito.mock(INavigationItem.class);
        cal.set(2012, 07, 29);
        final Date date = cal.getTime();
        Mockito.when(item.getDate()).thenReturn(date);
        kw.addItem(item);
        final Collection<INavigationParent> parents = kw.getParents();
        final INavigationParent parent = parents.iterator().next();
        final KalenderWoche kwoche = parent.getKalenderWoche();
        assertEquals("KalenderWoche", 35, kwoche.getKw());
        assertEquals("KalenderWoche", 2012, kwoche.getJahr());
    }

    private INavigationItem createItem(final int year, final int month, final int day) {
        final INavigationItem item = Mockito.mock(INavigationItem.class);
        cal.set(year, month, day);
        final Date date = cal.getTime();
        Mockito.when(item.getDate()).thenReturn(date);
        return item;
    }

}
