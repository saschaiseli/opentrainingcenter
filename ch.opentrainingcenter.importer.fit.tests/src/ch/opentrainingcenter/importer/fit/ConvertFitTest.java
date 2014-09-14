package ch.opentrainingcenter.importer.fit;

import static org.junit.Assert.assertEquals;

import java.io.File;

import org.junit.Before;
import org.junit.Test;

import ch.opentrainingcenter.core.exceptions.ConvertException;

@SuppressWarnings("nls")
public class ConvertFitTest {

    private ConvertFit converter;

    @Before
    public void setUp() {
        converter = new ConvertFit();
    }

    @Test
    public void testRealActivityConvert() throws ConvertException {
        converter.convert(new File("resources/2014_09_11.fit"));
        // converter.convert(new File("resources/settings_fr620.fit"));
    }

    @Test
    // @Ignore
    public void testConvert() throws ConvertException {
        converter.convert(new File("resources/Activity.fit"));
    }

    @Test
    public void testGetFilePrefix() {
        assertEquals("fit", converter.getFilePrefix());
    }

}
