package br.edu.ocdrf.agent.entities;

import br.edu.ocdrf.oal.domain.XMLSerializedable;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import java.io.File;

@XStreamAlias("ResourceAgentConfig")
public class ResourceAgentConfig extends XMLSerializedable {

    @XStreamAlias("Description")
    private String description;
    
    @XStreamAlias("uri")
    @XStreamAsAttribute
    private String uri;
    
    @XStreamAlias("ContextServiceID")
    @XStreamAsAttribute
    private String contextServiceID;

    @XStreamAlias("Ontology")
    private DeviceConfigOntolology ontology = new DeviceConfigOntolology();
    
    @XStreamAlias("Login")
    public  Login login = new Login();

    @XStreamAlias("DeviceConfig")
    private DeviceConfig deviceConfig = new DeviceConfig();

    @XStreamAlias("DirectoryService")
    private DeviceConfigDirectoryService devConfDirServ = new DeviceConfigDirectoryService();

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public DeviceConfigOntolology getOntology() {
        return ontology;
    }

    public void setOntology(DeviceConfigOntolology ontology) {
        this.ontology = ontology;
    }

    public DeviceConfig getDeviceConfig() {
        return deviceConfig;
    }

    public void setDeviceConfig(DeviceConfig deviceConfig) {
        this.deviceConfig = deviceConfig;
    }

    public DeviceConfigDirectoryService getDeviceConfigDirectoryService() {
        return devConfDirServ;
    }

    public void setDeviceConfigDirectoryService(DeviceConfigDirectoryService deviceConfigDirectoryService) {
        this.devConfDirServ = deviceConfigDirectoryService;
    }

    public String getContextServiceID() {
        return contextServiceID;
    }

    public void setContextServiceID(String contextServiceID) {
        this.contextServiceID = contextServiceID;
    }

    public Login getLogin() {
        return login;
    }

    public void setLogin(Login login) {
        this.login = login;
    }
    
    @Override
    protected void setXmlModel(XStream xstream) {
        xstream.processAnnotations(ResourceAgentConfig.class);
    }

    @Override
    public ResourceAgentConfig createObjectFromXML(String xml) {
        return (ResourceAgentConfig) ontologyXStreamModel.fromXML(xml);
    }
    
     
    public ResourceAgentConfig createObjectFromXML(File file) {
        return (ResourceAgentConfig) ontologyXStreamModel.fromXML(file);
    }
    
}
