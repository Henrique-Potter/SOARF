package br.edu.ocdrf.message;

import br.edu.ocdrf.oal.domain.OResourceComponent;
import com.fasterxml.jackson.databind.JavaType;
import java.util.ArrayList;
import java.util.List;

public class FoundResources extends CryptoMessages<ArrayList<OResourceComponent>> {

    public String encryptedResponseData;

    public void setResponseDataAndEncrypt(ArrayList<OResourceComponent> data, String key) throws Exception {

        try {
            encryptedResponseData = mapDataAsJsonAndEncrypt(data, key, false);
        } catch (Exception ex) {
            System.err.println(ex);
        }

    }

    public ArrayList<OResourceComponent> getDirectoryResponseData(String key) throws Exception {

        JavaType type = mapper.getTypeFactory().constructCollectionType(List.class, OResourceComponent.class);

        ArrayList<OResourceComponent> data = getDataField(key, encryptedResponseData, type, false);

        return data;
    }
}
