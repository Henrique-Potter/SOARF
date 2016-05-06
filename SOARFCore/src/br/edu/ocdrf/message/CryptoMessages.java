package br.edu.ocdrf.message;

import br.edu.ocdrf.auth.message.ServiceTicket;
import br.edu.ocdrf.util.AES;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.Serializable;

public class CryptoMessages<T> implements Serializable{

    
    protected transient ObjectMapper mapper = new ObjectMapper();

    public String requesterClaimedID;
    
    public String contextServiceTicket;
    public String dirServiceTicket;
    public String discServiceTicket;
    
    public String resourceServiceTicket;

    protected <T> T getDataField(String key, String dataFieldString, JavaType objClass, boolean hashKey) throws Exception {

        AES aes = new AES();
        if (hashKey) {
            aes.setKeyFromHashedPassword(key);
        } else {
            aes.setSecretKey(key);
        }

        String decryptedData = aes.decrypt(dataFieldString);

        T obj = mapper.readValue(decryptedData, objClass);

        return obj;
    }

    public ServiceTicket getDiscoveryServTicket(String discoveryServtKey) throws Exception {

        ServiceTicket disServt = getServiceTicket(discoveryServtKey, discServiceTicket);

        return disServt;
    }

    public void setDiscoveryServTicket(String discoveryServtKey, ServiceTicket servTicket) throws Exception {

        discServiceTicket = mapServiceTicketAsJsonAndEncrypt(servTicket, discoveryServtKey);

    }

    public ServiceTicket getContextServTicket(String contextServtKey) throws Exception {

        ServiceTicket disServt = getServiceTicket(contextServtKey, contextServiceTicket);

        return disServt;
    }

    public void setContextServTicket(String contextServtKey, ServiceTicket servTicket) throws Exception {

        contextServiceTicket = mapServiceTicketAsJsonAndEncrypt(servTicket, contextServtKey);

    }

    public ServiceTicket getDirectoryServTicket(String directoryServtKey) throws Exception {

        ServiceTicket dirServt = getServiceTicket(directoryServtKey, dirServiceTicket);

        return dirServt;
    }

    public void setDirectoryServTicket(String directoryServtKey, ServiceTicket servTicket) throws Exception {

        dirServiceTicket = mapServiceTicketAsJsonAndEncrypt(servTicket, directoryServtKey);

    }

    public ServiceTicket getResourceServTicket(String resourceServtKey) throws Exception {

        ServiceTicket dirServt = getServiceTicket(resourceServtKey, resourceServiceTicket);

        return dirServt;
    }

    public void setResourceServTicket(String resourceServtKey, ServiceTicket servTicket) throws Exception {

        resourceServiceTicket = mapServiceTicketAsJsonAndEncrypt(servTicket, resourceServtKey);

    }

    private ServiceTicket getServiceTicket(String serviceKey, String serviceTicket) throws Exception {

        AES aes = new AES();
        aes.setSecretKey(serviceKey);

        String decryptedServt = aes.decrypt(serviceTicket);

        ServiceTicket servT = mapper.readValue(decryptedServt, ServiceTicket.class);

        return servT;
    }

    protected String mapDataAsJsonAndEncrypt(T data, String key, boolean hashKey) throws Exception {

        String temp = mapper.writeValueAsString(data);

        AES aes = new AES();
        if (hashKey) {
            aes.setKeyFromHashedPassword(key);
        } else {
            aes.setSecretKey(key);
        }

        return aes.encrypt(temp);
    }

    protected String mapServiceTicketAsJsonAndEncrypt(ServiceTicket servTicket, String key) throws Exception {

        String temp = mapper.writeValueAsString(servTicket);

        AES aes = new AES();
        aes.setSecretKey(key);

        return aes.encrypt(temp);
    }

    public String reCryptografyDataObject(String oldObject, String oldKey, String newKey) throws Exception {

        String objHolder;

        AES aes = new AES();
        aes.setSecretKey(oldKey);

        objHolder = aes.decrypt(oldObject);

        aes.setSecretKey(newKey);
        objHolder = aes.encrypt(objHolder);

        return objHolder;

    }

}
