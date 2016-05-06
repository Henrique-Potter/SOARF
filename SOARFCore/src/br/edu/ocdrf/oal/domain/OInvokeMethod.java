package br.edu.ocdrf.oal.domain;

public class OInvokeMethod {

    private String invokeTechnology;

    private String invokeString;

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

    public String getInvokeTechnology() {
        return invokeTechnology;
    }

    public void setInvokeTechnology(String invokeTechnology) {
        this.invokeTechnology = invokeTechnology;
    }

    public String getInvokeString() {
        return invokeString;
    }

    public void setInvokeString(String invokeString) {
        this.invokeString = invokeString;
    }

}
