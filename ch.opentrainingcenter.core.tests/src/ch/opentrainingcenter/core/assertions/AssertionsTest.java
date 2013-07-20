package ch.opentrainingcenter.core.assertions;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

@SuppressWarnings("nls")
public class AssertionsTest {
    @Test(expected = IllegalArgumentException.class)
    public void testNotNull() {
        Assertions.notNull(null);
    }

    @Test()
    public void testNotNullMessage() {
        final String message = "Blabla";
        try {
            Assertions.notNull(null, message);
        } catch (final IllegalArgumentException ex) {
            assertEquals("Message muss in Exception drin sein", message, ex.getMessage());
        }
    }

    @Test()
    public void testNotNullDefaultMessage() {
        final String obj = null;
        try {
            Assertions.notNull(obj);
        } catch (final IllegalArgumentException ex) {
            assertEquals("Message muss in Exception drin sein", "Objekt " + obj + " darf nicht null sein!!", ex.getMessage());
        }
    }

    @Test()
    public void testNotNullTrue() {
        Assertions.notNull("obj");
        assertTrue(true);
    }

    @Test()
    public void testNotNullDefaultMessageTrue() {
        Assertions.notNull("obj", "Message");
        assertTrue(true);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEqualsTrue() {
        Assertions.isValid(true, "blabla");
    }

    @Test
    public void testEqualsFalse() {
        Assertions.isValid(false, "blabla");
        assertTrue(true);
    }
}
