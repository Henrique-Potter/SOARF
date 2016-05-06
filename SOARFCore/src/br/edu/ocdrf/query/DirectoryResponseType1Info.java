package br.edu.ocdrf.query;

import java.util.LinkedList;
import java.util.List;

import br.edu.ocdrf.entities.Attribute;
import br.edu.ocdrf.entities.InvokeMethod;

public class DirectoryResponseType1Info {

    // ID do resource
    private String id;
    private String componentId;
    private String componentType;
    private String capacityName;
    private InvokeMethod invokeMethod;
    private List<Attribute> attributes = new LinkedList<>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getComponentId() {
        return componentId;
    }

    public void setComponentId(String componentId) {
        this.componentId = componentId;
    }

    public String getComponentType() {
        return componentType;
    }

    public void setComponentType(String componentType) {
        this.componentType = componentType;
    }

    public String getCapacityName() {
        return capacityName;
    }

    public void setCapacityName(String capacityName) {
        this.capacityName = capacityName;
    }

    public InvokeMethod getInvokeMethod() {
        return invokeMethod;
    }

    public void setInvokeMethod(InvokeMethod invokeMethod) {
        this.invokeMethod = invokeMethod;
    }

    public List<Attribute> getAttributes() {
        return attributes;
    }

    public void addAttribute(Attribute attribute) {
        this.attributes.add(attribute);
    }

    public void clearAttributes() {
        this.attributes.clear();
    }

    public Attribute getAttributeOfIndex(int index) {
        return attributes.get(index);
    }

//	public boolean findAttributeByName(String searchName) {
//		for (Attribute at : attributes) {
//			if (at.getName().equalsIgnoreCase(searchName)) {
//				return true;
//			}
//		}
//		return false;
//	}
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((attributes == null) ? 0 : attributes.hashCode());
        result = prime * result + ((capacityName == null) ? 0 : capacityName.hashCode());
        result = prime * result + ((componentType == null) ? 0 : componentType.hashCode());
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((invokeMethod == null) ? 0 : invokeMethod.hashCode());
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
        final DirectoryResponseType1Info other = (DirectoryResponseType1Info) obj;
        if (attributes == null) {
            if (other.attributes != null) {
                return false;
            }
        }
        else if (!attributes.equals(other.attributes)) {
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
        if (id == null) {
            if (other.id != null) {
                return false;
            }
        }
        else if (!id.equals(other.id)) {
            return false;
        }
        if (invokeMethod == null) {
            if (other.invokeMethod != null) {
                return false;
            }
        }
        else if (!invokeMethod.equals(other.invokeMethod)) {
            return false;
        }
        return true;
    }
}
