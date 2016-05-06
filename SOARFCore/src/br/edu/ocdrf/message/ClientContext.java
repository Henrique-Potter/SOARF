package br.edu.ocdrf.message;

import com.fasterxml.jackson.databind.JavaType;

public class ClientContext extends CryptoMessages<ClientContextData> {

    public String contextData;

    public void setContextDataAndEncrypt(ClientContextData data, String sessionKey) throws Exception {

        contextData = mapDataAsJsonAndEncrypt(data, sessionKey, false);

    }

    public ClientContextData getContextData(String sessionKey)  throws Exception{

        JavaType type = mapper.getTypeFactory().constructType(ClientContextData.class);
        ClientContextData data = getDataField(sessionKey, contextData, type, false);

        return data;
    }

}
