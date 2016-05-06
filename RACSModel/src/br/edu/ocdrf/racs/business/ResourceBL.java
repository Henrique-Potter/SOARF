package br.edu.ocdrf.racs.business;

import br.edu.ocdrf.entities.ServiceID;
import br.edu.ocdrf.entities.ServicesTypeID;
import br.edu.ocdrf.racs.model.Capability;
import br.edu.ocdrf.racs.model.Entity;
import br.edu.ocdrf.racs.model.Resource;
import br.edu.ocdrf.util.AES;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.persistence.EntityManagerFactory;
import org.apache.log4j.Logger;

public class ResourceBL extends BaseDAO {

    private static final Logger log = Logger.getLogger(RACSServiceBL.class.getName());

    public ResourceBL(EntityManagerFactory entityManagerFactory) {
        super(entityManagerFactory);
    }

    public ServiceID registerDirService(String userID, String password, String dirServiceURL) throws Exception{

        String resourceUUID = null;
        String resourceKey = null;

        Entity entity = em.createNamedQuery("Entity.findByName", Entity.class).setParameter("name", userID).getSingleResult();

        if (entity != null) {

            resourceUUID = generateUUID();
            resourceKey = AES.generateSecretKey();

            em.getTransaction().begin();

            Resource res = new Resource();
            res.setSecretkey(resourceKey);
            res.setUuid(resourceUUID);
            res.setServiceUrl(dirServiceURL);
            res.setEntityfk(entity);
            res.setType(ServicesTypeID.DIRECTORY_SERVICE_TYPE);

            em.persist(res);
            em.getTransaction().commit();

        }

        ServiceID servID = new ServiceID();
        servID.uuid = resourceUUID;
        servID.loginID = userID;
        servID.password = password;
        servID.secretKey = resourceKey;

        return servID;
    }

    public ServiceID registerService( Entity ent, String dirUUID, int serviceType, String serviceURL) throws Exception{

        String resourceKey = null;
        String resourceUUID = null;
        Resource dirRes = em.createNamedQuery("Resource.findByUuid", Resource.class).setParameter("uuid", dirUUID).getSingleResult();

        resourceKey = AES.generateSecretKey();
        resourceUUID = generateUUID();

        em.getTransaction().begin();

        Resource servRes = new Resource();
        servRes.setSecretkey(resourceKey);
        servRes.setUuid(resourceUUID);
        servRes.setDirectoryServiceFk(dirRes);
        servRes.setType(serviceType);
        servRes.setServiceUrl(serviceURL);
        servRes.setSecuritypolicy(ent.getSecurityPolicy());
        servRes.setEntityfk(ent);

        em.persist(servRes);
        em.getTransaction().commit();

        ServiceID servID = new ServiceID();
        servID.uuid = resourceUUID;
        servID.secretKey = resourceKey;

        return servID;
    }

    public ServiceID registerService(Entity ent, String dirUUID, String resourceUUID, int serviceType, String serviceURL) throws Exception{

        String resourceKey = "";

        Resource dirRes = em.createNamedQuery("Resource.findByUuid", Resource.class).setParameter("uuid", dirUUID).getSingleResult();

        resourceKey = AES.generateSecretKey();

        em.getTransaction().begin();

        Resource servRes = new Resource();
        servRes.setSecretkey(resourceKey);
        servRes.setUuid(resourceUUID);
        servRes.setDirectoryServiceFk(dirRes);
        servRes.setType(serviceType);
        servRes.setServiceUrl(serviceURL);
        servRes.setSecuritypolicy(ent.getSecurityPolicy());
        servRes.setEntityfk(ent);

        em.persist(servRes);
        em.getTransaction().commit();

        ServiceID servID = new ServiceID();
        servID.uuid = resourceUUID;
        servID.secretKey = resourceKey;

        return servID;
    }

    public Resource getResourceByType(int serviceType) {

        List<Resource> contextServices = em.createNamedQuery("Resource.findByType", Resource.class).setParameter("type", serviceType).getResultList();

        return contextServices.get(0);
    }

    public Resource getResourceByUUID(String uuid) {

        Resource res = em.createNamedQuery("Resource.findByUuid", Resource.class).setParameter("uuid", uuid).getSingleResult();

        return res;
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

    protected String generateUUID() {
        return UUID.randomUUID().toString();
    }

    public void addResourceCapabilities(Resource res, List<Capability> capList) {

        em.getTransaction().begin();

        for (Capability cap : capList) {
            cap.setResourceFk(res);
            em.persist(cap);
        }

        em.getTransaction().commit();
    }

}
