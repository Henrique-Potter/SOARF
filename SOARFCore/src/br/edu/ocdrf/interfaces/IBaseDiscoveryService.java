package br.edu.ocdrf.interfaces;

import br.edu.ocdrf.exceptions.DiscoveryException;

public interface IBaseDiscoveryService {

    String findResources(String discoveryQuery)throws DiscoveryException;
    
    String findCapabilities(String clientContextData)throws DiscoveryException;
    
}
