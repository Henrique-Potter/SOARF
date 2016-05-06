package br.edu.ocdrf.racs.ws;

import br.edu.ocdrf.auth.message.AuthenticatorTicket;
import br.edu.ocdrf.auth.message.LoginRequestMessage;
import br.edu.ocdrf.auth.message.LoginResponseMessage;
import br.edu.ocdrf.auth.message.ServiceAccessResponse;
import br.edu.ocdrf.auth.message.ServiceTicket;
import br.edu.ocdrf.auth.message.SessionData;
import br.edu.ocdrf.auth.message.TicketGrantingTicket;
import br.edu.ocdrf.exceptions.ServiceBaseException;
import br.edu.ocdrf.racs.business.EntityBL;
import br.edu.ocdrf.racs.business.ResourceBL;
import br.edu.ocdrf.racs.core.AbstractRACSService;
import br.edu.ocdrf.racs.model.Entity;
import br.edu.ocdrf.racs.model.Resource;
import br.edu.ocdrf.util.AES;

import java.io.File;
import java.util.Arrays;
import java.util.Date;

import javax.jws.WebService;
import javax.xml.ws.Endpoint;
import org.apache.log4j.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;

@WebService(serviceName = "RACSWS", targetNamespace = "http://racs.ocdrf.edu.br", endpointInterface = "br.edu.ocdrf.interfaces.IRACSService")
public class RACSServiceWS extends AbstractRACSService {

    private final br.edu.ocdrf.util.PerformanceTest racsPT = new br.edu.ocdrf.util.PerformanceTest("RACS_Login", 1000);

    private static final Logger log = Logger.getLogger(RACSServiceWS.class.getName());

    private static final String RACSServiceURL = "http://192.168.0.10:3000/RACSWS?wsdl";
    //private static final String RACSServiceURL = "http://152.92.155.231:3000/RACSWS?wsdl";

    private Endpoint endpoint = null;

    @Override
    public void initialize() {
        log.info("Initializing RACS Service...\n\n");
        super.initialize();
        try {
            endpoint = Endpoint.publish(RACSServiceURL, this);
            log.info("RACS service successfully started! \n\n");
        } catch (Exception ex) {
            log.error(ex.toString(), ex);
            throw ex;
        }
    }

    @Override
    public String registerSystemUser(String jsonData) throws Exception {

        ObjectMapper mapper = new ObjectMapper();

        Entity entity = mapper.readValue(jsonData, Entity.class);

        String response = new EntityBL(emf).registerUser(entity);

        return response;
    }

    @Override
    public String loginRequest(String loginRequestData) throws Exception {

        racsPT.tagStartTime(0);

        LoginResponseMessage lResponseMsg = null;
        ObjectMapper mapper = new ObjectMapper();
        LoginRequestMessage lRqeMsg = mapper.readValue(loginRequestData, LoginRequestMessage.class);

        Entity entity = new EntityBL(emf).getEntityByName(lRqeMsg.clientID);

        racsPT.tagEndTime(0);

        if (entity != null) {

            racsPT.tagStartTime(1);

            AES aes = new AES();
            aes.setKeyFromHashedPassword(entity.getPassword());

            lResponseMsg = new LoginResponseMessage();

            String sessionKey = AES.generateSecretKey();
            String criptoSessionKey = aes.encrypt(sessionKey);

            lResponseMsg.cryptoSessionKey = criptoSessionKey;

            TicketGrantingTicket tgt = new TicketGrantingTicket();

            tgt.clientID = entity.getName();
            tgt.clientLocalAddress = getMessageSenderRemoteHost(wsContext);
            tgt.sessionKey = sessionKey;
            tgt.ticketValidity = new Date().getTime();

            lResponseMsg.setTGT(tgt, Arrays.toString(racsKey.getEncoded()));
        }

        String response = mapper.writeValueAsString(lResponseMsg);

        return response;
    }

    @Override
    public String clientServicesAccessRequest(String loginReqMsg) throws Exception {

        racsPT.tagStartTime(0);
        String accessResponse = null;
        ServiceAccessResponse servAR = null;
        ObjectMapper mapper = new ObjectMapper();
        LoginRequestMessage loginRegMsg = mapper.readValue(loginReqMsg, LoginRequestMessage.class);

        try {

            Entity entity = new EntityBL(emf).getEntityByName(loginRegMsg.clientID);

            if (entity != null) {

                AuthenticatorTicket auth = loginRegMsg.getAuthenticator(entity.getPassword());

                String clientIP = getMessageSenderRemoteHost(wsContext);

                boolean validAuth = validateAuth(auth, loginRegMsg, clientIP);
                racsPT.tagEndTime(0);
                if (validAuth) {
                    racsPT.tagStartTime(1);
                    ResourceBL resBL = new ResourceBL(emf);

                    String contextServicesSessionKey = AES.generateSecretKey();
                    String discoveryServicesSessionKey = AES.generateSecretKey();
                    String directoryServicesSessionKey = AES.generateSecretKey();

                    Resource contextServ = resBL.getResourceByType(CONTEXT_TYPE);
                    Resource discoverytServ = resBL.getResourceByType(DISCOVERY_TYPE);
                    Resource directorytServ = resBL.getResourceByType(DIRECTORY_TYPE);

                    servAR = new ServiceAccessResponse();

                    ServiceTicket contextTicket = createServiceTicket(entity, wsContext, contextServicesSessionKey, contextServ.getUuid(), clientIP);
                    ServiceTicket discoveryTicket = createServiceTicket(entity, wsContext, discoveryServicesSessionKey, discoverytServ.getUuid(), clientIP);
                    ServiceTicket directoryTicket = createServiceTicket(entity, wsContext, directoryServicesSessionKey, directorytServ.getUuid(), clientIP);

                    servAR.setContextServTicket(contextServ.getSecretkey(), contextTicket);
                    servAR.setDiscoveryServTicket(discoverytServ.getSecretkey(), discoveryTicket);
                    servAR.setDirectoryServTicket(directorytServ.getSecretkey(), directoryTicket);

                    racsPT.tagEndTime(1);
                    racsPT.tagStartTime(2);

                    SessionData sData = new SessionData();

                    sData.contextURL = contextServ.getServiceUrl();
                    sData.discoveryURL = discoverytServ.getServiceUrl();
                    sData.directoryURL = directorytServ.getServiceUrl();

                    sData.contextSessionKey = contextServicesSessionKey;
                    sData.discoverySessionKey = discoveryServicesSessionKey;
                    sData.directorySessionKey = directoryServicesSessionKey;
                    sData.ticketOwnerID = entity.getName();

                    servAR.setSessionData(sData, entity.getPassword());
                    accessResponse = mapper.writeValueAsString(servAR);
                    racsPT.tagEndTime(2);

                }

            }
        } catch (Exception exception) {
            System.err.print(exception.getLocalizedMessage());
        }
        racsPT.nextMeasurment();
        racsPT.saveMeasurmentsToDisk();
        return accessResponse;
    }

    @Override
    protected void readConfig(File file) throws ServiceBaseException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
