package br.edu.ocdrf.agent.entities;

import java.util.ArrayList;

public class AgentOperation {

    private ArrayList<AgentOperationTarget> agentOpTargetList = new ArrayList<>();

    public ArrayList<AgentOperationTarget> getAgentOpTargetList() {
        return agentOpTargetList;
    }

    public void setAgentOpTargetList(ArrayList<AgentOperationTarget> agentOpTargetList) {
        this.agentOpTargetList = agentOpTargetList;
    }

    public void addTarget(AgentOperationTarget agOpTgt) {
        agentOpTargetList.add(agOpTgt);
    }

}
