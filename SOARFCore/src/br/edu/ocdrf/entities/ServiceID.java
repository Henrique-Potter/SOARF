package br.edu.ocdrf.entities;

import br.edu.ocdrf.message.CryptoMessages;
import java.io.Serializable;

public class ServiceID extends CryptoMessages<Object> implements Serializable{

    private static final long serialVersionUID = 1L;
    
    public String loginID;
    public String password;

    public String uuid;
    public String secretKey;

    public String contextSessionKey;
    public String directorySessionKey;
    public String discoverySessionKey;

    public String contextURL;
    public String directoryURL;
    public String discoveryURL;



    public boolean areFieldsNullOrEmpty() {
        return uuid == null || secretKey == null || uuid.isEmpty() || secretKey.isEmpty();
    }

}
