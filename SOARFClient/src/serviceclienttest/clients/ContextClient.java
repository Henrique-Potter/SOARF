package serviceclienttest.clients;

import serviceclienttest.Base.WSDLWebserviceBase;
import br.edu.ocdrf.message.AgentResponse;
import br.edu.ocdrf.message.AgentResponseData;
import br.edu.ocdrf.message.ResourceOpData;
import br.edu.ocdrf.message.ResourceOperation;
import br.edu.ocdrf.oal.domain.OInvokeOpParameter;
import br.edu.ocdrf.oal.domain.OResourceComponent;
import br.edu.ocdrf.ws.wsdl.interfaces.IContextService;
import java.net.URL;
import java.util.ArrayList;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;

public class ContextClient extends WSDLWebserviceBase {

    private final QName CONT_SERVICE_NAME = new QName("http://context.ocdrf.edu.br", "ContextService");
    private final QName CONT_Port_NAME = new QName("http://context.ocdrf.edu.br", "ContextServicePort");

    private final br.edu.ocdrf.util.PerformanceTest invokeOp = new br.edu.ocdrf.util.PerformanceTest("Client_ContextService_InvokeOp", 1000);

    private final String contextURLString = ":1975/context/ContextService?wsdl";

    public ContextClient() {
        setServiceAddress(contextURLString);
    }

    public AgentResponseData invokeResourceOperation(String contextTicket, String sessionKey, ArrayList<OResourceComponent> resCompList, int iterations) throws Exception {

        AgentResponseData agData = null;
        String response = null;

        URL contextURL = new URL(serviceAddress);
        Service service = Service.create(contextURL, CONT_SERVICE_NAME);
        IContextService contService = service.getPort(IContextService.class);

        ResourceOperation resOp = new ResourceOperation();
        resOp.contextServiceTicket = contextTicket;

        for (int i = 0; i < iterations; i++) {

            invokeOp.tagStartTime(0);

            for (OResourceComponent resComp : resCompList) {

                ResourceOpData resOpData = new ResourceOpData();
                resOpData.capabilityName = resComp.getCapabilities().get(0).name;
                resOpData.resourceComponentName = "Iris_01";

                resOpData.invokeOperationID = resComp.getInvokeOperations().get(0).getNodeId();
                resOpData.invokeOperationName = resComp.getInvokeOperations().get(0).getOperationName();
                resOpData.requestingClientID = "admin";

                OInvokeOpParameter invOpStartDate = new OInvokeOpParameter();
                invOpStartDate.name = "startDate";
                invOpStartDate.value = String.valueOf(System.currentTimeMillis() - 10000000);

                OInvokeOpParameter invOpEndDate = new OInvokeOpParameter();
                invOpEndDate.name = "endDate";
                invOpEndDate.value = String.valueOf(System.currentTimeMillis() + 10000000);

                resOpData.invokeOperationParameters.add(invOpStartDate);
                resOpData.invokeOperationParameters.add(invOpEndDate);

                resOp.setResourceOpDataAndEncrypt(resOpData, sessionKey);

                response = contService.invokeResourceOperation(jsonMapper.writeValueAsString(resOp));

                AgentResponse agentResponse = jsonMapper.readValue(response, AgentResponse.class);
                agData = agentResponse.getAgentResponseData(sessionKey);

//                if (!agData.capabiltyData.capabilityAttributes.get("rawLuminosity").attValues.isEmpty()) {
//                    System.out.println("Data successfully colected from: " + resComp.getNodeId());
//
//                }

            }
            System.out.println("Interation number : " + i);

            invokeOp.tagEndTime(0);
            invokeOp.nextMeasurment();
            invokeOp.saveMeasurmentsToDisk();
        }
        return agData;
    }
}
