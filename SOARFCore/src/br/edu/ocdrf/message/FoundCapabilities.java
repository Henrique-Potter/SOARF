package br.edu.ocdrf.message;

import br.edu.ocdrf.oal.domain.OCapability;
import com.fasterxml.jackson.databind.JavaType;
import java.util.ArrayList;
import java.util.List;

public class FoundCapabilities extends CryptoMessages<ArrayList<OCapability>> {

    public String encryptedResponseData;

    public void setResponseDataAndEncrypt(ArrayList<OCapability> data, String key) throws Exception {

        encryptedResponseData = mapDataAsJsonAndEncrypt(data, key, false);

    }

    public ArrayList<OCapability> getDirectoryResponseData(String key) throws Exception {

        JavaType type = mapper.getTypeFactory().constructCollectionType(List.class, OCapability.class);

        ArrayList<OCapability> data = getDataField(key, encryptedResponseData, type, false);

        return data;
    }

}
