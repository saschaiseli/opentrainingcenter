package ch.opentrainingcenter.importer.fit;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

@SuppressWarnings("nls")
public class ConvertFitTest {

    private ConvertFit converter;

    @Before
    public void setUp() {
        converter = new ConvertFit();
    }

    @Test
    public void testGetFilePrefix() {
        assertEquals("fit", converter.getFilePrefix());
    }

}
