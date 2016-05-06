package br.edu.ocdrf.directory.ws;

import br.edu.ocdrf.agent.messages.ResourceOntologyRegistration;
import br.edu.ocdrf.auth.message.ServiceRegistrationData;
import br.edu.ocdrf.auth.message.ServiceRegistrationReq;
import br.edu.ocdrf.auth.message.ServiceTicket;
import br.edu.ocdrf.directory.OntologyDirectoryService;
import br.edu.ocdrf.entities.ServiceID;
import br.edu.ocdrf.entities.ServicesTypeID;
import br.edu.ocdrf.exceptions.DirectoryServiceException;
import br.edu.ocdrf.exceptions.OALException;
import br.edu.ocdrf.message.ClientContext;
import br.edu.ocdrf.message.ClientContextData;
import br.edu.ocdrf.message.DiscoveryQuery;
import br.edu.ocdrf.message.FoundCapabilities;
import br.edu.ocdrf.message.FoundResources;
import br.edu.ocdrf.ws.wsdl.interfaces.IDirectoryService;
import br.edu.ocdrf.oal.OAOResource;
import br.edu.ocdrf.oal.OAOResourceComponent;
import br.edu.ocdrf.oal.domain.OResourceComponent;
import br.edu.ocdrf.message.QueryData;
import br.edu.ocdrf.message.ResourceInfo;
import br.edu.ocdrf.message.ResourceInfoData;
import br.edu.ocdrf.message.ResourceOntologyRegistrationData;
import br.edu.ocdrf.message.ResourceOpData;
import br.edu.ocdrf.message.ResourceOperation;
import br.edu.ocdrf.oal.OAOCapability;
import br.edu.ocdrf.oal.domain.OCapability;
import br.edu.ocdrf.oal.domain.OResourceEntity;
import br.edu.ocdrf.racs.business.CapabilityBL;
import br.edu.ocdrf.racs.business.EntityBL;
import br.edu.ocdrf.racs.business.RACSServiceBL;
import br.edu.ocdrf.racs.business.ResourceBL;
import br.edu.ocdrf.racs.model.Entity;
import br.edu.ocdrf.racs.model.Resource;
import br.edu.ocdrf.util.AES;
import br.edu.ocdrf.util.PerformanceTest;
import com.hp.hpl.jena.query.ReadWrite;
import com.hp.hpl.jena.rdf.model.Model;
import java.util.ArrayList;
import java.util.List;
import javax.jws.WebService;
import javax.xml.ws.Endpoint;
import org.apache.log4j.Logger;

@WebService(serviceName = "DirectoryService", targetNamespace = "http://directory.ocdrf.edu.br", endpointInterface = "br.edu.ocdrf.ws.wsdl.interfaces.IDirectoryService")
public class DirectoryServiceWS extends OntologyDirectoryService {

    private final PerformanceTest regResPerformanceTest = new PerformanceTest("DirectoryService_RegisterResource", 1000);
    private final PerformanceTest findCapabilitiesPerformanceTest = new PerformanceTest("DirectoryService_FindCapabilities", 1000);
    private final PerformanceTest getFullresourceInfoPerformanceTest = new PerformanceTest("DirectoryService_FullResourceInfo", 1000);
    private final PerformanceTest findResourcesPerformanceTest = new PerformanceTest("DirectoryService_FindResource", 1000);

    private static final Logger log = Logger.getLogger(DirectoryServiceWS.class.getName());

    private Endpoint endpoint = null;

    @Override
    public void initialize() throws DirectoryServiceException {

        log.info("Initializing Directory Service...\n\n");
        super.initialize();
        try {

            endpoint = Endpoint.publish(resOntDesc.getInvokeTecnology(WEBSERVICE_INV_TECH), this);

            startNRDManager(30000);

            log.info("Directory service successfully started! \n\n");
        } catch (Exception ex) {
            log.error(ex.toString(), ex);
            throw ex;
        }
    }

