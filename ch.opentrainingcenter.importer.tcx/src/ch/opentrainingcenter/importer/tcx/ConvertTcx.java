package ch.opentrainingcenter.importer.tcx;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Collections;
import java.util.List;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
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
import ch.opentrainingcenter.tcx.ActivityT;
import ch.opentrainingcenter.tcx.TrainingCenterDatabaseT;

public class ConvertTcx implements IConvert2Tcx {
    private static final String RESOURCES_FITNESSLOGBOOK_XSD = "resources/tcx.xsd";//$NON-NLS-1$
    private static final Logger logger = Logger.getLogger(ConvertTcx.class);
    private final Bundle bundle;
    private final String locationOfScript;

    public ConvertTcx() {
        logger.info("ConvertTcx erfolgreich instanziert....");//$NON-NLS-1$
        bundle = Platform.getBundle(Activator.BUNDLE_ID);
        final Path path = new Path(RESOURCES_FITNESSLOGBOOK_XSD);
        final URL url = FileLocator.find(bundle, path, Collections.EMPTY_MAP);
        URL fileUrl = null;
        try {
            fileUrl = FileLocator.toFileURL(url);
        } catch (final IOException e) {
            logger.error("Fehler beim Instanzieren von ConvertTcx: " + e.getMessage());//$NON-NLS-1$
            throw new RuntimeException(e);
        }
        final File f = new File(fileUrl.getPath());
        locationOfScript = f.getAbsolutePath();
        logger.info("ConvertTcx erfolgreich instanziert....fertig");//$NON-NLS-1$
    }

    @Override
    public TrainingCenterDatabaseT convert(final File file) throws Exception {
        final TrainingCenterDatabaseT database = unmarshall(file);
        return database;
    }

    @SuppressWarnings("unchecked")
    public TrainingCenterDatabaseT unmarshall(final File file) throws JAXBException, SAXException {
        final Unmarshaller unmarshaller = createUnmarshaller();

        final JAXBElement<TrainingCenterDatabaseT> jaxb = (JAXBElement<TrainingCenterDatabaseT>) unmarshaller.unmarshal(file);
        return jaxb.getValue();
    }

    private Unmarshaller createUnmarshaller() throws JAXBException, SAXException {
        final JAXBContext jc = JAXBContext.newInstance(TrainingCenterDatabaseT.class);
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
        return "tcx"; //$NON-NLS-1$
    }

    @Override
    public String getName() {
        return "TCX (mit ANT+ aus der Uhr exportiert)"; //$NON-NLS-1$
    }

}
