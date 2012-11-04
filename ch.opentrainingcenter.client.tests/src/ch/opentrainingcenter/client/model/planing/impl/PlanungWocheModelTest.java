package ch.opentrainingcenter.client.model.planing.impl;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import ch.opentrainingcenter.transfer.CommonTransferFactory;
import ch.opentrainingcenter.transfer.IAthlete;
import ch.opentrainingcenter.transfer.IPlanungWoche;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@SuppressWarnings("nls")
public class PlanungWocheModelTest {

    private List<IPlanungWoche> planungen;
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
        planungen = new ArrayList<IPlanungWoche>();
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
        planungen = new ArrayList<IPlanungWoche>();
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

        planungen = new ArrayList<IPlanungWoche>();
        final IPlanungWoche eins = CommonTransferFactory.createPlanungWoche(athlete, jahr, 12, 42);
        final IPlanungWoche zwei = CommonTransferFactory.createPlanungWoche(athlete, jahr, 13, 84);
        planungen.add(eins);
        planungen.add(zwei);

        model = new PlanungWocheModel(planungen, athlete, jahr, kwStart, anzahl);

        assertPlanung(model, 2012, 12, 42, true);
        assertPlanung(model, 2012, 13, 84, true);

        assertEquals("Anzahl 2", 2, model.size());
    }

    @Test
    public void testUpdateNotEmpty() {
        jahr = 2012;
        anzahl = 5;
        kwStart = 50;

        planungen = new ArrayList<IPlanungWoche>();
        final IPlanungWoche eins = CommonTransferFactory.createPlanungWoche(athlete, jahr, 12, 42);
        final IPlanungWoche zwei = CommonTransferFactory.createPlanungWoche(athlete, jahr, 13, 84);
        final IPlanungWoche drei = CommonTransferFactory.createPlanungWoche(athlete, jahr, 42, 1042);
        planungen.add(eins);
        planungen.add(zwei);

        model = new PlanungWocheModel(planungen, athlete, jahr, kwStart, anzahl);

        assertPlanung(model, 2012, 12, 42, true);
        assertPlanung(model, 2012, 13, 84, true);

        eins.setActive(false);
        model.addOrUpdate(eins);
        zwei.setActive(false);
        model.addOrUpdate(zwei);
        model.addOrUpdate(drei);

        assertEquals("Anzahl 3", 3, model.size());

        assertPlanung(model, 2012, 12, 42, false);
        assertPlanung(model, 2012, 13, 84, false);
        assertPlanung(model, 2012, 42, 1042, true);
    }

    @Test
    public void testIteratorNotEmpty() {
        jahr = 2012;
        anzahl = 5;
        kwStart = 50;

        planungen = new ArrayList<IPlanungWoche>();
        final IPlanungWoche eins = CommonTransferFactory.createPlanungWoche(athlete, jahr, 12, 42);
        final IPlanungWoche zwei = CommonTransferFactory.createPlanungWoche(athlete, jahr, 13, 84);
        final IPlanungWoche drei = CommonTransferFactory.createPlanungWoche(athlete, jahr, 42, 1042);
        planungen.add(eins);
        planungen.add(zwei);

        model = new PlanungWocheModel(planungen, athlete, jahr, kwStart, anzahl);

        assertPlanung(model, 2012, 12, 42, true);
        assertPlanung(model, 2012, 13, 84, true);

        eins.setActive(false);
        model.addOrUpdate(eins);
        zwei.setActive(false);
        model.addOrUpdate(zwei);
        model.addOrUpdate(drei);

        assertEquals("Anzahl 3", 3, model.size());

        assertPlanung(model, 2012, 12, 42, false);
        assertPlanung(model, 2012, 13, 84, false);
        assertPlanung(model, 2012, 42, 1042, true);

        assertNotNull("Iterator muss vorhanden sein", model.iterator());
    }

    // Helpers
    private void assertPlanung(final PlanungWocheModel wochenModel, final int j, final int k, final int km, final boolean active) {
        final IPlanungWoche planung = wochenModel.getPlanung(j, k);
        assertTrue("Jahr: ", j == planung.getJahr());
        assertTrue("Kalenderwoche: ", k == planung.getKw());
        assertTrue("Default Km/woche: ", km == planung.getKmProWoche());
        assertEquals("Active: ", active, planung.isActive());
        assertFalse("Interval: ", planung.isInterval());
    }

    private void assertEmptyPlanung(final PlanungWocheModel wochenModel, final int j, final int k) {
        final IPlanungWoche planung = wochenModel.getPlanung(j, k);
        assertTrue("Jahr: ", j == planung.getJahr());
        assertTrue("Kalenderwoche: ", k == planung.getKw());
        assertTrue("Default Km/woche: ", 0 == planung.getKmProWoche());
        assertTrue("Active: ", planung.isActive());
        assertFalse("Interval: ", planung.isInterval());
    }
}
