package ch.opentrainingcenter.model.sportler;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;

import java.beans.PropertyChangeSupport;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;

@SuppressWarnings("nls")
public class HealthModelTest {

    private static final int A_DATE = 1_000_000_000;
    private PropertyChangeSupport propertyChangeSupport;
    private HealthModel model;

    @Before
    public void setUp() {
        propertyChangeSupport = mock(PropertyChangeSupport.class);
        model = new HealthModel(propertyChangeSupport);
    }

    @Test
    public void testDate() {
        model.setDateOfMeasure(new Date(A_DATE));

        verifyZeroInteractions(propertyChangeSupport);

        assertEquals(new Date(A_DATE), model.getDateOfMeasure());
    }

    @Test
    public void testCardio() {
        model.setRuhePuls(42);

        verify(propertyChangeSupport).firePropertyChange("ruhePuls", null, 42);

        assertEquals(42, model.getRuhePuls().intValue());
    }

    @Test
    public void testGewicht() {
        model.setWeight(42d);

        verify(propertyChangeSupport).firePropertyChange("weight", null, 42d);

        assertEquals(42, model.getWeight().intValue());
    }
}
