package br.edu.ocdrf.entities;

import br.edu.ocdrf.oal.domain.XMLSerializedable;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import java.io.File;

public class RegistrationCertificate extends XMLSerializedable {

    @XStreamAlias("UpdateType")
    @XStreamAsAttribute
    public String uniqueName;

    @XStreamAlias("UpdateType")
    @XStreamAsAttribute
    public String secretKey;

    @Override
    protected void setXmlModel(XStream xstream) {
        xstream.processAnnotations(RegistrationCertificate.class);
    }

    @Override
    public RegistrationCertificate createObjectFromXML(String xml) {
        return (RegistrationCertificate) ontologyXStreamModel.fromXML(xml);
    }

    public RegistrationCertificate createObjectFromXML(File file) {
        return (RegistrationCertificate) ontologyXStreamModel.fromXML(file);
    }
}
