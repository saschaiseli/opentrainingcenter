package ch.opentrainingcenter.importer.gpx;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Collections;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.osgi.framework.Bundle;
import org.xml.sax.SAXException;

import ch.opentrainingcenter.core.exceptions.ConvertException;
import ch.opentrainingcenter.core.helper.AltitudeCalculator;
import ch.opentrainingcenter.core.helper.AltitudeCalculator.Ascending;
import ch.opentrainingcenter.core.importer.IConvert2Tcx;
import ch.opentrainingcenter.transfer.ITraining;

import com.garmin.xmlschemas.gpxextensions.v3.TrackPointExtensionT;
import com.topografix.gpx.GpxType;

public class ConvertGpx implements IConvert2Tcx {

    private static final Logger LOGGER = Logger.getLogger(ConvertGpx.class);

    static final String PREFIX = "gpx";//$NON-NLS-1$
    static final String NAME = "GPS Exchange Format (gpx)"; //$NON-NLS-1$

    private static final String RESOURCES_GPX_XSD = "resources/gpx.xsd"; //$NON-NLS-1$
    private static final String RESOURCES_GPX_EXTENSION_XSD = "resources/GpxExtensionsv3.xsd"; //$NON-NLS-1$

    private final URL pfadSchema;
    private final URL pfadSchemaExtension;

    public ConvertGpx() {
        LOGGER.info("ConvertGpx erfolgreich instanziert...."); //$NON-NLS-1$
        pfadSchema = getURL(RESOURCES_GPX_XSD);
        pfadSchemaExtension = getURL(RESOURCES_GPX_EXTENSION_XSD);
        LOGGER.info("ConvertGpx erfolgreich instanziert....fertig"); //$NON-NLS-1$
    }

    private URL getURL(final String schema) {
        final Bundle bundle = Platform.getBundle(Activator.BUNDLE_ID);
        final Path path = new Path(schema);
        final URL url = FileLocator.find(bundle, path, Collections.EMPTY_MAP);
        URL fileUrl = null;
        try {
            fileUrl = FileLocator.toFileURL(url);
        } catch (final IOException e) {
            LOGGER.error(String.format("ConvertTcx failed. Schema '%s' nicht gefunden", url.toString())); //$NON-NLS-1$
        }
        return fileUrl;
    }

    @Override
    public String getFilePrefix() {
        return PREFIX;
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public ITraining convert(final File file) throws ConvertException {
        LOGGER.info("Start unmarshalling GPX File"); //$NON-NLS-1$
        GpxType gpxType = null;
        try {
            final JAXBElement<GpxType> root = unmarshall(file);
            gpxType = root.getValue();

        } catch (JAXBException | SAXException | IOException e) {
            throw new ConvertException(e);
        }
        final GpxConverter converter = new GpxConverter();
        final ITraining training = converter.convert(gpxType);
        final Ascending ascending = AltitudeCalculator.calculateAscending(training.getTrackPoints());
        training.setUpMeter(ascending.getUp());
        training.setDownMeter(ascending.getDown());
        return training;
    }

    @SuppressWarnings("unchecked")
    public JAXBElement<GpxType> unmarshall(final File file) throws JAXBException, SAXException, IOException {
        final Unmarshaller unmarshaller = createUnmarshaller();
        final JAXBElement<GpxType> root = (JAXBElement<GpxType>) unmarshaller.unmarshal(file);
        return root;
    }

    private Unmarshaller createUnmarshaller() throws JAXBException, SAXException, IOException {
        final JAXBContext jc = JAXBContext.newInstance(GpxType.class, TrackPointExtensionT.class);// ,
        final Unmarshaller unmarshaller = jc.createUnmarshaller();

        final SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);

        final Source mainXsd = new StreamSource(pfadSchema.openStream());
        final Source extXsd = new StreamSource(pfadSchemaExtension.openStream());

        final Schema schema = schemaFactory.newSchema(new Source[] { mainXsd, extXsd });
        unmarshaller.setSchema(schema);
        return unmarshaller;
    }
}
