package br.edu.ocdrf.ws.wsdl.interfaces;

import br.edu.ocdrf.exceptions.DirectoryServiceException;
import br.edu.ocdrf.interfaces.IBaseDirectoryService;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.ParameterStyle;
import javax.jws.soap.SOAPBinding.Style;
import javax.jws.soap.SOAPBinding.Use;

@WebService
@SOAPBinding(style = Style.DOCUMENT, use = Use.LITERAL, parameterStyle = ParameterStyle.WRAPPED)
public interface IDirectoryService extends IBaseDirectoryService {

    @Override
    String registerService(String xml) throws DirectoryServiceException;

    @Override
    String registerResource(String xml) throws DirectoryServiceException;

    @Override
    String findResources(String ontoQuery) throws DirectoryServiceException;

    @Override
    String findCapabilities(String clientContext) throws DirectoryServiceException;

    @Override
    String getFullResourceInfo(String jsonResourceOp) throws DirectoryServiceException;

}
