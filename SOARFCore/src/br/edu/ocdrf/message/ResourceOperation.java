package br.edu.ocdrf.message;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.JavaType;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ResourceOperation extends CryptoMessages<ResourceOpData> {

    public String resourceOpData;

    public void setResourceOpDataAndEncrypt(ResourceOpData data, String sessionKey) throws Exception {

        resourceOpData = mapDataAsJsonAndEncrypt(data, sessionKey, false);

    }

    public ResourceOpData getResourceOpData(String sessionKey)  throws Exception{

        JavaType type = mapper.getTypeFactory().constructType(ResourceOpData.class);
        ResourceOpData data = getDataField(sessionKey, resourceOpData, type, false);

        return data;
    }

}
