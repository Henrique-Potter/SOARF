package br.edu.ocdrf.ws.wsdl.util;

import java.net.URL;
import java.util.Random;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;

import br.edu.ocdrf.directory.parsers.DirectoryQueryType1Parser;
import br.edu.ocdrf.directory.parsers.DirectoryResponseType1Parser;
import br.edu.ocdrf.exceptions.DiscoveryException;
import br.edu.ocdrf.ws.wsdl.interfaces.IDiscoveryService;
import java.net.MalformedURLException;
import org.apache.log4j.Logger;

/**
 * @author André Luiz
 *
 */
public class DiscoveryServiceWSUtil extends WebServiceUtil {

    private static final Logger log = Logger.getLogger(DirectoryServiceWSUtil.class.getName());

    private static final QName DISCOVERY_SERVICE_NAME = new QName("http://discovery.ocdrf.edu.br", "DiscoveryService");
    private static final QName DISCOVERY_PORT_NAME = new QName("http://discovery.ocdrf.edu.br", "DiscoveryServiceWSPort");
    private final DirectoryQueryType1Parser directoryQueryType1Parser = new DirectoryQueryType1Parser();
    private final DirectoryResponseType1Parser directoryResponseType1Parser = new DirectoryResponseType1Parser();

    private final String DISCOVERY_CAPABILITY = "DiscoveryService";

    public static QName getDISCOVERY_SERVICE_NAME() {
        return DISCOVERY_SERVICE_NAME;
    }

    public static QName getDISCOVERY_PORT_NAME() {
        return DISCOVERY_PORT_NAME;
    }

    public IDiscoveryService getDiscoveryService(String url) throws DiscoveryException {

        int tentativas = 0;
        Random random = new Random();
        try {

            super.setServiceURL(new URL(url));

            while (true) {
                try {
                    Service service = Service.create(getServiceURL(), DISCOVERY_SERVICE_NAME);
                    setServiceWSDLURL(service.getWSDLDocumentLocation());
                    IDiscoveryService discoveryService = service.getPort(DISCOVERY_PORT_NAME, IDiscoveryService.class);

                    return discoveryService;
                } catch (Exception e) {
                    try {
                        tentativas++;
                        if (tentativas == 10) {
                            log.error("Discovery Service not found", e);
                            System.exit(2);
                        }
                        Thread.sleep(random.nextInt(5) * 1000);
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                        log.error(e1);
                    }
                }
            }
        } catch (Exception e1) {
            e1.printStackTrace();
            throw new DiscoveryException("Service URL invalid!");
        }
    }

    private IDiscoveryService setWebService(String stringUrl) throws MalformedURLException {
        URL url = new URL(stringUrl);
        Service srv = Service.create(url, DISCOVERY_SERVICE_NAME);
        IDiscoveryService service = srv.getPort(IDiscoveryService.class);
        return service;
    }

}
