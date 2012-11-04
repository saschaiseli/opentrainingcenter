package ch.opentrainingcenter.client.model.planing.impl;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import ch.opentrainingcenter.client.model.ModelFactory;
import ch.opentrainingcenter.transfer.IAthlete;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@SuppressWarnings("nls")
public class PlanungWocheModelTest {

    private List<PlanungModel> planungen;
    private IAthlete athlete;
    private PlanungWocheModel model;
    private int jahr;
    private int kwStart;
    private int anzahl;

    @Before
    public void before() {
        athlete = Mockito.mock(IAthlete.class);
    }

    @Test
    public void testPopulateEmpty() {
        planungen = new ArrayList<PlanungModel>();
        jahr = 2012;
        anzahl = 5;
        kwStart = 44;
        model = new PlanungWocheModel(planungen, athlete, jahr, kwStart, anzahl);

        assertEmptyPlanung(model, 2012, 44);
        assertEmptyPlanung(model, 2012, 45);
        assertEmptyPlanung(model, 2012, 46);
        assertEmptyPlanung(model, 2012, 47);
        assertEmptyPlanung(model, 2012, 48);

        assertEquals("Anzahl 5", 5, model.size());
    }

    @Test
    public void testPopulateEmptyNull() {
        jahr = 2012;
        anzahl = 5;
        kwStart = 44;
        model = new PlanungWocheModel(null, athlete, jahr, kwStart, anzahl);

        assertEmptyPlanung(model, 2012, 44);
        assertEmptyPlanung(model, 2012, 45);
        assertEmptyPlanung(model, 2012, 46);
        assertEmptyPlanung(model, 2012, 47);
        assertEmptyPlanung(model, 2012, 48);

        assertEquals("Anzahl 5", 5, model.size());
    }

    @Test
    public void testPopulateEmptyJahrUebergreifend() {
        planungen = new ArrayList<PlanungModel>();
        jahr = 2012;
        anzahl = 5;
        kwStart = 50;
        model = new PlanungWocheModel(planungen, athlete, jahr, kwStart, anzahl);

        assertEmptyPlanung(model, 2012, 50);
        assertEmptyPlanung(model, 2012, 51);
        assertEmptyPlanung(model, 2012, 52);
        assertEmptyPlanung(model, 2013, 1);
        assertEmptyPlanung(model, 2013, 2);

        assertEquals("Anzahl 5", 5, model.size());
    }

    @Test
    public void testPopulateNotEmpty() {
        jahr = 2012;
        anzahl = 5;
        kwStart = 50;

        planungen = new ArrayList<PlanungModel>();
        final PlanungModel eins = ModelFactory.createPlanungModel(athlete, jahr, 12, 42);
        final PlanungModel zwei = ModelFactory.createPlanungModel(athlete, jahr, 13, 84);
        planungen.add(eins);
        planungen.add(zwei);

        model = new PlanungWocheModel(planungen, athlete, jahr, kwStart, anzahl);

        assertEquals("Anzahl 5", anzahl, model.size());

        assertPlanung(model, 2012, 12, 42);
        assertPlanung(model, 2012, 13, 84);
        // diese müssen noch neu erstellt werden
        assertPlanung(model, 2012, 14, 0);
        assertPlanung(model, 2012, 15, 0);
        assertPlanung(model, 2012, 16, 0);

    }

    @Test
    public void testUpdateNotEmpty() {
        jahr = 2012;
        anzahl = 5;
        kwStart = 50;

        planungen = new ArrayList<PlanungModel>();
        final PlanungModel eins = ModelFactory.createPlanungModel(athlete, jahr, 12, 42);
        final PlanungModel zwei = ModelFactory.createPlanungModel(athlete, jahr, 13, 84);
        final PlanungModel drei = ModelFactory.createPlanungModel(athlete, jahr, 14, 1042);
        planungen.add(eins);
        planungen.add(zwei);

        model = new PlanungWocheModel(planungen, athlete, jahr, kwStart, anzahl);

        assertPlanung(model, 2012, 12, 42);
        assertPlanung(model, 2012, 13, 84);

        model.addOrUpdate(eins);
        zwei.setKmProWoche(10000);
        model.addOrUpdate(zwei);
        model.addOrUpdate(drei);

        assertEquals("Anzahl 5", anzahl, model.size());

        assertPlanung(model, 2012, 12, 42);
        assertPlanung(model, 2012, 13, 10000);
        assertPlanung(model, 2012, 14, 1042);
        assertPlanung(model, 2012, 15, 0);
        assertPlanung(model, 2012, 16, 0);
    }

    @Test
    public void testIteratorNotEmpty() {
        jahr = 2012;
        anzahl = 5;
        kwStart = 50;

        planungen = new ArrayList<PlanungModel>();
        final PlanungModel eins = ModelFactory.createPlanungModel(athlete, jahr, 12, 42);
        final PlanungModel zwei = ModelFactory.createPlanungModel(athlete, jahr, 13, 84);
        final PlanungModel drei = ModelFactory.createPlanungModel(athlete, jahr, 14, 1042);
        planungen.add(eins);
        planungen.add(zwei);

        model = new PlanungWocheModel(planungen, athlete, jahr, kwStart, anzahl);

        assertEquals("Anzahl 5", anzahl, model.size());

        assertPlanung(model, 2012, 12, 42);
        assertPlanung(model, 2012, 13, 84);
        assertPlanung(model, 2012, 14, 0);
        assertPlanung(model, 2012, 15, 0);
        assertPlanung(model, 2012, 16, 0);

        model.addOrUpdate(eins);
        model.addOrUpdate(zwei);
        model.addOrUpdate(drei);

        assertPlanung(model, 2012, 12, 42);
        assertPlanung(model, 2012, 13, 84);
        assertPlanung(model, 2012, 14, 1042);
        assertPlanung(model, 2012, 15, 0);
        assertPlanung(model, 2012, 16, 0);

        assertNotNull("Iterator muss vorhanden sein", model.iterator());
    }

    // Helpers
    private void assertPlanung(final PlanungWocheModel wochenModel, final int j, final int k, final int km) {
        final PlanungModel planung = wochenModel.getPlanung(j, k);
        assertTrue("Jahr: ", j == planung.getJahr());
        assertTrue("Kalenderwoche: ", k == planung.getKw());
        assertTrue("Default Km/woche: ", km == planung.getKmProWoche());
        assertFalse("Interval: ", planung.isInterval());
    }

    private void assertEmptyPlanung(final PlanungWocheModel wochenModel, final int j, final int k) {
        final PlanungModel planung = wochenModel.getPlanung(j, k);
        assertTrue("Jahr: ", j == planung.getJahr());
        assertTrue("Kalenderwoche: ", k == planung.getKw());
        assertTrue("Default Km/woche: ", 0 == planung.getKmProWoche());
        assertFalse("Interval: ", planung.isInterval());
    }
}
