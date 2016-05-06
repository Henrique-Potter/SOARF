package br.edu.ocdrf.directory.parsers;

import br.edu.ocdrf.parsers.AbstractParser;
import br.edu.ocdrf.exceptions.ValidationException;
import br.edu.ocdrf.query.DirectoryResponseType2;
import br.edu.ocdrf.query.DirectoryResponseType2Info;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;


/*
 * @author Leila Negris Bezerra
 * 
 * Esta classe será usada nas consultas tipo 2 do SD ao SRD
 *   
 */
public class DirectoryResponseType2Parser extends AbstractParser<DirectoryResponseType2> {

    private static final String XSD = "ClientOntologyDirectoryResponse.xsd";

    /**
     *
     * @return @throws ValidationException
     */
    /*
     * Formatos esperados - Vai ser usado pelo DS
     * 
     <DirectoryResponse>
     <TargetInfo ID="xxxxx" Value="" />
     <TargetInfo ID="xxxxx" Value="" />
     </DirectoryResponse>
     * 
     * Vou ignorar a tag DirectoryQuery
     */
    @Override
    public DirectoryResponseType2 parseDocument(String xml) throws ValidationException {
        validateDocument(XSD, xml);

        System.out.println("-----------------------");
        System.out.println(xml);
        System.out.println("-----------------------");

        XStream xstream = newParser();
        DirectoryResponseType2 query = (DirectoryResponseType2) xstream.fromXML(xml);

        return query;
    }

    public String parseObject(DirectoryResponseType2 query) throws Exception {
        XStream xstream = newParser();

        String xml = xstream.toXML(query);

        System.out.println("-----------------------");
        System.out.println(xml);
        System.out.println("-----------------------");

        validateDocument(XSD, xml);

        return xml;
    }

    private XStream newParser() {
        XStream xstream = new XStream(new DomDriver());

        xstream.alias("ClientOntologyDirectoryResponse", DirectoryResponseType2.class);
        xstream.addImplicitCollection(DirectoryResponseType2.class, "directoryResponseType2InfoList", DirectoryResponseType2Info.class);

        xstream.alias("TargetInfo", DirectoryResponseType2Info.class);
        xstream.useAttributeFor(DirectoryResponseType2Info.class, "id");
        xstream.useAttributeFor(DirectoryResponseType2Info.class, "property");
        xstream.useAttributeFor(DirectoryResponseType2Info.class, "value");

        return xstream;
    }
}
