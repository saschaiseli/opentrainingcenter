package ch.opentrainingcenter.importer.fitnesslog.converter;

import java.io.File;
import java.net.URL;
import java.util.Collections;
import java.util.List;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.osgi.framework.Bundle;
import org.xml.sax.SAXException;

import ch.opentrainingcenter.core.importer.IConvert2Tcx;
import ch.opentrainingcenter.importer.fitnesslog.Activator;
import ch.opentrainingcenter.importer.fitnesslog.model.FitnessWorkbook;
import ch.opentrainingcenter.tcx.ActivityT;
import ch.opentrainingcenter.tcx.TrainingCenterDatabaseT;

public class ConvertFitnesslog2Tcx implements IConvert2Tcx {

    private static final String RESOURCES_FITNESSLOGBOOK_XSD = "resources/fitnesslogbook.xsd";//$NON-NLS-1$
    private static final Logger logger = Logger.getLogger(ConvertFitnesslog2Tcx.class);
    private final Bundle bundle;
    private final String locationOfScript;

    public ConvertFitnesslog2Tcx() {
        logger.info("ConvertFitnesslog2Tcx erfolgreich instanziert....1");//$NON-NLS-1$
        bundle = Platform.getBundle(Activator.BUNDLE_ID);
        logger.info("ConvertFitnesslog2Tcx erfolgreich instanziert....2 " + bundle);//$NON-NLS-1$
        final Path path = new Path(RESOURCES_FITNESSLOGBOOK_XSD);
        logger.info("ConvertFitnesslog2Tcx erfolgreich instanziert....3 " + path);//$NON-NLS-1$
        final URL url = FileLocator.find(bundle, path, Collections.EMPTY_MAP);
        logger.info("ConvertFitnesslog2Tcx erfolgreich instanziert....4 " + url);//$NON-NLS-1$
        URL fileUrl = null;
        try {
            logger.info("ConvertFitnesslog2Tcx erfolgreich instanziert....5 " + url);//$NON-NLS-1$
            fileUrl = FileLocator.toFileURL(url);
            logger.info("ConvertFitnesslog2Tcx erfolgreich instanziert....6 " + fileUrl);//$NON-NLS-1$
        } catch (final Exception e) {
            logger.error("Fehler beim Instanzieren von ConvertFitnesslog2Tcx: " + e.getMessage());//$NON-NLS-1$
            // throw new RuntimeException(e);
        }
        logger.info("ConvertFitnesslog2Tcx erfolgreich instanziert....7 ");//$NON-NLS-1$
        final File f = new File(fileUrl.getPath());
        logger.info("ConvertFitnesslog2Tcx erfolgreich instanziert....8 ");//$NON-NLS-1$
        locationOfScript = f.getAbsolutePath();
        logger.info("ConvertFitnesslog2Tcx erfolgreich instanziert....9 ");//$NON-NLS-1$
        logger.info("ConvertFitnesslog2Tcx erfolgreich instanziert....fertig");//$NON-NLS-1$
    }

    public ConvertFitnesslog2Tcx(final boolean withJunit) {
        bundle = null;
        if (withJunit) {
            logger.info("working with the test constructor!!!"); //$NON-NLS-1$
        }
        final File xsd = new File(RESOURCES_FITNESSLOGBOOK_XSD);
        locationOfScript = xsd.getAbsolutePath();
    }

    @Override
    public TrainingCenterDatabaseT convert(final File file) throws Exception {
        final FitnessWorkbook workbook = unmarshall(file);
        final ConvertWorkbook2Tcx convertWorkbook2Tcx = new ConvertWorkbook2Tcx();
        return convertWorkbook2Tcx.convert(workbook);
    }

    protected FitnessWorkbook unmarshall(final File file) throws JAXBException, SAXException {
        final Unmarshaller unmarshaller = createUnmarshaller();
        final FitnessWorkbook unmarshal = (FitnessWorkbook) unmarshaller.unmarshal(file);
        return unmarshal;
    }

    private Unmarshaller createUnmarshaller() throws JAXBException, SAXException {
        final JAXBContext jc = JAXBContext.newInstance(FitnessWorkbook.class);
        final Unmarshaller unmarshaller = jc.createUnmarshaller();

        final SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);

        final Schema schema = schemaFactory.newSchema(new File(locationOfScript));
        unmarshaller.setSchema(schema);
        return unmarshaller;
    }

    @Override
    public List<ActivityT> convertActivity(final File file) throws Exception {
        return convert(file).getActivities().getActivity();
    }

    @Override
    public String getFilePrefix() {
        return "fitlog"; //$NON-NLS-1$
    }

    @Override
    public String getName() {
        return "Fitness Log (Export von Sporttracks)";
    }

}
