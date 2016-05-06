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


public class DeviceConfigDirectoryService extends XMLSerializedable {

    @XStreamAlias("url")
    @XStreamAsAttribute
    private String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    
    
    @Override
    protected void setXmlModel(XStream xstream) {
        xstream.processAnnotations(DeviceConfigDirectoryService.class);
    }

    @Override
    public DeviceConfigDirectoryService createObjectFromXML(String xml) {
        return (DeviceConfigDirectoryService) ontologyXStreamModel.fromXML(xml);
    }
}
