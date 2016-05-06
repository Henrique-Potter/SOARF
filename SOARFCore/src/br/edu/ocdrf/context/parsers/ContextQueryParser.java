package br.edu.ocdrf.context.parsers;

import br.edu.ocdrf.entities.Attribute;
import br.edu.ocdrf.entities.InvokeMethod;
import br.edu.ocdrf.parsers.AbstractParser;
import br.edu.ocdrf.exceptions.ValidationException;
import br.edu.ocdrf.query.ContextQuery;
import br.edu.ocdrf.query.ContextQueryTarget;
import br.edu.ocdrf.util.xml.ObjectStringConverter;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

public class ContextQueryParser extends AbstractParser<ContextQuery> {

    private static final String XSD = "ContextQuery.xsd";

    /*
     * (non-Javadoc)
     * 
     * @see br.edu.cdrf.directory.parsers.AbstractParser#parseDocument(java.io.InputStream)
     * 
     * <ContextQuery>
     <synchronized>true</synchronized>
     <Target ID="DiscoveryService01" Capacity="ActiveDevice" />
     <Target ID="TV01" Capacity="HeartRate" />
     <Attributes>
     <Attribute Name="power_on"/>
     <Attribute Name="xxx"/>
     </Attributes>
     </Target>
     </ContextQuery>
     * 
     * 
     */
    @Override
    public ContextQuery parseDocument(String xml) throws ValidationException {

        //validateDocument(XSD, xml);

        XStream xstream = newParser();
        ContextQuery query = (ContextQuery) xstream.fromXML(xml);

        return query;
    }

    public String parseObject(ContextQuery contextQuery) throws Exception {

        XStream xstream = newParser();

        String xml = xstream.toXML(contextQuery);

        System.out.println("-----------------------");
        System.out.println(xml);
        System.out.println("-----------------------");

        //validateDocument(XSD, xml);

        return xml;
    }

    private XStream newParser() {
        XStream xstream = new XStream(new DomDriver());
        xstream.registerConverter(new ObjectStringConverter());

        xstream.alias("ContextQuery", ContextQuery.class);
        xstream.addImplicitCollection(ContextQuery.class, "targets", ContextQueryTarget.class);
        xstream.useAttributeFor(ContextQuery.class, "requestFrom");

        xstream.alias("Target", ContextQueryTarget.class);
        xstream.useAttributeFor(ContextQueryTarget.class, "id");
        xstream.useAttributeFor(ContextQueryTarget.class, "uuid");
        xstream.useAttributeFor(ContextQueryTarget.class, "capacityName");

        xstream.aliasField("InvokeMethod", ContextQueryTarget.class, "invokeMethod");
        xstream.useAttributeFor(InvokeMethod.class, "invokeString");
        xstream.useAttributeFor(InvokeMethod.class, "operationName");
        xstream.useAttributeFor(InvokeMethod.class, "returnType");

        xstream.addImplicitCollection(ContextQueryTarget.class, "attributes", Attribute.class);
        xstream.alias("Attribute", Attribute.class);
        xstream.useAttributeFor(Attribute.class, "name");
        xstream.useAttributeFor(Attribute.class, "unit");

        return xstream;
    }
}
