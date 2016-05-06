package br.edu.ocdrf.ws.wsdl.interfaces;

import br.edu.ocdrf.exceptions.DiscoveryException;
import br.edu.ocdrf.interfaces.IBaseDiscoveryService;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;

@WebService
@SOAPBinding(style = SOAPBinding.Style.DOCUMENT, use = SOAPBinding.Use.LITERAL, parameterStyle = SOAPBinding.ParameterStyle.WRAPPED)
public interface IDiscoveryService extends IBaseDiscoveryService {

    @Override
    String findResources(String discoveryQuery) throws DiscoveryException;

    @Override
    String findCapabilities(String clientContextData) throws DiscoveryException;

}
