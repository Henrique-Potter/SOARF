package br.edu.ocdrf.agent.entities;

import br.edu.ocdrf.message.AgentResponseData;
import br.edu.ocdrf.message.ResourceOpData;



public interface IInvokeOperation {
    
    public AgentResponseData executeOperation(ResourceOpData opData);
    
}
