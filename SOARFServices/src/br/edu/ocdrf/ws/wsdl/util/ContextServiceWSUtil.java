package br.edu.ocdrf.ws.wsdl.util;

import br.edu.ocdrf.exceptions.ContextException;
import br.edu.ocdrf.ws.wsdl.interfaces.IContextService;
import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import org.apache.log4j.Logger;

public class ContextServiceWSUtil extends WebServiceUtil {

    private static final Logger log = Logger.getLogger(DirectoryServiceWSUtil.class.getName());

    private static final QName CONTEXT_SERVICE_NAME = new QName("http://context.ocdrf.edu.br", "ContextService");
    private static final QName CONTEXT_PORT_NAME = new QName("http://context.ocdrf.edu.br", "ContextServiceWSPort");

    public IContextService getContextService(String url) throws ContextException{
        IContextService contextService = null;
        try {

            URL directoryUrl = new URL(url);
            Service service = Service.create(directoryUrl, CONTEXT_SERVICE_NAME);

            contextService = service.getPort(IContextService.class);

        } catch (Exception e) {
            throw new ContextException(e);
        }
        return contextService;
    }

    public static QName getCONTEXT_SERVICE_NAME() {
        return CONTEXT_SERVICE_NAME;
    }

    public static QName getCONTEXT_PORT_NAME() {
        return CONTEXT_PORT_NAME;
    }

    public String invokeAgentOperation(String pergunta, String stringUrl) throws Exception {

        IContextService service = setWebService(stringUrl);

        log.info("\n\n++++++++++++++++++++Invoking Agent Operation: \n" + pergunta);
        String s = service.invokeResourceOperation(pergunta);
        log.info("\n\n++++++++++++++++++++Agent Response: \n" + s);

        return s;

    }

    public void subscribeObserverSC(String observerDescription, String stringUrl) throws Exception {
        IContextService service = setWebService(stringUrl);

        log.info("\n\n++++++++++++++++++++Registering Observer: " + observerDescription);
        service.registerObserver(observerDescription);
        log.info("\n\n++++++++++++++++++++Observer successfully registered");

    }

    private IContextService setWebService(String stringUrl) throws MalformedURLException {
        URL url = new URL(stringUrl);
        Service srv = Service.create(url, CONTEXT_SERVICE_NAME);
        IContextService service = srv.getPort(IContextService.class);
        return service;
    }

}
