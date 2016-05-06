package br.edu.ocdrf.context.parsers;

import br.edu.ocdrf.entities.ResourceObserver;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;


import br.edu.ocdrf.parsers.AbstractParser;
import br.edu.ocdrf.exceptions.ValidationException;

/**
 * @author andre
 *
 */
public class ContextObserverParser extends AbstractParser<ResourceObserver> {

    private static final String SERVICE_NAME = "ServiceName";
    private static final String RESOURCE_TYPE = "ResourceType";
    private static final String DESCRIPTION = "Description";
    private static final String URI = "URI";
    private static final String RESOURCE_OBSERVER = "ResourceObserver";

    /*
     * (non-Javadoc)
     * 
     * @see br.edu.cdrf.parsers.AbstractParser#parseDocument(java.io.InputStream)
     */
    @Override
    public ResourceObserver parseDocument(String xml) throws ValidationException {

        //TODO: Temporario
        InputStream inputStream = new ByteArrayInputStream(xml.getBytes());

        XMLStreamReader reader = null;
        ResourceObserver resourceObserver = null;
        try {
//			validateDocument("META-INF/resourceObserver.xsd", inputStream);
            inputStream.reset();
            Source source = new StreamSource(inputStream);
            reader = factory.createXMLStreamReader(source);
            while (true) {
                int event = reader.next();
                if (event == XMLStreamConstants.END_DOCUMENT) {
                    reader.close();
                    break;
                }
                switch (event) {
                    case XMLStreamConstants.START_ELEMENT:
                        switch (reader.getLocalName()) {
                            case RESOURCE_OBSERVER:
                                resourceObserver = new ResourceObserver();
                                break;
                            case URI:
                                resourceObserver.setURI(reader.getElementText().trim());
                                break;
                            case SERVICE_NAME:
                                resourceObserver.setServiceName(reader.getElementText().trim());
                                break;
                            case DESCRIPTION:
                                resourceObserver.setDescription(reader.getElementText()
                                        .trim());
                                break;
                            case RESOURCE_TYPE:
                                resourceObserver.setResourceType(reader.getElementText()
                                        .trim());
                                break;
                        }
                }
            }
        }
        catch (XMLStreamException | IOException e) {
            throw new ValidationException(e);
        }
        finally {
            if (reader != null) {
                try {
                    reader.close();
                }
                catch (XMLStreamException e) {
                    throw new ValidationException(e);
                }
            }
        }
        return resourceObserver;
    }
}
