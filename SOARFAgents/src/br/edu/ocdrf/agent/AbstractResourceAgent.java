package br.edu.ocdrf.agent;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.edu.ocdrf.agent.devicedriver.IDeviceDriver;
import br.edu.ocdrf.agent.entities.ResourceAgentConfig;
import br.edu.ocdrf.exceptions.ContextException;
import br.edu.ocdrf.exceptions.DirectoryServiceException;
import br.edu.ocdrf.exceptions.ResourceAgentException;
import br.edu.ocdrf.exceptions.ServiceBaseException;
import br.edu.ocdrf.entities.SEBaseService;
import br.edu.ocdrf.entities.CapabilityAttribute;
import br.edu.ocdrf.agent.entities.IInvokeOperation;
import br.edu.ocdrf.exceptions.OALException;
import br.edu.ocdrf.oal.OAOResource;
import br.edu.ocdrf.oal.domain.OAttribute;
import br.edu.ocdrf.oal.domain.OCapability;
import br.edu.ocdrf.oal.domain.OInvokeOperation;
import br.edu.ocdrf.oal.domain.OResourceComponent;
import br.edu.ocdrf.message.AgentResponseData;
import br.edu.ocdrf.message.CapabilityData;
import br.edu.ocdrf.agent.messages.ResourceOntologyRegistration;
import br.edu.ocdrf.auth.message.AuthenticatorTicket;
import br.edu.ocdrf.auth.message.LoginRequestMessage;
import br.edu.ocdrf.auth.message.ServiceAccessResponse;
import br.edu.ocdrf.auth.message.SessionData;
import br.edu.ocdrf.entities.ServiceID;
import br.edu.ocdrf.interfaces.IAgentDataNotification;
import br.edu.ocdrf.interfaces.IBaseContextService;
import br.edu.ocdrf.interfaces.IBaseDirectoryService;
import br.edu.ocdrf.interfaces.IRACSService;
import br.edu.ocdrf.message.ResourceOntologyRegistrationData;
import br.edu.ocdrf.message.ResourceOpData;
import br.edu.ocdrf.oal.domain.OInvokeMethod;
import br.edu.ocdrf.util.AES;
import br.edu.ocdrf.util.IMapPutListener;
import br.edu.ocdrf.util.MapListener;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hp.hpl.jena.query.ReadWrite;
import com.hp.hpl.jena.rdf.model.Model;
import java.net.InetAddress;
import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;

import org.apache.log4j.Logger;

public abstract class AbstractResourceAgent extends SEBaseService implements IAgentDataNotification {

    private static final Logger log = Logger.getLogger(AbstractResourceAgent.class.getName());

    protected final String EXECUTION_CAPACITY = "ExecutionCapability";
    protected final String CAPACITY_PROPERTY = "hasCapability";
    protected final String INPUT_CAPACITY = "InputCapability";

    protected String agentCredentialFileName;

    private final int TASK_DELAY = 4000;

    private String configFileName;
    protected String ontologyStr;

    protected ResourceAgentConfig resourceAgentConfig;

    protected final MapListener<String, CapabilityData> resourceColectedData = new MapListener<>(Collections.synchronizedMap(new HashMap()));
    protected final Map<String, IInvokeOperation> resourceOperations = Collections.synchronizedMap(new HashMap());

    protected int probeTime;
    private Timer timer;
    private Timer fakeDataTimer;

    protected boolean listenerRegistered = false;
    private boolean dataModified = false;

    protected IBaseDirectoryService directoryService;
    protected IBaseContextService contextService;

    protected DeviceCommWorker deviceConnetor;

    protected abstract void describeResourceInvokeOperations();

    protected abstract IBaseDirectoryService findDirectoryService(String url) throws ResourceAgentException;

    protected abstract IBaseContextService findContextService(String url) throws ResourceAgentException;

    protected abstract IDeviceDriver defineDeviceDriver() throws ResourceAgentException;

    @Override
    public void initialize() throws ResourceAgentException {

        try {

            initServiceBaseProps();
            File configFile = new File(getAgentsConfigPath(), configFileName);

            if (configFile.exists()) {

                readConfig(configFile);
                log.info("Loading ontology file.");
                loadOntologyFile(getAgentsConfigPath(), ontologyFileName);
                log.info("Ontology successfully loaded.");

                mapResourceCapabilitiesData();
                describeResourceInvokeOperations();

                setMapListener();
                localServiceIP = InetAddress.getLocalHost().getHostAddress();

//                setDeviceDriver(defineDeviceDriver());
//                startDeviceDriverConn();
//                startMonitoring();
                fakeDataTimer = new Timer();
                fakeDataTimer.schedule(new DataGenTask(resourceColectedData), 1000, 100);

            } else {
                log.error("Configuration file " + configFile.toString() + " not found.");
                System.exit(4);
            }
        } catch (Exception e) {
            throw new ResourceAgentException(e);
        }
    }

