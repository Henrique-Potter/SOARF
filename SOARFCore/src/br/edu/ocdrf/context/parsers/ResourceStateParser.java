package br.edu.ocdrf.context.parsers;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;

import br.edu.ocdrf.entities.Attribute;
import br.edu.ocdrf.entities.ResourceState;
import br.edu.ocdrf.parsers.AbstractParser;
import br.edu.ocdrf.exceptions.ValidationException;
import br.edu.ocdrf.util.xml.XMLUtility;

/**
 * @author andre
 *
 */
public class ResourceStateParser extends AbstractParser<Object> {

    private static final String INTERVAL = "Interval";
    private static final String COLLECT_TIME = "CollectTime";
    private static final String RESOURCE_STATE = "ResourceState";
    //private static final String UNITS = "Units";
    private static final String URI = "URI";
    private static final String VALUE = "Value";
    private static final String NAME = "Name";
    private static final String TAB = "\t";
    private static final String ENTER = "\n";
    private static final String ATTRIBUTE = "Attribute";
    private static final String TYPE = "Type";

    /**
     *
     * @param xml
     * @param inputStream
     * @return
     * @throws ValidationException
     */
    @Override
    public ResourceState parseDocument(String xml) throws ValidationException {

        //TODO: Temporario
        InputStream inputStream = new ByteArrayInputStream(xml.getBytes());

        XMLStreamReader reader = null;
        ResourceState resourceState = null;
        try {
        //validateDocument("META-INF/resource.xsd", inputStream);
            inputStream.reset();
            Source source = new StreamSource(inputStream);
            reader = factory.createXMLStreamReader(source);
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            while (true) {
                int event = reader.next();
                if (event == XMLStreamConstants.END_DOCUMENT) {
                    reader.close();
                    break;
                }
                switch (event) {
                    case XMLStreamConstants.START_ELEMENT:
                        switch (reader.getLocalName()) {
                            case RESOURCE_STATE:
                                resourceState = new ResourceState();
                                break;
                            case COLLECT_TIME:
                                resourceState.setCollectTime(df.parse(reader.getElementText().replaceAll(ENTER, "").replaceAll(TAB, "")));
                                break;
                            case INTERVAL:
                                resourceState.setInterval(Integer.parseInt(reader.getElementText().replaceAll(ENTER, "").replaceAll(TAB, "")));
                                break;
                            case TYPE:
                                resourceState.setType(reader.getElementText().replaceAll(ENTER, "").replaceAll(TAB, ""));
                                break;
                            case ATTRIBUTE:
                                Attribute attribute = new Attribute(XMLUtility.getAttributeValue(reader, NAME));
//						attribute.setType(XMLUtility.getAttributeValue(reader, TYPE));
                                attribute.setValue(XMLUtility.getAttributeValue(reader, VALUE));
//						attribute.setUnits(XMLUtility.getAttributeValue(reader, UNITS));
                                //resourceState.addAttribute(attribute);
                                break;
                            case URI:
                                resourceState.setURI(reader.getElementText().replaceAll(ENTER, "").replaceAll(TAB, ""));
                                break;
                        }
                        break;
                }
            }
        }
        catch (XMLStreamException | ParseException | IOException e) {
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
        return resourceState;
    }

}
