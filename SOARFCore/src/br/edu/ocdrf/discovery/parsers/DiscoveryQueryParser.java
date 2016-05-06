package br.edu.ocdrf.discovery.parsers;

import br.edu.ocdrf.entities.Attribute;
import br.edu.ocdrf.parsers.AbstractParser;
import br.edu.ocdrf.exceptions.ValidationException;
import br.edu.ocdrf.query.CapacityConstraint;
import br.edu.ocdrf.query.ClientConstraint;
import br.edu.ocdrf.query.DiscoveryQuery;
import br.edu.ocdrf.util.xml.ObjectStringConverter;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

/*
 * @author Leila Negris Bezerra
 * 
 * Esta classe será usada nas consultas do SC e SD ao SRD buscando por atributos em particular 
 * que eles irão "converter" como o cliente solicitar
 */
public class DiscoveryQueryParser extends AbstractParser<DiscoveryQuery> {

    private static final String XSD = "DiscoveryQuery.xsd";

    /**
     *
     * @return
     * @throws ValidationException
     */

    /*
     * Formato esperado
     * 
     * 
     * <DiscoveryQuery> <CapacityConstraint Component="Sensor"
     * Capacity="ActiveDevice"> <Attribute Name="temperatura" op=">" Value="30"
     * Unit="C" /> <Attribute Name="xxx" op="<" Value="ID de alguem"
     * Unit="CoreElement" /> <Attribute Name="temperatura">
     * </CapacityConstraint> <CapacityConstraint Component="Actuator">
     * <Attribute Name="mimetype" value="text/plain" /> </CapacityConstraint>
     * 
     * <UserConstraint Element="Resource" Property="locatedIn" op="1meterFrom"
     * Object="ID um Resource - AR" ObjectType="ResourceOntologyObject" />
     * <UserConstraint Element="Resource" Property="locatedIn" op="1meterFrom"
     * Object="PortaDaFrente" ObjectType="ClientOntologyObject" />
     * 
     * </DiscoveryQuery>
     */
    @Override
    public DiscoveryQuery parseDocument(String xml) throws Exception {

        System.out.println("-----------------------");
        System.out.println(xml);
        System.out.println("-----------------------");

        validateDocument(XSD, xml);

        XStream xstream = newParser();
        DiscoveryQuery query = (DiscoveryQuery) xstream.fromXML(xml);

        return query;
    }

    public String parseObject(DiscoveryQuery discoveryQuery) throws Exception {

        XStream xstream = newParser();

        String xml = xstream.toXML(discoveryQuery);

        System.out.println("-----------------------");
        System.out.println(xml);
        System.out.println("-----------------------");

        validateDocument(XSD, xml);

        return xml;
    }

    private XStream newParser() {
        XStream xstream = new XStream(new DomDriver());

        xstream.alias("DiscoveryQuery", DiscoveryQuery.class);
        xstream.addImplicitCollection(DiscoveryQuery.class, "capacityConstraints", CapacityConstraint.class);
        xstream.addImplicitCollection(DiscoveryQuery.class, "clientConstraints", ClientConstraint.class);

        xstream.alias("ClientConstraint", ClientConstraint.class);
        xstream.useAttributeFor(ClientConstraint.class, "element");
        xstream.useAttributeFor(ClientConstraint.class, "property");
        xstream.useAttributeFor(ClientConstraint.class, "operation");
        xstream.useAttributeFor(ClientConstraint.class, "object");
        xstream.useAttributeFor(ClientConstraint.class, "objectType");

        xstream.alias("CapacityConstraint", CapacityConstraint.class);
        xstream.useAttributeFor(CapacityConstraint.class, "componentType");
        xstream.useAttributeFor(CapacityConstraint.class, "capacityName");
        xstream.addImplicitCollection(CapacityConstraint.class, "attributes");

        xstream.alias("Attribute", Attribute.class);
        xstream.useAttributeFor(Attribute.class, "name");
        xstream.useAttributeFor(Attribute.class, "operator");
        xstream.useAttributeFor(Attribute.class, "value");
        xstream.useAttributeFor(Attribute.class, "unit");

        xstream.registerConverter(new ObjectStringConverter());

		// xstream.aliasField("author", Blog.class, "writer");
        // xstream.aliasField("countTotal", MyClass.class, "totalCountValue");
        return xstream;
    }
}
