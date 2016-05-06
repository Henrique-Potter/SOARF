
package br.edu.ocdrf.agent.entities;

import br.edu.ocdrf.oal.domain.XMLSerializedable;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;



public class DeviceConfigOntolology extends XMLSerializedable{
    
    @XStreamAlias("File")
    @XStreamAsAttribute
    private String file;

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    @Override
    protected void setXmlModel(XStream xstream) {
        xstream.processAnnotations(DeviceConfigOntolology.class);
    }

    @Override
    public DeviceConfigOntolology createObjectFromXML(String xml) {
        return (DeviceConfigOntolology) ontologyXStreamModel.fromXML(xml);
    }
    
    
}
