package br.edu.ocdrf.query;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import br.edu.ocdrf.entities.Attribute;
import br.edu.ocdrf.entities.InvokeMethod;

public class WorkResponseInfo {

    private String id;
    private String componentType;
    private String capacityNameInQuery;
    private String capacityNameInRDS;
    private InvokeMethod invokeMethod;
    private final List<WorkAttribute> attributes = new LinkedList<>();

    public WorkResponseInfo() {
    }

    public WorkResponseInfo(String id) {
        this.id = id;
    }

    public WorkResponseInfo(String id, String componentType, String capacityNameInQuery, String capacityNameInRDS, InvokeMethod invokeMethod) {
        this.id = id;
        this.componentType = componentType;
        this.capacityNameInQuery = capacityNameInQuery;
        this.capacityNameInRDS = capacityNameInRDS;
        this.invokeMethod = invokeMethod;
    }

    public void copyCapacityConstraint(CapacityConstraint cc) {
        this.componentType = cc.getComponentType();
        this.capacityNameInQuery = cc.getCapacityName();

        for (Attribute at : cc.getAttributes()) {
            this.attributes.add(new WorkAttribute(at));
        }
    }

    public void copyContextQueryTarget(ContextQueryTarget target) {
        this.id = target.getID();
        this.capacityNameInQuery = target.getCapacityName();
        this.invokeMethod = target.getInvokeMethod();

        for (Attribute at : target.getAttributes()) {
            this.attributes.add(new WorkAttribute(at));
        }
    }

    public void copyQueryValuesToRDSValues() {
        this.capacityNameInRDS = this.capacityNameInQuery;

        for (WorkAttribute at : attributes) {
            at.setNameInRDS(at.getNameInQuery());
            at.setTypeInRDS(WorkAttribute.DYNAMIC);
            at.setUnitInRDS(at.getUnitInQuery());
        }
    }

    public void addAttribute(WorkAttribute attribute) {
        this.attributes.add(attribute);
    }
    
    

    public void setAttributes(List <Attribute> attributesList) {
        for (Attribute at : attributesList) {
            this.attributes.add(new WorkAttribute(at));
        }
    }
    
    public List<WorkAttribute> getAttributes() {
        return Collections.unmodifiableList(attributes);
    }

    public WorkAttribute getAttributeByNameInRDS(String name) {
        for (WorkAttribute attribute : attributes) {
            if (name.equals(attribute.getNameInRDS())) {
                return attribute;
            }
        }
        return null;
    }

    public WorkAttribute getAttributeByNameInQuery(String name) {
        for (WorkAttribute attribute : attributes) {
            if (name.equals(attribute.getNameInQuery())) {
                return attribute;
            }
        }
        return null;
    }

    public boolean hasDynamicAttribute() {
        for (WorkAttribute att : attributes) {
            if (att.isDynamic()) {
                return true;
            }
        }
        return false;
    }

    public WorkAttribute getAttributeOfIndex(int index) {

        return attributes.get(index);
    }

    public int numberOfAttributes() {
        return attributes.size();
    }

    public String getCapacityName() {
        return capacityNameInQuery;
    }

    public void setCapacityName(String name) {
        this.capacityNameInQuery = name;
    }

    public String getComponentType() {
        return componentType;
    }

    public void setComponentType(String componentType) {
        this.componentType = componentType;
    }

    public String getCapacityNameInRDS() {
        return capacityNameInRDS;
    }

    public void setCapacityNameInRDS(String capacityNameInRDS) {
        this.capacityNameInRDS = capacityNameInRDS;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public InvokeMethod getInvokeMethod() {
        return invokeMethod;
    }

    public void setInvokeMethod(InvokeMethod invokeMethod) {
        this.invokeMethod = invokeMethod;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((attributes == null) ? 0 : attributes.hashCode());
        result = prime * result + ((capacityNameInQuery == null) ? 0 : capacityNameInQuery.hashCode());
        result = prime * result + ((capacityNameInRDS == null) ? 0 : capacityNameInRDS.hashCode());
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
        WorkResponseInfo other = (WorkResponseInfo) obj;
        if (attributes == null) {
            if (other.attributes != null) {
                return false;
            }
        }
        else if (!attributes.equals(other.attributes)) {
            return false;
        }
        if (capacityNameInQuery == null) {
            if (other.capacityNameInQuery != null) {
                return false;
            }
        }
        else if (!capacityNameInQuery.equals(other.capacityNameInQuery)) {
            return false;
        }
        if (capacityNameInRDS == null) {
            if (other.capacityNameInRDS != null) {
                return false;
            }
        }
        else if (!capacityNameInRDS.equals(other.capacityNameInRDS)) {
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
    
    @Override
    public String toString(){
        return this.getId();
    }

}
