package br.edu.ocdrf.query;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import br.edu.ocdrf.entities.Attribute;

public class DirectoryQueryType1Target {

    String id;
    String uuid;
    private String componentType;
    private String capacityName;
    private List<DirectoryQueryAtribute> attributeNames;

    public List<DirectoryQueryAtribute> attributeNames() {
        if (attributeNames == null) {
            attributeNames = new LinkedList<>();
        }
        return attributeNames;
    }

    public void copyCapacityConstraint(CapacityConstraint cc) {
        this.componentType = cc.getComponentType();
        this.capacityName = cc.getCapacityName();
	
        for (Attribute at : cc.getAttributes()) {
            addAttribute(at.getName());
        }
    }

    public void copyContextQueryTarget(ContextQueryTarget target) {
        this.id = target.getID();
        this.capacityName = target.getCapacityName();

        for (Attribute at : target.getAttributes()) {
            addAttribute(at.getName());
        }
    }

    public void addAttribute(String attributeName) {
        this.attributeNames().add(new DirectoryQueryAtribute(attributeName));
    }

    public List<DirectoryQueryAtribute> getAttributes() {
        return Collections.unmodifiableList(attributeNames());
    }

    public String getType() {
        return componentType;
    }

    public void setType(String type) {
        this.componentType = type;
    }

    public int numberOfAttributes() {
        return attributeNames().size();
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
        return id;
    }

    public void setID(String id) {
        this.id = id;
    }

    public String getUuid() {
	return uuid;
    }

    public void setUuid(String uuid) {
	this.uuid = uuid;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((attributeNames() == null) ? 0 : attributeNames().hashCode());
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
        final DirectoryQueryType1Target other = (DirectoryQueryType1Target) obj;
        if (id == null) {
            if (other.id != null) {
                return false;
            }
        }
        else if (!id.equals(other.id)) {
            return false;
        }
        if (attributeNames() == null) {
            if (other.attributeNames() != null) {
                return false;
            }
        }
        else if (!attributeNames().equals(other.attributeNames())) {
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
