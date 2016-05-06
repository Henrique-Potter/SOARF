package br.edu.ocdrf.exceptions;

import javax.xml.ws.WebFault;

@WebFault(name = "DirectoryServiceException", targetNamespace = "http://directory.ocdrf.edu.br")
public class DirectoryServiceException extends ServiceBaseException {

    private SimpleExceptionBean faultBean;

    private static final long serialVersionUID = 3380778045281401618L;

    public DirectoryServiceException() {
        super();
    }

    public DirectoryServiceException(String message, SimpleExceptionBean faultInfo) {
        super(message);
        faultBean = faultInfo;
    }

    public DirectoryServiceException(String message, SimpleExceptionBean faultInfo, Throwable cause) {
        super(message, cause);
        faultBean = faultInfo;
    }

    public DirectoryServiceException(Exception e) {
        super(e);

    }

    public SimpleExceptionBean getFaultInfo() {
        return faultBean;
    }
}
