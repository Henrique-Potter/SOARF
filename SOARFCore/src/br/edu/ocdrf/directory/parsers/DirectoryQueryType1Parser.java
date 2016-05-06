package br.edu.ocdrf.directory.parsers;

import br.edu.ocdrf.parsers.AbstractParser;
import br.edu.ocdrf.exceptions.ValidationException;
import br.edu.ocdrf.query.ContextQuery;
import br.edu.ocdrf.query.ContextQueryTarget;
import br.edu.ocdrf.query.DirectoryQueryAtribute;
import br.edu.ocdrf.query.DirectoryQueryType1;
import br.edu.ocdrf.query.DirectoryQueryType1Target;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

/*
 * @author Leila Negris Bezerra
 * 
 * Esta classe será usada nas consultas do SC e SD ao SRD buscando por atributos em particular 
 * que eles irão "converter" como o cliente solicitar
 * 
 * Tb usada na inicializacao dos servicos para a busca dos elementos da infraestrutura
 * 
 * <DirectoryQuery>
 * 		<Target ID="ContextService01" Component="" Capacity="" Attribute="" />
 * </DirectoryQuery>
 */
public class DirectoryQueryType1Parser extends AbstractParser<DirectoryQueryType1> {

    private static final String XSD = "DirectoryQuery.xsd";

    /**
     *
     * @param inputStream
     * @return
     * @throws ValidationException
     */
    /*
     * Formatos esperados - Vai ser usado pelo CS e DS (Só o DS vai usar o Component)
     * 
     * 	<DirectoryQuery><Target ID="" Component="É pro DS" Capacity="xxx" Attribute="xxx"></DirectoryQuery>
     Ou
     <DirectoryQuery><Target ID="" Capacity="xxx"></DirectoryQuery>
     ou
     <DirectoryQuery><Target ID=""></DirectoryQuery>
     * 
     * Vou ignorar a tag DirectoryQuery
     */
    @Override
    public DirectoryQueryType1 parseDocument(String xml) throws Exception {

        //validateDocument(XSD, xml);

        XStream xstream = newParser();
        DirectoryQueryType1 query = (DirectoryQueryType1) xstream.fromXML(xml);

        return query;
    }

    public String parseObject(DirectoryQueryType1 directoryQueryType1) throws ValidationException {
        XStream xstream = newParser();

        String xml = xstream.toXML(directoryQueryType1);
	
        System.out.println("-----------------------");
        System.out.println(xml);
        System.out.println("-----------------------");

        //validateDocument(XSD, xml);

        return xml;
    }

    private XStream newParser() {
        XStream xstream = new XStream(new DomDriver());

        xstream.alias("DirectoryQuery", DirectoryQueryType1.class);
        xstream.aliasField("Target", DirectoryQueryType1.class, "target");
	xstream.addImplicitCollection(DirectoryQueryType1.class, "targetList", DirectoryQueryType1Target.class);

        xstream.useAttributeFor(DirectoryQueryType1Target.class, "componentType");
        xstream.useAttributeFor(DirectoryQueryType1Target.class, "capacityName");
        xstream.useAttributeFor(DirectoryQueryType1Target.class, "uuid");
        xstream.useAttributeFor(DirectoryQueryType1Target.class, "id");
        
	xstream.addImplicitCollection(DirectoryQueryType1Target.class, "attributeNames");

        xstream.alias("Attribute", DirectoryQueryAtribute.class);
        xstream.useAttributeFor(DirectoryQueryAtribute.class, "name");

        return xstream;
    }
}
