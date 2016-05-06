package br.edu.ocdrf.ws.wsdl.agent;

import br.edu.ocdrf.exceptions.ContextException;
import br.edu.ocdrf.exceptions.DirectoryServiceException;
import br.edu.ocdrf.exceptions.ResourceAgentException;
import br.edu.ocdrf.interfaces.IBaseContextService;
import br.edu.ocdrf.interfaces.IBaseDirectoryService;
import br.edu.ocdrf.agent.AbstractResourceAgent;
import br.edu.ocdrf.message.AgentResponseData;
import br.edu.ocdrf.auth.message.ServiceTicket;
import br.edu.ocdrf.message.AgentResponse;
import br.edu.ocdrf.message.ResourceOpData;
import br.edu.ocdrf.message.ResourceOperation;
import br.edu.ocdrf.util.PerformanceTest;
import br.edu.ocdrf.ws.wsdl.interfaces.IResourceAgent;
import br.edu.ocdrf.ws.wsdl.util.ContextServiceWSUtil;
import br.edu.ocdrf.ws.wsdl.util.DirectoryServiceWSUtil;
import org.apache.log4j.Logger;

public abstract class AgentWSDLServices extends AbstractResourceAgent implements IResourceAgent {

    private static final Logger log = Logger.getLogger(AgentWSDLServices.class.getName());

    private final PerformanceTest invokeAgentOp = new PerformanceTest("ResourceAgent_InvokeOperation", 1000);

    @Override
    public String invokeAgentOperation(String jsonEncryptedResOp) throws Exception {

        invokeAgentOp.tagStartTime(0);
        String agentResponseString = "";

        ResourceOperation resOp = jsonMapper.readValue(jsonEncryptedResOp, ResourceOperation.class);

        ServiceTicket clientTicket = resOp.getResourceServTicket(servID.secretKey);
        ResourceOpData resOpData = resOp.getResourceOpData(clientTicket.serviceSessionKey);

        String clientIP = getMessageSenderRemoteHost(wsContext);

        boolean validRequest = validateServiceTicket(clientTicket, resOpData.ticketOwnerID, clientIP);
        invokeAgentOp.tagEndTime(0);
        if (validRequest) {

            invokeAgentOp.tagStartTime(1);
            AgentResponseData aResponse = getAgentTargetResponse(resOpData);
            invokeAgentOp.tagEndTime(1);
            invokeAgentOp.tagStartTime(2);
            AgentResponse agResponse = new AgentResponse();
            agResponse.setAgentResponseDataAndEncrypt(aResponse, clientTicket.serviceSessionKey);

            agentResponseString = jsonMapper.writeValueAsString(agResponse);

        }
        invokeAgentOp.tagEndTime(2);
        invokeAgentOp.nextMeasurment();
        invokeAgentOp.saveMeasurmentsToDisk();
        return agentResponseString;
    }

    @Override
    protected IBaseDirectoryService findDirectoryService(String url) throws ResourceAgentException {
        try {
            return new DirectoryServiceWSUtil().findDirectoryService(url);
        } catch (DirectoryServiceException e) {
            throw new ResourceAgentException(e);
        }
    }

    @Override
    protected IBaseContextService findContextService(String url) throws ResourceAgentException {
        try {
            return new ContextServiceWSUtil().getContextService(url);
        } catch (ContextException e) {
            throw new ResourceAgentException(e);
        }
    }
}
