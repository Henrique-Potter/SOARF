package br.edu.ocdrf.agent.entities;

import br.edu.ocdrf.oal.domain.OInvokeOperation;

public class AgentOperationTarget {

    private String uuid;

    private String resourceComponentNodeID;

    private Long startDate;

    private Long endDate;

    private OInvokeOperation invokeOperation;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public OInvokeOperation getInvokeOperation() {
        return invokeOperation;
    }

    public void setInvokeOperation(OInvokeOperation invokeOpeartion) {
        this.invokeOperation = invokeOpeartion;
    }

    public Long getStartDate() {
        return startDate;
    }

    public void setStartDate(Long startDate) {
        this.startDate = startDate;
    }

    public Long getEndDate() {
        return endDate;
    }

    public void setEndDate(Long endDate) {
        this.endDate = endDate;
    }

    public String getResourceComponentNodeID() {
        return resourceComponentNodeID;
    }

    public void setResourceComponentNodeID(String resourceComponentNodeID) {
        this.resourceComponentNodeID = resourceComponentNodeID;
    }

    public boolean isDateEquals() {
        return startDate.equals(endDate);
    }

    public String getMapKey() {
        return resourceComponentNodeID + invokeOperation.getReturnsCapacity();
    }

}
