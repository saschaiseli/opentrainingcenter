package ch.opentrainingcenter.model.planing;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import ch.opentrainingcenter.core.helper.RunType;
import ch.opentrainingcenter.model.planing.internal.PastPlanungModel;
import ch.opentrainingcenter.transfer.IAthlete;
import ch.opentrainingcenter.transfer.IPlanungWoche;
import ch.opentrainingcenter.transfer.ITraining;
import ch.opentrainingcenter.transfer.ITrainingType;

@SuppressWarnings("nls")
public class PastPlanungModelTest {

    private PastPlanungModel model;
    private IAthlete athlete;
    private KwJahrKey now;

    @Before
    public void before() {
        athlete = Mockito.mock(IAthlete.class);
        Mockito.when(athlete.getName()).thenReturn("Junit");
        now = new KwJahrKey(2012, 45);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBeideNull() {
        new PastPlanungModel(null, null, now);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPlanungLeerRecordsNull() {
        final List<IPlanungWoche> planungen = new ArrayList<IPlanungWoche>();
        new PastPlanungModel(planungen, null, now);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPlanungNullRecordsLeer() {
        final List<ITraining> records = new ArrayList<ITraining>();
        new PastPlanungModel(null, records, now);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPlanungLeerRecordsLeer() {
        final List<IPlanungWoche> planungen = new ArrayList<IPlanungWoche>();
        final List<ITraining> records = new ArrayList<ITraining>();
        new PastPlanungModel(planungen, records, now);
    }

    @Test
    public void testPlanungKeineRecords() {
        final List<IPlanungWoche> planungen = new ArrayList<IPlanungWoche>();
        final IPlanungWoche planung = Mockito.mock(IPlanungWoche.class);

        Mockito.when(planung.getAthlete()).thenReturn(athlete);
        Mockito.when(planung.getJahr()).thenReturn(2012);
        Mockito.when(planung.getKw()).thenReturn(44);

        planungen.add(planung);
        model = new PastPlanungModel(planungen, null, now);
        final List<IPastPlanung> list = model.getPastPlanungen();
        assertEquals("Eine Planung vorhanden", 1, list.size());
        final IPastPlanung pastPlanung = list.get(0);
        final IPlanungWoche planungModel = pastPlanung.getPlanung();
        assertEquals(2012, planungModel.getJahr());
        assertEquals(44, planungModel.getKw());
        assertEquals("Keine Records --> Initiale Werte", 0, pastPlanung.getKmEffective());
        assertEquals("Keine Records --> Initiale Werte", 0, pastPlanung.getLangerLaufEffective());
        assertEquals("Keine Records --> Initiale Werte", false, pastPlanung.hasInterval());
    }

    @Test
    public void testEinRecordKeinePlanung() {
        final List<ITraining> list = new ArrayList<ITraining>();
        final Calendar cal = Calendar.getInstance(Locale.getDefault());
        cal.set(2012, 07, 28);
        list.add(createImported(cal.getTime(), 10300d, RunType.INT_INTERVALL));
        model = new PastPlanungModel(null, list, now);

        final List<IPastPlanung> pastPlanungen = model.getPastPlanungen();
        assertEquals("Ein Record vorhanden", 1, pastPlanungen.size());

        final IPastPlanung pastPlanung = pastPlanungen.get(0);

        final IPlanungWoche planung = pastPlanung.getPlanung();
        assertNotNull("Planung darf nicht null sein, auch wenn keine da ist..", planung);
        final IAthlete athlete2 = planung.getAthlete();
        assertEquals("Athlete muss in der leeren planung gesetzt sein", "Junit", athlete2.getName());

        assertEquals("Keine Planung --> Initiale Werte", 0, planung.getKmProWoche());
        assertEquals("Keine Planung --> Initiale Werte", 0, planung.getLangerLauf());
        assertEquals("Keine Planung --> Initiale Werte", false, planung.isInterval());

        assertEquals("Effektive Distanz", 10, pastPlanung.getKmEffective());
        assertEquals("Effektive Distanz", 10, pastPlanung.getLangerLaufEffective());
        assertEquals("Effektive Distanz", true, pastPlanung.hasInterval());
    }

    @Test
    public void testPopulateFehlerMitErstem1Januar2012() {
        final List<ITraining> list = new ArrayList<ITraining>();
        final Calendar cal = Calendar.getInstance(Locale.GERMAN);
        cal.set(2012, 00, 01);
        list.add(createImported(cal.getTime(), 10300d, RunType.INT_INTERVALL));

        // execute
        model = new PastPlanungModel(null, list, now);

        final List<IPastPlanung> pastPlanungen = model.getPastPlanungen();
        assertEquals("Ein Record vorhanden", 1, pastPlanungen.size());
        final IPastPlanung pastPlanung = pastPlanungen.get(0);

        final IPlanungWoche planung = pastPlanung.getPlanung();

        assertEquals("1 Januar 2012 ist in KW52", 52, planung.getKw());
        assertEquals("1 Januar 2012 ist in KW52 aber 2011!!!", 2011, planung.getJahr());
    }

    @Test
    public void testZweiRecordsKeinePlanung() {
        final List<ITraining> list = new ArrayList<ITraining>();
        final Calendar cal = Calendar.getInstance(Locale.getDefault());
        cal.set(2012, 07, 28);
        list.add(createImported(cal.getTime(), 10300d, RunType.INT_INTERVALL));
        cal.set(2012, 07, 29);
        list.add(createImported(cal.getTime(), 20300d, RunType.INT_INTERVALL));
        model = new PastPlanungModel(null, list, now);

        final List<IPastPlanung> pastPlanungen = model.getPastPlanungen();
        assertEquals("Ein Record vorhanden", 1, pastPlanungen.size());

        final IPastPlanung pastPlanung = pastPlanungen.get(0);

        final IPlanungWoche planung = pastPlanung.getPlanung();
        assertNotNull("Planung darf nicht null sein, auch wenn keine da ist..", planung);
        final IAthlete athlete2 = planung.getAthlete();
        assertEquals("Athlete muss in der leeren planung gesetzt sein", "Junit", athlete2.getName());

        assertEquals("Keine Planung --> Initiale Werte", 0, planung.getKmProWoche());
        assertEquals("Keine Planung --> Initiale Werte", 0, planung.getLangerLauf());
        assertEquals("Keine Planung --> Initiale Werte", false, planung.isInterval());

        assertEquals("Effektive Distanz", 30, pastPlanung.getKmEffective());
        assertEquals("Effektive Distanz", 20, pastPlanung.getLangerLaufEffective());
        assertEquals("Effektive Distanz", true, pastPlanung.hasInterval());
    }

    @Test
    public void testZweiRecordsInZweiWochenKeinePlanung() {
        final List<ITraining> list = new ArrayList<ITraining>();
        final Calendar cal = Calendar.getInstance(Locale.getDefault());
        cal.set(2012, 07, 28);
        list.add(createImported(cal.getTime(), 10300d, RunType.INT_INTERVALL));
        cal.set(2012, 8, 05);
        list.add(createImported(cal.getTime(), 20300d, RunType.INT_INTERVALL));
        model = new PastPlanungModel(null, list, now);

        final List<IPastPlanung> pastPlanungen = model.getPastPlanungen();

        final IPastPlanung pastPlanung1 = pastPlanungen.get(0);

        final IPlanungWoche planung1 = pastPlanung1.getPlanung();
        assertNotNull("Planung darf nicht null sein, auch wenn keine da ist..", planung1);
        final IAthlete athlete3 = planung1.getAthlete();
        assertEquals("Athlete muss in der leeren planung gesetzt sein", "Junit", athlete3.getName());

        assertEquals("Keine Planung --> Initiale Werte", 0, planung1.getKmProWoche());
        assertEquals("Keine Planung --> Initiale Werte", 0, planung1.getLangerLauf());
        assertEquals("Keine Planung --> Initiale Werte", false, planung1.isInterval());

        assertEquals("Effektive Distanz", 20, pastPlanung1.getKmEffective());
        assertEquals("Effektive Distanz", 20, pastPlanung1.getLangerLaufEffective());
        assertEquals("Effektive Distanz", true, pastPlanung1.hasInterval());

        assertEquals("Zwei Record vorhanden", 2, pastPlanungen.size());

        final IPastPlanung pastPlanung = pastPlanungen.get(1);

        final IPlanungWoche planung = pastPlanung.getPlanung();
        assertNotNull("Planung darf nicht null sein, auch wenn keine da ist..", planung);
        final IAthlete athlete2 = planung.getAthlete();
        assertEquals("Athlete muss in der leeren planung gesetzt sein", "Junit", athlete2.getName());

        assertEquals("Keine Planung --> Initiale Werte", 0, planung.getKmProWoche());
        assertEquals("Keine Planung --> Initiale Werte", 0, planung.getLangerLauf());
        assertEquals("Keine Planung --> Initiale Werte", false, planung.isInterval());

        assertEquals("Effektive Distanz", 10, pastPlanung.getKmEffective());
        assertEquals("Effektive Distanz", 10, pastPlanung.getLangerLaufEffective());
        assertEquals("Effektive Distanz", true, pastPlanung.hasInterval());
    }

    @Test
    public void testEinRecordEinePlanungPassend() {
        final List<ITraining> list = new ArrayList<ITraining>();
        final Calendar cal = Calendar.getInstance(Locale.getDefault());
        cal.set(2012, 07, 28);
        list.add(createImported(cal.getTime(), 10300d, RunType.INT_INTERVALL));

        final List<IPlanungWoche> pl = new ArrayList<IPlanungWoche>();
        final IPlanungWoche p = createPlanung(2012, 35, 40, 22, false);

        pl.add(p);

        model = new PastPlanungModel(pl, list, now);

        final List<IPastPlanung> pastPlanungen = model.getPastPlanungen();
        assertEquals("Ein Record vorhanden", 1, pastPlanungen.size());

        final IPastPlanung pastPlanung = pastPlanungen.get(0);

        final IPlanungWoche planung = pastPlanung.getPlanung();
        assertNotNull("Planung darf nicht null sein, auch wenn keine da ist..", planung);
        final IAthlete athlete2 = planung.getAthlete();
        assertEquals("Athlete muss in der leeren planung gesetzt sein", "Junit", athlete2.getName());

        assertEquals("Keine Planung --> Initiale Werte", 40, planung.getKmProWoche());
        assertEquals("Keine Planung --> Initiale Werte", 22, planung.getLangerLauf());
        assertEquals("Keine Planung --> Initiale Werte", false, planung.isInterval());

        assertEquals("Effektive Distanz", 10, pastPlanung.getKmEffective());
        assertEquals("Effektive Distanz", 10, pastPlanung.getLangerLaufEffective());
        assertEquals("Effektive Distanz", true, pastPlanung.hasInterval());
    }

    @Test
    public void testSortiertePlanung() {

        final List<IPlanungWoche> pl = new ArrayList<IPlanungWoche>();
        final IPlanungWoche p1 = createPlanung(2012, 36, 1, 22, false);
        final IPlanungWoche p2 = createPlanung(2012, 35, 2, 22, false);
        final IPlanungWoche p3 = createPlanung(2012, 37, 3, 22, false);
        pl.add(p1);
        pl.add(p2);
        pl.add(p3);

        model = new PastPlanungModel(pl, null, now);
        final List<IPastPlanung> pastPlanungen = model.getPastPlanungen();
        final IPastPlanung pp1 = pastPlanungen.get(0);
        final IPastPlanung pp2 = pastPlanungen.get(1);
        final IPastPlanung pp3 = pastPlanungen.get(2);

        assertEquals("Neuster Eintrag zuerst", 37, pp1.getPlanung().getKw());
        assertEquals("Neuster Eintrag zuerst", 36, pp2.getPlanung().getKw());
        assertEquals("Neuster Eintrag zuerst", 35, pp3.getPlanung().getKw());
    }

    @Test
    public void testSortiertePlanungMitJahr() {

        final List<IPlanungWoche> pl = new ArrayList<IPlanungWoche>();
        final IPlanungWoche p1 = createPlanung(2011, 36, 1, 22, false);
        final IPlanungWoche p2 = createPlanung(2012, 2, 2, 22, false);
        final IPlanungWoche p3 = createPlanung(2011, 37, 3, 22, false);
        pl.add(p1);
        pl.add(p2);
        pl.add(p3);

        model = new PastPlanungModel(pl, null, now);
        final List<IPastPlanung> pastPlanungen = model.getPastPlanungen();
        final IPastPlanung pp1 = pastPlanungen.get(0);
        final IPastPlanung pp2 = pastPlanungen.get(1);
        final IPastPlanung pp3 = pastPlanungen.get(2);

        assertEquals("Neuster Eintrag zuerst", 2, pp1.getPlanung().getKw());
        assertEquals("Neuster Eintrag zuerst", 37, pp2.getPlanung().getKw());
        assertEquals("Neuster Eintrag zuerst", 36, pp3.getPlanung().getKw());
    }

    @Test
    public void testKeinePlaeneInDerZukunft() {

        final List<IPlanungWoche> pl = new ArrayList<IPlanungWoche>();

        final IPlanungWoche p1 = createPlanung(2012, 36, 1, 22, false);
        final IPlanungWoche p2 = createPlanung(2012, 43, 2, 22, false);
        final IPlanungWoche p3 = createPlanung(2012, 44, 3, 22, false);
        final IPlanungWoche p4 = createPlanung(2012, 45, 4, 22, false);
        // ---- diese dürfen nicht mehr berücksichtigt werden
        final IPlanungWoche p5 = createPlanung(2012, 46, 5, 22, false);
        pl.add(p1);
        pl.add(p2);
        pl.add(p3);
        pl.add(p4);
        pl.add(p5);

        model = new PastPlanungModel(pl, null, now);
        final List<IPastPlanung> pastPlanungen = model.getPastPlanungen();
        assertEquals("Die KW46 darf nicht erscheinen", 4, pastPlanungen.size());

        final IPastPlanung pp1 = pastPlanungen.get(0);
        final IPastPlanung pp2 = pastPlanungen.get(1);
        final IPastPlanung pp3 = pastPlanungen.get(2);
        final IPastPlanung pp4 = pastPlanungen.get(3);

        assertEquals("Neuster Eintrag zuerst", 45, pp1.getPlanung().getKw());
        assertEquals("Neuster Eintrag zuerst", 44, pp2.getPlanung().getKw());
        assertEquals("Neuster Eintrag zuerst", 43, pp3.getPlanung().getKw());
        assertEquals("Neuster Eintrag zuerst", 36, pp4.getPlanung().getKw());
    }

    private IPlanungWoche createPlanung(final int jahr, final int kw, final Integer km, final Integer langerLauf, final boolean inter) {
        final IPlanungWoche p = Mockito.mock(IPlanungWoche.class);
        Mockito.when(p.getAthlete()).thenReturn(athlete);
        Mockito.when(p.getJahr()).thenReturn(jahr);
        Mockito.when(p.getKw()).thenReturn(kw);
        Mockito.when(p.getKmProWoche()).thenReturn(km);
        Mockito.when(p.getLangerLauf()).thenReturn(langerLauf);
        Mockito.when(p.isInterval()).thenReturn(inter);
        return p;
    }

    private ITraining createImported(final Date date, final Double distanzInMeter, final RunType runType) {
        final ITraining rec = Mockito.mock(ITraining.class);
        Mockito.when(rec.getAthlete()).thenReturn(athlete);
        Mockito.when(rec.getDatum()).thenReturn(date.getTime());
        Mockito.when(rec.getLaengeInMeter()).thenReturn(distanzInMeter);

        final ITrainingType type = Mockito.mock(ITrainingType.class);
        Mockito.when(type.getId()).thenReturn(runType.getIndex());
        Mockito.when(rec.getTrainingType()).thenReturn(type);
        return rec;
    }
}
