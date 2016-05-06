package serviceclienttest.clients;

import br.edu.ocdrf.message.AgentResponseData;

public class OCDRFClient {

    public LoginClientTest racsClient = new LoginClientTest();
    public DiscoveryClient disClient = new DiscoveryClient();
    public RegisterResource regRes = new RegisterResource();
    public ContextClient contextClient = new ContextClient();

    public void loginAndLoadCredentials(int iterations) throws Exception {
        racsClient.login(iterations);
    }

    public void discoverCapabilities(int iterations) throws Exception {
        disClient.findCapabilities("", racsClient.sAResponse.discServiceTicket, racsClient.session.discoverySessionKey, iterations);
    }

    public void findResourcesByCap(int iterations) throws Exception {
        disClient.findCapabilityResources(racsClient.sAResponse.discServiceTicket, racsClient.session.discoverySessionKey, disClient.capList.get(0).getNodeId(), iterations);
    }

    public AgentResponseData invokResOp(int iterations) throws Exception {
        return contextClient.invokeResourceOperation(racsClient.sAResponse.contextServiceTicket, racsClient.session.contextSessionKey, disClient.resList, iterations);
    }

    public void registerResources(int amountOfRes) throws Exception {
        regRes.registerAgentOntology(racsClient.sAResponse.dirServiceTicket, racsClient.session.directorySessionKey, racsClient.session.ticketOwnerID, amountOfRes);
    }

}
