package br.edu.ocdrf.auth.message;

public class ServiceTicket {

    public String clientID;
    public String clientLocalAddress;
    public String ticketType;
    public String serviceSessionKey;
    public long creationDate;
    public long validity;
    public String targetServiceUUID;
    public String ownerServiceUUID;

}
