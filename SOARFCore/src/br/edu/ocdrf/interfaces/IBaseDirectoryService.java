package br.edu.ocdrf.interfaces;

import br.edu.ocdrf.exceptions.DirectoryServiceException;

public interface IBaseDirectoryService {

    String registerService(String xml) throws DirectoryServiceException;

    String registerResource(String xml) throws DirectoryServiceException;

    String findResources(String ontoQuery) throws DirectoryServiceException;

    String findCapabilities(String clientContext) throws DirectoryServiceException;

    String getFullResourceInfo(String jsonResourceOp) throws DirectoryServiceException;

}
