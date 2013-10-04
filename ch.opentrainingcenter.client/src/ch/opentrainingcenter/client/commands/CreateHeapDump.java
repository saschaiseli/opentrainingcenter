package ch.opentrainingcenter.client.commands;

import java.io.File;
import java.lang.management.ManagementFactory;

import javax.management.MBeanServer;

import org.apache.log4j.Logger;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.joda.time.DateTime;

import ch.opentrainingcenter.i18n.Messages;

import com.sun.management.HotSpotDiagnosticMXBean;

/**
 * Erstellt einen Heapdump snapshot in das home verzeichnis.
 * 
 * https://blogs.oracle.com/sundararajan/entry/
 * programmatically_dumping_heap_from_java
 */

public class CreateHeapDump extends AbstractHandler {

    private static final Logger LOGGER = Logger.getLogger(CreateHeapDump.class.getName());

    // This is the name of the HotSpot Diagnostic MBean
    private static final String HOTSPOT_BEAN_NAME = "com.sun.management:type=HotSpotDiagnostic"; //$NON-NLS-1$

    // field to store the hotspot diagnostic MBean
    private static volatile HotSpotDiagnosticMXBean hotspotMBean;

    private static final Object LOCK = new Object();

    @Override
    public Object execute(final ExecutionEvent event) throws ExecutionException {
        final String fileName = String.format("%s%s%s%s%s", System.getProperty("user.home"), File.separator, "otc_", DateTime.now().getMillis(), ".hprof"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
        LOGGER.info(String.format("create heapdump: %s", fileName)); //$NON-NLS-1$
        dumpHeap(fileName, true);
        final Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
        MessageDialog.openInformation(shell, Messages.CreateHeapDump_0, String.format(Messages.CreateHeapDump_1, fileName));
        return null;
    }

    static void dumpHeap(final String fileName, final boolean live) {
        // initialize hotspot diagnostic MBean
        initHotspotMBean();
        try {
            hotspotMBean.dumpHeap(fileName, live);
        } catch (final RuntimeException re) {
            throw re;
        } catch (final Exception exp) {
            throw new RuntimeException(exp);
        }
    }

    // initialize the hotspot diagnostic MBean field
    private static void initHotspotMBean() {
        if (hotspotMBean == null) {
            synchronized (LOCK) {
                if (hotspotMBean == null) {
                    hotspotMBean = getHotspotMBean();
                }
            }
        }
    }

    private static HotSpotDiagnosticMXBean getHotspotMBean() {
        try {
            final MBeanServer server = ManagementFactory.getPlatformMBeanServer();
            final HotSpotDiagnosticMXBean bean = ManagementFactory.newPlatformMXBeanProxy(server, HOTSPOT_BEAN_NAME, HotSpotDiagnosticMXBean.class);
            return bean;
        } catch (final RuntimeException re) {
            throw re;
        } catch (final Exception exp) {
            throw new RuntimeException(exp);
        }
    }
}
