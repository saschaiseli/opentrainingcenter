package ch.opentrainingcenter.model.navigation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import ch.opentrainingcenter.model.ModelFactory;
import ch.opentrainingcenter.transfer.ITraining;
import static org.junit.Assert.assertEquals;

import static org.mockito.Mockito.mock;

public class NavigationSelectionTest {
    private NavigationSelection selection;

    @Before
    public void setUp() {
        selection = new NavigationSelection();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConvertSelectionToTrainingsNull() {
        selection.convertSelectionToTrainings(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConvertSelectionToTrainingsEmpty() {
        selection.convertSelectionToTrainings(Collections.emptyList());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConvertSelectionToTrainings_Mit_Andereren_Instanzen() {
        final List<Object> input = new ArrayList<>();

        input.add(new ConcreteImported(mock(ITraining.class)));
        input.add(Long.valueOf(42));

        final List<ITraining> result = selection.convertSelectionToTrainings(input);

        assertEquals(1, result.size());
    }

    @Test
    public void testConvertSelectionToTrainings_Parent() {
        final List<INavigationParent> input = new ArrayList<>();

        final INavigationParent parent = ModelFactory.createNavigationParent();
        parent.add(new ConcreteImported(mock(ITraining.class)));
        input.add(parent);

        final List<ITraining> result = selection.convertSelectionToTrainings(input);

        assertEquals(1, result.size());
    }

    @Test
    public void testConvertSelectionToTrainings_1_Parent_2_Items() {
        final List<INavigationParent> input = new ArrayList<>();

        final INavigationParent parent = ModelFactory.createNavigationParent();
        parent.add(new ConcreteImported(mock(ITraining.class)));
        parent.add(new ConcreteImported(mock(ITraining.class)));
        input.add(parent);

        final List<ITraining> result = selection.convertSelectionToTrainings(input);

        assertEquals(2, result.size());
    }

    @Test
    public void testConvertSelectionToTrainings_2_Parent_2_Items() {
        final List<INavigationParent> input = new ArrayList<>();

        final INavigationParent parentA = ModelFactory.createNavigationParent();
        parentA.add(new ConcreteImported(mock(ITraining.class)));
        parentA.add(new ConcreteImported(mock(ITraining.class)));

        final INavigationParent parentB = ModelFactory.createNavigationParent();
        parentB.add(new ConcreteImported(mock(ITraining.class)));
        parentB.add(new ConcreteImported(mock(ITraining.class)));

        input.add(parentA);
        input.add(parentB);

        final List<ITraining> result = selection.convertSelectionToTrainings(input);

        assertEquals(4, result.size());
    }

    @Test
    public void testConvertSelectionToTrainings_2_Items() {
        final List<ConcreteImported> input = new ArrayList<>();

        input.add(new ConcreteImported(mock(ITraining.class)));
        input.add(new ConcreteImported(mock(ITraining.class)));

        final List<ITraining> result = selection.convertSelectionToTrainings(input);

        assertEquals(2, result.size());
    }
}
