/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ocdrf.directory.ws;

import br.edu.ocdrf.exceptions.DirectoryServiceException;
import br.edu.ocdrf.exceptions.ServiceBaseException;

/**
 *
 * @author henrique
 */
public class DirectoryServiceMain {

    /**
     * @param args the command line arguments
     * @throws br.edu.ocdrf.exceptions.DirectoryServiceException
     */
    public static void main(String[] args) throws DirectoryServiceException, ServiceBaseException {
        DirectoryServiceWS service = new DirectoryServiceWS();
        service.initialize();
    }
}
