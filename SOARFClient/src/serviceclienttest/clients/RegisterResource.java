package serviceclienttest.clients;

import serviceclienttest.Base.WSDLWebserviceBase;
import br.edu.ocdrf.agent.messages.ResourceOntologyRegistration;
import br.edu.ocdrf.entities.ServiceID;
import br.edu.ocdrf.message.ResourceOntologyRegistrationData;
import br.edu.ocdrf.ws.wsdl.interfaces.IDirectoryService;
import java.net.URL;
import br.edu.ocdrf.util.PerformanceTest;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;

public class RegisterResource extends WSDLWebserviceBase {

    private final PerformanceTest regResPT = new PerformanceTest("Client_Res_Reg", 1000);

    private final QName DIR_SERVICE_NAME = new QName("http://directory.ocdrf.edu.br", "DirectoryService");

    private final String dirURL = ":1974/directory/DirectoryService?wsdl";

    private IDirectoryService dirService;

    public ServiceID registeredServID;

    public RegisterResource() {
        setServiceAddress(dirURL);
    }

    public void registerAgentOntology(String directoryTicket, String dirSessionKey, String userID, int amountOfResource) throws Exception {

        URL directoryUrl = new URL(serviceAddress);
        Service service = Service.create(directoryUrl, DIR_SERVICE_NAME);
        dirService = service.getPort(IDirectoryService.class);

        ResourceOntologyRegistration resOntReg = new ResourceOntologyRegistration();
        resOntReg.dirServiceTicket = directoryTicket;
        resOntReg.requesterClaimedID = "admin";

        ResourceOntologyRegistrationData data = new ResourceOntologyRegistrationData();
        data.ticketOwnerID = "admin";
        data.requestingClientID = "admin";
        data.serviceUrl = "http://152.92.155.231:3008/IrisNetworkWS?wsdl";
        data.resourceOntology = RDFXMLOntologyLoader.getOntologyAsString();

        resOntReg.setOntology(data, dirSessionKey);

        for (int i = 0; i < amountOfResource; i++) {
            regResPT.tagStartTime(0);
            String encryptedJsonServID = dirService.registerResource(jsonMapper.writeValueAsString(resOntReg));
            regResPT.tagEndTime(0);
            regResPT.nextMeasurment();
            regResPT.saveMeasurmentsToDisk();
            System.out.println(i);
        }
    }
}
