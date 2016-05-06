package br.edu.ocdrf.oal.domain;

import java.util.ArrayList;
import java.util.List;

public class OCapability  {

    private String capacityType;

    public String name;
    
    public int accessLevel;

    public int accessPolicy;
    
    public boolean accessControlCheck;

    private List<OAttribute> attributes = new ArrayList<>();

    private String nodeId;

    public String getNodeId() {
        return nodeId;
    }

    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }

    public String getCapacityType() {
        return capacityType;
    }

    public void setCapacityType(String capacityType) {
        this.capacityType = capacityType;
    }

    public List<OAttribute> getAttributes() {
        return attributes;
    }

    public void setAttributes(List<OAttribute> attributes) {
        this.attributes = attributes;
    }

    @Override
    public String toString() {
        return nodeId;
    }
}
