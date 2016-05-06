package br.edu.ocdrf.ws.wsdl.interfaces;

import br.edu.ocdrf.exceptions.ContextException;
import br.edu.ocdrf.interfaces.IBaseContextService;
import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;


@WebService
@SOAPBinding(style=SOAPBinding.Style.DOCUMENT, use=SOAPBinding.Use.LITERAL, parameterStyle=SOAPBinding.ParameterStyle.WRAPPED)
public interface IContextService extends IBaseContextService{
    
    @Override
    @WebMethod(action = "invokeAgentOperationAction", operationName = "invokeAgentOperation")  
    String invokeResourceOperation(String query) throws ContextException;
    
    @Override
    @WebMethod(action = "notifyChangeStateAction", operationName = "notifyChangeState")  
    void notifyChangeState(String stateDescription) throws ContextException;

    @Override
    @WebMethod(action = "registerObserverAction", operationName = "registerObserver")  
    void registerObserver(String observerDescription) throws ContextException;
    
}
