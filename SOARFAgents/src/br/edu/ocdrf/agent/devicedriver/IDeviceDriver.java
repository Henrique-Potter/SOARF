package br.edu.ocdrf.agent.devicedriver;

import br.edu.ocdrf.agent.entities.AgentOperationTarget;


public interface IDeviceDriver {
    
    public void initialize();
    public void executeDeviceOperation(AgentOperationTarget agTarget);
    
}
