package br.edu.ocdrf.util;

import com.hp.hpl.jena.shared.uuid.JenaUUID;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

public class UniquefyOntologyNodes {

    public static String resUUID;
    
    public static String uniquefyOntology(String ontologyXML) throws Exception {

        Document doc = loadXMLFromString(ontologyXML);

        uniquifyOntologyNode(doc, "Sensor", "hasComponent");
        uniquifyOntologyNode(doc, "Actuator", "hasComponent");
        uniquifyOntologyNode(doc, "Resource", "");
        uniquifyOntologyNode(doc, "InvokeMethod", "invokedBy");
        uniquifyOntologyNode(doc, "InvokeOperation", "hasInvokeOperation");
        uniquifyOntologyNode(doc, "Location", "hasLocation");

        ArrayList<String> capabilityRelationTags = new ArrayList<>();
        capabilityRelationTags.add("hasCapability");
        capabilityRelationTags.add("returnsCapability");
        uniquifyOntologyNode(doc, "InputCapability", capabilityRelationTags);

        return toString(doc);
    }

    private static void uniquifyOntologyNode(Document doc, String mainTag, List<String> relatedTags) throws DOMException {

        NodeList nodeList = doc.getElementsByTagName(mainTag);
        searchAndChangeNodeIDs(nodeList, relatedTags, doc);

    }

    private static void uniquifyOntologyNode(Document doc, String mainTag, String relatedTag) throws DOMException {

        NodeList nodeList = doc.getElementsByTagName(mainTag);

        ArrayList<String> relatedTags = null;
        
        if (!relatedTag.isEmpty()) {
            relatedTags = new ArrayList<>();
            relatedTags.add(relatedTag);
        }

        searchAndChangeNodeIDs(nodeList, relatedTags, doc);

    }

    private static void searchAndChangeNodeIDs(NodeList nodeList, List<String> relatedTags, Document doc) throws DOMException {
        if (nodeList != null) {
            for (int i = 0; i < nodeList.getLength(); i++) {

                String uniqueNodeID = JenaUUID.generate().asString().replaceAll("-", "");

                Node node = nodeList.item(i);

                NamedNodeMap attr = node.getAttributes();
                Node nodeAttr = attr.getNamedItem("rdf:ID");
                String nodeAttrName = nodeAttr.getTextContent();
                String tempNodeContent = "#" + nodeAttrName;

                if (relatedTags != null && !relatedTags.isEmpty()) {
                    for (String relatedTag : relatedTags) {
                        NodeList relatedNodeList = doc.getElementsByTagName(relatedTag);
                        if (relatedNodeList != null) {
                            setUniqueIDtoRelationTags(relatedNodeList, tempNodeContent, uniqueNodeID);
                        }
                    }
                }
                
                String newNodeName = nodeAttrName + "_" + uniqueNodeID;
                
                if(node.getNodeName().equals("Resource")){
                    resUUID=newNodeName;
                }
                
                nodeAttr.setNodeValue(newNodeName);
            }
        }
    }

    public static Document loadXMLFromString(String xml) throws Exception {

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();

        Document doc;
        try (StringReader str = new StringReader(xml)) {
            InputSource is = new InputSource();
            is.setEncoding("UTF-8");
            is.setCharacterStream(str);
            doc = builder.parse(is);
        }

        return doc;
    }

    private static void setUniqueIDtoRelationTags(NodeList relatedNodeList, String tempNodeContent, String uniqueNodeID) throws DOMException {

        for (int j = 0; j < relatedNodeList.getLength(); j++) {

            Node relatedNode = relatedNodeList.item(j);
            NamedNodeMap rAttr = relatedNode.getAttributes();
            Node rNodeAttr = rAttr.getNamedItem("rdf:resource");

            String rNodeAttrName = rNodeAttr.getTextContent();

            if (rNodeAttrName.equals(tempNodeContent)) {
                rNodeAttr.setNodeValue(rNodeAttrName + "_" + uniqueNodeID);
            }
        }

    }

    public static String toString(Document doc) {
        try {

            StringWriter sw = new StringWriter();

            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer transformer = tf.newTransformer();
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
            transformer.setOutputProperty(OutputKeys.METHOD, "xml");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");

            StreamResult str = new StreamResult();
            
            transformer.transform(new DOMSource(doc), new StreamResult(sw));

            String output = sw.getBuffer().toString();
            sw.close();
            //String output = sw.getBuffer().toString().replaceAll("\n|\r", "");

            return output;
        } catch (Exception ex) {
            throw new RuntimeException("Error converting to String", ex);
        }
    }
}
