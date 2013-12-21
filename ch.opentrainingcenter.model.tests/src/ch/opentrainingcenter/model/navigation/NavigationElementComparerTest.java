package ch.opentrainingcenter.model.navigation;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;

import ch.opentrainingcenter.model.navigation.internal.KWTraining;
import ch.opentrainingcenter.model.navigation.internal.NavigationParent;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class NavigationElementComparerTest {

    private NavigationElementComparer comparer;

    @Before
    public void setUp() {
        comparer = new NavigationElementComparer();
    }

    @Test
    public void testEqualsObjectObject() {
        final Object a = new Object();
        final Object b = new Object();
        final boolean equals = comparer.equals(a, b);

        assertFalse(equals);
    }

    @Test
    public void testEqualsIntegerObject() {
        final Object a = new Object();
        final Integer b = Integer.valueOf(42);
        final boolean equals = comparer.equals(a, b);

        assertFalse(equals);
    }

    @Test
    public void testEqualsIntegerInteger() {
        final Integer a = Integer.valueOf(42);
        final Integer b = Integer.valueOf(42);

        final boolean result = comparer.equals(a, b);

        assertEquals(true, result);
    }

    @Test
    public void testEqualsKWTrainingKWTraining_NotEquals() {
        final KWTraining a = mock(KWTraining.class);
        final KWTraining b = mock(KWTraining.class);

        final boolean result = comparer.equals(a, b);

        assertFalse(result);
    }

    @Test
    public void testEqualsKWTrainingKWTraining_Equals() {
        final KWTraining a = mock(KWTraining.class);

        final boolean result = comparer.equals(a, a);

        assertTrue(result);
    }

    @Test
    public void testEquals_NavigationParent_NotEquals() {
        final NavigationParent a = mock(NavigationParent.class);
        final KalenderWoche kw1 = mock(KalenderWoche.class);
        when(a.getKalenderWoche()).thenReturn(kw1);

        final NavigationParent b = mock(NavigationParent.class);
        final KalenderWoche kw2 = mock(KalenderWoche.class);
        when(b.getKalenderWoche()).thenReturn(kw2);

        final boolean result = comparer.equals(a, b);

        assertFalse(result);
    }

    @Test
    public void testEquals_NavigationParent_Equals() {
        final NavigationParent a = mock(NavigationParent.class);
        final KalenderWoche kw1 = mock(KalenderWoche.class);
        when(a.getKalenderWoche()).thenReturn(kw1);

        final NavigationParent b = mock(NavigationParent.class);
        when(b.getKalenderWoche()).thenReturn(kw1);

        final boolean result = comparer.equals(a, b);

        assertTrue(result);
    }

    @Test
    public void testEquals_ConcreteImported_NotEquals() {
        final ConcreteImported a = mock(ConcreteImported.class);
        when(a.getDatum()).thenReturn(142L);

        final ConcreteImported b = mock(ConcreteImported.class);
        when(b.getDatum()).thenReturn(1042L);

        final boolean result = comparer.equals(a, b);

        assertFalse(result);
    }

    @Test
    public void testEquals_ConcreteImported_Equals() {
        final ConcreteImported a = mock(ConcreteImported.class);
        when(a.getDatum()).thenReturn(142L);

        final ConcreteImported b = mock(ConcreteImported.class);
        when(b.getDatum()).thenReturn(142L);

        final boolean result = comparer.equals(a, b);

        assertTrue(result);
    }

    @Test
    public void testHashCode_Object() {
        final int hashCode = comparer.hashCode(new Object());
        assertEquals(0, hashCode);
    }

    @Test
    public void testHashCode_Integer() {
        final Integer value = Integer.valueOf(42);

        final int result = comparer.hashCode(value);

        assertEquals(value.hashCode(), result);
    }

    @Test
    public void testHashCode_NavigationParent() {
        final NavigationParent a = mock(NavigationParent.class);
        final KalenderWoche kw1 = new KalenderWoche(new Date(0));
        when(a.getKalenderWoche()).thenReturn(kw1);

        final int result = comparer.hashCode(a);

        assertEquals(kw1.hashCode(), result);
    }

    @Test
    public void testHashCode_ConcreteImported() {
        final ConcreteImported a = mock(ConcreteImported.class);

        final Long value = 42L;
        when(a.getDatum()).thenReturn(value);

        final int result = comparer.hashCode(a);

        assertEquals(value.hashCode(), result);
    }

    @Test
    public void testHashCode_KWTraining() {
        final KWTraining a = mock(KWTraining.class);

        final int result = comparer.hashCode(a);

        assertEquals(a.hashCode(), result);
    }
}
