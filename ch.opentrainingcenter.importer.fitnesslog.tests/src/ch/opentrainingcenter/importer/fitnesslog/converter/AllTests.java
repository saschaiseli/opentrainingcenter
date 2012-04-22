package ch.opentrainingcenter.importer.fitnesslog.converter;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ ActivityLapConverterTest.class, ConvertFitnesslog2TcxTest.class, ConvertWorkbook2TcxTest.class, TrackPointConverterTest.class })
public class AllTests {

}
