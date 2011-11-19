package ch.iseli.sportanalyzer.client.action;

import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;

import ch.iseli.sportanalyzer.client.cache.TrainingCenterDataCache;
import ch.iseli.sportanalyzer.client.cache.TrainingCenterDatabaseTParent;
import ch.iseli.sportanalyzer.client.helper.DaoHelper;
import ch.iseli.sportanalyzer.db.IImportedDao;

public class DeleteImportedRecord extends Action implements ISelectionListener, IWorkbenchAction {

    public static final String ID = "ch.iseli.sportanalyzer.client.action.DeleteImportedRecord";

    private static final Logger log = Logger.getLogger(ImportGpsFilesAction.class.getName());

    private final TrainingCenterDataCache cache = TrainingCenterDataCache.getInstance();

    private static final IImportedDao dao;

    static {
        IConfigurationElement[] daos = Platform.getExtensionRegistry().getConfigurationElementsFor("ch.opentrainingdatabase.db");
        dao = (IImportedDao) DaoHelper.getDao(daos, IImportedDao.EXTENSION_POINT_NAME);
    }

    public DeleteImportedRecord() {
        setId(ID);
        setText("Lauf löschen");
    }

    @Override
    public void dispose() {
    }

    @Override
    public void selectionChanged(IWorkbenchPart part, ISelection selection) {
    }

    @Override
    public void run() {
        List<?> selection = cache.getSelection();
        for (Object obj : selection) {
            TrainingCenterDatabaseTParent t = (TrainingCenterDatabaseTParent) obj;
            log.debug("Lösche den Lauf mit der ID " + t.getId());
            dao.removeImportedRecord(t.getId());
            cache.remove(t.getId());
        }
    }
}
