package br.edu.ocdrf.exceptions;

import javax.xml.ws.WebFault;

@WebFault(name = "ContextServiceException", targetNamespace = "http://context.ocdrf.edu.br")
public class ContextException extends ServiceBaseException {

    private SimpleExceptionBean faultBean;
    /**
     *
     */
    private static final long serialVersionUID = -1801564414757412841L;

    /**
     *
     * @param message
     */
    public ContextException(String message) {
        super(message);
    }

    public ContextException(String message, SimpleExceptionBean faultInfo) {
        super(message);
        faultBean = faultInfo;
    }

    public ContextException(String message, SimpleExceptionBean faultInfo, Throwable cause) {
        super(message, cause);
        faultBean = faultInfo;
    }

    public SimpleExceptionBean getFaultInfo() {
        return faultBean;
    }

    /**
     *
     * @param e
     */
    public ContextException(Exception e) {
        super(e);
    }

}
