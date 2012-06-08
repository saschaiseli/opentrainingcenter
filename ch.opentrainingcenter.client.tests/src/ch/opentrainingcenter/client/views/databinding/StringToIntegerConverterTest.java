package ch.opentrainingcenter.client.views.databinding;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class StringToIntegerConverterTest {
    private StringToIntegerConverter converter;

    @Before
    public void before() {
        converter = new StringToIntegerConverter();
    }

    @Test
    public void getToType() {
        assertEquals("Muss integer sein", Integer.class, converter.getToType()); //$NON-NLS-1$
    }

    @Test
    public void getFromType() {
        assertEquals("Muss string sein", String.class, converter.getFromType()); //$NON-NLS-1$
    }

    @Test
    public void convert() {
        assertEquals(1, converter.convert("1")); //$NON-NLS-1$
    }

    @Test(expected = NumberFormatException.class)
    public void convertException() {
        converter.convert("abc"); //$NON-NLS-1$
    }
}
