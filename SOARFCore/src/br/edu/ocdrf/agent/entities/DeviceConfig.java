package br.edu.ocdrf.agent.entities;

import br.edu.ocdrf.oal.domain.XMLSerializedable;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;


public class DeviceConfig extends XMLSerializedable{
    
    @XStreamAlias("UpdateType")
    @XStreamAsAttribute
    private String updateType;
    
    @XStreamAlias("ProbePeriod")
    @XStreamAsAttribute
    private int probePeriod;

    @XStreamAlias("Connection")
    private DeviceConfigConnection deviceConfigConnection = new DeviceConfigConnection();
    
    @XStreamAlias("ByteArrayMap")
    private DeviceConfigByteArrayMap deviceConfigByteArrayMap = new DeviceConfigByteArrayMap();

    public DeviceConfigConnection getDeviceConfigConnection() {
        return deviceConfigConnection;
    }

    public void setDeviceConfigConnection(DeviceConfigConnection deviceConfigConnection) {
        this.deviceConfigConnection = deviceConfigConnection;
    }

    public DeviceConfigByteArrayMap getDeviceConfigByteArrayMap() {
        return deviceConfigByteArrayMap;
    }

    public void setDeviceConfigByteArrayMap(DeviceConfigByteArrayMap deviceConfigByteArrayMap) {
        this.deviceConfigByteArrayMap = deviceConfigByteArrayMap;
    }
    

    public String getUpdateType() {
        return updateType;
    }

    public void setUpdateType(String updateType) {
        this.updateType = updateType;
    }

    public int getProbePeriod() {
        return probePeriod;
    }

    public void setProbePeriod(int probePeriod) {
        this.probePeriod = probePeriod;
    }

    @Override
    protected void setXmlModel(XStream xstream) {
        xstream.processAnnotations(DeviceConfig.class);
    }

    @Override
    public DeviceConfig createObjectFromXML(String xml) {
        return (DeviceConfig) ontologyXStreamModel.fromXML(xml);
    }
    
}
