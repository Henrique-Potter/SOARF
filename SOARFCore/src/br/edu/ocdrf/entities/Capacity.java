package br.edu.ocdrf.entities;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class Capacity {

    private final List<Attribute> attributes = new LinkedList<>();
    private String componentType;
    private String name;

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

    public String getType() {
        return componentType;
    }

    public void setType(String type) {
        this.componentType = type;
    }

//	public List<Attribute> getDynamicAttributes() {
//		List<Attribute> dinamicAttributes = new LinkedList<Attribute>();
//		for (Attribute attribute : attributes) {
//			if (attribute.isDynamic()) {
//				dinamicAttributes.add(attribute);
//			}
//		}
//		return dinamicAttributes;
//	}
//
//	public List<Attribute> getFixedAttributes() {
//		List<Attribute> fixedAttributes = new LinkedList<Attribute>();
//		for (Attribute attribute : attributes) {
//			if (!attribute.isDynamic()) {
//				fixedAttributes.add(attribute);
//			}
//		}
//		return fixedAttributes;
//	}
//
//	public boolean containsDynamicAttr() {
//		for (Attribute attribute : attributes) {
//			if (attribute.isDynamic()) {
//				return true;
//			}
//		}
//		return false;
//	}
//
    public int numberOfAttributes() {
        return attributes.size();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getComponentType() {
        return componentType;
    }

    public void setComponentType(String componentType) {
        this.componentType = componentType;
    }
}
