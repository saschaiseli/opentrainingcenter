package ch.opentrainingcenter.client.model.navigation;

import java.util.ArrayList;
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
        final Collection<Integer> parents = kw.getParents();
        assertEquals("Ein Child an einem Parent", 1, parents.size());
        final Integer parent = parents.iterator().next();
        assertEquals("Das Jahr ist 2012", 2012, parent.intValue());
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
    public void testAnzahlList() {
        final List<INavigationItem> list = new ArrayList<INavigationItem>();
        list.add(createItem(2012, 7, 28)); // kw35
        list.add(createItem(2012, 7, 29)); // kw35
        list.add(createItem(2012, 8, 3)); // kw36

        kw.addItems(list);

        final Collection<Integer> parents = kw.getParents();
        assertEquals("1 Jahr", 1, parents.size());

        final Collection<INavigationParent> weeks = kw.getWeeks(2012);

        final Iterator<INavigationParent> iterator = weeks.iterator();
        final INavigationParent parent = iterator.next();
        final List<INavigationItem> childs = parent.getChilds();
        assertEquals("1 Child vorhanden", 1, childs.size());
        assertEquals("KW36", "KW36", parent.getName());
        assertEquals("2012", 2012, parent.getKalenderWoche().getJahr());

        final INavigationParent parent2 = iterator.next();
        assertEquals("2 Child vorhanden", 2, parent2.getChilds().size());
    }

    @Test
    public void testAnzahl2() {
        kw.addItem(createItem(2012, 7, 28)); // kw35
        kw.addItem(createItem(2012, 7, 29)); // kw35
        kw.addItem(createItem(2012, 8, 3)); // kw36
        final Collection<Integer> parents = kw.getParents();
        assertEquals("1 Jahr", 1, parents.size());

        final Collection<INavigationParent> weeks = kw.getWeeks(2012);

        final Iterator<INavigationParent> iterator = weeks.iterator();
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

        final Collection<Integer> parents = kw.getParents();
        assertEquals("1 Jahr", 1, parents.size());

        final Collection<INavigationParent> weeks = kw.getWeeks(2012);

        final Iterator<INavigationParent> iterator = weeks.iterator();
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
    public void testAnzahl3Remove() {
        final INavigationItem a = createItem(2012, 7, 28);
        final INavigationItem b = createItem(2012, 7, 29);
        final INavigationItem c = createItem(2013, 8, 3);
        kw.addItem(a);
        kw.addItem(b);
        kw.addItem(c);
        kw.removeItem(a);
        kw.removeItem(b);
        kw.removeItem(c);

        final Collection<Integer> parents = kw.getParents();
        assertEquals("0 Jahr", 0, parents.size());

        Collection<INavigationParent> weeks = kw.getWeeks(2012);
        assertEquals("Jahr 2012 leer", 0, weeks.size());

        weeks = kw.getWeeks(2013);
        assertEquals("Jahr 2013 leer", 0, weeks.size());
    }

    @Test
    public void testAnzahl3() {
        kw.addItem(createItem(2012, 11, 24));
        kw.addItem(createItem(2013, 0, 1));

        final Collection<Integer> parents = kw.getParents();
        assertEquals("2 Jahre", 2, parents.size());

        Collection<INavigationParent> weeks = kw.getWeeks(2013);

        final INavigationParent parent2013 = weeks.iterator().next();
        final List<INavigationItem> childs = parent2013.getChilds();
        assertEquals("1 Child vorhanden", 1, childs.size());
        assertEquals("KW1", "KW1", parent2013.getName());

        weeks = kw.getWeeks(2012);
        final INavigationParent parent2012 = weeks.iterator().next();
        final List<INavigationItem> childs2 = parent2012.getChilds();
        assertEquals("1 Child vorhanden", 1, childs2.size());
        assertEquals("KW52", "KW52", parent2012.getName());
    }

    @Test
    public void testAnzahl4() {
        kw.addItem(createItem(2012, 0, 1)); // kw52 --> aber 2011 eintragen!!
        kw.addItem(createItem(2012, 0, 2)); // kw1 --> 2012

        final Collection<Integer> parents = kw.getParents();
        assertEquals("2 Jahre", 2, parents.size());

        Collection<INavigationParent> weeks = kw.getWeeks(2012);

        final INavigationParent parent2012 = weeks.iterator().next();
        final List<INavigationItem> childs = parent2012.getChilds();
        assertEquals("1 Child vorhanden", 1, childs.size());
        assertEquals("KW1", "KW1", parent2012.getName());

        weeks = kw.getWeeks(2011);
        final INavigationParent parent2011 = weeks.iterator().next();
        final List<INavigationItem> childs2 = parent2011.getChilds();
        assertEquals("1 Child vorhanden", 1, childs2.size());
        assertEquals("KW52", "KW52", parent2011.getName());
    }

    @Test
    public void testKW() {
        final INavigationItem item = Mockito.mock(INavigationItem.class);
        cal.set(2012, 07, 29);
        final Date date = cal.getTime();
        Mockito.when(item.getDate()).thenReturn(date);
        kw.addItem(item);

        final Collection<INavigationParent> weeks = kw.getWeeks(2012);

        final INavigationParent parent = weeks.iterator().next();
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
