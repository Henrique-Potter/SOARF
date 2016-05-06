package br.edu.ocdrf.oal.domain;

import java.util.ArrayList;
import java.util.List;

public class OInvokeOperation {

    private String operationName;

    private String invokeReturnType;

    private OCapability returnsCapability;

    private List<OInvokeOpParameter> invokeParameters = new ArrayList<>();

    private String nodeId;

    public OInvokeOperation() {

    }

    public OInvokeOperation(String operationName) {
        this.operationName = operationName;
    }

    public String getNodeId() {
        return nodeId;
    }

    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }

    public OCapability getReturnsCapacity() {
        return returnsCapability;
    }

    public void setReturnsCapacity(OCapability returnsCapacity) {
        this.returnsCapability = returnsCapacity;
    }

    @Override
    public String toString() {
        return nodeId;
    }

    public String getOperationName() {
        return operationName;
    }

    public void setOperationName(String operationName) {
        this.operationName = operationName;
    }

    public String getInvokeReturnType() {
        return invokeReturnType;
    }

    public void setInvokeReturnType(String invokeReturnType) {
        this.invokeReturnType = invokeReturnType;
    }

    public List<OInvokeOpParameter> getParameters() {
        return invokeParameters;
    }

    public void setParameters(List<OInvokeOpParameter> invokeOpParams) {
        this.invokeParameters = invokeOpParams;
    }

}