    @Override
    public void stop() {
        if (endpoint != null) {
            endpoint.stop();
        }
    }

    @Override
    public synchronized String registerResource(String resourceOntologyReg) throws DirectoryServiceException {

        regResPerformanceTest.tagStartTime(0);

        String encryptedJsonResponse = null;
        ServiceID resourceServiceID = null;

        try {

            ResourceOntologyRegistration resOntReg = jsonMapper.readValue(resourceOntologyReg, ResourceOntologyRegistration.class);

            ServiceTicket servTicket = resOntReg.getDirectoryServTicket(servID.secretKey);
            ResourceOntologyRegistrationData data = resOntReg.getOntology(servTicket.serviceSessionKey);

            boolean validRequest = validateServiceTicket(servTicket, data.ticketOwnerID, getMessageSenderRemoteHost(wsContext));

            regResPerformanceTest.tagEndTime(0);

            if (validRequest) {

                regResPerformanceTest.tagStartTime(1);

                RACSServiceBL dtAccess = new RACSServiceBL(emf);
                Entity user = dtAccess.getEntityByName(data.requestingClientID);

                resourceServiceID = addResourceToModel(data.resourceOntology, user, data);

                regResPerformanceTest.tagEndTime(1);

            }

            regResPerformanceTest.tagStartTime(2);

            AES cryptoAES = new AES();
            cryptoAES.setSecretKey(servTicket.serviceSessionKey);
            encryptedJsonResponse = cryptoAES.encrypt(jsonMapper.writeValueAsString(resourceServiceID));

            regResPerformanceTest.tagEndTime(2);
            regResPerformanceTest.nextMeasurment();
            regResPerformanceTest.saveMeasurmentsToDisk();

            return encryptedJsonResponse;

        } catch (Exception ex) {
            log.error(ex.toString());
            throw new DirectoryServiceException(ex);
        }

    }

    @Override
    public String registerService(String jsonServiceRegistration) throws DirectoryServiceException {

        String encryptedJsonResponse = null;

        try {

            ServiceRegistrationReq resOntReg = jsonMapper.readValue(jsonServiceRegistration, ServiceRegistrationReq.class);

            RACSServiceBL dtAccess = new RACSServiceBL(emf);
            Entity user = dtAccess.getEntityByName(resOntReg.requesterClaimedID);

            if (user != null) {

                ServiceRegistrationData data = resOntReg.getEncryptedServiceData(user.getPassword());
                String serviceIP = getMessageSenderRemoteHost(wsContext);

                boolean validRequest = validateClaimedNameAndLocation(resOntReg.requesterClaimedID, data.ticketOwnerID, data.localAddress, serviceIP);

                if (validRequest) {

                    ResourceBL resbl = new ResourceBL(emf);
                    ServiceID requestingServNewID = resbl.registerService(user, servID.uuid, data.serviceType, data.serviceUrl);

                    if (data.serviceType == ServicesTypeID.RESOURCE_AGENT_SERVICE_TYPE) {
                        AddContextServiceCredentials(user, requestingServNewID, serviceIP);
                    }

                    AddDirectoryServiceTicket(user, requestingServNewID, serviceIP);

                    String jsonResponse = jsonMapper.writeValueAsString(requestingServNewID);
                    AES cryptoAES = new AES();
                    cryptoAES.setKeyFromHashedPassword(user.getPassword());
                    encryptedJsonResponse = cryptoAES.encrypt(jsonResponse);

                }
            }
        } catch (Exception ex) {
            log.error(ex.toString(), ex);
            throw new DirectoryServiceException(ex);
        }
        return encryptedJsonResponse;
    }

