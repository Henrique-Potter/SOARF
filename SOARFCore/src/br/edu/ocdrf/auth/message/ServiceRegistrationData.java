package br.edu.ocdrf.auth.message;

import br.edu.ocdrf.message.MessageData;


public class ServiceRegistrationData extends MessageData{
    
    public int serviceType;
    public String comunicationTechnology;
    public String serviceUrl;
    public long creationDate;
    public long validity;
    public String localAddress;  
    
}
