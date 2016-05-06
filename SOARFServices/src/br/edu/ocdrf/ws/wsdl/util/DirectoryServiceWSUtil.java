package br.edu.ocdrf.ws.wsdl.util;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Random;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;

import br.edu.ocdrf.ws.wsdl.interfaces.IDirectoryService;
import br.edu.ocdrf.exceptions.DirectoryServiceException;
import org.apache.log4j.Logger;

public class DirectoryServiceWSUtil extends WebServiceUtil {

    private static final Logger log = Logger.getLogger(DirectoryServiceWSUtil.class.getName());

    private static final QName DIRECTORY_SERVICE_NAME = new QName("http://directory.ocdrf.edu.br", "DirectoryService");
    private static final QName DIRECTORY_Port_NAME = new QName("http://directory.ocdrf.edu.br", "DirectoryServiceWSPort");

    public static QName getDIRECTORY_SERVICE_NAME() {
        return DIRECTORY_SERVICE_NAME;
    }

    public static QName getDIRECTORY_Port_NAME() {
        return DIRECTORY_Port_NAME;
    }

    /**
     * @param url
     * @return
     * @throws DirectoryServiceException
     */
    public IDirectoryService findDirectoryService(String url) throws DirectoryServiceException {
        int tentativas = 0;
        Random random = new Random();
        try {
            URL directoryUrl = new URL(url);
            setServiceURL(directoryUrl);
            while (true) {
                try {
                    Service service = Service.create(directoryUrl, DIRECTORY_SERVICE_NAME);
                    setServiceWSDLURL(service.getWSDLDocumentLocation());
                    IDirectoryService directoryService = service.getPort(IDirectoryService.class);
                    log.info("Directory Service found with the url: " + url);
                    return directoryService;
                } catch (Exception e) {
                    tentativas++;
                    if (tentativas == 10) {
                        log.error("Directory Service at " + url + " not found");
                        System.exit(1);
                    }
                    try {
                        Thread.sleep(random.nextInt(5) * 1000);
                    } catch (InterruptedException el) {
                        log.error(el);
                        throw new DirectoryServiceException(el);
                    }
                }
            }
        } catch (MalformedURLException e1) {
            e1.printStackTrace();
            throw new DirectoryServiceException(e1);
        }
    }


    private IDirectoryService setWebService(String stringUrl) throws MalformedURLException {
        URL url = new URL(stringUrl);

        Service srv = Service.create(url, DIRECTORY_SERVICE_NAME);
        IDirectoryService service = srv.getPort(IDirectoryService.class);

        return service;
    }

    public String registerService(String stringUrl, String jsonServiceRegistrationInfo) throws Exception {

        IDirectoryService service = setWebService(stringUrl);

        return service.registerService(jsonServiceRegistrationInfo);
    }

}
