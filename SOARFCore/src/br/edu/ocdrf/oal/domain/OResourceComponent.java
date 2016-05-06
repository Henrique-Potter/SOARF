package br.edu.ocdrf.oal.domain;

import java.util.ArrayList;
import java.util.List;

public class OResourceComponent  {

    private String type;

    private String name;

    private List<String> objPropertiesList = new ArrayList<>();

    private List<OInvokeOperation> invokeOperations = new ArrayList<>();

    private List<OCapability> capacities = new ArrayList<>();

    private List<String> objectPropRangeInstancesList = new ArrayList<>();

    private String nodeId;

    public String getNodeId() {
        return nodeId;
    }

    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }

    @Override
    public String toString() {
        return nodeId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<String> getObjPropertiesList() {
        return objPropertiesList;
    }

    public void setObjPropertiesList(List<String> objPropertiesList) {
        this.objPropertiesList = objPropertiesList;
    }

    public List<String> getObjectPropRangeInstancesList() {
        return objectPropRangeInstancesList;
    }

    public void setObjectPropRangeInstancesList(List<String> objectPropRangeInstancesList) {
        this.objectPropRangeInstancesList = objectPropRangeInstancesList;
    }

    public List<OInvokeOperation> getInvokeOperations() {
        return invokeOperations;
    }

    public void setInvokeOperations(List<OInvokeOperation> invokeOperations) {
        this.invokeOperations = invokeOperations;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<OCapability> getCapabilities() {
        return capacities;
    }

    public void setCapabilities(List<OCapability> capacities) {
        this.capacities = capacities;
    }

}
