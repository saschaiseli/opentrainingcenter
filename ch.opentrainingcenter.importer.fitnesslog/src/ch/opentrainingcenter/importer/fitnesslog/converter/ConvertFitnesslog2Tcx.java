package ch.opentrainingcenter.importer.fitnesslog.converter;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Collections;

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

import ch.iseli.sportanalyzer.importer.IConvert2Tcx;
import ch.iseli.sportanalyzer.tcx.TrainingCenterDatabaseT;
import ch.opentrainingcenter.importer.fitnesslog.Activator;
import ch.opentrainingcenter.importer.fitnesslog.model.FitnessWorkbook;

public class ConvertFitnesslog2Tcx implements IConvert2Tcx {

    private static final String RESOURCES_FITNESSLOGBOOK_XSD = "resources/fitnesslogbook.xsd";//$NON-NLS-1$
    private static final Logger logger = Logger.getLogger(ConvertFitnesslog2Tcx.class);
    private final Bundle bundle;
    private final String locationOfScript;

    public ConvertFitnesslog2Tcx() {
        logger.info("ConvertFitnesslog2Tcx erfolgreich instanziert....");//$NON-NLS-1$
        bundle = Platform.getBundle(Activator.BUNDLE_ID);
        final Path path = new Path(RESOURCES_FITNESSLOGBOOK_XSD);
        final URL url = FileLocator.find(bundle, path, Collections.EMPTY_MAP);
        URL fileUrl = null;
        try {
            fileUrl = FileLocator.toFileURL(url);
        } catch (final IOException e) {
            logger.error("Fehler beim Instanzieren von Gmn2Tcx: " + e.getMessage());//$NON-NLS-1$
            throw new RuntimeException(e);
        }
        final File f = new File(fileUrl.getPath());
        locationOfScript = f.getAbsolutePath();
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
    public String getFilePrefix() {
        return "logfile"; //$NON-NLS-1$
    }
}
