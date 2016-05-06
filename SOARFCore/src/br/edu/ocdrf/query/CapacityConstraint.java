package br.edu.ocdrf.query;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import br.edu.ocdrf.entities.Attribute;

public class CapacityConstraint {

    /*
     <CapacityConstraint Component="Sensor" Capacity="ActiveDevice">
     <Attribute Name="temperatura" op=">" Value="30" Unit="C" />
     <Attribute Name="xxx" op="<" Value="ID de alguem" Unit="CoreElement" />
     <Attribute Name="temperatura">
     </CapacityConstraint>
     */
    private String componentType;
    private String capacityName;
    private List<Attribute> attributes = new LinkedList<>();

    @Override
    public String toString() {
        return "[componentType: " + componentType + ", capacityName: " + capacityName + ", attributes: " + attributes() + "]";
    }

    private List<Attribute> attributes() {
        if (attributes == null) {
            attributes = new LinkedList<>();
        }
        return attributes;
    }

    public void addAttribute(Attribute attribute) {
        this.attributes().add(attribute);
    }

    public List<Attribute> getAttributes() {
        return Collections.unmodifiableList(attributes());
    }

    public Attribute getAttributeByName(String name) {
        for (Attribute attribute : attributes()) {
            if (name.equals(attribute.getName())) {
                return attribute;
            }
        }
        return null;
    }

    public int numberOfAttributes() {
        return attributes().size();
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

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((attributes() == null) ? 0 : attributes().hashCode());
        result = prime * result + ((capacityName == null) ? 0 : capacityName.hashCode());
        result = prime * result + ((componentType == null) ? 0 : componentType.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final CapacityConstraint other = (CapacityConstraint) obj;
        if (attributes() == null) {
            if (other.attributes() != null) {
                return false;
            }
        }
        else if (!attributes().equals(other.attributes())) {
            return false;
        }
        if (capacityName == null) {
            if (other.capacityName != null) {
                return false;
            }
        }
        else if (!capacityName.equals(other.capacityName)) {
            return false;
        }
        if (componentType == null) {
            if (other.componentType != null) {
                return false;
            }
        }
        else if (!componentType.equals(other.componentType)) {
            return false;
        }
        return true;
    }
}
