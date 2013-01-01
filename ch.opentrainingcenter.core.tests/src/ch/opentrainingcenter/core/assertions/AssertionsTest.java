package ch.opentrainingcenter.core.assertions;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

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
}
