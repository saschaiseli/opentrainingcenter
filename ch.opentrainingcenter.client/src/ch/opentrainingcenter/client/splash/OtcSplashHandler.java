package ch.opentrainingcenter.client.splash;

import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.splash.AbstractSplashHandler;

import ch.opentrainingcenter.client.Activator;
import ch.opentrainingcenter.client.cache.InitialLoad;
import ch.opentrainingcenter.client.cache.impl.TrainingCenterDataCache;
import ch.opentrainingcenter.client.views.ApplicationContext;
import ch.opentrainingcenter.db.DatabaseAccessFactory;
import ch.opentrainingcenter.db.IDatabaseAccess;
import ch.opentrainingcenter.importer.ConvertContainer;
import ch.opentrainingcenter.importer.ExtensionHelper;
import ch.opentrainingcenter.importer.IImportedConverter;
import ch.opentrainingcenter.importer.ImporterFactory;
import ch.opentrainingcenter.transfer.IAthlete;
import ch.opentrainingcenter.transfer.IImported;

public class OtcSplashHandler extends AbstractSplashHandler {

    public static final String ID = "ch.opentrainingcenter.client.splash"; //$NON-NLS-1$

    private final IPreferenceStore store = Activator.getDefault().getPreferenceStore();

    private static final Logger LOG = Logger.getLogger(OtcSplashHandler.class);

    private static final int HEIGHT = 20;
    private static final int MARGIN = 5;
    private ProgressBar fBar;

    public OtcSplashHandler() {
        super();
    }

    @Override
    public void init(final Shell splash) {
        super.init(splash);

        createUI(splash);

    }

    private void createUI(final Shell shell) {

        final Composite container = new Composite(shell, SWT.NONE);
        container.setLayout(new GridLayout(1, false));
        final Point size = shell.getSize();

        container.setLocation(0, size.y - HEIGHT);
        container.setSize(size.x, HEIGHT);
        container.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));

        /* Progress Bar */
        fBar = new ProgressBar(container, SWT.HORIZONTAL | SWT.SMOOTH);
        final GridData gd = new GridData(SWT.FILL, SWT.BEGINNING, true, true);
        gd.horizontalIndent = 5;
        fBar.setLayoutData(gd);

        ((GridData) fBar.getLayoutData()).heightHint = 2 * MARGIN;

        /* Layout All */
        shell.layout(true, true);
    }

    @Override
    public IProgressMonitor getBundleProgressMonitor() {
        return new NullProgressMonitor() {

            @Override
            public void done() {
                super.done();
            }

            @Override
            public void worked(final int work) {
                super.worked(work);
            }

            @Override
            public void beginTask(final String name, final int totalWork) {

                getSplash().getDisplay().syncExec(new Runnable() {
                    @Override
                    public void run() {
                        final IAthlete athlete = ApplicationContext.getApplicationContext().getAthlete();
                        if (athlete != null) {
                            final IDatabaseAccess databaseAccess = DatabaseAccessFactory.getDatabaseAccess();
                            final List<IImported> allImported = databaseAccess.getAllImported(athlete, 10);
                            final ConvertContainer cc = new ConvertContainer(ExtensionHelper.getConverters());
                            final IImportedConverter fileLoader = ImporterFactory.createGpsFileLoader(store, cc);
                            final InitialLoad load = new InitialLoad(TrainingCenterDataCache.getInstance(), fileLoader);
                            try {
                                load.initalLoad(fBar, allImported);
                            } catch (final Exception e) {
                                LOG.info("Initial Load failed: " + e); //$NON-NLS-1$
                            }
                        }
                    }
                });
            }
        };
    }
}
