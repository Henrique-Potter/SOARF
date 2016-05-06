package br.edu.ocdrf.context;

import br.edu.ocdrf.entities.ResourceObserver;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.ws.Service;
import javax.xml.ws.WebServiceException;

import br.edu.ocdrf.context.parsers.ContextObserverParser;
import br.edu.ocdrf.context.parsers.ResourceStateParser;
import br.edu.ocdrf.context.ws.ContextServiceWS;
import br.edu.ocdrf.ws.wsdl.interfaces.IDirectoryService;
import br.edu.ocdrf.interfaces.IContextObserver;
import br.edu.ocdrf.entities.ResourceState;
import br.edu.ocdrf.entities.SEBaseService;
import br.edu.ocdrf.exceptions.ContextException;
import br.edu.ocdrf.exceptions.ServiceBaseException;
import br.edu.ocdrf.ws.wsdl.interfaces.IContextService;
import br.edu.ocdrf.ws.wsdl.interfaces.IResourceAgent;
import br.edu.ocdrf.exceptions.ValidationException;
import br.edu.ocdrf.entities.ServicesTypeID;
import br.edu.ocdrf.exceptions.DirectoryServiceException;
import br.edu.ocdrf.util.xml.XMLEncoder;
import br.edu.ocdrf.util.xml.XMLUtility;
import br.edu.ocdrf.ws.wsdl.util.DirectoryServiceWSUtil;
import java.io.FileNotFoundException;
import java.net.InetAddress;
import org.apache.log4j.Logger;

/**
 * Implementação do serviço de contexto.
 *
 * @author Hpotter
 * @since 04 / 11 / 2013
 * @version 2.0
 * @category Monitoramento
 *
 * Implementação do serviço de contexto.
 *
 * @author André Luiz Barbosa Rodrigues <albr74@gmail.com>
 * @since 04 / 11 / 2007
 * @version 1.1
 * @category Monitoramento
 *
 */
public abstract class AbstractContextService extends SEBaseService implements IContextService {

    private static final Logger log = Logger.getLogger(ContextServiceWS.class.getName());

    protected final String CONFIG_FILE_NAME = "context_serv_config.xml";
    protected final String ID_FILE_NAME = "context_serv_id.data";

    private static final ResourceStateParser resourceStateParser = new ResourceStateParser();
    private static final ContextObserverParser observerParser = new ContextObserverParser();

    protected IDirectoryService directoryServices;
    private final List<ResourceObserver> observers = new LinkedList<>();
    protected String ontologyStr;
    private String contextServiceURL = "";
    private String directoryServiceURL = "";

    private final DirectoryServiceWSUtil directoryServiceUtil = new DirectoryServiceWSUtil();

    public abstract void stop() throws ContextException;

    protected Map<String, IResourceAgent> resourceAgents = new HashMap<>();

    @Override
    public void initialize() throws ContextException {
        try {

            initServiceBaseProps();
            File configFile = new File(getMainServicesConfigPath(), CONFIG_FILE_NAME);
            if (configFile.exists()) {
                log.info("Loading config file...");
                readConfig(configFile);
                log.info("Config file successfully loaded!\n");

                manageRegistration();

                //log.info("Loading ontology file...");
                //ontologyStr = resOntDesc.serializeOntology(getMainServicesConfigPath());
                //log.info("Ontology file successfully loaded!\n");

            } else {
                log.error("Context Service configuration file not found: " + configFile.toString());
                System.exit(4);
            }

        } catch (ServiceBaseException serviceBaseException) {
            throw new ContextException(serviceBaseException);
        }
    }

