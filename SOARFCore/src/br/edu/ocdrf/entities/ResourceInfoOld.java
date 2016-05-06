package br.edu.ocdrf.entities;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class ResourceInfoOld {

    //ID do resource
    String ID;
    private String componentType;
    private String capacityName;
    private String invokeString;
    private List<Attribute> attributes = new LinkedList<>();

    public ResourceInfoOld() {
    }

    public ResourceInfoOld(String ID) {
        this.ID = ID;
    }

    public void addAttribute(Attribute attribute) {
        this.attributes.add(attribute);
    }

    public List<Attribute> getAttributes() {
        return Collections.unmodifiableList(attributes);
    }

    public Attribute getAttributeByName(String name) {
        for (Attribute attribute : attributes) {
            if (name.equals(attribute.getName())) {
                return attribute;
            }
        }
        return null;
    }

    public Attribute getAttributeByIndex(int index) {
        return attributes.get(index);
    }

    public String getType() {
        return componentType;
    }

    public void setType(String type) {
        this.componentType = type;
    }

    public int numberOfAttributes() {
        return attributes.size();
    }

    public String getCapacityName() {
        return capacityName;
    }

    public void setCapacityName(String name) {
        this.capacityName = name;
    }

    public String getComponentType() {
        return componentType;
    }

    public void setComponentType(String componentType) {
        this.componentType = componentType;
    }

    public String getID() {
        return ID;
    }

    public void setID(String id) {
        ID = id;
    }

    public String getInvokeString() {
        return invokeString;
    }

    public void setInvokeString(String invokeString) {
        this.invokeString = invokeString;
    }
}
