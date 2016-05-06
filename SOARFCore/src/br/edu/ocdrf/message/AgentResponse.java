package br.edu.ocdrf.message;

import com.fasterxml.jackson.databind.JavaType;

public class AgentResponse extends CryptoMessages<AgentResponseData> {

    public String angentResponseData;

    public void setAgentResponseDataAndEncrypt(AgentResponseData data, String sessionKey) throws Exception {

        angentResponseData = mapDataAsJsonAndEncrypt(data, sessionKey, false);

    }

    public AgentResponseData getAgentResponseData(String sessionKey) throws Exception {

        JavaType type = mapper.getTypeFactory().constructType(AgentResponseData.class);
        AgentResponseData data = getDataField(sessionKey, angentResponseData, type, false);

        return data;
    }

}
