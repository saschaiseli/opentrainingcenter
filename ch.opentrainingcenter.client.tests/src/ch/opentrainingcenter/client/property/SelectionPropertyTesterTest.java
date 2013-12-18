package ch.opentrainingcenter.client.property;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import ch.opentrainingcenter.model.navigation.ConcreteImported;
import ch.opentrainingcenter.model.navigation.INavigationParent;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import static org.mockito.Mockito.mock;

@SuppressWarnings("nls")
public class SelectionPropertyTesterTest {

    private SelectionPropertyTester tester;
    private String property;
    private Object[] args;
    private Object expectedValue;
    private List<Object> receiver;

    @Before
    public void setUp() {
        tester = new SelectionPropertyTester();
        property = "navigationSelection";
        args = null;
        expectedValue = null;
        receiver = new ArrayList<>();
    }

    @Test
    public void testEmpty() {
        final boolean result = tester.test(receiver, property, args, expectedValue);

        assertFalse(result);
    }

    @Test
    public void testEinInteger() {
        receiver.add(Integer.valueOf(42));

        final boolean result = tester.test(receiver, property, args, expectedValue);

        assertFalse(result);
    }

    @Test
    public void testZweiInteger() {
        receiver.add(Integer.valueOf(42));
        receiver.add(Integer.valueOf(43));

        final boolean result = tester.test(receiver, property, args, expectedValue);

        assertFalse(result);
    }

    @Test
    public void testEinNavigationParent() {
        receiver.add(mock(INavigationParent.class));

        final boolean result = tester.test(receiver, property, args, expectedValue);

        assertTrue(result);
    }

    @Test
    public void testZweiNavigationParent() {
        receiver.add(mock(INavigationParent.class));
        receiver.add(mock(INavigationParent.class));

        final boolean result = tester.test(receiver, property, args, expectedValue);

        assertTrue(result);
    }

    @Test
    public void testEinConcreteImported() {
        receiver.add(mock(ConcreteImported.class));

        final boolean result = tester.test(receiver, property, args, expectedValue);

        assertTrue(result);
    }

    @Test
    public void testZweiConcreteImported() {
        receiver.add(mock(ConcreteImported.class));
        receiver.add(mock(ConcreteImported.class));

        final boolean result = tester.test(receiver, property, args, expectedValue);

        assertTrue(result);
    }

    @Test
    public void testZweiUnterschiedliche_1() {
        receiver.add(mock(ConcreteImported.class));
        receiver.add(mock(INavigationParent.class));

        final boolean result = tester.test(receiver, property, args, expectedValue);

        assertFalse(result);
    }

    @Test
    public void testZweiUnterschiedliche_2() {
        receiver.add(mock(INavigationParent.class));
        receiver.add(mock(ConcreteImported.class));

        final boolean result = tester.test(receiver, property, args, expectedValue);

        assertFalse(result);
    }

    @Test
    public void testFalschesObjekt() {
        receiver.add(Long.valueOf(42));

        final boolean result = tester.test(receiver, property, args, expectedValue);

        assertFalse(result);
    }

    @Test
    public void testFalschesObjektMitRIchtigem() {
        receiver.add(mock(ConcreteImported.class));
        receiver.add(Long.valueOf(42));

        final boolean result = tester.test(receiver, property, args, expectedValue);

        assertFalse(result);
    }
}
