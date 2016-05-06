package br.edu.ocdrf.directory.parsers;

import javax.xml.stream.XMLInputFactory;

import br.edu.ocdrf.exceptions.ValidationException;
import br.edu.ocdrf.query.DirectoryQueryType2;
import br.edu.ocdrf.query.DirectoryQueryType2Target;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;


/*
 * @author Leila Negris Bezerra
 * 
 * Esta classe será usada nas consultas tipo 2 do SD ao SRD
 *   
 */
public class DirectoryQueryType2Parser {

    protected static final XMLInputFactory factory;

    static {
        factory = XMLInputFactory.newInstance();
        factory.setProperty(XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES, Boolean.FALSE);
    }

    /**
     *
     * @param xml
     * @return
     * @throws ValidationException
     */
    /*
     * Formatos esperados - Vai ser usado pelo DS
     * 
     *  <DirectoryQuery>
     <Target ID="que eu já peguei se for Resouce" Property="locatedIn">
     <Target ID="vazio se CapacityConstraints estiver vazio" Property="locatedIn">
     <Target ID="passada pra comparar" Property="locatedIn">
		 		
     <Target ID="da Capacity passada se for Capacity" Property=xxx">
     <Target ID="do Attribute passada se for Attribute" Property=xxx">
     </DirectoryQuery>
     * 
     * Vou ignorar a tag DirectoryQuery
     */
    public DirectoryQueryType2 parseDocument(String xml) throws ValidationException {
        XStream xstream = newParser();
        DirectoryQueryType2 query = (DirectoryQueryType2) xstream.fromXML(xml);

        return query;
    }

    public String parseObject(DirectoryQueryType2 directoryQueryType2) {
        XStream xstream = newParser();

        String xml = xstream.toXML(directoryQueryType2);

        System.out.println("-----------------------");
        System.out.println(xml);
        System.out.println("-----------------------");
        return xml;
    }

    private XStream newParser() {
        XStream xstream = new XStream(new DomDriver());

        xstream.alias("DirectoryClientOntologyQuery", DirectoryQueryType2.class);

        xstream.useAttributeFor(DirectoryQueryType2Target.class, "property");
        xstream.useAttributeFor(DirectoryQueryType2Target.class, "element");
        xstream.useAttributeFor(DirectoryQueryType2Target.class, "ID");

        return xstream;
    }

}
