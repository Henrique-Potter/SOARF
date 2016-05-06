package br.edu.ocdrf.directory;

import br.edu.ocdrf.auth.message.ServiceTicket;
import br.edu.ocdrf.entities.SEBaseService;
import br.edu.ocdrf.entities.ServiceID;
import br.edu.ocdrf.exceptions.DirectoryServiceException;
import br.edu.ocdrf.ws.wsdl.interfaces.IDirectoryService;
import br.edu.ocdrf.racs.business.ResourceBL;
import br.edu.ocdrf.racs.model.Entity;
import br.edu.ocdrf.racs.model.Resource;
import br.edu.ocdrf.util.AES;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Timer;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import org.apache.log4j.Logger;


public abstract class AbstractDirectoryService extends SEBaseService implements IDirectoryService {

    protected final EntityManagerFactory emf = Persistence.createEntityManagerFactory("RACSModelPU");

    protected final String CONFIG_FILE_NAME = "directory_serv_config.xml";
    protected final String ID_FILE_NAME = "directory_serv_id.data";

    
    protected static final Logger LOGGER = Logger.getLogger(AbstractDirectoryService.class);

    protected Timer nrdrTimer;

    private final List<IDirectoryService> directories = new ArrayList<>();

    public abstract IDirectoryService federateDirectoryService(String url) throws DirectoryServiceException;

    protected abstract void startNRDManager(long period);

    public abstract void stop();


    protected final List<IDirectoryService> getDirectories() {
        return Collections.unmodifiableList(directories);
    }

    protected void AddContextServiceCredentials(Entity user, ServiceID requestingServNewID, String serviceIP) throws Exception {

        ResourceBL resBl = new ResourceBL(emf);
        Resource res = resBl.getResourceByType(CONTEXT_TYPE);

        String contextSessionKey = AES.generateSecretKey();

        ServiceTicket contextServTick = createServiceTicket(user, serviceIP, contextSessionKey, res.getUuid(), requestingServNewID.uuid);

        requestingServNewID.contextURL = res.getServiceUrl();
        requestingServNewID.contextSessionKey = contextSessionKey;
        requestingServNewID.setContextServTicket(res.getSecretkey(), contextServTick);

    }

    protected void AddDirectoryServiceTicket(Entity user, ServiceID requestingServNewID, String serviceIP) throws Exception {

        String directorySessionKey = AES.generateSecretKey();

        ServiceTicket directServTick = createServiceTicket(user, serviceIP, directorySessionKey, servID.uuid, requestingServNewID.uuid);

        requestingServNewID.directorySessionKey = directorySessionKey;
        requestingServNewID.setDirectoryServTicket(servID.secretKey, directServTick);

    }

    protected ServiceTicket createServiceTicket(Entity user, String serviceIP, String sessionKey, String targetUUID, String ticketOwnerUUID) {

        ServiceTicket servTicket = new ServiceTicket();
        servTicket.clientID = user.getName();
        servTicket.clientLocalAddress = serviceIP;
        servTicket.creationDate = System.currentTimeMillis();
        servTicket.serviceSessionKey = sessionKey;
        servTicket.validity = 3600L * 1000L * 30L;
        servTicket.targetServiceUUID = targetUUID;
        servTicket.ownerServiceUUID = ticketOwnerUUID;

        return servTicket;
    }

}
