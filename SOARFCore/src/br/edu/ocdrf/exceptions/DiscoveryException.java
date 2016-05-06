package br.edu.ocdrf.exceptions;

import javax.xml.ws.WebFault;

@WebFault(name = "DiscoveryServiceException", targetNamespace = "http://discovery.ocdrf.edu.br")
public class DiscoveryException extends ServiceBaseException {

    private SimpleExceptionBean faultBean;
    /**
     *
     */
    private static final long serialVersionUID = 8685481996340787146L;

    /**
     *
     * @param e
     */
    public DiscoveryException(Exception e) {
        super(e);
    }

    public DiscoveryException(String message, SimpleExceptionBean faultInfo) {
        super(message);
        faultBean = faultInfo;
    }

    public DiscoveryException(String message, SimpleExceptionBean faultInfo, Throwable cause) {
        super(message, cause);
        faultBean = faultInfo;
    }

    public SimpleExceptionBean getFaultInfo() {
        return faultBean;
    }

    /**
     *
     */
    public DiscoveryException() {
        super();
    }

    /**
     *
     * @param message
     */
    public DiscoveryException(String message) {
        super(message);
    }

}
