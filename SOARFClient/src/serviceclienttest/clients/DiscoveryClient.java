package serviceclienttest.clients;

import serviceclienttest.Base.WSDLWebserviceBase;
import br.edu.ocdrf.message.ClientContext;
import br.edu.ocdrf.message.ClientContextData;
import br.edu.ocdrf.message.FoundCapabilities;
import br.edu.ocdrf.message.DiscoveryQuery;
import br.edu.ocdrf.message.FoundResources;
import br.edu.ocdrf.message.QueryData;
import br.edu.ocdrf.oal.domain.OCapability;
import br.edu.ocdrf.oal.domain.OResourceComponent;
import br.edu.ocdrf.ws.wsdl.interfaces.IDiscoveryService;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;

public class DiscoveryClient extends WSDLWebserviceBase {

    private final br.edu.ocdrf.util.PerformanceTest findCapPT = new br.edu.ocdrf.util.PerformanceTest("Client_DiscoveryService_FindCapabilities", 1000);
    private final br.edu.ocdrf.util.PerformanceTest findResPT = new br.edu.ocdrf.util.PerformanceTest("Client_DiscoveryService_FindResources", 1000);

    private final QName DIS_SERVICE_NAME = new QName("http://discovery.ocdrf.edu.br", "DiscoveryService");
    private final QName DIS_Port_NAME = new QName("http://discovery.ocdrf.edu.br", "DiscoveryServicePort");

    private final String disURL = ":1976/discovery/DiscoveryService?wsdl";

    public ArrayList<OCapability> capList;
    public ArrayList<OResourceComponent> resList;

    public DiscoveryClient() {
        setServiceAddress(disURL);
    }

    public List<OCapability> findCapabilities(String location, String discoveryTicket, String sessionKey, int iterations) throws Exception {

        String response = null;

        URL directoryUrl = new URL(serviceAddress);
        Service service = Service.create(directoryUrl, DIS_SERVICE_NAME);
        IDiscoveryService discService = service.getPort(IDiscoveryService.class);

        ClientContext clientContext = new ClientContext();
        clientContext.discServiceTicket = discoveryTicket;

        ClientContextData cData = new ClientContextData();
        cData.location = location;
        cData.requestingClientID = "admin";

        clientContext.setContextDataAndEncrypt(cData, sessionKey);

        String capReq = jsonMapper.writeValueAsString(clientContext);
        for (int i = 0; i < iterations; i++) {
            findCapPT.tagStartTime(0);
            response = discService.findCapabilities(capReq);
            findCapPT.tagEndTime(0);
            findCapPT.nextMeasurment();
            findCapPT.saveMeasurmentsToDisk();
            System.out.println(i);
        }

        FoundCapabilities frr = jsonMapper.readValue(response, FoundCapabilities.class);

        ArrayList<OCapability> foundCapabilities = frr.getDirectoryResponseData(sessionKey);

        capList = foundCapabilities;

        return capList;

    }

    public List<OResourceComponent> findCapabilityResources(String discoveryTicket, String sessionKey, String capability, int iterations) throws Exception {

        String response = null;

        URL directoryUrl = new URL(serviceAddress);
        Service service = Service.create(directoryUrl, DIS_SERVICE_NAME);
        IDiscoveryService discService = service.getPort(IDiscoveryService.class);

        DiscoveryQuery discQuery = new DiscoveryQuery();
        discQuery.discServiceTicket = discoveryTicket;

        QueryData qDt = new QueryData();
        qDt.requestingClientID = "admin";
        qDt.resourceCapabilityID = capability;

        discQuery.setQueryDataAndEncrypt(qDt, sessionKey);

        String resourceRequest = jsonMapper.writeValueAsString(discQuery);
        for (int i = 0; i < iterations; i++) {
            findResPT.tagStartTime(0);
            response = discService.findResources(resourceRequest);
            findResPT.tagEndTime(0);
            findResPT.nextMeasurment();
            findResPT.saveMeasurmentsToDisk();
            System.out.println(i);
        }

        FoundResources frr = jsonMapper.readValue(response, FoundResources.class);

        ArrayList<OResourceComponent> foundResList = frr.getDirectoryResponseData(sessionKey);

        resList = foundResList;

        return resList;

    }

}
