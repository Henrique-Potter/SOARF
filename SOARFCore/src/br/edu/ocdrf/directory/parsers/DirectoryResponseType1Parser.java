package br.edu.ocdrf.directory.parsers;

import br.edu.ocdrf.entities.Attribute;
import br.edu.ocdrf.entities.InvokeMethod;
import br.edu.ocdrf.parsers.AbstractParser;
import br.edu.ocdrf.exceptions.ValidationException;
import br.edu.ocdrf.query.DirectoryResponseType1;
import br.edu.ocdrf.query.DirectoryResponseType1Info;
import br.edu.ocdrf.util.xml.ObjectStringConverter;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;


/*
 * @author Leila Negris Bezerra
 * 
 * Esta classe será usada nas respostas das consultas do SC e SD ao SRD buscando por atributos em particular 
 * que eles irão "converter" como o cliente solicitar
 */
public class DirectoryResponseType1Parser extends AbstractParser<DirectoryResponseType1> {

    private static final String XSD = "DirectoryResponse.xsd";

    /**
     *
     * @param inputStream
     * @return
     * @throws ValidationException
     */
    /*
     * Formato esperado
     * 
     <DirectoryResponse>
     <ResourceInfo ID="xxxxx" Component="Sensor" Capacity="" InvokeString="">
     <Attribute Name="" Type="" Value="" Unit="" Precision="">
     <Attribute Name="" Type="" Value="" Unit="" />
     <Attribute Name="mimetype" Value="text/plain"/>
     <Attribute Name="result" unit="boolean"/>
     <Attribute Name="method" value="play"/>
     </ResourceInfo>
     <ResourceInfo ID="xxxxx" Component="Sensor" Capacity="">
     <Attribute Name="" Type="" Value="" Unit="" Precision="">
     </ResourceInfo>
     </DirectoryResponse>

     * 
     * 
     * 
     <Attributes>
     <Attribute Name="" Type="" Value="" Unit="">
     <Attribute Name="mimetype" Value="text/plain"/>
     <Attribute Name="result" unit="boolean"/>
     <Attribute Name="method" value="play"/>
     </Attributes>
     * 
     * Vou ignorar a tag DirectoryResponse
     */
    @Override
    public DirectoryResponseType1 parseDocument(String xml) throws Exception {

        //validateDocument(XSD, xml);

        XStream xstream = newParser();
        DirectoryResponseType1 query = (DirectoryResponseType1) xstream.fromXML(xml);

        return query;
    }

    public String parseObject(DirectoryResponseType1 response) throws Exception {

        XStream xstream = newParser();

        String xml = xstream.toXML(response);

        System.out.println("-----------------------");
        System.out.println(xml);
        System.out.println("-----------------------");

        //validateDocument(XSD, xml);

        return xml;
    }

    private XStream newParser() {
        
	XStream xstream = new XStream(new DomDriver());
        xstream.registerConverter(new ObjectStringConverter());

        xstream.alias("DirectoryResponse", DirectoryResponseType1.class);
        xstream.addImplicitCollection(DirectoryResponseType1.class, "directoryResponseType1InfoList", DirectoryResponseType1Info.class);

        xstream.alias("ResourceInfo", DirectoryResponseType1Info.class);
        xstream.useAttributeFor(DirectoryResponseType1Info.class, "id");
        xstream.useAttributeFor(DirectoryResponseType1Info.class, "componentId");
        xstream.useAttributeFor(DirectoryResponseType1Info.class, "componentType");
        xstream.useAttributeFor(DirectoryResponseType1Info.class, "capacityName");
        xstream.addImplicitCollection(DirectoryResponseType1Info.class, "attributes", Attribute.class);

        xstream.aliasField("InvokeMethod", DirectoryResponseType1Info.class, "invokeMethod");
        xstream.useAttributeFor(InvokeMethod.class, "invokeString");
        xstream.useAttributeFor(InvokeMethod.class, "operationName");
        xstream.useAttributeFor(InvokeMethod.class, "returnType");

        xstream.alias("Attribute", Attribute.class);
        xstream.useAttributeFor(Attribute.class, "name");
        xstream.useAttributeFor(Attribute.class, "operator");
        xstream.useAttributeFor(Attribute.class, "value");
        xstream.useAttributeFor(Attribute.class, "type");
        xstream.useAttributeFor(Attribute.class, "unit");

        return xstream;
    }
}
