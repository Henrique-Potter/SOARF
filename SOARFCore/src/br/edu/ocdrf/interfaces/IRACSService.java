package br.edu.ocdrf.interfaces;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;


@WebService
@SOAPBinding(style = SOAPBinding.Style.DOCUMENT, use = SOAPBinding.Use.LITERAL, parameterStyle = SOAPBinding.ParameterStyle.WRAPPED)
public interface IRACSService {

    @WebMethod(action = "registerSystemUserAction", operationName = "registerSystemUser")
    String registerSystemUser(String userRegRequest) throws Exception;
    
    @WebMethod(action = "loginRequestAction", operationName = "loginRequest")
    String loginRequest(String loginRequestData) throws Exception;
    
    @WebMethod(action = "clientServicesAccessRequestAction", operationName = "clientServicesAccessRequest")
    String clientServicesAccessRequest(String clientServicesAccessRequest) throws Exception;
    
}
