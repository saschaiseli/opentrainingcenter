package ch.opentrainingcenter.client.action;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import ch.opentrainingcenter.client.cache.MockDataAccess;
import ch.opentrainingcenter.transfer.CommonTransferFactory;
import ch.opentrainingcenter.transfer.IImported;
import ch.opentrainingcenter.transfer.ITrainingType;

public class DeleteImportedRecordTest {
    private DeleteImportedRecord delete;
    private MockCache cache;
    private MockDataAccess dataAccess;

    @Before
    public void before() {
        cache = new MockCache();
        dataAccess = new MockDataAccess();
    }

    @Test
    public void testConstructor() {
        delete = new DeleteImportedRecord(cache, dataAccess);
    }

    @Test
    public void testRunNoSelection() {
        // prepare
        cache.setSelection(new Object[] {});
        delete = new DeleteImportedRecord(cache, dataAccess);
        // execute
        delete.run();
        // assert
        final List<Date> deletedIds = cache.getDeletedIds();
        assertNotNull("auch wenn nichts gelöscht wird, darf die liste nicht null sein", deletedIds);
        assertEquals("Nichts wurde gelöscht", 0, deletedIds.size());
    }

    @Test
    public void testRunSelection() {
        // prepare
        final IImported selected = CommonTransferFactory.createIImported();
        selected.setId(42);
        final Date activityId = new Date();
        selected.setActivityId(activityId);
        final ITrainingType trainingType = CommonTransferFactory.createTrainingType(11, "junit trainig type", "description");
        selected.setTrainingType(trainingType);
        cache.setSelection(new Object[] { selected });
        delete = new DeleteImportedRecord(cache, dataAccess);
        // execute
        delete.run();
        // assert
        final List<Date> deletedIds = cache.getDeletedIds();
        assertEquals("Ein Record wird gelöscht", 1, deletedIds.size());
        assertEquals("Der Record zum Löschen hat die ID 42", activityId, deletedIds.get(0));
    }

    @Test
    public void testDispose() {
        // prepare
        delete = new DeleteImportedRecord(cache, dataAccess);
        // execute
        delete.dispose();
    }

    @Test
    public void testSelectionChanged() {
        // prepare
        delete = new DeleteImportedRecord(cache, dataAccess);
        // execute
        delete.selectionChanged(null, null);
    }
}
