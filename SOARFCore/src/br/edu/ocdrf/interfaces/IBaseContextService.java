package br.edu.ocdrf.interfaces;

import br.edu.ocdrf.exceptions.ContextException;



public interface IBaseContextService {


    String invokeResourceOperation(String jsonResOperation) throws ContextException;
    
    void notifyChangeState(String stateDescription) throws ContextException;


    void registerObserver(String observerDescription) throws ContextException;
}