    @Override
    protected void readConfig(File file) throws ServiceBaseException {

        resourceAgentConfig = new ResourceAgentConfig().createObjectFromXML(file);

        oResource.setNodeId(resourceAgentConfig.getUri());
        ontologyFileName = resourceAgentConfig.getOntology().getFile();
        //probeTime = resourceAgentConfig.getDeviceConfig().getProbePeriod();
        //directoryService = findDirectoryService(resourceAgentConfig.getDeviceConfigDirectoryService().getUrl());

        log.info("Agent config file sucessfully loaded!");

    }

    private void setMapListener() {
        resourceColectedData.addPutListener(new IMapPutListener() {
            public void putListener() {
                dataModified = true;
            }
        });
    }

    private void setInvokeTechnologies() throws OALException {

//        OAOInvokeMethod aOInvokeMethod = new OAOInvokeMethod(ontologyDLModel);
//        List<OInvokeMethod> oinvokeList = aOInvokeMethod.getAllInvokeMethods();
//
//        for (OInvokeMethod invM : oinvokeList) {
//            resOntDesc.setInvokeTecnology(invM.getInvokeTechnology(), invM.getInvokeString());
//        }
        List<OInvokeMethod> oinvokeList = oResource.getInvokeMethod();

        for (OInvokeMethod invM : oinvokeList) {
            resOntDesc.setInvokeTecnology(invM.getInvokeTechnology(), invM.getInvokeString());
        }

    }

    protected void mapResourceCapabilitiesData() {

        for (OResourceComponent resComp : oResource.getResourceComponents()) {
            for (OCapability oCap : resComp.getCapabilities()) {

                CapabilityData capData = new CapabilityData();
                capData.capabilityNodeID = oCap.getNodeId();

                for (OAttribute oAtt : oCap.getAttributes()) {

                    CapabilityAttribute capAtt = new CapabilityAttribute();
                    capAtt.attributeModel = oAtt;
                    capData.capabilityAttributes.put(oAtt.getNodeId(), capAtt);

                }

                StringBuilder dataKey = new StringBuilder();
                dataKey.append(resComp.getNodeId())
                        .append(oCap.getNodeId());

                resourceColectedData.put(dataKey.toString(), capData);

            }
        }
    }

    protected void setOperation(String operationID, IInvokeOperation operation) {
        resourceOperations.put(operationID, operation);
    }

    protected AgentResponseData getAgentTargetResponse(ResourceOpData opData) throws Exception {

        AgentResponseData aResponse = new AgentResponseData();

        aResponse = executeOperation(opData.invokeOperationName, opData);

        return aResponse;
    }

    private AgentResponseData executeOperation(String opeartionID, ResourceOpData opTarget) throws Exception {

        AgentResponseData agRespData = null;

        IInvokeOperation invokeOp = resourceOperations.get(opeartionID);

        if (invokeOp != null) {
            agRespData = invokeOp.executeOperation(opTarget);
        } else {
            throw new Exception("Operation name is not mapped within the resource agent.");
        }
        return agRespData;

    }

