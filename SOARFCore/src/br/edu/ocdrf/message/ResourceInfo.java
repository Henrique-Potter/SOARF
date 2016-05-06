package br.edu.ocdrf.message;

import com.fasterxml.jackson.databind.JavaType;

public class ResourceInfo extends CryptoMessages<ResourceInfoData> {

    public String resourceInfoData;

    public void setResourceInfoDataAndEncrypt(ResourceInfoData data, String sessionKey) throws Exception {

        resourceInfoData = mapDataAsJsonAndEncrypt(data, sessionKey, false);

    }

    public ResourceInfoData getResourceInfoData(String sessionKey) throws Exception {

        JavaType type = mapper.getTypeFactory().constructType(ResourceInfoData.class);
        ResourceInfoData data = getDataField(sessionKey, resourceInfoData, type, false);

        return data;
    }

}
