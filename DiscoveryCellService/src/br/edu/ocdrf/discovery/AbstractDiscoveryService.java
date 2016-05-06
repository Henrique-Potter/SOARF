package br.edu.ocdrf.discovery;

import br.edu.ocdrf.ws.wsdl.interfaces.IDiscoveryService;
import br.edu.ocdrf.exceptions.DiscoveryException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.apache.log4j.Logger;

import br.edu.ocdrf.entities.Component;
import br.edu.ocdrf.entities.SEBaseService;
import br.edu.ocdrf.entities.ServicesTypeID;
import br.edu.ocdrf.exceptions.ContextException;
import br.edu.ocdrf.exceptions.DirectoryServiceException;
import br.edu.ocdrf.exceptions.ServiceBaseException;
import br.edu.ocdrf.util.xml.XMLUtility;
import br.edu.ocdrf.ws.wsdl.interfaces.IContextService;
import br.edu.ocdrf.ws.wsdl.interfaces.IDirectoryService;
import br.edu.ocdrf.ws.wsdl.util.ContextServiceWSUtil;
import br.edu.ocdrf.ws.wsdl.util.DirectoryServiceWSUtil;

import java.io.FileNotFoundException;
import java.net.InetAddress;

public abstract class AbstractDiscoveryService extends SEBaseService implements IDiscoveryService {

    private final String CONFIG_FILE_NAME = "discovery_serv_config.xml";
    protected final String ID_FILE_NAME = "discovery_serv_id.data";

    protected String discoveyServiceURL;

    protected String ontologyStr;

    private static final Logger log = Logger.getLogger(AbstractDiscoveryService.class.getName());

    private final DirectoryServiceWSUtil directoryServiceUtil = new DirectoryServiceWSUtil();

    protected IDirectoryService directoryService;
    protected IContextService contextService;

    private final List<IDirectoryService> directoryServices = new ArrayList<>();

    //private final Map<String, String> discoveryOntologies = new HashMap<>();
    @Override
    public void initialize() throws DiscoveryException {
        try {
            initServiceBaseProps();
            File configFile = new File(getMainServicesConfigPath(), CONFIG_FILE_NAME);

            if (configFile.exists()) {

                log.info("Loading config file...");
                readConfig(configFile);
                log.info("Config file successfully loaded!");

                manageRegistration();

                //contextService = findContextService(servID.contextURL);
                //log.info("Loading ontology file...");
                //ontologyStr = resOntDesc.serializeOntology(getMainServicesConfigPath());
                //loadOntologyModels(getMainServicesConfigPath(), resOntDesc.getOntologyFileName());
                //log.info("Ontology file successfully loaded!\n");
            } else {
                log.error("Discovery Service configuration file not found: " + configFile.toString());
                System.exit(4);
            }

        } catch (ServiceBaseException serviceBaseException) {
            throw new DiscoveryException(serviceBaseException);
        }
    }

    @Override
    protected void readConfig(File file) throws ServiceBaseException {

        XMLStreamReader reader = null;
        XMLInputFactory factory = XMLInputFactory.newInstance();
        factory.setProperty(XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES, Boolean.FALSE);
        InputStream inputStream = null;

        Component component = new Component();
        resOntDesc.addComponent(component);

        try {
            inputStream = new FileInputStream(file);
            reader = factory.createXMLStreamReader(inputStream);
            while (true) {
                int event = reader.next();
                if (event == XMLStreamConstants.END_DOCUMENT) {
                    reader.close();
                    break;
                }

                if (event == XMLStreamConstants.START_ELEMENT) {
                    switch (reader.getLocalName()) {
                        case "DiscoveryService":
                            break;
                        case "Ontology":
                            resOntDesc.setOntologyFileName(XMLUtility.getAttributeValue(reader, "File"));
                            break;
                        case "Login":
                            servID.loginID = XMLUtility.getAttributeValue(reader, "UserID");
                            servID.password = XMLUtility.getAttributeValue(reader, "Password");
                            break;
                        case "DirectoryService":
                            directoryService = findDirectoryService(XMLUtility.getAttributeValue(reader, "url"));
                            break;
//                        case "ContextService":
//                            //contextService = findContextService(directoryService, XMLUtility.getAttributeValue(reader, "ID"));
//                            break;
                        case "Tecnology":
                            resOntDesc.setInvokeTecnology(XMLUtility.getAttributeValue(reader, "type"), XMLUtility.getAttributeValue(reader, "url"));
                            discoveyServiceURL = XMLUtility.getAttributeValue(reader, "url");
                            break;
                    }
                }
            }

        } catch (XMLStreamException | IOException e) {
            throw new ServiceBaseException(e);
        } finally {
            closeStreams(reader, inputStream);
        }
    }

    private void manageRegistration() throws DirectoryServiceException {
        String fullFilePath = null;
        try {
            fullFilePath = getMainServicesConfigPath() +"ontoData/" + ID_FILE_NAME;
            loadCredentialsFile(fullFilePath);
        } catch (FileNotFoundException fileEX) {
            log.info("Cached Credentials not found...");
            try {

                localServiceIP = InetAddress.getLocalHost().getHostAddress();
                String registrationRequest = createServiceRegistrationReq(ServicesTypeID.DISCOVERY_SERVICE_TYPE, discoveyServiceURL, servID.loginID, servID.password, localServiceIP);

                log.info("Preparing to request new Credential.");

                String encryptedJsonServID = directoryService.registerService(registrationRequest);

                setNewServiceCrendentials(encryptedJsonServID, servID.password);

                log.info("New credentials received, caching new crendetials...");

                cacheServiceID(fullFilePath);

            } catch (Exception ex) {
                throw new DirectoryServiceException(ex);
            }

        } catch (IOException | ClassNotFoundException e) {
            throw new DirectoryServiceException(e);
        }
    }

    private IDirectoryService findDirectoryService(String url)
            throws DiscoveryException {
        try {
            return directoryServiceUtil.findDirectoryService(url);
        } catch (DirectoryServiceException e) {
            log.info(e);
            throw new DiscoveryException(e);
        }
    }

    private IContextService findContextService(String contextURL) throws DiscoveryException {
        try {
            return new ContextServiceWSUtil().getContextService(contextURL);
        } catch (ContextException e) {
            log.info(e);
            throw new DiscoveryException(e.getMessage());
        }
    }

}
