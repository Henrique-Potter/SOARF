package br.edu.ocdrf.context.parsers;

import br.edu.ocdrf.parsers.AbstractParser;
import br.edu.ocdrf.query.ContextResponse;
import br.edu.ocdrf.query.WorkAttribute;
import br.edu.ocdrf.query.WorkResponseInfo;
import br.edu.ocdrf.util.xml.ObjectStringConverter;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

/**
 * @author andre
 *
 */

/*
 * <ContextResponse>
 * 	<ResourceInfo ID="TV01" Component="D32W831_Active_Engine" Capacity="ActiveDevice" >
 * 		<Attribute Name="tipo" Value="bonitona" Type="Static" Unit="String"/>
 * 		<Attribute Name="power_on" Value="false" Type="Dynamic" Unit="Boolean"/>
 * 	</ResourceInfo>
 * </ContextResponse>
 */
public class ContextResponseParser extends AbstractParser<ContextResponse> {

    private static final String XSD = "ContextResponse.xsd";

    /*
     * (non-Javadoc)
     * 
     * @see br.edu.cdrf.directory.parsers.AbstractParser#parseDocument(java.io.InputStream)
     */
    @Override
    public ContextResponse parseDocument(String xml) throws Exception {

        //validateDocument(XSD, xml);

        XStream xstream = newParser();
        ContextResponse query = (ContextResponse) xstream.fromXML(xml);

        return query;
    }

    public String parseObject(ContextResponse contextResponse) throws Exception {

        //Anular os atributos que eu nao quero que aparecam no XML de resposta - senao o xstrem pega estes atribs tb
        XStream xstream = newParser();

        String xml = xstream.toXML(ContextResponse.prepareForParsing(contextResponse));

        System.out.println("-----------------------");
        System.out.println(xml);
        System.out.println("-----------------------");

        //validateDocument(XSD, xml);

        return xml;
    }

    private XStream newParser() {
        XStream xstream = new XStream(new DomDriver());
        xstream.registerConverter(new ObjectStringConverter());

        xstream.alias("ContextResponse", ContextResponse.class);
        xstream.addImplicitCollection(ContextResponse.class, "contextResponseInfoList", WorkResponseInfo.class);

        xstream.alias("ResourceInfo", WorkResponseInfo.class);
        xstream.useAttributeFor(WorkResponseInfo.class, "id");
        xstream.useAttributeFor(WorkResponseInfo.class, "componentType");
        xstream.useAttributeFor(WorkResponseInfo.class, "capacityNameInQuery");
        xstream.aliasAttribute(WorkResponseInfo.class, "capacityNameInQuery", "capacityName");

        xstream.addImplicitCollection(WorkResponseInfo.class, "attributes", WorkAttribute.class);
        xstream.alias("Attribute", WorkAttribute.class);
        xstream.useAttributeFor(WorkAttribute.class, "nameInQuery");
        xstream.aliasAttribute(WorkAttribute.class, "nameInQuery", "name");
        xstream.useAttributeFor(WorkAttribute.class, "valueInQuery");
        xstream.useAttributeFor(WorkAttribute.class, "valueUpdated");
        xstream.aliasAttribute(WorkAttribute.class, "valueUpdated", "value");

        //Temporario dado que nao estou fazendo conversao das unidades
        xstream.useAttributeFor(WorkAttribute.class, "unitInRDS");
        xstream.aliasAttribute(WorkAttribute.class, "unitInRDS", "unit");
        //xstream.useAttributeFor(WorkAttribute.class, "unitInQuery");
        //xstream.aliasAttribute(WorkAttribute.class, "unitInQuery", "unit");

        return xstream;
    }
}
