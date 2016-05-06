package br.edu.ocdrf.exceptions;

import javax.xml.ws.WebFault;

@WebFault(name = "ResourceAgentServiceException", targetNamespace = "http://resourceagent.ocdrf.edu.br")
public class ResourceAgentException extends ServiceBaseException {

    private SimpleExceptionBean faultBean;

    private static final long serialVersionUID = 1767574858722017418L;

    public ResourceAgentException(Exception e) {
        super(e);
    }

    public ResourceAgentException(String message, SimpleExceptionBean faultInfo) {
        super(message);
        faultBean = faultInfo;
    }

    public ResourceAgentException(String message, SimpleExceptionBean faultInfo, Throwable cause) {
        super(message, cause);
        faultBean = faultInfo;
    }

    public SimpleExceptionBean getFaultInfo() {
        return faultBean;
    }

    public ResourceAgentException(String mensagem) {
        super(mensagem);
    }
}
