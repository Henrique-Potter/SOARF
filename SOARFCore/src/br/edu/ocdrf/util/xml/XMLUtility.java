package br.edu.ocdrf.util.xml;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import javax.xml.stream.XMLStreamReader;

/**
 * @author andre
 *
 */
public abstract class XMLUtility {

    public static final int SUCCESS = 0;
    public static final int TYPE_NOT_REGISTERED = 1;
    public static final int RESOURCE_NOT_REGISTERED = 2;
    public static final int RESOURCE_ALREADY_REGISTERED = 3;
    public static final int INVALID_XML = 4;
    public static final int RESOURCE_NOT_FOUND = 5;
    static final SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

    /**
     *
     * @param reader
     * @param attributeName
     * @return
     */
    public static String getAttributeValue(XMLStreamReader reader,
            String attributeName) {
        for (int i = 0; i < reader.getAttributeCount(); i++) {
            String attrName = reader.getAttributeLocalName(i);
            if (attributeName.equals(attrName)) {
                return reader.getAttributeValue(i);
            }
        }
        return null;
    }

    /**
     *
     * @param constraintValue
     * @param dataType
     * @return
     */
    public static Object cast(String value, String dataType) {
        Object newValue;
        if ("Dynamic".equalsIgnoreCase(value)) {
            newValue = value;
        }
        else {
            if ("float".equals(dataType)) {
                newValue = Float.valueOf(value);
            }
            else if ("double".equals(dataType)) {
                newValue = Double.valueOf(value);
            }
            else if ("integer".equals(dataType)) {
                newValue = Integer.valueOf(value);
            }
            else if ("long".equals(dataType)) {
                newValue = Long.valueOf(value);
            }
            else if ("DateTime".equals(dataType)) {
                //xml.append(df.format(state.getCollectTime()));
//				Date d = df.parse("15/12/2010T10:00:00");
                try {
                    newValue = df.parse(value);
                }
                catch (ParseException e) {
                    newValue = "";
                }
            }
            else {
                newValue = value;
            }
        }
        return newValue;
    }

    /**
     *
     * @param errorCode
     * @return
     */
    public static String makeMessageError(int errorCode) {
        switch (errorCode) {
            case TYPE_NOT_REGISTERED:
                return "<Result><Code>" + TYPE_NOT_REGISTERED + "</Code><Message>"
                        + "Resource type is not registred yet."
                        + "</Message></Result>";
            case RESOURCE_NOT_REGISTERED:
                return "<Result><Code>" + RESOURCE_NOT_REGISTERED
                        + "</Code><Message>" + "Resource is not registred yet."
                        + "</Message></Result>";
            case RESOURCE_ALREADY_REGISTERED:
                return "<Result><Code>" + RESOURCE_ALREADY_REGISTERED
                        + "</Code><Message>" + "Resource already registred."
                        + "</Message></Result>";
            case INVALID_XML:
                return "<Result><Code>" + INVALID_XML + "</Code><Message>"
                        + "Invalid XML." + "</Message></Result>";
            case RESOURCE_NOT_FOUND:
                return "<Result><Code>" + RESOURCE_NOT_FOUND + "</Code><Message>"
                        + "Resource was not found." + "</Message></Result>";
        }
        return null;
    }

    /**
     *
     * @return
     */
    public static String makeSuccessMessage() {
        return "<Result><Code>" + SUCCESS
                + "</Code><Message>Success.</Message></Result>";
    }

//	public static String makeQueryByURI(String uri) {
//		return "<DirectoryQuery><URI>" + uri + "</URI></DirectoryQuery>";
//	}
//
//	public static String makeQueryByURI(String uri, String resourceType) {
//		return "<DirectoryQuery><URI>" + uri + "</URI><Type Value=\""+ resourceType
//		+ "\"/></DirectoryQuery>";
//	}
    /**
     *
     * @param xmlResult
     * @return
     */
    public static boolean isError(String xmlResult) {
        return xmlResult.startsWith("<Result>")
                && !xmlResult.startsWith("<Result><Code>0</Code>");
    }
    ////////////////////////////////////////////////////////
//	public static String makeDirectoryQueryType2(DirectoryQueryType2 directoryQueryType2) {
//		StringBuilder xmlQuery = new StringBuilder("<DirectoryQuery>");
//		
//		DirectoryQueryType2Target target = directoryQueryType2.getTarget();
//		
//		xmlQuery.append("<Target ID=\"" + target.getID() + 
//							"\" Element=\"" + target.getElement() + 
//							"\" Property=\"" + target.getProperty() + "\" /");
//
//		xmlQuery.append("</DirectoryQuery>");
//		
//		return xmlQuery.toString();
//	}
//    public static String makeContextQuery(ContextQuery contextQuery) throws Exception {
//        
//        StringBuilder builder = new StringBuilder();
//        builder.append("<ContextQuery>");
//        
//        for (ContextQueryTarget target : contextQuery.getTargets()) {
//    
//            builder.append("<Target ID=\"" + target.getID() + "\" CapacityName=\"" + target.getCapacityName() + "\">");
//    
//            for (Attribute a : target.getAttributes()) {
//            
//            	builder.append("<Attribute Name=\"" + a.getName() + "\"" + (a.getUnit()==null?"": " Unit=\"" + a.getUnit() + "\"") + " />");
//            }
//                        
//            builder.append("</Target>");
//            
//        }
//
//        if (contextQuery.getRequestFrom() != null)
//        	builder.append("<RequestFrom>" + contextQuery.getRequestFrom() + "</RequestFrom>");
//    
//        builder.append("</ContextQuery>");
//        
//        return builder.toString();        
//        
//    }
}