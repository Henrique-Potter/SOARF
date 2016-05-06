package br.edu.ocdrf.message;

import br.edu.ocdrf.oal.domain.XMLSerializedable;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;


@XStreamAlias("OntologyQueryTriples")
public class OntologyQueryTriple extends XMLSerializedable{

    @XStreamAlias("subjectInstance")
    @XStreamAsAttribute
    private String subjectInstance;
    
    @XStreamAlias("subjectClass")
    @XStreamAsAttribute
    private String subjectClass;
    
    @XStreamAlias("objectProperty")
    @XStreamAsAttribute
    private String objectProperty;
    
    @XStreamAlias("dataProperty")
    @XStreamAsAttribute
    private String dataProperty;
    
    @XStreamAlias("dataPropertyValue")
    @XStreamAsAttribute
    private String dataPropertyValue;
    
    @XStreamAlias("propertyRangeClass")
    @XStreamAsAttribute
    private String propertyRangeClass;
    
    @XStreamAlias("propertyRangeInstance")
    @XStreamAsAttribute
    private String propertyRangeInstance;

    public String getSubjectInstance() {
        return subjectInstance;
    }

    public void setSubjectInstance(String subjectInstance) {
        this.subjectInstance = subjectInstance;
    }

    public String getSubjectClass() {
        return subjectClass;
    }

    public void setSubjectClass(String subjectClass) {
        this.subjectClass = subjectClass;
    }

    public String getObjectProperty() {
        return objectProperty;
    }

    public void setObjectProperty(String objectProperty) {
        this.objectProperty = objectProperty;
    }

    public String getDataProperty() {
        return dataProperty;
    }

    public void setDataProperty(String dataProperty) {
        this.dataProperty = dataProperty;
    }

    public String getDataPropertyValue() {
        return dataPropertyValue;
    }

    public void setDataPropertyValue(String dataPropertyValue) {
        this.dataPropertyValue = dataPropertyValue;
    }

    public String getPropertyRangeClass() {
        return propertyRangeClass;
    }

    public void setPropertyRangeClass(String propertyRangeClass) {
        this.propertyRangeClass = propertyRangeClass;
    }

    public String getPropertyRangeInstance() {
        return propertyRangeInstance;
    }

    public void setPropertyRangeInstance(String propertyRangeInstance) {
        this.propertyRangeInstance = propertyRangeInstance;
    }

    @Override
    protected void setXmlModel(XStream xstream) {
	xstream.processAnnotations(OntologyQueryTriple.class);
    }

    @Override
    public OntologyQueryTriple createObjectFromXML(String xml) {
	return (OntologyQueryTriple) ontologyXStreamModel.fromXML(xml);
    }
}