    @Override
    public String findResources(String ontoQueryString) throws DirectoryServiceException {

        findResourcesPerformanceTest.tagStartTime(0);

        String response = null;
        try {
            DiscoveryQuery discQuery = jsonMapper.readValue(ontoQueryString, DiscoveryQuery.class);

            ServiceTicket servTicket = discQuery.getDirectoryServTicket(servID.secretKey);
            QueryData qData = discQuery.getQueryData(servTicket.serviceSessionKey);

            boolean validQuery = validateServiceTicket(servTicket, qData.ticketOwnerID, getMessageSenderRemoteHost(wsContext));

            findResourcesPerformanceTest.tagEndTime(0);
            if (validQuery) {

                findResourcesPerformanceTest.tagStartTime(1);

                List<OResourceComponent> resCompList = getResourceComp(qData);

                

                findResourcesPerformanceTest.tagStartTime(3);

                FoundResources fRes = new FoundResources();
                fRes.setResponseDataAndEncrypt((ArrayList<OResourceComponent>) resCompList, servTicket.serviceSessionKey);

                response = jsonMapper.writeValueAsString(fRes);

                findResourcesPerformanceTest.tagEndTime(3);
            }

        } catch (Exception ex) {
            log.error(ex.toString());
            throw new DirectoryServiceException(ex);
        }

        findResourcesPerformanceTest.nextMeasurment();
        findResourcesPerformanceTest.saveMeasurmentsToDisk();

        return response;
    }

    private List<OResourceComponent> getResourceComp(QueryData qData) throws OALException {

        Entity entity = new EntityBL(emf).getEntityByName(qData.requestingClientID);

        dataset.begin(ReadWrite.READ);
        Model model = dataset.getDefaultModel();

        OAOResourceComponent resComp = new OAOResourceComponent(model);
        List<OResourceComponent> resCompList = resComp.findResCompByCapability(qData.resourceCapabilityID);

        findResourcesPerformanceTest.tagEndTime(1);
        
        dataset.end();
        model.close();
        findResourcesPerformanceTest.tagStartTime(2);
        
        resCompList = new CapabilityBL(emf).setOnlyAllowedResCompAccesses(resCompList, entity);
        
        findResourcesPerformanceTest.tagEndTime(2);
        return resCompList;
    }

    @Override
    public String findCapabilities(String clientContextString) throws DirectoryServiceException {

        findCapabilitiesPerformanceTest.tagStartTime(0);
        String dirResponse = null;

        try {

            ClientContext clientContext = jsonMapper.readValue(clientContextString, ClientContext.class);

            ServiceTicket servTicket = clientContext.getDirectoryServTicket(servID.secretKey);
            ClientContextData data = clientContext.getContextData(servTicket.serviceSessionKey);

            boolean validRequest = validateServiceTicket(servTicket, data.discoveryServiceUUID, getMessageSenderRemoteHost(wsContext));

            findCapabilitiesPerformanceTest.tagEndTime(0);

            if (validRequest) {

                findCapabilitiesPerformanceTest.tagStartTime(1);

                Entity entity = new EntityBL(emf).getEntityByName(data.requestingClientID);

                List<OCapability> capList = null;

                capList = getCapabilityFromOntology(data);
                findCapabilitiesPerformanceTest.tagEndTime(1);
                
                findCapabilitiesPerformanceTest.tagStartTime(2);
                CapabilityBL capBl = new CapabilityBL(emf);
                capList = capBl.setOnlyAllowedCapabilityAccesses(capList, entity);
                findCapabilitiesPerformanceTest.tagEndTime(2);
                
                findCapabilitiesPerformanceTest.tagStartTime(3);

                FoundCapabilities fCaps = new FoundCapabilities();
                fCaps.setResponseDataAndEncrypt((ArrayList<OCapability>) capList, servTicket.serviceSessionKey);
                dirResponse = jsonMapper.writeValueAsString(fCaps);

                findCapabilitiesPerformanceTest.tagEndTime(3);

            }

        } catch (Exception e) {
            throw new DirectoryServiceException(e);
        }

        findCapabilitiesPerformanceTest.nextMeasurment();
        findCapabilitiesPerformanceTest.saveMeasurmentsToDisk();

        return dirResponse;

    }

