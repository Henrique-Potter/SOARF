package br.edu.ocdrf.query;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import br.edu.ocdrf.entities.Attribute;
import br.edu.ocdrf.entities.InvokeMethod;

public class ContextQueryTarget {

    private String id;
    private String uuid;
    private String capacityName;
    private InvokeMethod invokeMethod;
    private List<Attribute> attributes = new LinkedList<>();

    public List<Attribute> attributes() {
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

    public String getID() {
        return id;
    }

    public void setID(String iD) {
        id = iD;
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
        result = prime * result + ((attributes() == null) ? 0 : attributes().hashCode());
        result = prime * result + ((capacityName == null) ? 0 : capacityName.hashCode());
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
        final ContextQueryTarget other = (ContextQueryTarget) obj;
        if (id == null) {
            if (other.id != null) {
                return false;
            }
        }
        else if (!id.equals(other.id)) {
            return false;
        }
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
