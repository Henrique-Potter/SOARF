/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ocdrf.agent.entities;

import br.edu.ocdrf.oal.domain.XMLSerializedable;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

public class DeviceConfigConnection extends XMLSerializedable {

    @XStreamAlias("Type")
    @XStreamAsAttribute
    private String type;

    @XStreamAlias("CommPort")
    @XStreamAsAttribute
    private String commPort;

    @XStreamAlias("Timeout")
    @XStreamAsAttribute
    private int timeout;

    @XStreamAlias("DataRate")
    @XStreamAsAttribute
    private int dataRate;

    @XStreamAlias("Databits")
    @XStreamAsAttribute
    private int databits;

    @XStreamAlias("StopBits")
    @XStreamAsAttribute
    private int stopBits;

    @XStreamAlias("Parity")
    @XStreamAsAttribute
    private int parity;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCommPort() {
        return commPort;
    }

    public void setCommPort(String commPort) {
        this.commPort = commPort;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public int getDataRate() {
        return dataRate;
    }

    public void setDataRate(int dataRate) {
        this.dataRate = dataRate;
    }

    public int getDatabits() {
        return databits;
    }

    public void setDatabits(int databits) {
        this.databits = databits;
    }

    public int getStopBits() {
        return stopBits;
    }

    public void setStopBits(int stopBits) {
        this.stopBits = stopBits;
    }

    public int getParity() {
        return parity;
    }

    public void setParity(int parity) {
        this.parity = parity;
    }

    
    
    @Override
    protected void setXmlModel(XStream xstream) {
        xstream.processAnnotations(DeviceConfigConnection.class);
    }

    @Override
    public DeviceConfigConnection createObjectFromXML(String xml) {
        return (DeviceConfigConnection) ontologyXStreamModel.fromXML(xml);
    }
}
