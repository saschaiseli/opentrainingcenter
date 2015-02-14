package ch.opentrainingcenter.client.views.ngchart;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.preference.IPreferenceStore;
import org.junit.Before;
import org.junit.Test;

import ch.opentrainingcenter.charts.single.XAxisChart;
import ch.opentrainingcenter.core.service.IDatabaseService;
import ch.opentrainingcenter.model.chart.IStatistikCreator;
import ch.opentrainingcenter.transfer.HeartRate;
import ch.opentrainingcenter.transfer.IAthlete;
import ch.opentrainingcenter.transfer.ITraining;
import ch.opentrainingcenter.transfer.RunData;
import ch.opentrainingcenter.transfer.Sport;
import ch.opentrainingcenter.transfer.factory.CommonTransferFactory;

@SuppressWarnings("nls")
public class DynamicChartViewPartTest {

    private IDatabaseService service;
    private IPreferenceStore store;
    private IStatistikCreator statistik;
    private IAthlete athlete;
    private DynamicChartViewPart viewPart;

    @Before
    public void setUp() {
        service = mock(IDatabaseService.class);
        store = mock(IPreferenceStore.class);
        statistik = mock(IStatistikCreator.class);
        athlete = mock(IAthlete.class);
        viewPart = new DynamicChartViewPart(service, store, athlete);
    }

    @Test
    public void testEmpty_Day() {

        final List<ITraining> filteredData = new ArrayList<ITraining>();

        final List<ITraining> result = viewPart.createSum(filteredData, XAxisChart.DAY, statistik);

        assertEquals(Collections.emptyList(), result);
    }

    @Test
    public void test_Day() {

        final List<ITraining> filteredData = new ArrayList<ITraining>();
        filteredData.add(createTraining(0, 10, 1000, 12, 140, 200));
        filteredData.add(createTraining(1, 10, 1000, 12, 140, 200));
        filteredData.add(createTraining(2, 10, 1000, 12, 140, 200));

        when(statistik.getTrainingsProTag(filteredData)).thenReturn(filteredData);

        final List<ITraining> result = viewPart.createSum(filteredData, XAxisChart.DAY, statistik);

        assertEquals(filteredData.size(), result.size());
        assertEquals(filteredData, result);
    }

    @Test
    public void test_YEAR() {

        final List<ITraining> filteredData = new ArrayList<ITraining>();
        filteredData.add(createTraining(0, 10, 1000, 12, 140, 200));
        filteredData.add(createTraining(1, 10, 1000, 12, 150, 203));
        filteredData.add(createTraining(2, 10, 1000, 12, 160, 202));

        final Map<Integer, List<ITraining>> jahr = new HashMap<Integer, List<ITraining>>();
        jahr.put(1970, filteredData);
        when(statistik.getTrainingsProJahr(filteredData)).thenReturn(jahr);

        final List<ITraining> result = viewPart.createSum(filteredData, XAxisChart.YEAR, statistik);

        assertEquals(1, result.size());
        final ITraining summiert = createTraining(2, 30, 3000, 12, 150, 203);
        assertTraining(summiert, result.get(0));
    }

    @Test
    public void test_YEAR_BIS_JETZT() {

        final List<ITraining> filteredData = new ArrayList<ITraining>();
        filteredData.add(createTraining(0, 10, 1000, 12, 140, 200));
        filteredData.add(createTraining(1, 10, 1000, 12, 150, 203));
        filteredData.add(createTraining(2, 10, 1000, 12, 160, 202));

        final Map<Integer, List<ITraining>> jahr = new HashMap<Integer, List<ITraining>>();
        jahr.put(1970, filteredData);
        when(statistik.getTrainingsProJahr(filteredData)).thenReturn(jahr);

        final List<ITraining> result = viewPart.createSum(filteredData, XAxisChart.YEAR_START_TILL_NOW, statistik);

        assertEquals(1, result.size());
        final ITraining summiert = createTraining(2, 30, 3000, 12, 150, 203);
        assertTraining(summiert, result.get(0));
    }

    @Test
    public void testFilter() {
        final List<ITraining> allTrainings = new ArrayList<>();
        final ITraining trainingA = createTraining(1_100_000_000, 100, 123, 23, 233, 345);
        trainingA.setSport(Sport.BIKING);

        final ITraining trainingB = createTraining(1_200_000_000, 100, 123, 23, 233, 345);
        trainingB.setSport(Sport.RUNNING);

        allTrainings.add(trainingA);
        allTrainings.add(trainingB);

        final Date start = new Date(1_000_000_000);
        final Date end = new Date(2_000_000_000);

        final List<ITraining> result = viewPart.filter(allTrainings, start, end, Sport.RUNNING);

        assertEquals(1, result.size());
        assertTraining(trainingB, result.get(0));
    }

    private void assertTraining(final ITraining expected, final ITraining result) {
        assertEquals("Datum", expected.getDatum(), result.getDatum());
        assertEquals("getLaengeInMeter", expected.getLaengeInMeter(), result.getLaengeInMeter(), 0.00001);
        assertEquals("getDauer", expected.getDauer(), result.getDauer(), 0.00001);
        assertEquals("getMaxHeartBeat", expected.getMaxHeartBeat(), result.getMaxHeartBeat());
        assertEquals("getAverageHeartBeat", expected.getAverageHeartBeat(), result.getAverageHeartBeat());
    }

    ITraining createTraining(final long dateOfStart, final double timeInSeconds, final double distanceInMeter, final double maxSpeed, final int average,
            final int max) {
        final RunData runData = new RunData(dateOfStart, timeInSeconds, distanceInMeter, maxSpeed);
        final HeartRate heart = new HeartRate(average, max);
        return CommonTransferFactory.createTraining(runData, heart);
    }
}
