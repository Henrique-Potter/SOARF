package br.edu.ocdrf.util.xml;

import java.text.SimpleDateFormat;

import br.edu.ocdrf.entities.ResourceState;

public class XMLEncoder {

    /**
     *
     * @param state
     * @return
     */
    public static String encode(ResourceState state) {
        StringBuilder xml = new StringBuilder();
        xml.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        xml.append("<ResourceState>");
        xml.append("<CollectTime>");
        final SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        xml.append(df.format(state.getCollectTime()));
        xml.append("</CollectTime>");
        xml.append("<Interval>");
        xml.append(state.getInterval());
        xml.append("</Interval>");
        if (!state.getAttributes().isEmpty()) {
            xml.append("<Attributes>");
//            for (Attribute value : state.getAttributes()) {
//                xml.append("<Attribute Name=\"");
//                xml.append(value.getName());
//                xml.append("\" Value=\"");
//                xml.append(
//                        (value.getUnit() != null && value.getUnit().equals("DateTime") && value.getValue() != null ? df.format(value.getValue()) : value.getValue()));
//                xml.append("\" />");
//            }
            xml.append("</Attributes>");
        }
        //TODO: Decidir o que fazer com essa URI.... Queria matar isso
        xml.append("<URI>");
        xml.append(state.getURI());
        xml.append("</URI>");
        xml.append("</ResourceState>");
        return xml.toString();
    }

    /**
     *
     * @param localURI
     * @param localServiceName
     * @param localServiceDescription
     * @param resourceType
     * @return
     */
    public static String encodeResourceObserver(String localURI,
            String localServiceName, String localServiceDescription,
            String resourceType) {
        final StringBuilder xmlMessage = new StringBuilder("<ResourceObserver>");
        xmlMessage.append("<URI>");
        xmlMessage.append(localURI);
        xmlMessage.append("</URI>");
        xmlMessage.append("<ServiceName>");
        xmlMessage.append(localServiceName);
        xmlMessage.append("</ServiceName>");
        xmlMessage.append("<Description>");
        xmlMessage.append(localServiceDescription);
        xmlMessage.append("</Description>");
        xmlMessage.append("<ResourceType>");
        xmlMessage.append(resourceType);
        xmlMessage.append("</ResourceType>");
        xmlMessage.append("</ResourceObserver>");
        return xmlMessage.toString();
    }
}