package br.edu.ocdrf.agent.messages;

import br.edu.ocdrf.message.CryptoMessages;
import br.edu.ocdrf.message.ResourceOntologyRegistrationData;
import com.fasterxml.jackson.databind.JavaType;

public class ResourceOntologyRegistration extends CryptoMessages<ResourceOntologyRegistrationData> {

    public String encryptedResOntoRegData;

    public void setOntology(ResourceOntologyRegistrationData resOntoRegData, String secretKey) throws Exception {

        encryptedResOntoRegData = mapDataAsJsonAndEncrypt(resOntoRegData, secretKey, false);

    }

    public ResourceOntologyRegistrationData getOntology(String secretKey) throws Exception {

        JavaType type = mapper.getTypeFactory().constructType(ResourceOntologyRegistrationData.class);
        ResourceOntologyRegistrationData data = getDataField(secretKey, encryptedResOntoRegData, type, false);

        return data;
    }

}
