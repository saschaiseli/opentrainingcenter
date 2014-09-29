package ch.opentrainingcenter.db.internal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;

import ch.opentrainingcenter.database.dao.AthleteDao;
import ch.opentrainingcenter.database.dao.ShoeDao;
import ch.opentrainingcenter.transfer.IAthlete;
import ch.opentrainingcenter.transfer.IShoe;
import ch.opentrainingcenter.transfer.factory.CommonTransferFactory;

@SuppressWarnings("nls")
public class ShoeDaoTest extends DatabaseTestBase {

    private ShoeDao shoeDao;
    private IAthlete athlete;

    @Before
    public void setUp() {

        shoeDao = new ShoeDao(connectionConfig);

        final AthleteDao athleteDao = new AthleteDao(connectionConfig);
        athlete = CommonTransferFactory.createAthlete("junit", DateTime.now().toDate(), 220);
        athleteDao.save(athlete);

        connectionConfig.getSession().close();
    }

    @Test
    public void testSaveSchuh() {
        final String schuhName = "Asics";
        final String imageicon = "pathtoimage";
        final IShoe schuh = CommonTransferFactory.createSchuh(athlete, schuhName, imageicon);
        final int id = shoeDao.saveOrUpdate(schuh);
        assertTrue(0 <= id);
        final List<IShoe> schuhe = shoeDao.getShoes(athlete);
        assertNotNull(schuhe);
        assertEquals(schuhName, schuhe.get(0).getSchuhname());
        assertEquals(imageicon, schuhe.get(0).getImageicon());
    }

    @Test
    public void testExistiertSchuh() {
        final String schuhName = "Asics";
        final String imageicon = "pathtoimage";
        final IShoe schuh = CommonTransferFactory.createSchuh(athlete, schuhName, imageicon);
        shoeDao.saveOrUpdate(schuh);

        assertTrue("Schuh muss existieren", shoeDao.exists(athlete, schuhName));
    }

    @Test
    public void testShoeNotFound() {
        final List<IShoe> shoes = shoeDao.getShoes(athlete);
        assertTrue(shoes.isEmpty());
    }

    @Test
    public void testGetSchuh() {
        final String schuhName = "Asics";
        final String imageicon = "pathtoimage";
        final IShoe schuh = CommonTransferFactory.createSchuh(athlete, schuhName, imageicon);
        shoeDao.saveOrUpdate(schuh);

        IShoe result = shoeDao.getShoe(schuhName, athlete);

        assertNotNull(result);
        assertEquals(schuhName, result.getSchuhname());
        assertEquals(imageicon, result.getImageicon());

        result = shoeDao.getShoe(schuhName + "_1", athlete);

        assertNull(result);
    }

    @Test
    public void testUpdateSchuh() {
        final String schuhName = "Asics";
        final String imageicon = "pathtoimage";
        final IShoe schuh = CommonTransferFactory.createSchuh(athlete, schuhName, imageicon);
        int id = shoeDao.saveOrUpdate(schuh);

        schuh.setImageicon("updatedIcon");
        id = shoeDao.saveOrUpdate(schuh);
        assertTrue(0 <= id);
        final List<IShoe> schuhe = shoeDao.getShoes(athlete);
        assertNotNull(schuhe);
        assertEquals(schuhName, schuhe.get(0).getSchuhname());
        assertEquals("updatedIcon", schuhe.get(0).getImageicon());

        assertTrue(schuhe.size() == 1);
    }
}