    private List<OCapability> getCapabilityFromOntology(ClientContextData data) throws OALException {

        List<OCapability> capList;

        dataset.begin(ReadWrite.READ);
        Model model = dataset.getDefaultModel();

        OAOCapability oaoCap = new OAOCapability(model);
        if (data.location.isEmpty()) {
            capList = oaoCap.getAllAvailableCapabilities();
        } else {
            capList = oaoCap.getAllAvailableCapabilitiesByLocation(data.location);
        }

        dataset.end();
        model.close();

        return capList;
    }

    @Override
    public String getFullResourceInfo(String jsonResOp) throws DirectoryServiceException {

        getFullresourceInfoPerformanceTest.tagStartTime(0);

        String dirResponse = null;

        try {

            ResourceOperation resOp = jsonMapper.readValue(jsonResOp, ResourceOperation.class);

            ServiceTicket clientTicket = resOp.getDirectoryServTicket(servID.secretKey);

            ResourceOpData resOpData = resOp.getResourceOpData(clientTicket.serviceSessionKey);

            String clientIP = getMessageSenderRemoteHost(wsContext);

            boolean validRequest = validateServiceTicket(clientTicket, resOpData.ticketOwnerID, clientIP);

            getFullresourceInfoPerformanceTest.tagEndTime(0);

            if (validRequest) {

                getFullresourceInfoPerformanceTest.tagStartTime(1);

                OResourceEntity oResEnt = getResCompSingle(resOpData);
                
                getFullresourceInfoPerformanceTest.tagEndTime(1);
                
                getFullresourceInfoPerformanceTest.tagStartTime(2);
                
                Entity entity = new EntityBL(emf).getEntityByName(resOpData.requestingClientID);
                //TODO Reverificar acesso do client ao recurso

                ResourceBL resBL = new ResourceBL(emf);
                Resource res = resBL.getResourceByUUID(oResEnt.getNodeId());
                
                String resourceSessionKey = AES.generateSecretKey();
                ServiceTicket resourceTicket = createServiceTicket(entity, clientIP, resourceSessionKey, oResEnt.getNodeId(), resOpData.ticketOwnerID);

                getFullresourceInfoPerformanceTest.tagEndTime(2);
                
                getFullresourceInfoPerformanceTest.tagStartTime(3);

                ResourceInfo resInfo = new ResourceInfo();

                ResourceInfoData resInfData = new ResourceInfoData();
                resInfData.ticketOwnerID = resOpData.ticketOwnerID;
                resInfData.resourceData = oResEnt;
                resInfData.resourceSessionKey = resourceSessionKey;

                resInfo.setResourceServTicket(res.getSecretkey(), resourceTicket);
                resInfo.setResourceInfoDataAndEncrypt(resInfData, clientTicket.serviceSessionKey);

                dirResponse = jsonMapper.writeValueAsString(resInfo);

                getFullresourceInfoPerformanceTest.tagEndTime(3);

            }
            getFullresourceInfoPerformanceTest.nextMeasurment();
            getFullresourceInfoPerformanceTest.saveMeasurmentsToDisk();
            return dirResponse;
        } catch (Exception e) {
            throw new DirectoryServiceException(e);
        }

    }

    private OResourceEntity getResCompSingle(ResourceOpData resOpData) throws OALException {

        dataset.begin(ReadWrite.READ);
        Model model = dataset.getDefaultModel();

        OAOResource oaoRes = new OAOResource(model);
        OResourceEntity oResEnt = oaoRes.getFullResourceDataByInvokeOp(resOpData.invokeOperationID);

        dataset.end();
        model.close();

        return oResEnt;
    }

    @Override
    public IDirectoryService federateDirectoryService(String url) throws DirectoryServiceException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
