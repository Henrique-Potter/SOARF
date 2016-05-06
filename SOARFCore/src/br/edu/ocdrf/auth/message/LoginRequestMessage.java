package br.edu.ocdrf.auth.message;

import br.edu.ocdrf.message.CryptoMessages;
import com.fasterxml.jackson.databind.JavaType;

public class LoginRequestMessage extends CryptoMessages<AuthenticatorTicket> {

    public String clientID;
    public String authenticatorTicket;

    public void setAuthenticator(AuthenticatorTicket auth, String sessionKey) throws Exception {
        authenticatorTicket = mapDataAsJsonAndEncrypt(auth, sessionKey, true);
    }

    public AuthenticatorTicket getAuthenticator(String sessionKey) throws Exception {

        JavaType type = mapper.getTypeFactory().constructType(AuthenticatorTicket.class);
        AuthenticatorTicket data = getDataField(sessionKey, authenticatorTicket, type, true);

        return data;

    }
}
