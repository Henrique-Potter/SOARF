package br.edu.ocdrf.racs.core;

import br.edu.ocdrf.auth.message.AuthenticatorTicket;
import br.edu.ocdrf.auth.message.LoginRequestMessage;
import br.edu.ocdrf.auth.message.ServiceTicket;
import br.edu.ocdrf.entities.SEBaseService;
import br.edu.ocdrf.racs.model.Entity;
import br.edu.ocdrf.interfaces.IRACSService;

import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Date;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.xml.ws.WebServiceContext;
import org.apache.log4j.Logger;

public abstract class AbstractRACSService extends SEBaseService implements IRACSService {

    private static final Logger log = Logger.getLogger(AbstractRACSService.class.getName());
    protected final EntityManagerFactory emf = Persistence.createEntityManagerFactory("RACSModelPU");

    private static final long serviceSessionValidity = 3600000;
    private static final long TGTSessionValidity = 3600000;

    protected SecretKey racsKey;

    @Override
    public void initialize() {

        try {
            KeyGenerator generator = KeyGenerator.getInstance("AES");
            generator.init(128);
            racsKey = generator.generateKey();
        } catch (Exception ex) {
            log.error(ex);
        }

    }

    protected String generateSecretKey() {

        SecretKey key = null;
        String keyString = null;
        try {

            KeyGenerator generator = KeyGenerator.getInstance("AES");
            generator.init(128);
            key = generator.generateKey();

        } catch (NoSuchAlgorithmException noSuchAlgorithmException) {
            log.error(noSuchAlgorithmException);
        }

        if (key != null) {
            keyString = Arrays.toString(key.getEncoded());
        }

        return keyString;
    }
//
//    protected ServiceTicket createServiceTicket(String clientID, String localA, String serviceSessionKey) {
//
//        ServiceTicket servT = new ServiceTicket();
//
//        servT.clientID = clientID;
//        servT.clientLocalAddress = localA;
//        servT.serviceSessionKey = serviceSessionKey;
//        servT.validity = serviceSessionValidity;
//        servT.creationDate = new Date().getTime();
//
//        return servT;
//    }

    protected boolean validateAuth(AuthenticatorTicket auth, LoginRequestMessage lReqMsg, String clientIP) {

        boolean isValid = true;

        if (!auth.clientLocalAddress.equals(clientIP)) {
            isValid = false;
        }

        if (System.currentTimeMillis() >= auth.ticketValidity) {
            isValid = false;
        }

        boolean val2 = (lReqMsg.clientID == null ? auth.requestingClientID == null : lReqMsg.clientID.equals(auth.requestingClientID));

        return isValid & val2;

    }

    protected ServiceTicket createServiceTicket(Entity user, WebServiceContext wsContext, String sessionKey, String servUUID, String clientIP) throws NoSuchAlgorithmException {

        ServiceTicket servTick = new ServiceTicket();
        servTick.clientID = user.getName();
        servTick.clientLocalAddress = clientIP;
        servTick.creationDate = System.currentTimeMillis();
        servTick.serviceSessionKey = sessionKey;
        servTick.validity =  3600L * 1000L * 30L * 24L;
        servTick.targetServiceUUID = servUUID;
        
        return servTick;
    }
    
    

}
