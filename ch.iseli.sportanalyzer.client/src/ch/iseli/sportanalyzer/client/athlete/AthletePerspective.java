package ch.iseli.sportanalyzer.client.athlete;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

import ch.iseli.sportanalyzer.client.Activator;
import ch.iseli.sportanalyzer.client.PreferenceConstants;
import ch.iseli.sportanalyzer.db.IAthleteDao;

public class AthletePerspective implements IPerspectiveFactory {

    public static final String ID = "ch.iseli.sportanalyzer.client.athletePerspective";

    @Override
    public void createInitialLayout(IPageLayout layout) {
        String editorArea = layout.getEditorArea();
        layout.setEditorAreaVisible(false);

        String athleteName = Activator.getDefault().getPreferenceStore().getString(PreferenceConstants.ATHLETE_NAME);
        if (athleteName == null || athleteName.length() <= 0) {

            IConfigurationElement[] configurationElementsFor = Platform.getExtensionRegistry().getConfigurationElementsFor("ch.opentrainingdatabase.db");
            // to read all of the extension contributed and
            for (final IConfigurationElement element : configurationElementsFor) {
                try {
                    element.createExecutableExtension("class");
                } catch (CoreException e) {
                    System.err.println(e.getMessage());
                }
            }

            IConfigurationElement[] daos = Platform.getExtensionRegistry().getConfigurationElementsFor("ch.opentrainingdatabase.db");
            IAthleteDao dao = getDao(daos);
            layout.addStandaloneView(CreateAthleteView.ID, false, IPageLayout.LEFT, 1f, editorArea);
            layout.getViewLayout(CreateAthleteView.ID).setCloseable(false);
        } else {
            // user navigation
        }

        // IFolderLayout folder = layout.createFolder("rightPart", IPageLayout.TOP, 0.75f, editorArea);
        // folder.addPlaceholder(SingleActivityViewPart.ID + ":*");
    }

    private IAthleteDao getDao(IConfigurationElement[] configurationElementsFor) {
        for (final IConfigurationElement element : configurationElementsFor) {
            try {
                element.getName();
                return (IAthleteDao) element.createExecutableExtension("class");
            } catch (CoreException e) {
                System.err.println(e.getMessage());
            }
        }
        return null;
    }

}
