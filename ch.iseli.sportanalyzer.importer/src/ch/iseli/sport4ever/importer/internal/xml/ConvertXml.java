package ch.iseli.sport4ever.importer.internal.xml;

import java.io.File;
import java.io.InputStream;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.xml.sax.SAXException;

import ch.iseli.sportanalyzer.tcx.TrainingCenterDatabaseT;

public class ConvertXml {

    private final String locationOfScript;

    public ConvertXml(String urlOfXsd) {
        this.locationOfScript = urlOfXsd;
    }

    @SuppressWarnings("unchecked")
    public TrainingCenterDatabaseT unmarshall(InputStream stream) throws JAXBException, SAXException {
        Unmarshaller unmarshaller = createUnmarshaller();

        JAXBElement<TrainingCenterDatabaseT> jaxb = (JAXBElement<TrainingCenterDatabaseT>) unmarshaller.unmarshal(stream);
        return jaxb.getValue();
    }

    @SuppressWarnings("unchecked")
    public TrainingCenterDatabaseT unmarshall(File file) throws JAXBException, SAXException {
        Unmarshaller unmarshaller = createUnmarshaller();

        JAXBElement<TrainingCenterDatabaseT> jaxb = (JAXBElement<TrainingCenterDatabaseT>) unmarshaller.unmarshal(file);
        return jaxb.getValue();
    }

    private Unmarshaller createUnmarshaller() throws JAXBException, SAXException {
        JAXBContext jc = JAXBContext.newInstance(TrainingCenterDatabaseT.class);
        Unmarshaller unmarshaller = jc.createUnmarshaller();

        SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);

        Schema schema = schemaFactory.newSchema(new File(locationOfScript));
        unmarshaller.setSchema(schema);
        return unmarshaller;
    }
}