    protected void registerService() throws DirectoryServiceException {

        String fullFilePath = getAgentsConfigPath() + agentCredentialFileName;
        try {

            manageCachedCredentials();

            ResourceOntologyRegistration resOReg = new ResourceOntologyRegistration();

            resOReg.dirServiceTicket = servID.dirServiceTicket;
            resOReg.requesterClaimedID = resourceAgentConfig.getLogin().userid;

            ResourceOntologyRegistrationData resOntoRegData = new ResourceOntologyRegistrationData();
            resOntoRegData.resourceOntology = ontologyStr;
            resOntoRegData.serviceUrl = resOntDesc.getInvokeTecnology(WEBSERVICE_INV_TECH);
            resOntoRegData.requestingClientID = resourceAgentConfig.getLogin().userid;
            resOntoRegData.ticketOwnerID = resourceAgentConfig.getLogin().userid;

            resOReg.setOntology(resOntoRegData, servID.directorySessionKey);

            String response = directoryService.registerResource(jsonMapper.writeValueAsString(resOReg));

            AES cryptoAES = new AES();
            cryptoAES.setSecretKey(servID.directorySessionKey);

            ServiceID resourceServiceID = jsonMapper.readValue(cryptoAES.decrypt(response), ServiceID.class);

            servID.secretKey = resourceServiceID.secretKey;
            servID.uuid = resourceServiceID.uuid;

            //cacheServiceID(fullFilePath);
            //log.info(response);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void manageCachedCredentials() throws ResourceAgentException {
        String fullFilePath = null;
        try {

            fullFilePath = getAgentsConfigPath() + agentCredentialFileName;
            loadCredentialsFile(fullFilePath);
            directoryService = findDirectoryService(servID.directoryURL);
            contextService = findContextService(servID.contextURL);

        } catch (FileNotFoundException fileEX) {

            log.info("Cached Credentials not found...");
            try {

                log.info("Preparing to Log Resource Agent...");

                resourceAgentLogin();

                log.info("Resource Agent is Logged!");

            } catch (Exception ex) {
                throw new ResourceAgentException(ex);
            }

        } catch (IOException | ClassNotFoundException e) {
            throw new ResourceAgentException(e);
        }
    }

    private void resourceAgentLogin() {

        final QName RACS_SERVICE_NAME = new QName("http://racs.ocdrf.edu.br", "RACSWS");
        final QName RACS_Port_NAME = new QName("http://racs.ocdrf.edu.br", "RACSWSPort");

        //final String racsURL = "http://152.92.155.231:3000/RACSWS?wsdl";
        final String racsURL = "http://192.168.1.100:3000/RACSWS?wsdl";

        String response = null;
        SessionData session;
        ServiceAccessResponse sAResponse;

        try {

            URL directoryUrl = new URL(racsURL);
            Service service = Service.create(directoryUrl, RACS_SERVICE_NAME);
            IRACSService iracService = service.getPort(IRACSService.class);

            LoginRequestMessage lReqMsg = new LoginRequestMessage();
            lReqMsg.clientID = resourceAgentConfig.getLogin().userid;

            AuthenticatorTicket authTicket = new AuthenticatorTicket();
            authTicket.requestingClientID = resourceAgentConfig.getLogin().userid;
            authTicket.ticketOwnerID = resourceAgentConfig.getLogin().userid;

            authTicket.clientLocalAddress = InetAddress.getLocalHost().getHostAddress();

            lReqMsg.setAuthenticator(authTicket, resourceAgentConfig.getLogin().password);

            ObjectMapper mapper = new ObjectMapper();

            String loginData = mapper.writeValueAsString(lReqMsg);

            response = iracService.clientServicesAccessRequest(loginData);

            sAResponse = mapper.readValue(response, ServiceAccessResponse.class);

            System.out.println(response);

            session = sAResponse.getSessionData(resourceAgentConfig.getLogin().password);

            directoryService = findDirectoryService(session.directoryURL);
            contextService = findContextService(session.contextURL);

            loadLoginData(sAResponse, session);

        } catch (Exception exception) {

            System.err.println(response);

        }

    }

    private void loadLoginData(ServiceAccessResponse sAResponse, SessionData session) {

        servID.contextSessionKey = session.contextSessionKey;
        servID.contextURL = session.contextURL;

        servID.directorySessionKey = session.directorySessionKey;
        servID.directoryURL = session.directoryURL;

        servID.discoverySessionKey = session.discoverySessionKey;
        servID.discoveryURL = session.discoveryURL;

        servID.dirServiceTicket = sAResponse.dirServiceTicket;
        servID.contextServiceTicket = sAResponse.contextServiceTicket;
        servID.discServiceTicket = sAResponse.discServiceTicket;

    }

    private void startDeviceDriverConn() throws ResourceAgentException {
        if (deviceConnetor != null) {
            deviceConnetor.start();
        } else {
            throw new ResourceAgentException("Device Driver is Null");
        }
    }

    private void setDeviceDriver(IDeviceDriver deviceDriver) {
        deviceConnetor = new DeviceCommWorker(deviceDriver);
    }

    protected String getInvokeOpCapability(String invokeOpNodeId) {
        OCapability cap = null;
        for (OResourceComponent resComp : oResource.getResourceComponents()) {
            for (OInvokeOperation invOp : resComp.getInvokeOperations()) {
                if (invOp.getNodeId().equals(invokeOpNodeId)) {
                    cap = invOp.getReturnsCapacity();
                }
            }
        }
        return cap.getNodeId();
    }

    protected synchronized void loadOntologyFile(String agentConfigPath, String agentFileName) throws Exception {

        ontologyStr = readOntologyFile(agentConfigPath, agentFileName);

        //loadOntologyModels(agentConfigPath, agentFileName);

        oaoResource = new OAOResource(baseModel);
        oResource = oaoResource.getSingleResource();


        //ontologyStr = oResource.serializeOntology(agentConfigPath, agentFileName);
        setInvokeTechnologies();

    }

    public void startMonitoring() {
        setProbeTask(new ProbeTask(this), probeTime);
    }

    public void stopMonitoring() {
        if (timer != null) {
            timer.cancel(); // Termina a thread probeTask
        }
    }

    @Override
    public void notifyContextService() throws ContextException {

        //contextService.notifyChangeState(getResourceState("xxx"));
        System.out.append("Context is being called");
    }

    public String getConfigFileName() {
        return configFileName;
    }

    public void setConfigFileName(String configFileName) {
        this.configFileName = configFileName;
    }

    public boolean isDataModified() {
        return dataModified;
    }

    protected void setProbeTask(TimerTask probeTask, long probePeriod) {
        timer = new Timer();
        timer.schedule(probeTask, TASK_DELAY, probePeriod);
    }

    @Override
    public boolean checkDataModification() {
        return isDataModified();
    }

    @Override
    public boolean isThereListeners() {
        return listenerRegistered;
    }

}
