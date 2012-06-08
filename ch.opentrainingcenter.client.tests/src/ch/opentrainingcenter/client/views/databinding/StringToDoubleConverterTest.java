package ch.opentrainingcenter.client.views.databinding;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class StringToDoubleConverterTest {
    private StringToDoubleConverter converter;

    @Before
    public void before() {
        converter = new StringToDoubleConverter();
    }

    @Test
    public void getToType() {
        assertEquals("Muss double sein", Double.class, converter.getToType()); //$NON-NLS-1$
    }

    @Test
    public void getFromType() {
        assertEquals("Muss string sein", String.class, converter.getFromType()); //$NON-NLS-1$
    }

    @Test
    public void convert() {
        assertEquals(1.0d, converter.convert("1.0")); //$NON-NLS-1$
    }

    @Test(expected = NumberFormatException.class)
    public void convertException() {
        converter.convert("abc"); //$NON-NLS-1$
    }
}
