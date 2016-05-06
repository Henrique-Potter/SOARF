package br.edu.ocdrf.parsers;

import br.edu.ocdrf.exceptions.ValidationException;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;

import javax.xml.XMLConstants;
import javax.xml.stream.XMLInputFactory;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.xml.sax.SAXException;

/**
 * Classe básica do diretório de recursos.
 *
 * @author Henrique Potter <HenriquePotter.hp@gmail.com>
 * @since 07 / 10 / 20013
 * @version 1.1
 * @category Parsing
 *
 * @author andre
 * @version 1.0
 *
 */
public abstract class AbstractParser<TYPE extends Object> {

    protected static final XMLInputFactory factory;

    static {
        factory = XMLInputFactory.newInstance();
        factory.setProperty(XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES,
                Boolean.FALSE);
    }

    /**
     *
     * @param xml
     * @return
     * @throws ValidationException
     */
    public abstract TYPE parseDocument(String xml)
            throws Exception;

    /**
     *
     * @param xsdFileName
     * @param xml
     * @throws ValidationException
     */
    protected void validateDocument(String xsdFileName, String xml) throws ValidationException {
        try {
            SchemaFactory sSactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);

            String CDRF_HOME = System.getenv("CDRF_HOME");
            File xsdFile = new File(CDRF_HOME + "\\conf\\xsds\\", xsdFileName);

            Source schemaFile = new StreamSource(xsdFile);
            Schema schema = sSactory.newSchema(schemaFile);

            Validator validator = schema.newValidator();
            //File xmlFile = new File(CDRF_HOME, xml);
            validator.validate(new StreamSource(new ByteArrayInputStream(xml.getBytes())));

            //System.out.println("Sucesso: " + xsdFileName);
        }
        catch (SAXException | IOException e) {
//			e.printStackTrace();
            throw new ValidationException(e);
        }
    }
}
