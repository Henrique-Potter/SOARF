
package br.edu.ocdrf.exceptions;


public class OALException extends ServiceBaseException {

    
    public OALException(String message) {
        super(message);
    }
    
    public OALException(Exception e) {
        super(e);
    }
    
}
