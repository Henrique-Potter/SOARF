package br.edu.ocdrf.auth.message;

import br.edu.ocdrf.util.AES;
import com.fasterxml.jackson.databind.ObjectMapper;

public class LoginResponseMessage {

    public String cryptoSessionKey;

    public String ticketGrantingTicket;

    public void setTGT(TicketGrantingTicket tgt, String racsKey) {

        try {
            
            ObjectMapper mapper = new ObjectMapper();
            String temp = mapper.writeValueAsString(tgt);

            AES aes = new AES();
            aes.setSecretKey(racsKey);
            
            ticketGrantingTicket = aes.encrypt(temp);

        } catch (Exception ex) {
            System.err.println(ex);
        }
        
    }

    public TicketGrantingTicket getTGT(String racsKey) {

        TicketGrantingTicket tgt = null;
        try {

            AES aes = new AES();
            aes.setSecretKey(racsKey);
            
            String decryptedTGT = aes.decrypt(ticketGrantingTicket);
            
            ObjectMapper mapper = new ObjectMapper();
            
            tgt = mapper.readValue(decryptedTGT, TicketGrantingTicket.class);
            
            
        } catch (Exception ex) {
            System.err.println(ex);
        }
        
        return tgt;
    }

}
