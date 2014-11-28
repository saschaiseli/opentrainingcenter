package ch.opentrainingcenter.client.commands.runtype;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import org.eclipse.core.commands.ExecutionException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import ch.opentrainingcenter.client.views.ApplicationContext;
import ch.opentrainingcenter.core.cache.Cache;
import ch.opentrainingcenter.core.db.IDatabaseAccess;
import ch.opentrainingcenter.core.service.IDatabaseService;
import ch.opentrainingcenter.model.navigation.ConcreteImported;
import ch.opentrainingcenter.transfer.ITraining;
import ch.opentrainingcenter.transfer.TrainingType;

public class ChangeRunTypeTest {

    private ChangeRunType change;
    private IDatabaseService service;
    private Cache cache;

    @Before
    public void setUp() {
        service = mock(IDatabaseService.class);
        cache = mock(Cache.class);
        change = new ExtIntervall(service, cache);
    }

    @After
    public void after() {
        ApplicationContext.getApplicationContext().setSelection(new Object[] {});
    }

    @Test
    public void testType() {
        assertEquals(TrainingType.EXT_INTERVALL, change.getType());
    }

    @Test
    public void testExecuteNull() throws ExecutionException {
        ApplicationContext.getApplicationContext().setSelection(null);

        change.execute(null);

        verifyZeroInteractions(service);
        verifyZeroInteractions(cache);
    }

    @Test
    public void testExecuteEmpty() throws ExecutionException {
        ApplicationContext.getApplicationContext().setSelection(new Object[] {});

        change.execute(null);

        verifyZeroInteractions(service);
        verifyZeroInteractions(cache);
    }

    @Test
    public void testExecuteOneElement() throws ExecutionException {
        final ConcreteImported concreteImported = mock(ConcreteImported.class);
        final ITraining training = mock(ITraining.class);
        when(concreteImported.getTraining()).thenReturn(training);

        final IDatabaseAccess access = mock(IDatabaseAccess.class);
        when(service.getDatabaseAccess()).thenReturn(access);

        ApplicationContext.getApplicationContext().setSelection(new Object[] { concreteImported });

        change.execute(null);

        verify(access).updateTrainingType(training, TrainingType.EXT_INTERVALL.getIndex());
    }

    @Test
    public void testExecuteTwoElements() throws ExecutionException {
        final ConcreteImported concreteImportedA = mock(ConcreteImported.class);
        final ITraining trainingA = mock(ITraining.class);
        when(concreteImportedA.getTraining()).thenReturn(trainingA);

        final ConcreteImported concreteImportedB = mock(ConcreteImported.class);
        final ITraining trainingB = mock(ITraining.class);
        when(concreteImportedB.getTraining()).thenReturn(trainingB);

        final IDatabaseAccess access = mock(IDatabaseAccess.class);
        when(service.getDatabaseAccess()).thenReturn(access);

        ApplicationContext.getApplicationContext().setSelection(new Object[] { concreteImportedA, concreteImportedB });

        change.execute(null);

        verify(access).updateTrainingType(trainingA, TrainingType.EXT_INTERVALL.getIndex());
        verify(access).updateTrainingType(trainingB, TrainingType.EXT_INTERVALL.getIndex());
    }
}
