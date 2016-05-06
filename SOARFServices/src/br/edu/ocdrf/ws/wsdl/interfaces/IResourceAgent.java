package br.edu.ocdrf.ws.wsdl.interfaces;

import br.edu.ocdrf.interfaces.IBaseResourceAgent;
import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;

@WebService
@SOAPBinding(style = SOAPBinding.Style.DOCUMENT, use = SOAPBinding.Use.LITERAL, parameterStyle = SOAPBinding.ParameterStyle.WRAPPED)
public interface IResourceAgent extends IBaseResourceAgent {

    @Override
    @WebMethod(action = "invokeAgentOperationAction", operationName = "invokeAgentOperation")
    String invokeAgentOperation(String agentOpeartionTarget) throws Exception;

}
