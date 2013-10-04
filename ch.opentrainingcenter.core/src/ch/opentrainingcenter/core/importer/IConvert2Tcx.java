package ch.opentrainingcenter.core.importer;

import java.io.File;

import ch.opentrainingcenter.core.exceptions.ConvertException;
import ch.opentrainingcenter.transfer.ITraining;

public interface IConvert2Tcx {

    String PROPERETY = "class"; //$NON-NLS-1$

    String CORE_PLUGIN_ID = "ch.opentrainingcenter.client"; //$NON-NLS-1$

    /**
     * Liest ein GPS File ein und konvertiert dies in eine oder mehrere
     * Aktivitäten.
     * 
     * 
     * @param file
     *            Ursprungsfile
     * @return in Garmin konvertiertes Object, welches einem oder mehreren
     *         Läufen entspricht.
     * @throws ConvertException
     *             wenn etwas schiefgeht beim parsen lesen des files,...
     */
    ITraining convert(final File file) throws ConvertException;

    /**
     * @return den prefix für ein GPS file. Bei Garmin wird demnach 'gmn'
     *         zurückgegeben.
     */
    String getFilePrefix();

    /**
     * @return Den Namen des Importers
     */
    String getName();

}
