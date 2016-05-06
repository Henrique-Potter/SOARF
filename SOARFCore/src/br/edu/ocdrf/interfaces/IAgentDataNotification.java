package br.edu.ocdrf.interfaces;

import br.edu.ocdrf.exceptions.ContextException;


public interface IAgentDataNotification {
    
    boolean checkDataModification();    
    boolean isThereListeners();    
    void notifyContextService()throws ContextException;
    
}