    @Override
    protected void readConfig(File file) throws ServiceBaseException {

        XMLStreamReader reader = null;
        XMLInputFactory factory = XMLInputFactory.newInstance();
        factory.setProperty(XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES, Boolean.FALSE);
        InputStream inputStream = null;

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
                        case "ContextService":
                            break;
                        case "Login":
                            servID.loginID = XMLUtility.getAttributeValue(reader, "UserID");
                            servID.password = XMLUtility.getAttributeValue(reader, "Password");
                            break;
                        case "Ontology":
                            resOntDesc.setOntologyFileName(XMLUtility.getAttributeValue(reader, "File"));
                            break;
                        case "Tecnology":
                            resOntDesc.setInvokeTecnology(XMLUtility.getAttributeValue(reader, "type"), XMLUtility.getAttributeValue(reader, "url"));
                            contextServiceURL = XMLUtility.getAttributeValue(reader, "url");
                            break;
                        case "DirectoryService":
                            directoryServiceURL = XMLUtility.getAttributeValue(reader, "url");
                            IDirectoryService service = findDirectoryService(directoryServiceURL);
                            if (service != null) {
                                directoryServices = service;
                            }
                            break;
                    }
                }
            }
            if (directoryServices == null) {
                throw new ContextException("Directory service not found!");
            }
        } catch (XMLStreamException | IOException e) {
            throw new ContextException(e);
        } finally {
            closeStreams(reader, inputStream);
        }
    }

    private void manageRegistration() throws DirectoryServiceException {
        String fullFilePath = null;
        try {

            fullFilePath = getMainServicesConfigPath() + ID_FILE_NAME;
            loadCredentialsFile(fullFilePath);

        } catch (FileNotFoundException fileEX) {

            log.info("Cached Credentials not found...");
            try {

                localServiceIP = InetAddress.getLocalHost().getHostAddress();

                String registrationRequest = createServiceRegistrationReq(ServicesTypeID.CONTEXT_SERVICE_TYPE, contextServiceURL, servID.loginID, servID.password, localServiceIP);

                log.info("Preparing to request new Credential.");

                String encryptedJsonServID = directoryServices.registerService(registrationRequest);

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

    protected IDirectoryService findDirectoryService(String url) throws ContextException {
        try {
            return directoryServiceUtil.findDirectoryService(url);
        } catch (DirectoryServiceException e) {
            throw new ContextException(e);
        }
    }

    @Override
    public void notifyChangeState(String stateDescription) throws ContextException {
        try {
            ResourceState state = resourceStateParser.parseDocument(stateDescription);
            notifyObservers(state);
        } catch (ValidationException e) {
            throw new ContextException(e);
        }
    }

//    protected void registerContextService(String ontologyStr) throws ContextException {
//        try {
//            if (directoryServices != null) {
//                directoryServices.registerResource(ontologyStr);
//            }
//        } catch (Exception e) {
//            throw new ContextException(e);
//        }
//    }
//    protected String queryRDS(AgentOperation aOperation) throws Exception {
//
//        AgentResponse agentResponses = new AgentResponse();
//        try {
//            agentResponses = new AgentResponse();
//            QueryResponseEntity detaliedResources = getResourceInfo(aOperation);
//
//            for (AgentOperationTarget aTarget : aOperation.getAgentOpTargetList()) {
//
//                for (OResourceEntity res : detaliedResources.getFoundResourcesList()) {
//
//                    if (aTarget.getUuid() == null ? res.getUUID() == null : aTarget.getUuid().equals(res.getUUID())) {
//                        IResourceAgent resAgent = findResourceAgent(res.getInvokeMethod().get(0).getInvokeString());
//                        aTarget.defineXStreamParser();
//
//                        String resourceResponse = resAgent.invokeAgentOperation(aTarget.parseThisToXML());
//
//                        AgentTargetResponse agTgResponse = new AgentTargetResponse().createObjectFromXML(resourceResponse);
//                        agentResponses.addAgentTargetResponse(agTgResponse);
//                    }
//                }
//            }
//        } catch (Exception ex) {
//            throw new ContextException(ex);
//        }
//
//        return "";
//    }
//    private QueryResponseEntity getResourceInfo(AgentOperation aOperation) throws ContextException {
//
//        OntologyQuery ontoQuery = new OntologyQuery();
//
//        for (AgentOperationTarget aTarget : aOperation.getAgentOpTargetList()) {
//            ontoQuery.searchByUUID(aTarget.getUuid());
//        }
//        String response = directoryServices.findResources(ontoQuery.parseThisToXML());
//        return null;
//    }
    protected IResourceAgent findResourceAgent(String resourceURL) throws ContextException {
        try {

            URL url = new URL(resourceURL);

            String serviceName = resourceURL.substring(resourceURL.lastIndexOf('/') + 1);

            serviceName = serviceName.substring(0, serviceName.indexOf('?'));

            QName qName = new QName("http://agents.ocdrf.edu.br", serviceName);

            Service service = Service.create(url, qName);

            IResourceAgent agent = service.getPort(IResourceAgent.class);
            return agent;
        } catch (MalformedURLException e) {
            throw new ContextException(e);
        }
    }

    @Override
    public void registerObserver(String observerDescription) throws ContextException {
        log.info("AbstractContextService.registerObserver: " + observerDescription);
        try {

            ResourceObserver observer = observerParser.parseDocument(observerDescription);
            // Analisar Observer
            includeObserver(observer);

        } catch (ValidationException e) {
            throw new ContextException(e);
        }
        log.info("Cliente registrado.");
    }

    private void includeObserver(ResourceObserver observer) throws ContextException {
        log.info("AbstractContextService.incudeObserver: " + observer);

        if (!observers.contains(observer)) {
            observers.add(observer);
        }
    }

    private void notifyObservers(ResourceState state) throws ContextException {

        try {
            for (ListIterator<ResourceObserver> iterator = observers.listIterator(); iterator.hasNext();) {
                ResourceObserver observer = iterator.next();
                IContextObserver contextObserver = null;
                try {
                    if (state.getType().equals(observer.getResourceType())) {
                        String uri = observer.getURI();
                        URL url = new URL(uri);
                        contextObserver = observer.getContextObserver();
                        if (contextObserver == null) {
                            String serviceName = observer.getServiceName();
                            Service service = Service.create(url, new QName(serviceName.substring(0, serviceName.lastIndexOf('/')), serviceName
                                    .substring(serviceName.lastIndexOf('/') + 1)));
                            contextObserver = service.getPort(IContextObserver.class);
                            observer.setContextObserver(contextObserver);
                        }
                        contextObserver.resourceState(XMLEncoder.encode(state));
                    }
                } catch (MalformedURLException | WebServiceException e) {
                    //LOGGER.error("Não foi possível contactar o observador com URI " + observer.getURI() + ". O mesmo está sendo removido.");
                    observers.remove(observer);
                }
            }
        } catch (Exception e) {
            throw new ContextException(e);
        }
    }

}
