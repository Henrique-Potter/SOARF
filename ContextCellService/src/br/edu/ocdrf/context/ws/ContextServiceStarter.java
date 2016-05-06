package br.edu.ocdrf.context.ws;

import br.edu.ocdrf.exceptions.ContextException;
import br.edu.ocdrf.exceptions.DirectoryServiceException;

public class ContextServiceStarter {


    public static void main(String[] args) throws  DirectoryServiceException {
        
        try {
            ContextServiceWS service = new ContextServiceWS();
            service.initialize();
        } catch (ContextException contextException) {
            System.err.println(contextException);
        }
        
    }
    
}
