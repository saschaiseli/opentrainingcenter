package ch.iseli.sportanalyzer.client.splash;

import java.io.File;
import java.util.HashMap;
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
import ch.iseli.sportanalyzer.client.PreferenceConstants;
import ch.iseli.sportanalyzer.client.cache.TrainingCenterDataCache;
import ch.iseli.sportanalyzer.client.cache.TrainingCenterRecord;
import ch.iseli.sportanalyzer.client.views.navigation.NavigationView;
import ch.iseli.sportanalyzer.db.DatabaseAccessFactory;
import ch.iseli.sportanalyzer.importer.FindGarminFiles;
import ch.iseli.sportanalyzer.importer.IConvert2Tcx;
import ch.iseli.sportanalyzer.tcx.TrainingCenterDatabaseT;
import ch.opentrainingcenter.transfer.IAthlete;

public class OtcSplashHandler extends BasicSplashHandler {

    private static final String SPACER = "                                                                            ";

    private static final Logger logger = Logger.getLogger(NavigationView.class);

    private static final String CH_ISELI_SPORTANALYZER_MYIMPORTER = "ch.iseli.sportanalyzer.myimporter";

    private final Map<Integer, TrainingCenterRecord> allRuns = new HashMap<Integer, TrainingCenterRecord>();
    private final IConvert2Tcx tcx;

    private ProgressBar fBar;
    private Label titel;
    private Label infotext;
    private final boolean preload;
    private final IAthlete athlete;

    public OtcSplashHandler() {
        final String athleteId = Activator.getDefault().getPreferenceStore().getString(PreferenceConstants.ATHLETE_ID);
        if (!isValidId(athleteId)) {
            preload = false;
            athlete = null;
            tcx = null;
        } else {
            preload = true;
            athlete = DatabaseAccessFactory.getDatabaseAccess().getAthlete(Integer.parseInt(athleteId));
            final IConfigurationElement[] configurationElementsFor = Platform.getExtensionRegistry().getConfigurationElementsFor(
                    CH_ISELI_SPORTANALYZER_MYIMPORTER);
            this.tcx = getConverterImplementation(configurationElementsFor);
        }
    }

    private boolean isValidId(final String athleteId) {
        return athleteId != null && athleteId.length() > 0;
    }

    private IConvert2Tcx getConverterImplementation(final IConfigurationElement[] configurationElementsFor) {
        for (final IConfigurationElement element : configurationElementsFor) {
            try {
                return (IConvert2Tcx) element.createExecutableExtension("class");
            } catch (final CoreException e) {
                System.err.println(e.getMessage());
            }
        }
        return null;
    }

    @Override
    public void init(final Shell splash) {
        super.init(splash);
        getSplash().setBackgroundMode(SWT.INHERIT_DEFAULT);
        if (preload) {
            createUI(splash);
        }
    }

    @Override
    public void dispose() {
        super.dispose();
    }

    @Override
    public IProgressMonitor getBundleProgressMonitor() {
        if (preload) {
            return new NullProgressMonitor() {

                @Override
                public void beginTask(final String name, final int totalWork) {

                    getSplash().getDisplay().syncExec(new Runnable() {
                        @Override
                        public void run() {
                            final Map<Integer, String> importedRecords = DatabaseAccessFactory.getDatabaseAccess().getImportedRecords(athlete);
                            final Map<Integer, File> loadAllGPSFiles = FindGarminFiles.loadAllGPSFilesFromAthlete(importedRecords);
                            final int size = loadAllGPSFiles.size();
                            titel.setText("Lade GPS Daten:    " + size);
                            fBar.setMaximum(size);
                            int i = 0;
                            try {
                                for (final Map.Entry<Integer, File> entry : loadAllGPSFiles.entrySet()) {
                                    fBar.setSelection(i);
                                    infotext.setText("Importiere : " + entry.getValue().getName());
                                    titel.setText("Lade GPS Daten:    " + (size - (i)));
                                    i++;
                                    final TrainingCenterDatabaseT record = tcx.convert(entry.getValue());
                                    allRuns.put(entry.getKey(), new TrainingCenterRecord(entry.getKey(), record));
                                }
                                TrainingCenterDataCache.getInstance().setSelectedProfile(athlete);
                                TrainingCenterDataCache.getInstance().addAll(allRuns);
                                TrainingCenterDataCache.getInstance().cacheLoaded();
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

    private void createUI(final Shell shell) {

        final Composite container = new Composite(shell, SWT.NONE);
        container.setLayout(new GridLayout(1, false));
        container.setLocation(5, 310);
        container.setSize(430, 60);

        /* Progress Bar */
        final GridData gd = new GridData();

        fBar = new ProgressBar(container, SWT.HORIZONTAL);
        fBar.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, true, false));
        gd.grabExcessHorizontalSpace = true;
        ((GridData) fBar.getLayoutData()).heightHint = 10;

        titel = new Label(container, SWT.NONE);
        titel.setLayoutData(gd);
        titel.setText("Lade GPS Daten:" + SPACER);
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
    public Shell getSplash() {
        return super.getSplash();
    }

}
