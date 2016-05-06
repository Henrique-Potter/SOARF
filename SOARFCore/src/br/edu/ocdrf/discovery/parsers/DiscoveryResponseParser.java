package br.edu.ocdrf.discovery.parsers;

import br.edu.ocdrf.parsers.AbstractParser;
import br.edu.ocdrf.query.DiscoveryResponse;
import br.edu.ocdrf.query.WorkAttribute;
import br.edu.ocdrf.query.WorkResponseInfo;
import br.edu.ocdrf.util.xml.ObjectStringConverter;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

/**
 * @author andre
 *
 */
public class DiscoveryResponseParser extends AbstractParser<DiscoveryResponse> {

    private static final String XSD = "DiscoveryResponse.xsd";

    /*
     * (non-Javadoc)
     * 
     * @see br.edu.cdrf.directory.parsers.AbstractParser#parseDocument(java.io.InputStream)
     */
    @Override
    public DiscoveryResponse parseDocument(String xml) throws Exception {
        //validateDocument(XSD, xml);

        XStream xstream = newParser();
        DiscoveryResponse query = (DiscoveryResponse) xstream.fromXML(xml);

        return query;
    }

    public String parseObject(DiscoveryResponse discoveryQuery) throws Exception {

	//Anular os atributos que eu nao quero que aparecam no XML de resposta - senao o xstrem pega estes atribs tb
        XStream xstream = newParser();

        String xml = xstream.toXML(DiscoveryResponse.prepareForParsing(discoveryQuery));

        System.out.println("-----------------------");
        System.out.println(xml);
        System.out.println("-----------------------");

        //validateDocument(XSD, xml);

        return xml;
    }

    private XStream newParser() {
        XStream xstream = new XStream(new DomDriver());
        xstream.registerConverter(new ObjectStringConverter());

        xstream.alias("DiscoveryResponse", DiscoveryResponse.class);
        xstream.addImplicitCollection(DiscoveryResponse.class, "discoveryResponseInfoList", WorkResponseInfo.class);

        xstream.alias("ResourceInfo", WorkResponseInfo.class);
        xstream.useAttributeFor(WorkResponseInfo.class, "id");
        xstream.useAttributeFor(WorkResponseInfo.class, "componentType");
        xstream.useAttributeFor(WorkResponseInfo.class, "capacityNameInQuery");
        xstream.aliasAttribute(WorkResponseInfo.class, "capacityNameInQuery", "capacityName");
        xstream.addImplicitCollection(WorkResponseInfo.class, "attributes", WorkAttribute.class);

        xstream.alias("Attribute", WorkAttribute.class);
        xstream.useAttributeFor(WorkAttribute.class, "nameInQuery");
        xstream.aliasAttribute(WorkAttribute.class, "nameInQuery", "name");
//	xstream.useAttributeFor(WorkAttribute.class, "valueInQuery");
        xstream.useAttributeFor(WorkAttribute.class, "valueUpdated");
        xstream.aliasAttribute(WorkAttribute.class, "valueUpdated", "value");
        xstream.useAttributeFor(WorkAttribute.class, "unitInQuery");
        xstream.aliasAttribute(WorkAttribute.class, "unitInQuery", "unit");

        return xstream;
    }
}
