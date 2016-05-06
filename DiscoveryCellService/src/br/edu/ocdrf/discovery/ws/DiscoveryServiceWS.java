package br.edu.ocdrf.discovery.ws;

import br.edu.ocdrf.auth.message.ServiceTicket;
import javax.jws.WebService;
import javax.xml.ws.Endpoint;

import br.edu.ocdrf.discovery.AbstractDiscoveryService;
import br.edu.ocdrf.exceptions.DiscoveryException;
import br.edu.ocdrf.message.ClientContext;
import br.edu.ocdrf.message.ClientContextData;
import br.edu.ocdrf.message.DiscoveryQuery;
import br.edu.ocdrf.message.FoundCapabilities;
import br.edu.ocdrf.message.FoundResources;
import br.edu.ocdrf.message.QueryData;
import br.edu.ocdrf.util.PerformanceTest;
import org.apache.log4j.Logger;

@WebService(serviceName = "DiscoveryService", targetNamespace = "http://discovery.ocdrf.edu.br", endpointInterface = "br.edu.ocdrf.ws.wsdl.interfaces.IDiscoveryService")
public class DiscoveryServiceWS extends AbstractDiscoveryService {

    private final PerformanceTest findCapPT = new PerformanceTest("DiscoveryService_FindCapabilities", 1000);
    private final PerformanceTest findResPT = new PerformanceTest("DiscoveryService_FindResources", 1000);

    private static final Logger log = Logger.getLogger(DiscoveryServiceWS.class.getName());
    private Endpoint endpoint = null;

    public DiscoveryServiceWS() {
        super();
    }

    @Override
    public void initialize() throws DiscoveryException {

        log.info("Initializing Discovery Service...\n\n");

        try {
            super.initialize();
            endpoint = Endpoint.publish(resOntDesc.getInvokeTecnology(WEBSERVICE_INV_TECH), this);

            log.info("Discovery service successfully started! \n\n");
        } catch (DiscoveryException ex) {
            log.error(ex.toString(), ex);
            throw ex;
        }
    }

    public void stop() {
        if (endpoint != null) {
            endpoint.stop();
        }
        log.info("Discovery service stoped");
    }

    @Override
    public String findResources(String discoveryQuery) throws DiscoveryException {

        findResPT.tagStartTime(0);

        String dirResponse = null;

        try {

            DiscoveryQuery discQuery = jsonMapper.readValue(discoveryQuery, DiscoveryQuery.class);

            ServiceTicket servTicket = discQuery.getDiscoveryServTicket(servID.secretKey);
            QueryData qData = discQuery.getQueryData(servTicket.serviceSessionKey);

            boolean validQuery = validateServiceTicket(servTicket, qData.requestingClientID, getMessageSenderRemoteHost(wsContext));

            findResPT.tagEndTime(0);

            if (validQuery) {

                findResPT.tagStartTime(1);

                discQuery.dirServiceTicket = servID.dirServiceTicket;
                qData.ticketOwnerID = servID.uuid;

                discQuery.setQueryDataAndEncrypt(qData, servID.directorySessionKey);

                dirResponse = directoryService.findResources(jsonMapper.writeValueAsString(discQuery));

                findResPT.tagEndTime(1);

                findResPT.tagStartTime(2);
                FoundResources fResources = jsonMapper.readValue(dirResponse, FoundResources.class);
                fResources.encryptedResponseData = fResources.reCryptografyDataObject(fResources.encryptedResponseData, servID.directorySessionKey, servTicket.serviceSessionKey);

                dirResponse = jsonMapper.writeValueAsString(fResources);
                findResPT.tagEndTime(2);

            }

        } catch (Exception ex) {
            throw new DiscoveryException(ex);
        }
        findResPT.nextMeasurment();
        findResPT.saveMeasurmentsToDisk();
        return dirResponse;
    }

    @Override
    public String findCapabilities(String jsonClientContextData) throws DiscoveryException {

        findCapPT.tagStartTime(0);

        String dirResponse = null;

        try {

            ClientContext clientContext = jsonMapper.readValue(jsonClientContextData, ClientContext.class);

            ServiceTicket servTicket = clientContext.getDiscoveryServTicket(servID.secretKey);
            ClientContextData data = clientContext.getContextData(servTicket.serviceSessionKey);

            boolean validRequest = validateServiceTicket(servTicket, data.requestingClientID, getMessageSenderRemoteHost(wsContext));

            findCapPT.tagEndTime(0);

            if (validRequest) {

                findCapPT.tagStartTime(1);

                clientContext.dirServiceTicket = servID.dirServiceTicket;
                data.discoveryServiceUUID = servID.uuid;
                clientContext.setContextDataAndEncrypt(data, servID.directorySessionKey);
                dirResponse = directoryService.findCapabilities(jsonMapper.writeValueAsString(clientContext));

                findCapPT.tagEndTime(1);
                findCapPT.tagStartTime(2);

                FoundCapabilities fCaps = jsonMapper.readValue(dirResponse, FoundCapabilities.class);
                fCaps.encryptedResponseData = fCaps.reCryptografyDataObject(fCaps.encryptedResponseData, servID.directorySessionKey, servTicket.serviceSessionKey);
                dirResponse = jsonMapper.writeValueAsString(fCaps);

                findCapPT.tagEndTime(2);
            }

        } catch (Exception e) {
            throw new DiscoveryException(e);
        }

        findCapPT.nextMeasurment();
        findCapPT.saveMeasurmentsToDisk();

        return dirResponse;
    }

}
