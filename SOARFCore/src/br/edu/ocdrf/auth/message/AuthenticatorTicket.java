package br.edu.ocdrf.auth.message;

import br.edu.ocdrf.message.MessageData;

public class AuthenticatorTicket extends MessageData {
    
    public long ticketValidity = System.currentTimeMillis() + 100000;
    public String clientLocalAddress;
    
}
