package serviceclienttest.clients;

import serviceclienttest.Base.WSDLWebserviceBase;
import br.edu.ocdrf.auth.message.AuthenticatorTicket;
import br.edu.ocdrf.auth.message.LoginRequestMessage;
import br.edu.ocdrf.auth.message.ServiceAccessResponse;
import br.edu.ocdrf.auth.message.SessionData;
import br.edu.ocdrf.interfaces.IRACSService;
import java.net.InetAddress;
import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;

public class LoginClientTest extends WSDLWebserviceBase {

    private final br.edu.ocdrf.util.PerformanceTest RACSloginPT = new br.edu.ocdrf.util.PerformanceTest("Client_Racs_Login", 1000);

    private final QName RACS_SERVICE_NAME = new QName("http://racs.ocdrf.edu.br", "RACSWS");
    private final QName RACS_Port_NAME = new QName("http://racs.ocdrf.edu.br", "RACSWSPort");

    private final String racsURL = ":3000/RACSWS?wsdl";

    public SessionData session;
    public ServiceAccessResponse sAResponse;

    public LoginClientTest() {
        setServiceAddress(racsURL);
    }

    public void login(int iterations) throws Exception {

        String response = null;

        URL directoryUrl = new URL(serviceAddress);
        Service service = Service.create(directoryUrl, RACS_SERVICE_NAME);
        IRACSService iracService = service.getPort(IRACSService.class);

        LoginRequestMessage lReqMsg = new LoginRequestMessage();
        lReqMsg.clientID = "admin";

        AuthenticatorTicket authTicket = new AuthenticatorTicket();
        authTicket.requestingClientID = "admin";
        authTicket.ticketOwnerID = "admin";

        authTicket.clientLocalAddress = InetAddress.getLocalHost().getHostAddress();

        lReqMsg.setAuthenticator(authTicket, "admin");

        String loginData = jsonMapper.writeValueAsString(lReqMsg);

        for (int i = 0; i < iterations; i++) {
            RACSloginPT.tagStartTime(0);
            response = iracService.clientServicesAccessRequest(loginData);
            RACSloginPT.tagEndTime(0);
            RACSloginPT.nextMeasurment();
            RACSloginPT.saveMeasurmentsToDisk();
            System.out.println(i);
        }

        sAResponse = jsonMapper.readValue(response, ServiceAccessResponse.class);
        System.out.println(response);

        session = sAResponse.getSessionData("admin");

    }

}
