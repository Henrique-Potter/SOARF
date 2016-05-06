package br.edu.ocdrf.context.ws;

import br.edu.ocdrf.auth.message.ServiceTicket;
import javax.jws.WebService;
import javax.xml.ws.Endpoint;

import br.edu.ocdrf.context.AbstractContextService;
import br.edu.ocdrf.exceptions.ContextException;
import br.edu.ocdrf.message.AgentResponse;
import br.edu.ocdrf.message.ResourceInfo;
import br.edu.ocdrf.message.ResourceInfoData;
import br.edu.ocdrf.message.ResourceOpData;
import br.edu.ocdrf.message.ResourceOperation;
import br.edu.ocdrf.oal.domain.OInvokeMethod;
import br.edu.ocdrf.util.PerformanceTest;
import br.edu.ocdrf.ws.wsdl.interfaces.IResourceAgent;
import org.apache.log4j.Logger;

@WebService(serviceName = "ContextService", targetNamespace = "http://context.ocdrf.edu.br", endpointInterface = "br.edu.ocdrf.ws.wsdl.interfaces.IContextService")
public class ContextServiceWS extends AbstractContextService {

    private final PerformanceTest invokeOpTest = new PerformanceTest("ContextService_InvokeOperation", 1000);

    private static final Logger log = Logger.getLogger(ContextServiceWS.class.getName());

    private Endpoint endpoint = null;

    @Override
    public void initialize() throws ContextException {

        try {
            log.info("Initializing Context Service...\n\n");
            super.initialize();

            endpoint = Endpoint.publish(resOntDesc.getInvokeTecnology(WEBSERVICE_INV_TECH), this);

            log.info("Context Service started!\n\n");

        } catch (Exception ex) {
            log.error(ex.toString(), ex);
            throw ex;
        }

    }

    @Override
    public String invokeResourceOperation(String jsonResOp) throws ContextException {

        invokeOpTest.tagStartTime(0);

        String resResponse = "";

        try {

            ResourceOperation resOp = jsonMapper.readValue(jsonResOp, ResourceOperation.class);

            ServiceTicket servTicket = resOp.getContextServTicket(servID.secretKey);

            ResourceOpData resOpData = resOp.getResourceOpData(servTicket.serviceSessionKey);

            boolean validRequest = validateServiceTicket(servTicket, resOpData.requestingClientID, getMessageSenderRemoteHost(wsContext));

            invokeOpTest.tagEndTime(0);

            if (validRequest) {

                invokeOpTest.tagStartTime(1);

                resOp.dirServiceTicket = servID.dirServiceTicket;

                resOpData.ticketOwnerID = servID.uuid;

                resOp.setResourceOpDataAndEncrypt(resOpData, servID.directorySessionKey);

                String dirResponse = directoryServices.getFullResourceInfo(jsonMapper.writeValueAsString(resOp));

                ResourceInfo resInfo = jsonMapper.readValue(dirResponse, ResourceInfo.class);

                invokeOpTest.tagEndTime(1);

                invokeOpTest.tagStartTime(2);
                resResponse = requestResourceInvOp(resInfo, resOp, resOpData, servTicket.serviceSessionKey);
            }
            
            invokeOpTest.tagEndTime(3);
            invokeOpTest.nextMeasurment();
            invokeOpTest.saveMeasurmentsToDisk();
            return resResponse;

        } catch (Exception e) {
            throw new ContextException(e);
        }
    }

    private String requestResourceInvOp(ResourceInfo resInfo, ResourceOperation resOp, ResourceOpData resOpData, String serviceSessionKey) throws Exception {

        ResourceInfoData resInfData = resInfo.getResourceInfoData(servID.directorySessionKey);

        String resourceURL = "";

        for (OInvokeMethod invMethod : resInfData.resourceData.getInvokeMethod()) {
            if (invMethod.getInvokeTechnology().equals("webservice")) {
                resourceURL = invMethod.getInvokeString();
            }
        }

        IResourceAgent resAgent = findResourceAgent(resourceURL);

        resOp.dirServiceTicket = null;
        resOp.resourceServiceTicket = resInfo.resourceServiceTicket;
        resOp.setResourceOpDataAndEncrypt(resOpData, resInfData.resourceSessionKey);

        String resourceResponse = resAgent.invokeAgentOperation(jsonMapper.writeValueAsString(resOp));
        invokeOpTest.tagEndTime(2);

        invokeOpTest.tagStartTime(3);
        AgentResponse agResponse = jsonMapper.readValue(resourceResponse, AgentResponse.class);

        agResponse.angentResponseData = agResponse.reCryptografyDataObject(agResponse.angentResponseData, resInfData.resourceSessionKey, serviceSessionKey);

        String agResponseString = jsonMapper.writeValueAsString(agResponse);

        return agResponseString;

    }

    @Override
    public void stop() {
        if (endpoint != null) {
            endpoint.stop();
        }
        log.info("Context service stoped");
    }

}
