/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ocdrf.entities;

import br.edu.ocdrf.oal.domain.XMLSerializedable;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 *
 * @author henrique
 */
@XStreamAlias("AttributeData")
public class AttributeData extends XMLSerializedable {

    @XStreamAlias("Value")
    @XStreamAsAttribute
    private String value;

    @XStreamAlias("DateInMilliseconds")
    @XStreamAsAttribute
    private Long date;

    public AttributeData(String v, Long d) {
        value = v;
        date = d;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Long getDate() {
        return date;
    }

    public void setDate(Long date) {
        this.date = date;
    }

    public boolean areFieldsNull() {
        return ((value == null) && (date == null));
    }

    @Override
    protected void setXmlModel(XStream xstream) {
        xstream.processAnnotations(AttributeData.class);
    }

    @Override
    public AttributeData createObjectFromXML(String xml) {
        return (AttributeData) ontologyXStreamModel.fromXML(xml);
    }

}
