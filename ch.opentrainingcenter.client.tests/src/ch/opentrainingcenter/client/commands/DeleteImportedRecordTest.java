package ch.opentrainingcenter.client.commands;

import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import ch.opentrainingcenter.core.db.IDatabaseAccess;
import ch.opentrainingcenter.core.service.IDatabaseService;
import ch.opentrainingcenter.transfer.IAthlete;
import ch.opentrainingcenter.transfer.IRoute;
import ch.opentrainingcenter.transfer.ITraining;
import ch.opentrainingcenter.transfer.factory.CommonTransferFactory;

@SuppressWarnings("nls")
public class DeleteImportedRecordTest {

    DeleteImportedRecord handler;
    private IAthlete athlete;
    private IDatabaseAccess databaseAccess;
    private IRoute unbekannt;

    @Before
    public void setUp() {
        final IDatabaseService service = mock(IDatabaseService.class);
        databaseAccess = mock(IDatabaseAccess.class);
        when(service.getDatabaseAccess()).thenReturn(databaseAccess);
        athlete = mock(IAthlete.class);
        unbekannt = mock(IRoute.class);
        when(unbekannt.getReferenzTrack()).thenReturn(null); // <---- Unbekannt
        when(unbekannt.getName()).thenReturn("Unbekannt");
        handler = new DeleteImportedRecord(service);
    }

    @Test
    public void testRemoveRecords_emptylist() {
        handler.removeRecords(Collections.emptyList(), databaseAccess);
        verifyZeroInteractions(databaseAccess);
    }

    @Test
    public void testRemoveRecords_UnbekannterRecord() {
        // given
        final List<ITraining> list = new ArrayList<>();
        final ITraining training = mock(ITraining.class);
        when(training.getId()).thenReturn(42);
        when(training.getRoute()).thenReturn(unbekannt);
        when(training.getDatum()).thenReturn(142L);
        list.add(training);

        // when
        handler.removeRecords(list, databaseAccess);

        // then
        verify(databaseAccess).removeTrainingByDate(142L);
    }

    @Test
    public void testRemoveRecords_Record_welcher_auch_referenz_ist() {
        // given
        final List<ITraining> list = new ArrayList<>();
        final ITraining training = mock(ITraining.class);
        when(training.getId()).thenReturn(42);
        final IRoute route = mock(IRoute.class);
        when(route.getId()).thenReturn(12345);
        when(route.getReferenzTrack()).thenReturn(training);
        when(training.getRoute()).thenReturn(route);
        when(training.getDatum()).thenReturn(142L);
        list.add(training);

        when(databaseAccess.getAllTrainingByRoute(athlete, route)).thenReturn(list);

        // when
        handler.removeRecords(list, databaseAccess);

        // then
        verify(databaseAccess, never()).deleteRoute(anyInt());
        verify(databaseAccess, never()).removeTrainingByDate(anyLong());
    }

    @Test
    public void testRemoveRecords_Record_welcher_nicht_referenz_ist() {
        // given
        final List<ITraining> list = new ArrayList<>();
        final IRoute routeA = mock(IRoute.class);

        final ITraining trainingB = mock(ITraining.class);
        when(trainingB.getId()).thenReturn(41);
        // final IRoute routeB = mock(IRoute.class);
        // when(routeB.getId()).thenReturn(12346);
        // when(routeB.getReferenzTrack()).thenReturn(trainingA);
        when(trainingB.getRoute()).thenReturn(routeA);
        when(trainingB.getDatum()).thenReturn(1142L);

        final ITraining trainingA = mock(ITraining.class);
        when(trainingA.getId()).thenReturn(42);
        when(routeA.getId()).thenReturn(12345);
        when(routeA.getReferenzTrack()).thenReturn(trainingB);
        when(trainingA.getRoute()).thenReturn(routeA);
        when(trainingA.getDatum()).thenReturn(142L);

        list.add(trainingA);

        // when
        handler.removeRecords(list, databaseAccess);

        // then
        verify(databaseAccess).removeTrainingByDate(142L);
    }

    @Test
    public void testRemoveRecords_Record_Referenz_mit_mehrerenTrainings() {
        // given
        final List<ITraining> list = new ArrayList<>();
        final ITraining trainingA = mock(ITraining.class);
        when(trainingA.getAthlete()).thenReturn(athlete);
        final IRoute routeA = CommonTransferFactory.createRoute("junit", "beschreibung", trainingA);

        final ITraining trainingB = mock(ITraining.class);
        when(trainingB.getId()).thenReturn(41);
        when(trainingB.getRoute()).thenReturn(routeA);
        when(trainingB.getAthlete()).thenReturn(athlete);
        when(trainingB.getDatum()).thenReturn(1142L);

        when(trainingA.getId()).thenReturn(42);
        routeA.setId(12345);
        routeA.setReferenzTrack(trainingA);
        when(trainingA.getRoute()).thenReturn(routeA);
        when(trainingA.getDatum()).thenReturn(142L);

        list.add(trainingA);
        list.add(trainingB);

        when(databaseAccess.getAllTrainingByRoute(athlete, routeA)).thenReturn(list);
        // when
        handler.removeRecords(list, databaseAccess);

        // then
        verify(databaseAccess).removeTrainingByDate(1142L);
        verify(databaseAccess, times(1)).removeTrainingByDate(anyLong());
        verify(databaseAccess, never()).deleteRoute(anyInt());
    }
}
