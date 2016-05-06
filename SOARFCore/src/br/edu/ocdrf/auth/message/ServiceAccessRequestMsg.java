package br.edu.ocdrf.auth.message;

import br.edu.ocdrf.util.AES;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ServiceAccessRequestMsg {

    public String serviceUUID;
    public String ticketGrantingTickect;
    public String authenticator;

    public void setTGT(TicketGrantingTicket tgt, String racsKey) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            String temp = mapper.writeValueAsString(tgt);

            AES aes = new AES();
            aes.setSecretKey(racsKey);

            ticketGrantingTickect = aes.encrypt(temp);

        } catch (Exception ex) {
            System.err.println(ex);
        }
    }

    public TicketGrantingTicket getTGT(String racsKey) {

        TicketGrantingTicket tgt = null;
        try {

            AES aes = new AES();
            aes.setSecretKey(racsKey);

            String decryptedTGT = aes.decrypt(ticketGrantingTickect);

            ObjectMapper mapper = new ObjectMapper();

            tgt = mapper.readValue(decryptedTGT, TicketGrantingTicket.class);

        } catch (Exception ex) {
            System.err.println(ex);
        }

        return tgt;
    }

    public void setAuthenticator(AuthenticatorTicket auth, String sessionKey) {

        try {
            ObjectMapper mapper = new ObjectMapper();
            String temp = mapper.writeValueAsString(auth);

            AES aes = new AES();
            aes.setSecretKey(sessionKey);

            authenticator = aes.encrypt(temp);

        } catch (Exception ex) {
            System.err.println(ex);
        }
    }

    public AuthenticatorTicket getAuthenticator(String sessionKey) {

        AuthenticatorTicket auth = null;
        try {

            AES aes = new AES();
            aes.setSecretKey(sessionKey);

            String decryptedAuth = aes.decrypt(authenticator);

            ObjectMapper mapper = new ObjectMapper();

            auth = mapper.readValue(decryptedAuth, AuthenticatorTicket.class);

        } catch (Exception ex) {
            System.err.println(ex);
        }

        return auth;
    }
}
