package br.edu.ocdrf.discovery.ws;

import br.edu.ocdrf.exceptions.DiscoveryException;

public class DiscoveryServiceStarter {


    public static void main(String[] args) throws DiscoveryException {
        try {
            DiscoveryServiceWS service = new DiscoveryServiceWS();
            service.initialize();
        }
        catch (DiscoveryException discoveryException) {
            System.err.println(discoveryException.getMessage());
        }
    }
    
}
