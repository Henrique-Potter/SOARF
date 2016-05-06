package br.edu.ocdrf.auth.message;

import br.edu.ocdrf.message.CryptoMessages;
import com.fasterxml.jackson.databind.JavaType;

public class ServiceAccessResponse extends CryptoMessages<SessionData> {

    public String encryptedJsonSessionData;

    public void setSessionData(SessionData sData, String clientPassword) throws Exception {
        encryptedJsonSessionData = mapDataAsJsonAndEncrypt(sData, clientPassword, true);
    }

    public SessionData getSessionData(String sessionKey) throws Exception {

        JavaType type = mapper.getTypeFactory().constructType(SessionData.class);
        SessionData data = getDataField(sessionKey, encryptedJsonSessionData, type, true);

        return data;
    }

}
