
package br.edu.ocdrf.agent;

public class DataBlock {
    
    private int blockSize;
    private int order;
    private String componentNodeID;
    private String capacityNodeID;
    private String attributeNodeID;

    public int getBlockSize() {
        return blockSize;
    }

    public void setBlockSize(int blockSize) {
        this.blockSize = blockSize;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public String getComponentNodeID() {
        return componentNodeID;
    }

    public void setComponentNodeID(String componentNodeID) {
        this.componentNodeID = componentNodeID;
    }

    public String getCapacityNodeID() {
        return capacityNodeID;
    }

    public void setCapacityNodeID(String capacityNodeID) {
        this.capacityNodeID = capacityNodeID;
    }

    public String getAttributeNodeID() {
        return attributeNodeID;
    }

    public void setAttributeNodeID(String attributeNodeID) {
        this.attributeNodeID = attributeNodeID;
    }
    
    
}
