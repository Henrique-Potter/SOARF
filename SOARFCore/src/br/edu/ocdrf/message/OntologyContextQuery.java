package br.edu.ocdrf.message;

import br.edu.ocdrf.oal.domain.XMLSerializedable;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;


@XStreamAlias("OntologyContextQuery")
public class OntologyContextQuery extends XMLSerializedable {

    @XStreamAlias("capability")
    @XStreamAsAttribute
    private String capability;

    @XStreamAlias("location")
    @XStreamAsAttribute
    private String location;

    public String getCapability() {
        return capability;
    }

    public void setCapability(String capability) {
        this.capability = capability;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }    
    
    @Override
    protected void setXmlModel(XStream xstream) {
        xstream.processAnnotations(OntologyContextQuery.class);
    }

    @Override
    public OntologyContextQuery createObjectFromXML(String xml) {
        return (OntologyContextQuery) ontologyXStreamModel.fromXML(xml);
    }
}
