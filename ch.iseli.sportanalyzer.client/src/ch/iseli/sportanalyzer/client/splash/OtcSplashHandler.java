package ch.iseli.sportanalyzer.client.splash;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Platform;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.splash.BasicSplashHandler;

import ch.iseli.sportanalyzer.client.Activator;
import ch.iseli.sportanalyzer.client.Application;
import ch.iseli.sportanalyzer.client.Messages;
import ch.iseli.sportanalyzer.client.PreferenceConstants;
import ch.iseli.sportanalyzer.client.cache.TrainingCenterDataCache;
import ch.iseli.sportanalyzer.db.DatabaseAccessFactory;
import ch.iseli.sportanalyzer.importer.ConvertHandler;
import ch.iseli.sportanalyzer.importer.FindGarminFiles;
import ch.iseli.sportanalyzer.importer.IConvert2Tcx;
import ch.iseli.sportanalyzer.tcx.ActivityT;
import ch.opentrainingcenter.transfer.IAthlete;

public class OtcSplashHandler extends BasicSplashHandler {

    public static final Logger logger = Logger.getLogger(OtcSplashHandler.class);

    private static final String SPACER = "                                                                            "; //$NON-NLS-1$

    // private final Map<Integer, TrainingCenterRecord> allRuns = new HashMap<Integer, TrainingCenterRecord>();

    private ProgressBar fBar;
    private Label titel;
    private Label infotext;
    private final boolean loadFromCache;
    private final IAthlete athlete;

    private final ConvertHandler convertHandler;

    public OtcSplashHandler() {

        final boolean dblocked = isDatabaseLocked();

        createDataBaseIfNotExists();

        final String athleteId = Activator.getDefault().getPreferenceStore().getString(PreferenceConstants.ATHLETE_ID);
        if (dblocked && !isValidId(athleteId)) {
            loadFromCache = false;
            athlete = null;
            convertHandler = null;
        } else {
            loadFromCache = true;
            athlete = DatabaseAccessFactory.getDatabaseAccess().getAthlete(Integer.parseInt(athleteId));
            final IConfigurationElement[] configurationElementsFor = Platform.getExtensionRegistry().getConfigurationElementsFor(
                    Application.IMPORT_EXTENSION_POINT);
            convertHandler = getConverterImplementation(configurationElementsFor);
        }
    }

    private boolean isDatabaseLocked() {
        try {
            DatabaseAccessFactory.getDatabaseAccess().getAthlete(1);
        } catch (final Exception e) {
            final Throwable cause = e.getCause();
            final String message = cause.getMessage();
            if (message.contains("Locked by another process")) { //$NON-NLS-1$
                logger.error("Database Locked by another process"); //$NON-NLS-1$
                System.exit(0);
                return true;
            }
        }
        return false;
    }

    private void createDataBaseIfNotExists() {

        try {
            DatabaseAccessFactory.getDatabaseAccess().getAthlete(1);
        } catch (final Exception e) {
            // db erstellen
            final Throwable cause = e.getCause();
            final String message = cause.getMessage();
            if (message.contains("Table \"ATHLETE\" not found;")) { //$NON-NLS-1$
                logger.info("Datenbank existiert noch nicht. Es wird eine neue Datenbank erstellt"); //$NON-NLS-1$
                DatabaseAccessFactory.getDatabaseAccess().createDatabase();
            }
        }
    }

    private boolean isValidId(final String athleteId) {
        return athleteId != null && athleteId.length() > 0;
    }

    private ConvertHandler getConverterImplementation(final IConfigurationElement[] configurationElementsFor) {
        final ConvertHandler handler = new ConvertHandler();
        for (final IConfigurationElement element : configurationElementsFor) {
            try {
                final IConvert2Tcx tcx = (IConvert2Tcx) element.createExecutableExtension(IConvert2Tcx.PROPERETY);
                handler.addConverter(tcx);
            } catch (final CoreException e) {
                logger.error(e.getMessage());
            }
        }
        return handler;
    }

    @Override
    public void init(final Shell splash) {
        super.init(splash);
        getSplash().setBackgroundMode(SWT.INHERIT_DEFAULT);
        if (loadFromCache) {
            createUI(splash);
        }
    }

    private void createUI(final Shell shell) {
        final Composite container = new Composite(shell, SWT.NONE);
        container.setLayout(new GridLayout(1, false));
        container.setLocation(5, 310);
        container.setSize(430, 60);

        /* Progress Bar */
        fBar = new ProgressBar(container, SWT.HORIZONTAL);
        fBar.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, true, false));
        ((GridData) fBar.getLayoutData()).heightHint = 10;

        titel = new Label(container, SWT.NONE);
        titel.setText(SPACER);
        titel.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false));
        infotext = new Label(container, SWT.NONE);
        final FontData fd = new FontData();
        fd.setHeight(9);
        final Font font = new Font(shell.getDisplay(), fd);
        infotext.setFont(font);
        infotext.setText(SPACER);
        infotext.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false));
        shell.layout(true, true);
    }

    @Override
    public void dispose() {
        super.dispose();
    }

    @Override
    public IProgressMonitor getBundleProgressMonitor() {
        if (loadFromCache) {
            return new NullProgressMonitor() {

                @Override
                public void beginTask(final String name, final int totalWork) {

                    getSplash().getDisplay().syncExec(new Runnable() {
                        @Override
                        public void run() {
                            final Map<Date, String> importedRecords = DatabaseAccessFactory.getDatabaseAccess().getImportedRecords(athlete);
                            final Map<Date, File> loadAllGPSFiles = FindGarminFiles.loadAllGPSFilesFromAthlete(importedRecords);
                            final List<ActivityT> activitiesToImport = new ArrayList<ActivityT>();
                            final int size = loadAllGPSFiles.size();
                            fBar.setMaximum(size);
                            int i = 0;
                            try {
                                for (final Map.Entry<Date, File> entry : loadAllGPSFiles.entrySet()) {
                                    titel.setText(Messages.OtcSplashHandler_0 + (size - (i)));
                                    infotext.setText(Messages.OtcSplashHandler_1 + entry.getValue().getName());
                                    final File fileForConverting = entry.getValue();

                                    final List<ActivityT> activities = convertHandler.getMatchingConverter(fileForConverting)
                                            .convertActivity(fileForConverting);
                                    for (final ActivityT activityT : activities) {
                                        if (activityT.getId().toGregorianCalendar().getTime().equals(entry.getKey()) && !activitiesToImport.contains(activityT)) {
                                            activitiesToImport.add(activityT);
                                        }
                                    }

                                    fBar.setSelection(i);
                                    i++;
                                }
                                final TrainingCenterDataCache cache = TrainingCenterDataCache.getInstance();
                                cache.setSelectedProfile(athlete);
                                cache.addAll(activitiesToImport);
                                cache.cacheLoaded();
                            } catch (final Exception e) {
                                logger.error(e.getMessage());
                            }

                        }
                    });
                }
            };
        }
        return null;
    }

    @Override
    public Shell getSplash() {
        return super.getSplash();
    }

}
